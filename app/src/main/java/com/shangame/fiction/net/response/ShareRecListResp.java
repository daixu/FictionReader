package com.shangame.fiction.net.response;

import java.util.List;

public class ShareRecListResp extends BaseResp {
    public DataBean data;

    public static class DataBean {
        public List<RecDataBean> recdata;

        public static class RecDataBean {
            public int bookid;
            public String bookname;
            public String author;
            public String bookcover;
            public String synopsis;
            public int chapterid;
            public int status;
            public String shareLink;
        }
    }
}
