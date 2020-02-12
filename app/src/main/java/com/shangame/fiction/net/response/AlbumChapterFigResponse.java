package com.shangame.fiction.net.response;

import java.util.List;

public class AlbumChapterFigResponse {

    /**
     * title : sample string 1
     * readmoney : 2.0
     * IsVIP : 3
     * subdata : [{"subprice":1,"disprce":2,"subnumber":3,"discount":4,"subtext":"sample string 5"},{"subprice":1,"disprce":2,"subnumber":3,"discount":4,"subtext":"sample string 5"}]
     */

    public String title;
    public double readmoney;
    public int IsVIP;
    public int autorenew;
    public List<SubDataBean> subdata;

    public static class SubDataBean {
        /**
         * subprice : 1.0
         * disprce : 2.0
         * subnumber : 3
         * discount : 4.0
         * subtext : sample string 5
         */
        public boolean isChecked;
        public double subprice;
        public double disprce;
        public int subnumber;
        public double discount;
        public String subtext;
    }
}
