package com.aimer.seckill.utils;

import java.util.UUID;

/**
 * @author :覃玉锦
 * @create :2023-03-20 14:44:00
 */
public class UUIDUtil {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
