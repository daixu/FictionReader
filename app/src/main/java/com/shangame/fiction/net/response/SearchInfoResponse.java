package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2018/8/23
 */
public class SearchInfoResponse {

    public List<ClassSearchBean> classdata;
    public List<HotSearchBean> retdata;
    public List<HistoryDataBean> histdata;
    public List<AlbumsDataBean> albumsdata;

    public static class ClassSearchBean {
        public int classId;
        public String classname;
    }

    public static class HotSearchBean {
        public int rankid;
        public int bookid;
        public String bookname;
    }

    public static class HistoryDataBean {
        public int rankid;
        public int bookid;
        public String bookname;
        public int booktype;
    }

    public static class AlbumsDataBean {
        public int rankid;
        public int bookid;
        public String bookname;
    }
}
