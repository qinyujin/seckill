package com.aimer.seckill.utils;

import java.util.regex.Pattern;

/**
 * @author :覃玉锦
 * @create :2023-03-20 14:02:00
 */
public class ValidatorUtils {

    private static final Pattern mobile_pattern = Pattern.compile("[1]([3-9])[0-9]{9}$");

    public static boolean isMobile(String mobile) {
        return true;
//        if (StringUtils.isEmpty(mobile))
//            return false;
//        Matcher matcher = mobile_pattern.matcher(mobile);
//        return matcher.matches();
    }
}
