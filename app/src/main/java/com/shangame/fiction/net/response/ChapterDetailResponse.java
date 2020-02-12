package com.shangame.fiction.net.response;

import com.shangame.fiction.storage.model.BookCover;
import com.shangame.fiction.storage.model.BookParagraph;
import com.shangame.fiction.storage.model.ChapterInfo;

import java.util.List;

/**
 * Create by Speedy on 2018/8/17
 */
public class ChapterDetailResponse {
    public ChapterInfo chaptermode;
    public List<BookParagraph> textdata;

    public int advertopen;  // 广告开关 0：关，1:开
    public int advertpage;  // 广告显示页数控制字段

    public BookCover bookmode;

    public int advertmin;
}
