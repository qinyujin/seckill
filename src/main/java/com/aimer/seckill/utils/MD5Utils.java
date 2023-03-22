package com.aimer.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

/**
 * @author :覃玉锦
 * @create :2023-03-14 14:57:00
 */
@Component
public class MD5Utils {

    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";

    //客户端->服务端
    public static String inputPassToFromPass(String inputPass) {
        String str = salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    //服务端->db，用一个新的salt再加密，相当于独立md5加密一次
    public static String fromPassToDBPass(String fromPass, String salt) {
        String str = salt.charAt(0) + salt.charAt(2) + fromPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDBPass(String inputPass, String salt) {
        String clientSalt = inputPassToFromPass(inputPass);
        String dbPass = fromPassToDBPass(clientSalt, salt);
        return dbPass;
    }

    public static void main(String[] args) {
        String s = fromPassToDBPass("123456", salt);
        System.out.println(s);
    }
}
