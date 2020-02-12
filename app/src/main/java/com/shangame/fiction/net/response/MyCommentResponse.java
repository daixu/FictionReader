package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2018/8/24
 */
public class MyCommentResponse {

    public int records;
    public int total;
    public List<CommentBean> pagedata;

    public static class CommentBean {

        public int userid;
        public int bookid;
        public int comid;
        public String nickname;
        public String headimgurl;
        public int viplv;
        public String vipname;
        public String comment;
        public int pracount;
        public int replycount;
        public String creatortime;
        public String bookname;
        public String author;
        public String bookcover;
        public int state;
    }
}
