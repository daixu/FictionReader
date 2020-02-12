package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2019/1/4
 */
public class AutoPayResponse {

    public int records;
    public int total;
    public List<AutoPayItem> pagedata;


    public static class AutoPayItem{
        public int userid;
        public int bookid;
        public int autorenew;
        public String bookname;

    }
}
