package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2018/8/28
 */
public class BookRackFilterConfigResponse {

    public int sumboookcount;
    public List<FilterItemBean> statusdata;
    public List<FilterItemBean> readdata;
    public List<FilterItemBean> numberdata;
    public List<FilterItemBean> maledata;

    public static class FilterItemBean {
        public int cid;
        public String configname;
        public int boookcount;
    }

}
