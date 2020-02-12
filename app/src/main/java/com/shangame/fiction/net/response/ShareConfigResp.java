package com.shangame.fiction.net.response;

public class ShareConfigResp extends BaseResp {
    public DataBean data;

    public static class DataBean {
        public String linkurl;
        public String titile;
        public String content;
        public String imgurl;
        public String author;
    }
}
