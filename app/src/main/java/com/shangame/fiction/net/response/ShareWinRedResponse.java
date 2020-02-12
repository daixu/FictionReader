package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2019/3/29
 */
public class ShareWinRedResponse {

    public int readTime;
    public List<InviteEntity> cardata;

    public static class InviteEntity {
        public int inviteid;
        public String title;
        public String describe;
        public String rule;
        public int state;
        public double number;
    }
}
