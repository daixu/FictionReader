package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2018/8/28
 */
public class SystemMessageResponse {

    public List<SystemMessageBean> sysentity;

    public static class SystemMessageBean {

        public int id;
        public String title;
        public String msgcontent;
        public String addtime;
    }
}
