package com.shangame.fiction.net.response;

import java.util.List;

public class NewsResp extends BaseResp {

    public DataBean data;

    public static class DataBean {
        public int records;
        public int total;
        public List<PageDataBean> pagedata;

        public static class PageDataBean {
            public int bookid;
            public String title;
            public String author;
            public int showType;
            public String bookname;
            public String bookcover;
            public int readcount;
            public int chapterid;
            public int bookShelves;
            public String keyword;
            public List<MediaImageBean> mediaImage;

            public static class MediaImageBean {
                public String imageurl;
            }
        }
    }
}
