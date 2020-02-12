package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2018/8/27
 */
public class PlayTourResponse {

    public int records;
    public int total;
    public List<PlayTourBean> pagedata;

    public static class PlayTourBean {
        public int propid;
        public String propname;
        public String propimage;
        public int bookid;
        public String bookname;
        public String creatortime;
    }
}
