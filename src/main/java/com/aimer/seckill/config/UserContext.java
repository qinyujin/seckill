package com.aimer.seckill.config;

import com.aimer.seckill.pojo.User;

/**
 * @author :覃玉锦
 * @create :2023-03-29 15:36:00
 */
public class UserContext {
    private static ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public static void setUser(User user){
        threadLocal.set(user);
    }

    public static User getUser(){
        return threadLocal.get();
    }
}
