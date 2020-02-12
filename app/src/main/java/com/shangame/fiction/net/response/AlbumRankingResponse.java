package com.shangame.fiction.net.response;

import java.util.List;

public class AlbumRankingResponse {
    public List<DataBean> subdata;
    public List<DataBean> clickdata;
    public List<DataBean> collectdata;
    public List<DataBean> giftdata;

    public static class DataBean {
        public int rankid;
        public int albumid;
        public String albumName;
        public String bookcover;
        public String synopsis;
        public String author;
    }
}
