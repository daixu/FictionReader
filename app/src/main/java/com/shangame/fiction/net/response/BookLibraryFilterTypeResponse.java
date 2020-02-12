package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2018/8/28
 */
public class BookLibraryFilterTypeResponse {
    public int classid;
    public List<SubclassdataBean> subclassdata;
    public List<WorddataBean> worddata;
    public List<SortdataBean> sortdata;
    public List<SerdataBean> serdata;

    public static class SubclassdataBean {
        public int classid;
        public String classname;
    }

    public static class WorddataBean {
        public int cid;
        public String configname;
    }

    public static class SortdataBean {
        public int cid;
        public String configname;
    }

    public static class SerdataBean {
        public int cid;
        public String configname;
    }
}
