package com.shangame.fiction.net.response;

import com.shangame.fiction.storage.model.BookInfoEntity;

import java.util.List;

/**
 * Create by Speedy on 2018/8/28
 */
public class LibFilterBookByTypeResponse {
    public int records;
    public int total;
    public List<BookInfoEntity> pagedata;

}
