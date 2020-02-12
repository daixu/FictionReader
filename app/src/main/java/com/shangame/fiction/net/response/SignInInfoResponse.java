package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2018/8/24
 */
public class SignInInfoResponse {

    public int todaydays;
    public int sumdays;
    public int days;
    public int todaystate;

    public List<SignInDataBean> signindata;

    public static class SignInDataBean {
        public String day;
        public String state;
        public int intday;
        public int signstate;//0：未签，1：已签
        public String reward;
    }
}
