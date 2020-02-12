package com.shangame.fiction.net.response;

import com.shangame.fiction.storage.model.BookDetail;
import com.shangame.fiction.storage.model.BookInfoEntity;

import java.util.List;

/**
 * Create by Speedy on 2018/8/13
 */
public class BookDetailResponse {

    public BookDetail detailsdata;
    public int fristchapter;
    public List<BookInfoEntity> likedata;
    public List<BookInfoEntity> clickbook;
    public int bookshelves;//判断是否加入书架 0为未加入，1为已加入
}
