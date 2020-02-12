package com.shangame.fiction.net.response;

import java.util.List;

public class RechargeListResp extends BaseResp{
    public DataBean data;

    public static class DataBean {
        public int records;
        public int total;
        public double sumPrice;
        public double selPirce;
        public List<PageDataBean> pagedata;

        public static class PageDataBean {
            public int userid;
            public String nickname;
            public double orderMoney;
            public String creatorTime;
        }
    }
}
