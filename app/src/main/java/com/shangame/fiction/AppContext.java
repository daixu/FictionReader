package com.shangame.fiction;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.shangame.fiction.ad.TTAdManagerHolder;
import com.shangame.fiction.core.config.AppConfig;
import com.shangame.fiction.core.manager.SmartRefreshLayoutManager;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;

import cn.jpush.android.api.JPushInterface;

/**
 * Create by Speedy on 2018/7/17
 */
public final class AppContext extends Application {
    private static Context sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        //强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
        TTAdManagerHolder.init(this);

        AppCrashHandler.getInstance().init(this);

        CrashReport.initCrashReport(getApplicationContext(), "cbb65ab280", AppConfig.isDebug);

        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
        UMConfigure.setLogEnabled(true);

        SmartRefreshLayoutManager.initRefreshLayout();

        JPushInterface.setDebugMode(AppConfig.isDebug);
        JPushInterface.init(this);

        ARouter.init(this);
    }

    public static Context getContext(){
        return sInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
