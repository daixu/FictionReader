package com.shangame.fiction.net.response;

import java.util.List;

public class MemberListResp extends BaseResp {
    public DataBean data;

    public static class DataBean {
        public int records;
        public int total;
        public List<PageDataBean> pagedata;

        public static class PageDataBean {
            public int userid;
            public String nickname;
            public double money;
            public int expend;
            public double ordermoney;
            public String registerDate;
        }
    }
}
