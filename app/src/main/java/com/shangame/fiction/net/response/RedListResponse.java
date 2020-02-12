package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2019/3/29
 */
public class RedListResponse {

    public int records;
    public int total;
    public List<RedItem> pagedata;

    public static class RedItem {
        public String price;
        public String remark;
        public String datatime;
    }
}
