package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2019/1/3
 */
public class CoinSummaryResponse {

    public double coin;

    public List<CoinItem> validdata;
    public List<CoinItem> losedata;

    public static class CoinItem {

        public int counts;
        public long deadline;
        public int state;
        public int number;
        public String remark;
    }
}
