package com.shangame.fiction.ui.wifi;

public class WifiBean {
    public int fileTotal = 0;
    private static final Object mLock = new Object();
    private static WifiBean wifiBean;

    public static WifiBean getInstance() {
        if (wifiBean == null) {
            synchronized (mLock) {
                if (wifiBean == null) {
                    wifiBean = new WifiBean();
                }
            }
        }
        return wifiBean;
    }
}
