package com.shangame.fiction.net.response;

import java.util.List;

public class CardListResp extends BaseResp {
    public DataBean data;

    public static class DataBean {
        public List<CardListBean> cardList;

        public static class CardListBean {
            public String cardName;
        }
    }
}
