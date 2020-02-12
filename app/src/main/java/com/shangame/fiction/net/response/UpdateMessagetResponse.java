package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2018/8/27
 */
public class UpdateMessagetResponse {

    public List<UpdateReminBean> upentity;

    public static class UpdateReminBean {
        public int id;
        public String bookname;
        public String chaptername;
        public String bookimage;
        public String title;
        public String msgcontent;
        public String addtime;
    }
}
