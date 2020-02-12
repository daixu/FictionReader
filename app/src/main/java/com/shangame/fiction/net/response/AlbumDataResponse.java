package com.shangame.fiction.net.response;

import java.util.List;

public class AlbumDataResponse {

    public int records;
    public int total;
    public List<PageDataBean> pagedata;

    public static class PageDataBean {
        public int albumid;
        public String albumName;
        public String bookcover;
        public String synopsis;
        public String classname;
        public int chapternumber;
        public String serstatus;
        public String author;
    }
}
