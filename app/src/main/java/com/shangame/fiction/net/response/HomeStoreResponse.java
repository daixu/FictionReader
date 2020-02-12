package com.shangame.fiction.net.response;

import com.shangame.fiction.storage.model.BookInfoEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Speedy on 2018/8/13
 */
public class HomeStoreResponse {

    /** 推荐书籍 */
    public List<BookInfoEntity> recdata = new ArrayList<>();

    /**  畅销书籍 */
    public List<BookInfoEntity> subdata = new ArrayList<>();

    /** 新书上架 */
    public List<BookInfoEntity> newdata = new ArrayList<>();

    /** 热门小说 */
    public List<BookInfoEntity> hotdata = new ArrayList<>();

    /** 猜你喜欢 */
    public List<BookInfoEntity> likedata = new ArrayList<>();
}
