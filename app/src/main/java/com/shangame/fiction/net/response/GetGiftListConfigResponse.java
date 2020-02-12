package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2018/8/29
 */
public class GetGiftListConfigResponse {

    public double readmoney;
    public List<GiftBean> data;

    public static class GiftBean {

        public int propid;
        public String propname;
        public int price;
        public String propimage;

        public boolean isChecked;
    }
}
