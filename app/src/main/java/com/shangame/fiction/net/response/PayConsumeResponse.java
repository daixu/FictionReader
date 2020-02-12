package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2018/8/27
 */
public class PayConsumeResponse {

    public int records;
    public int total;
    public List<PayConsumeBean> pagedata;

    public static class PayConsumeBean {

        public long money;
        public String ramark;
        public String month;
        public String daytime;
        public int buytype;
    }
}
