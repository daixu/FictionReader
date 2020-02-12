package com.shangame.fiction.net.response;

import com.shangame.fiction.storage.model.Chapter;

import java.util.List;

/**
 * Create by Speedy on 2018/8/17
 */
public class ChapterListResponse {
    public int records;
    public int total;
    public int chapternumber;
    public String serstatus;
    public List<Chapter> pagedata;

}
