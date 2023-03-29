package com.aimer.seckill.service.impl;

import com.aimer.seckill.mapper.OrderMapper;
import com.aimer.seckill.pojo.Order;
import com.aimer.seckill.pojo.SeckillGoods;
import com.aimer.seckill.pojo.SeckillOrder;
import com.aimer.seckill.pojo.User;
import com.aimer.seckill.service.IOrderService;
import com.aimer.seckill.service.ISeckillGoodsService;
import com.aimer.seckill.service.ISeckillOrderService;
import com.aimer.seckill.utils.MD5Utils;
import com.aimer.seckill.utils.UUIDUtil;
import com.aimer.seckill.vo.GoodsVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author aimer
 * @since 2023-03-23
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    private ISeckillGoodsService seckillGoodsService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Order secKill(User user, GoodsVO goods) {
        ValueOperations valueOperations = redisTemplate.opsForValue();

        //减库存
        SeckillGoods seckillGoods = seckillGoodsService.getGoodsById(goods.getId());
        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
//        seckillGoodsService.updateById(seckillGoods);
        //通过乐观锁比较库存同时减库存解决超卖
        boolean res = seckillGoodsService.deductStock(seckillGoods.getId());
        //seckillGoods.getStockCount()<1 || res?
        if (!res) {
            valueOperations.set("isStockEmpty:" + goods.getId(), 1);
            return null;
        }
        //isStockEmpty

        //生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0l);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(goods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(LocalDateTime.now());

        orderMapper.insert(order);

        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrderService.save(seckillOrder);

        valueOperations.set("order:" + user.getId() + ":" + goods.getId(), seckillOrder);
        return order;
    }

    @Override
    public String createPath(User user, Long goodsId) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String cacheKey = "seckillPath:" + user.getId() + ":" + goodsId;
        String str = (String) valueOperations.get(cacheKey);
        if (StringUtils.isEmpty(str)) {
            str = MD5Utils.md5(UUIDUtil.uuid() + "123456");
            redisTemplate.opsForValue().set("seckillPath:" + user.getId() + ":" + goodsId, str, 60, TimeUnit.SECONDS);
        }
        return str;
    }

    /**
     * 校验秒杀地址
     *
     * @param user
     * @param goodsId
     * @param path
     * @return
     */
    @Override
    public boolean checkPath(User user, Long goodsId, String path) {
        if (user == null || goodsId < 0 || StringUtils.isEmpty(path)) {
            return false;
        }
        String redisPath = (String) redisTemplate.opsForValue().get("seckillPath:" + user.getId() + ":" + goodsId);
        return path.equals(redisPath);
    }

    /**
     * 校验验证码
     *
     * @param user
     * @param goodsId
     * @param captcha
     * @return
     */
    @Override
    public boolean checkCaptcha(User user, Long goodsId, String captcha) {
        if (user == null || StringUtils.isEmpty(captcha) || goodsId < 0) return false;
        String redisCaptcha = (String) redisTemplate.opsForValue().get("captcha:" + user.getId() + ":" + goodsId);
        return captcha.equals(redisCaptcha);
    }
}
