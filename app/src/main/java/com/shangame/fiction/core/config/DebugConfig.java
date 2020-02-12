package com.shangame.fiction.core.config;

/**
 *
 * 测试环境配置
 *
 * Create by Speedy on 2018/7/17
 */
interface DebugConfig {


    /**
     * 标记是否为调试环境
     */
    boolean isDebug = true;

    String baseUrl = "http://192.168.3.48:8020";
//    String baseUrl = "https://webapi.anmaa.com/";


}
