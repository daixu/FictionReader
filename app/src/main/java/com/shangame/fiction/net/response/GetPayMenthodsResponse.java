package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2018/9/12
 */
public class GetPayMenthodsResponse {

    public List<PayItem> paydata;

    public static class PayItem {
        public int payprot;
        public String payname;
        public int operate;
        public int paytype;
        public String payurl;

        public boolean isChecked;
    }

}
