package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2018/8/6
 */
public class PictureConfigResponse{

    public List<PicItem> picdata;

    public class PicItem{
        public String imgurl;
        public int bookid;
        public String linkurl;
        public String color;
    }
}
