package com.shangame.fiction.book.parser;

import com.shangame.fiction.book.page.PageData;

import java.util.List;

/**
 * 书籍解析器监听器
 * Create by Speedy on 2018/8/31
 */
public interface ChapterParserListener {

    /**
     * 完成解析
     * @param list
     */
    void  parseFinish(List<PageData> list);
}
