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
    BIND_ERROR(50003, "参数校验异常"),
    SERIALIZE_ERROR(50004, "序列化异常"),
    ID_ERROR(50005, "账号不存在"),
    PASSWORD_ERROR(50006, "密码错误"),
    MOBILE_NOT_EXIST_ERROR(50007, "手机号码不存在"),
    PASSWORD_UPDATE_FAIL(50008, "更新密码失败"),
    ORDER_REPEAT(50009, "重复创建订单"),
    STOCK_EMPTY(50010, "库存为空"),
    PATH_ERROR(50011, "路径错误"),
    VERIFYCODE_ERROR(50012, "验证码错误"),
    ACCESS_LIMIT_REAHCED(50013, "访问过于频繁，请稍后再试");

    private Integer code;

    private String message;
}
