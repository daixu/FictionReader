package com.shangame.fiction.net.response;

import java.util.List;

public class SumPriceListResp extends BaseResp {
    public DataBean data;

    public static class DataBean {
        public int records;
        public int total;
        public double sumPrice;
        public double cashPrice;
        public double yesterPrice;
        public double selPirce;
        public List<PageDataBean> pagedata;

        public static class PageDataBean {
            public int userid;
            public String nickname;
            public double userMoney;
        }
    }
}
