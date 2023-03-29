package com.aimer.seckill.utils;

import com.aimer.seckill.exception.GlobalException;
import com.aimer.seckill.vo.RespBeanEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author :覃玉锦
 * @create :2023-03-23 20:05:00
 */
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String serialize(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new GlobalException(RespBeanEnum.SERIALIZE_ERROR);
        }
    }

    public static  <T> T deserialize(String str, Class<T> beanType) {
        try {
            return objectMapper.readValue(str, beanType);
        } catch (JsonProcessingException e) {
            throw new GlobalException(RespBeanEnum.SERIALIZE_ERROR);
        }
    }
}
