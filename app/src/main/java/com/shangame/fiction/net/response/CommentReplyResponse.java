package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2018/8/20
 */
public class CommentReplyResponse {


    public int records;
    public int total;
    public List<CommentReplyBean> pagedata;

    public static class CommentReplyBean {

        public int userid;
        public int comid;
        public String nickname;
        public String headimgurl;
        public int replyuserid;
        public String replynickname;
        public int viplv;
        public String vipname;
        public String comment;
        public int pracount;
        public int replycount;
        public String creatortime;
        public int state;
    }
}
