package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2019/3/29
 */
public class InviteRecordResponse {

    public int records;
    public int total;
    public List<InviteRecord> pagedata;

    public static class InviteRecord {

        public String price;
        public String remark;
        public String datatime;
        public String nickname;
        public String headimgurl;
    }
}
