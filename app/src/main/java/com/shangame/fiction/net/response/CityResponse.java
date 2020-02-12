package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2018/8/27
 */
public class CityResponse {

    public List<City> areaList;

    public static class City{
        public String fullName;
        public int fId;
    }
}
