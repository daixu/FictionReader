package com.shangame.fiction.net.response;

public class WithdrawResp extends BaseResp {
    public DataBean data;

    public static class DataBean {
        public double cashPrice;
    }
}
