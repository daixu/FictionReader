package com.shangame.fiction.net.response;

import java.util.List;

public class PartnerListResp extends BaseResp {
    public DataBean data;

    public static class DataBean {
        public int records;
        public int total;
        public List<PageDataBean> pagedata;

        public static class PageDataBean {
            public int agentId;
            public int userid;
            public String agentName;
            public int parentId;
            public int userCount;
            public String agentDate;
        }
    }
}
