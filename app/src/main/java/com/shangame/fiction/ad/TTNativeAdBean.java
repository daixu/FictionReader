package com.shangame.fiction.ad;

import com.bytedance.sdk.openadsdk.TTNativeAd;

/**
 * Create by Speedy on 2019/7/19
 */
public class TTNativeAdBean {

    private TTNativeAd nativeAd;
    private static TTNativeAdBean instance = new TTNativeAdBean();

    private TTNativeAdBean() {
    }

    public static TTNativeAdBean getInstance() {
        return instance;
    }

    public TTNativeAd getNativeAd() {
        return nativeAd;
    }

    public void setNativeAd(TTNativeAd nativeAd) {
        this.nativeAd = nativeAd;
    }
}
