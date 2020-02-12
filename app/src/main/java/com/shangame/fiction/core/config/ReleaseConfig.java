package com.shangame.fiction.core.config;

import com.shangame.fiction.BuildConfig;

/**
 *
 * 正式环境配置
 *
 *
 * Create by Speedy on 2018/7/17
 */
interface ReleaseConfig {


    /**
     * 标记是否为调试环境
     */
    boolean isDebug = BuildConfig.DEBUG;

    String baseUrl = "https://webapi.anmaa.com/";

//    String baseUrl = "http://webapitest.anmaa.com";

}
