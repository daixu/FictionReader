package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2018/9/19
 */
public class ReceivedGiftResponse {

    public int records;
    public int total;
    public List<ReceivedGift> pagedata;

    public static class ReceivedGift {

        public String propname;
        public String author;
        public String creatortime;
        public String nickname;
        public String headimgurl;
    }
}
