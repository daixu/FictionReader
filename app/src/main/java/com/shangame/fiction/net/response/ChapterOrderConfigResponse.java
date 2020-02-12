package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2018/9/3
 */
public class ChapterOrderConfigResponse {

    public String title;
    public long readmoney;
    public long coin;
    public List<SubdataBean> subdata;

    public static class SubdataBean {
        public boolean isChecked;
        public double subprice;
        public double disprce;
        public int subnumber;
        public double discount;
        public String subtext;
    }
}
