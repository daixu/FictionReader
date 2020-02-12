package com.shangame.fiction.net.response;

import java.util.List;

public class WithdrawListResp extends BaseResp {

    public DataBean data;

    public static class DataBean {
        public int records;
        public int total;
        public double ktxPrice;
        public double ytxPrice;
        public List<PageDataBean> pagedata;

        public static class PageDataBean {
            public String selTime;
            public int ordeid;
            public double cashPrice;
            public String addTime;
            public String askforTime;
            public String accTime;
            public String bankname;
            public String bankcard;
            public int state;
        }
    }
}
