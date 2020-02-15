package com.shangame.fiction.net.response;

public class GetSharePosterResp extends BaseResp {

    public DataBean data;

    public static class DataBean {
        public String shareImage;
    }
}
