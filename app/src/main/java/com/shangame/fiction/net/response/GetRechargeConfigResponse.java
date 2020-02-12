package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2018/9/12
 */
public class GetRechargeConfigResponse {

    public List<RechargeBean> rechdata;

    public static class RechargeBean {

        public int propid;
        public double price;
        public int number;
        public int givenumber;
        public String remark;
        public int isdouble;

        public boolean isCheck;
        public int coinnumber;
    }
}
