package com.shangame.fiction.net.response;

import com.shangame.fiction.storage.model.BookInfoEntity;

import java.util.List;

/**
 * Create by Speedy on 2019/4/2
 */
public class SearchHintResponse {

    public int shelvercount;
    public List<BookInfoEntity> shelverdata;
    public List<BookInfoEntity> bookdata;

}
