package com.shangame.fiction.core.manager;

import android.util.Log;

import com.shangame.fiction.core.config.AppConfig;

/**
 * 日志管理
 * Create by Speedy on 2018/8/4
 */
public final class Logger {

    public static final int V = Log.VERBOSE;
    public static final int D = Log.DEBUG;
    public static final int I = Log.INFO;
    public static final int W = Log.WARN;
    public static final int E = Log.ERROR;


    public static final void v(String tag,String msg){
        if(AppConfig.isDebug|| AppConfig.forceDebug){
            Log.v(tag,msg);
        }
    }
    public static final void d(String tag,String msg){
        if(AppConfig.isDebug|| AppConfig.forceDebug){
            Log.d(tag,msg);
        }
    }
    public static final void i(String tag,String msg){
        if(AppConfig.isDebug|| AppConfig.forceDebug){
            Log.i(tag,msg);
        }
    }
    public static final void w(String tag,String msg){
        if(AppConfig.isDebug|| AppConfig.forceDebug){
            Log.w(tag,msg);
        }
    }
    public static final void e(String tag,String msg){
        if(AppConfig.isDebug|| AppConfig.forceDebug){
            Log.e(tag,msg);
        }
    }
    public static final void e(String tag,String msg,Throwable throwable){
        if(AppConfig.isDebug|| AppConfig.forceDebug){
            Log.e(tag,msg,throwable);
        }
    }



}
