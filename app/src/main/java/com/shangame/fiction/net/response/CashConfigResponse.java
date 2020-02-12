package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2019/3/30
 */
public class CashConfigResponse {


    public double cashmoney;
    public String nickname;
    public List<CashItem> data;

    public static class CashItem {

        public int cashid;
        public double price;
        public String remark;
        public boolean isChecked;
    }
}
