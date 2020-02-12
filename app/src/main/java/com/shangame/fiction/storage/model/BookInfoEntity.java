package com.shangame.fiction.storage.model;

/**
 * Create by Speedy on 2018/8/6
 */
public class BookInfoEntity {

    public long bookid;
    public long chapterid;
    public String bookname;
    public String author;
    public String bookcover;
    public String synopsis;
    public String classname;//分类名称
    public String serstatus;//连载状态
    public String wordnumbers;//字数文本
    public String reccount;//字数文本
    public int bookshelves;//0为未加入，1为已加入
    public int booktype;//0为小说作品，1为听书作品
}
