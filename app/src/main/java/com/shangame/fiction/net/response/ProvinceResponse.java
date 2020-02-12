package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2018/8/27
 */
public class ProvinceResponse {

    public List<Province> areaList;

    public static class Province{
        public String fullName;
        public int fId;
    }
}
