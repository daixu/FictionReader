package com.shangame.fiction.net.response;

public class AgentIdInfoResp extends BaseResp {
    public DataBean data;

    public static class DataBean {
        public int agentId;
        public int userid;
        public String agentName;
        public int parentId;
        public int agentGrade;
        public String gradeName;
        public String headimgurl;
        public double sumPrice;
        public double yesterPrice;
        public double cashPrice;
        public double orderPrice;
        public double configPrice;
        public int userCount;
        public int agentCount;
        public int butdisplay;
    }
}
