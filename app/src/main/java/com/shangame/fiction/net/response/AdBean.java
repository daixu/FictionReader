package com.shangame.fiction.net.response;

public class AdBean {

    private static AdBean instance = new AdBean();
    private int verify = 0;

    private AdBean() {
    }

    public static AdBean getInstance() {
        return instance;
    }

    public int getVerify() {
        return verify;
    }

    public void setVerify(int verify) {
        this.verify = verify;
    }
}
