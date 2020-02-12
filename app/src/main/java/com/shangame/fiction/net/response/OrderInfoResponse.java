package com.shangame.fiction.net.response;

public class OrderInfoResponse extends BaseResp {

    public DataBean data;

    public static class DataBean {
        public int payStatus;
        public int propID;
        public int number;
    }
}
