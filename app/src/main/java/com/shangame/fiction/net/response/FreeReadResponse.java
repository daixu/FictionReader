package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2019/1/7
 */
public class FreeReadResponse {

    public int newvip;
    public List<FreeReadItem> newdata;

    public static class FreeReadItem{
        public String newviptext;
        public int newtype;//1:会员通知，2签到
    }
}
