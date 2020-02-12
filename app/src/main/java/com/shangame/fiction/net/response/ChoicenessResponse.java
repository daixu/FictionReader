package com.shangame.fiction.net.response;

import com.shangame.fiction.storage.model.BookInfoEntity;

import java.util.List;

/**
 * Create by Speedy on 2018/8/13
 */
public class ChoicenessResponse {

    public List<CardataBean> cardata;
    public List<BookInfoEntity> heavydata;
    public List<BookInfoEntity> choicedata;
    public List<BookInfoEntity> recdata;
    public List<BookInfoEntity> hotdata;
    public List<BookInfoEntity> searchdata;
    public List<BookInfoEntity> completedata;
    public List<ClassdataBean> classdata;

    public static class CardataBean {
        public int bookid;
        public String contents;
        public String classname;
    }

    public static class ClassdataBean {

        public int classid;
        public String classname;
        public String classimage;

    }
}
