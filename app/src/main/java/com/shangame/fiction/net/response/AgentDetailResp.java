package com.shangame.fiction.net.response;

public class AgentDetailResp extends BaseResp {
    public DataBean data;

    public static class DataBean {
        public int agentId;
        public String userName;
        public String iPhone;
        public String card;
        public String cardName;
        public String zhCardName;
        public String identitycard;
    }
}
