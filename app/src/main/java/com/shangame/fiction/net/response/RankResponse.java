package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2018/8/22
 */
public class RankResponse {

    public List<RankBookBean> subdata;
    public List<RankBookBean> clickdata;
    public List<RankBookBean> collectdata;
    public List<RankBookBean> giftdata;

    public static class RankBookBean {
        public int rankid;
        public int bookid;
        public String bookname;
        public String author;
        public String bookcover;
        public String synopsis;
    }
}
