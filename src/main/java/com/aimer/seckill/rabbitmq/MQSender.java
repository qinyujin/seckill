package com.aimer.seckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author :覃玉锦
 * @create :2023-03-28 20:46:00
 */
@Service
@Slf4j
public class MQSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(Object msg) {
        log.info("发送消息" + msg);
        rabbitTemplate.convertAndSend("queue", msg);
    }

    public void send01(Object msg) {
        log.info("发送消息" + msg);
        rabbitTemplate.convertAndSend("fanoutExchange", "", msg);
    }

    public void send03(Object msg) {
        log.info("发送消息" + msg);
        rabbitTemplate.convertAndSend("directExchange", "queue.red", msg);
    }

    public void send04(Object msg) {
        log.info("发送消息" + msg);
        rabbitTemplate.convertAndSend("directExchange", "queue.green", msg);
    }

    public void send05(Object msg) {
        log.info("发送消息(仅被queue01接受)" + msg);
        rabbitTemplate.convertAndSend("topicExchange", "queue.red.message", msg);
    }

    public void send06(Object msg) {
        log.info("发送消息(两个queue接受)" + msg);
        rabbitTemplate.convertAndSend("topicExchange", "message.queue.green.abc", msg);
    }

    public void secKillSend(String message) {
        log.info("秒杀发送消息" + message);
        rabbitTemplate.convertAndSend("seckillExchange", "seckill.message", message);
    }
}
