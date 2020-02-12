package com.shangame.fiction.ad;

import com.bytedance.sdk.openadsdk.TTFeedAd;

/**
 * Create by Speedy on 2019/7/18
 */
public class FeedAdBean {
    private TTFeedAd feedAd;
    private static FeedAdBean instance = new FeedAdBean();

    private boolean isCloseAd = false;

    private FeedAdBean() {
    }

    public static FeedAdBean getInstance() {
        return instance;
    }

    public TTFeedAd getFeedAd() {
        return feedAd;
    }

    public void setFeedAd(TTFeedAd feedAd) {
        this.feedAd = feedAd;
    }

    public boolean isCloseAd() {
        return isCloseAd;
    }

    public void setCloseAd(boolean closeAd) {
        isCloseAd = closeAd;
    }
}
