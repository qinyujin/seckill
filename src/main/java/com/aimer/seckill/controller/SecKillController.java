package com.aimer.seckill.controller;

import com.aimer.seckill.config.AccessLimit;
import com.aimer.seckill.exception.GlobalException;
import com.aimer.seckill.pojo.SeckillMessage;
import com.aimer.seckill.pojo.SeckillOrder;
import com.aimer.seckill.pojo.User;
import com.aimer.seckill.rabbitmq.MQSender;
import com.aimer.seckill.service.IGoodsService;
import com.aimer.seckill.service.IOrderService;
import com.aimer.seckill.service.ISeckillOrderService;
import com.aimer.seckill.utils.JsonUtil;
import com.aimer.seckill.vo.GoodsVO;
import com.aimer.seckill.vo.RespBean;
import com.aimer.seckill.vo.RespBeanEnum;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author :覃玉锦
 * @create :2023-03-25 11:04:00
 */
@Controller
@RequestMapping("/secKill")
@Slf4j
public class SecKillController implements InitializingBean {
    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MQSender mqSender;

    private HashMap<Long, Boolean> emptyStockMap = new HashMap<>();

    /**
     * 优化前qps:605
     * <p>
     * 通过redis预减库存、内存标记减少redis网络通信、创单流程异步化(需要结合前端轮询订单状态)优化过后的qps：1100
     *
     * @param path    通过getPath获取到每个用户对应的秒杀地址.压测时可以去掉path这个参数
     * @param user
     * @param goodsId
     * @return orderId:成功秒杀 -1:秒杀失败 0:排队中
     */
    @RequestMapping("/{path}/doSecKill/{goodsId}")
    @ResponseBody
    public RespBean doSecKill(@PathVariable String path, User user, @PathVariable Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }

        /*//库存校验
        GoodsVO goods = goodsService.findGoodsVoById(goodsId);
        if (goods == null || goods.getStockCount() < 1) {
            return "stock is empty";
        }

        //重复订单校验
//        SeckillOrder byUidAndGoodsId = seckillOrderService.findByUidAndGoodsId(user.getId(), goodsId);
        SeckillOrder byUidAndGoodsId = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (byUidAndGoodsId != null) {
            return "user have been purchased";
        }

        //秒杀
        Order order = orderService.secKill(user, goods);
        return JsonUtil.serialize("suceess seckill:" + order);*/

        //采用redis预扣减库存、异步下单操作来进行优化
        ValueOperations valueOperations = redisTemplate.opsForValue();

        boolean check = orderService.checkPath(user, goodsId, path);
        if (!check) {
            return RespBean.error(RespBeanEnum.PATH_ERROR);
        }

        //目前这块靠的是唯一索引保证的不重复
        SeckillOrder byUidAndGoodsId = (SeckillOrder) valueOperations.get("order:" + user.getId() + ":" + goodsId);
        if (byUidAndGoodsId != null) {
            return RespBean.error(RespBeanEnum.ORDER_REPEAT, "请勿重复创建订单");
        }

        if (emptyStockMap.get(goodsId)) {
            return RespBean.error(RespBeanEnum.STOCK_EMPTY, "库存不足");
        }

        //有原子性
        Long stock = valueOperations.decrement("secKillGoods:" + goodsId);
        if (stock < 0) {
            emptyStockMap.put(goodsId, true);
            valueOperations.increment("secKillGoods:" + goodsId);

            return RespBean.error(RespBeanEnum.STOCK_EMPTY, "库存不足");
        }

        //通过mq消息异步下单
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        mqSender.secKillSend(JsonUtil.serialize(seckillMessage));
        return RespBean.success(0);
    }

    @RequestMapping("/result/{goodsId}")
    @ResponseBody
    public RespBean getResult(User user, @PathVariable Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }
        SeckillOrder seckillOrder = seckillOrderService.getResult(user, goodsId);
        if (seckillOrder != null) {
            return RespBean.success(seckillOrder.getOrderId());
        } else if (redisTemplate.hasKey("isStockEmpty:" + goodsId)) {
            return RespBean.error(RespBeanEnum.STOCK_EMPTY, -1L);
        } else {
            return RespBean.success(0L);
        }
    }

    /**
     * 获取秒杀地址
     *
     * @param user
     * @param goodsId
     * @param captcha 验证码
     * @return
     */
    @AccessLimit(second = 5, maxCount = 5, needLogin = true)
    @RequestMapping("/path/{goodsId}/{captcha}")
    @ResponseBody
    public RespBean getPath(User user, @PathVariable Long goodsId, @PathVariable String captcha, HttpServletRequest request) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }

        //通过注解AccessLimit代替。
        /*
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //限流-计数器，限制访问次数，5s内访问5次
        String uri = request.getRequestURI();
        Integer count = (Integer) valueOperations.get(uri + ":" + user.getId());
        //记录数量，最大为5,时间间隔为5分钟
        if (count == null) {
            valueOperations.set(uri + ":" + user.getId(), 1, 5, TimeUnit.SECONDS);
        } else if (count < 5) {
            valueOperations.increment(uri + ":" + user.getId());
        } else {
            return RespBean.error(RespBeanEnum.ACCESS_LIMIT_REAHCED);
        }*/

        boolean check = orderService.checkCaptcha(user, goodsId, captcha);
        if (!check) {
            return RespBean.error(RespBeanEnum.VERIFYCODE_ERROR);
        }
        String str = orderService.createPath(user, goodsId);
        return RespBean.success(str);
    }

    @RequestMapping("/captcha/{goodsId}")
    public void verifyCode(User user, @PathVariable Long goodsId, HttpServletResponse response) {
        if (user == null || goodsId < 0) {
            throw new GlobalException();
        }
        response.setContentType("image/jpg");
        response.setHeader("Pargam", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 32, 3);
        redisTemplate.opsForValue().set("captcha:" + user.getId() + ":" + goodsId, captcha.text(), 300, TimeUnit.SECONDS);
        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            log.error("验证码生成失败", e.getMessage());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        List<GoodsVO> goodsVo = goodsService.findGoodsVo();
        if (CollectionUtils.isEmpty(goodsVo)) return;

        //redis预处理库存
        goodsVo.forEach(g -> {
            valueOperations.set("secKillGoods:" + g.getId(), g.getStockCount());
            emptyStockMap.put(g.getId(), false);
        });
    }
}
