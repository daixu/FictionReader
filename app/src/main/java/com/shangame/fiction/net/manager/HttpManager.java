package com.shangame.fiction.net.manager;

import com.shangame.fiction.core.config.AppConfig;
import com.shangame.fiction.net.interceptor.EncryptInterceptor;
import com.shangame.fiction.net.interceptor.LogInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create by Speedy on 2018/8/3
 */
public final class HttpManager {

    private static final Object mLock = new Object();
    private static final Object mLock2 = new Object();

    private static Retrofit mRetrofit;
    private static OkHttpClient mOkHttpClient;

    private static final int TIMEOUT_READ = 10;
    private static final int TIMEOUT_CONNECTION = 10;


    public static final Retrofit getRetrofit(){
        if(mRetrofit == null){
            synchronized (mLock){
                if(mRetrofit == null){
                    mRetrofit = new Retrofit.Builder()
                            .client(getOkHttpClient())
                            .baseUrl(AppConfig.baseUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                }
            }
        }
        return mRetrofit;
    }


    /**
     *
     * @return
     */
    public static final OkHttpClient getOkHttpClient() {
        if(mOkHttpClient == null) {
            synchronized (mLock2) {
                if (mOkHttpClient == null) {
                    mOkHttpClient = new OkHttpClient.Builder()
                            .retryOnConnectionFailure(false)
//                            .addInterceptor(new GzipRequestInterceptor())
                            .addNetworkInterceptor(new EncryptInterceptor())
                            .addNetworkInterceptor(new LogInterceptor())
                            .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                            .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
        return mOkHttpClient;

    }


    public static final void uploadFile(){

    }

    public static final void downlFile(){

    }

}
