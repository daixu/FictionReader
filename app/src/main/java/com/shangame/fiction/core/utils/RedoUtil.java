package com.shangame.fiction.core.utils;

/**
 * Create by Speedy on 2019/1/22
 */
public class RedoUtil {

    private static long timsMillis;

    /**
     * 判断是否为快速点击两次
     * @param newTime
     * @return
     */
    public static boolean isQuickDoubleClick(long newTime){
        if(newTime - timsMillis < 1000){
            return true;
        }
        timsMillis = newTime ;
        return false;
    }
}
