package com.aimer.seckill.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author :覃玉锦
 * @create :2023-03-28 20:45:00
 */
@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue queue() {
        //durable 消息是否持久化处理
        return new Queue("queue", true);
    }

    //region fanoutExchange 广播模式.
    private static final String QUEUE1 = "queue_fanout01";

    private static final String QUEUE2 = "queue_fanout02";

    private static final String EXCHANGE = "fanoutExchange";

    @Bean
    public Queue queue01() {
        return new Queue(QUEUE1);
    }

    @Bean
    public Queue queue02() {
        return new Queue(QUEUE2);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(EXCHANGE);
    }

    @Bean
    public Binding binding01() {
        return BindingBuilder.bind(queue01()).to(fanoutExchange());
    }

    @Bean
    public Binding binding02() {
        return BindingBuilder.bind(queue02()).to(fanoutExchange());
    }
    //endregion

    //region directExchange 指定导向消费。通过routingKey来指引交换机发送数据到具体队列里
    private static final String QUEUE3 = "queue_direct01";

    private static final String QUEUE4 = "queue_direct02";

    private static final String DIRECT_EXCHANGE = "directExchange";

    private static final String DIRECT_ROUTINGKEY01 = "queue.red";

    private static final String DIRECT_ROUTINGKEY02 = "queue.green";

    @Bean
    public Queue queue03() {
        return new Queue(QUEUE3);
    }

    @Bean
    public Queue queue04() {
        return new Queue(QUEUE4);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE);
    }

    @Bean
    public Binding binding03() {
        //通过routingkey把队列和交换机绑定起来
        return BindingBuilder.bind(queue03()).to(directExchange()).with(DIRECT_ROUTINGKEY01);
    }

    @Bean
    public Binding binding04() {
        return BindingBuilder.bind(queue04()).to(directExchange()).with(DIRECT_ROUTINGKEY02);
    }
    //endregion

    //region topicExchange 相当于direct加上通配符的情况。*表示只能匹配1个词，#表示可以匹配【0，n】个词
    private static final String QUEUE05 = "queue_topic01";

    private static final String QUEUE06 = "queue_topic02";

    private static final String TOPIC_EXCHANGE = "topicExchange";

    //* 匹配1个单词 # 匹配 [0,n]个单词  例如#.queue.*  可以匹配  queue.red、 orange.green.queue.red
    private static final String TOPIC_ROUTINGKEY01 = "#.queue.#";

    private static final String TOPIC_ROUTINGKEY02 = "*.queue.#";

    @Bean
    public Queue queue05() {
        return new Queue(QUEUE05);
    }

    @Bean
    public Queue queue06() {
        return new Queue(QUEUE06);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Binding binding05() {
        return BindingBuilder.bind(queue05()).to(topicExchange()).with(TOPIC_ROUTINGKEY01);
    }

    @Bean
    public Binding binding06() {
        return BindingBuilder.bind(queue06()).to(topicExchange()).with(TOPIC_ROUTINGKEY02);
    }
    //endregion

    private static final String SECKILL_QUEUE = "seckillQueue";
    private static final String SECKILL_EXCHANGE = "seckillExchange";

    @Bean
    public Queue secKillQueue(){
        return new Queue(SECKILL_QUEUE);
    }

    @Bean
    public TopicExchange secKillExchange(){
        return new TopicExchange(SECKILL_EXCHANGE);
    }

    @Bean
    public Binding secKillBinding(){
        return BindingBuilder.bind(secKillQueue()).to(secKillExchange()).with("seckill.#");
    }
}
