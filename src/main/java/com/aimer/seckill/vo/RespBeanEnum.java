package com.aimer.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author :覃玉锦
 * @create :2023-03-14 15:43:00
 */
@ToString
@Getter
@AllArgsConstructor
public enum RespBeanEnum {
    SUCCESS(200, "SUCCESS"),
    ERROR(500, "服务端异常"),
    LOGIN_ERROR(50001, "登录异常"),
    MOBILE_ERROR(50002, "手机号码格式不正确"),
    BIND_ERROR(50003, "参数校验异常");

    private Integer code;

    private String message;
}
