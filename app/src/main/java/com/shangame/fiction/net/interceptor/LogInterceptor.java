package com.shangame.fiction.net.interceptor;

import com.shangame.fiction.core.config.AppConfig;
import com.shangame.fiction.core.manager.Logger;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 日志拦截器
 * Create by Speedy on 2018/8/4
 */
public class LogInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private static final String TAG = "LogInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        if(AppConfig.isDebug || AppConfig.forceDebug){
            Logger.i(TAG,"url : " + request.url().toString());

            Response originalResponse  = chain.proceed(request);

            ResponseBody responseBody = originalResponse.body();
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            long contentLength = responseBody.contentLength();
            if (contentLength != 0) {
                Logger.i(TAG,"Response :"+buffer.clone().readString(charset));
            }
            return originalResponse;
        }
        return chain.proceed(request);
    }


}
