package com.shangame.fiction.net.http;

/**
 * 网络请求回调
 */
public interface OnRequestCallback {

    /**
     * 请求正常返回回调方法
     *
     * @param response
     */
    void onResponse(String response);

    /**
     * 请求失败回调方法（网络错误）
     *
     * @param errorMsg
     */
    void onErrorResponse(String errorMsg);

}