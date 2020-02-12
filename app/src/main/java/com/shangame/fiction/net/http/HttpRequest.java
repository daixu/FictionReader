package com.shangame.fiction.net.http;

import java.util.Map;

/**
 * Create by Speedy on 2018/9/11
 */
public interface HttpRequest {

    void doPost( String url, final Map<String, String> params, OnRequestCallback requestCallback) ;

    void doGet( String url, final Map<String, String> params, OnRequestCallback requestCallback) ;

    void onCancel();
}
