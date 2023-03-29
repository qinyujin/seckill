package com.aimer.seckill.controller;

import com.aimer.seckill.rabbitmq.MQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通过mybatis代码生成器生成的代码
 * <p>
 * 前端控制器
 * </p>
 *
 * @author aimer
 * @since 2023-03-14
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private MQSender mqSender;

    @RequestMapping("/mq")
    @ResponseBody
    public void mq() {
        mqSender.send("hello");
    }

    @RequestMapping("/mq/fanout")
    @ResponseBody
    public void mq01() {
        mqSender.send01("hello");
    }

    @RequestMapping("/mq/direct01")
    @ResponseBody
    public void mq03() {
        mqSender.send03("hello");
    }

    @RequestMapping("/mq/direct02")
    @ResponseBody
    public void mq04() {
        mqSender.send04("hello");
    }

    @RequestMapping("/mq/topic01")
    @ResponseBody
    public void mq05() {
        mqSender.send05("hello");
    }

    @RequestMapping("/mq/topic02")
    @ResponseBody
    public void mq06() {
        mqSender.send06("hello");
    }
}
