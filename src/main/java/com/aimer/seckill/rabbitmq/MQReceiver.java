package com.aimer.seckill.rabbitmq;

import com.aimer.seckill.pojo.SeckillMessage;
import com.aimer.seckill.pojo.SeckillOrder;
import com.aimer.seckill.pojo.User;
import com.aimer.seckill.service.IGoodsService;
import com.aimer.seckill.service.IOrderService;
import com.aimer.seckill.utils.JsonUtil;
import com.aimer.seckill.vo.GoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author :覃玉锦
 * @create :2023-03-28 20:48:00
 */
@Service
@Slf4j
public class MQReceiver {
    @Autowired
    private IOrderService orderService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RabbitListener(queues = "queue")
    public void receive(Object msg) {
        log.info("接收消息" + msg);
    }

    @RabbitListener(queues = "queue_fanout01")
    public void receive01(Object msg) {
        log.info("queue01接受消息" + msg);
    }

    @RabbitListener(queues = "queue_fanout02")
    public void receive02(Object msg) {
        log.info("queue02接受消息" + msg);
    }

    @RabbitListener(queues = "queue_direct01")
    public void receive03(Object msg) {
        log.info("queue03接受消息" + msg);
    }

    @RabbitListener(queues = "queue_direct02")
    public void receive04(Object msg) {
        log.info("queue04接受消息" + msg);
    }

    @RabbitListener(queues = "queue_topic01")
    public void receive05(Object msg) {
        log.info("queue05接受消息" + msg);
    }

    @RabbitListener(queues = "queue_topic02")
    public void receive06(Object msg) {
        log.info("queue06接受消息" + msg);
    }

    @RabbitListener(queues = "seckillQueue")
    public void secKillReceiver(String message) {
        log.info("秒杀接受消息:" + message);
        //处理秒杀的情况
        SeckillMessage deserialize = JsonUtil.deserialize(message, SeckillMessage.class);
        User user = deserialize.getUser();
        Long goodsId = deserialize.getGoodsId();

        GoodsVO goodsVO = goodsService.findGoodsVoById(goodsId);

        //校验库存、重复性订单
        if (goodsVO.getStockCount() < 1) {
            return;
        }

        SeckillOrder byUidAndGoodsId = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (byUidAndGoodsId != null) {
            log.info("下单时有重复订单." + user.getId() + ":" + goodsId);
            return;
        }

        orderService.secKill(user, goodsVO);
    }
}
