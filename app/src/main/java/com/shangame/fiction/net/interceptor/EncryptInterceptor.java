package com.shangame.fiction.net.interceptor;

import com.shangame.fiction.BuildConfig;
import com.shangame.fiction.core.config.AppConfig;
import com.shangame.fiction.core.manager.Logger;
import com.shangame.fiction.core.utils.DeviceUtils;
import com.shangame.fiction.core.utils.MD5Utils;
import com.shangame.fiction.net.api.ApiConstant;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 加密拦截器
 * Create by Speedy on 2018/8/4
 */
public class EncryptInterceptor implements Interceptor {

    public static String device;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
//        String method = originalRequest.method();
//        Logger.i("EncryptInterceptor","method = "+method);

        HttpUrl httpUrl = originalRequest.url();
        String params = httpUrl.encodedQuery() + "SshdAmWx2018";
        String digest = MD5Utils.MD5Encode(params);

        device = DeviceUtils.getAndroidID();
        if (AppConfig.isDebug) {
            Logger.i("EncryptInterceptor", "accounttoken = " + digest);
            Logger.i("EncryptInterceptor", "device = " + device);
        }
        Request encryptRequest = originalRequest.newBuilder()
                .header("accounttoken", digest)
                .header("device", device)
                .header("channel", String.valueOf(ApiConstant.Channel.ANDROID))
                .header("version", String.valueOf(BuildConfig.VERSION_CODE))
                .build();
        return chain.proceed(encryptRequest);
    }
}
