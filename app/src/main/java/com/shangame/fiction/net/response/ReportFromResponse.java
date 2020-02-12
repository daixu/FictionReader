package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2019/7/30
 */
public class ReportFromResponse {
    /**
     * timedata : [{"addtime":"03-17","number":0},{"addtime":"03-18","number":0},{"addtime":"03-20","number":0},{"addtime":"03-21","number":0},{"addtime":"03-22","number":0}]
     * sumnumber : 0
     */

    public int sumnumber;
    public List<TimeDataBean> timedata;

    public static class TimeDataBean {
        /**
         * addtime : 03-17
         * number : 0
         */

        public String addtime;
        public int number;
    }
}
