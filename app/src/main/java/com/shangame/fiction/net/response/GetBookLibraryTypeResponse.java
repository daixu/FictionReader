package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2018/8/28
 */
public class GetBookLibraryTypeResponse {

    public List<ClassdataBean> classdata;

    public static class ClassdataBean {
        public int classid;
        public String classname;
        public String classimage;
    }
}
