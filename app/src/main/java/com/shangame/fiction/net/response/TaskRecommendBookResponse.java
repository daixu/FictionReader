package com.shangame.fiction.net.response;

import java.util.List;

/**
 * Create by Speedy on 2019/3/30
 */
public class TaskRecommendBookResponse {

    public int records;
    public int total;
    public List<TaskRecommendBook> pagedata;

    public static class TaskRecommendBook {
        public int bookid;
        public String bookname;
        public String author;
        public String classname;
        public String serstatus;
        public String synopsis;
        public String bookcover;
        public String wordnumbers;
        public int chapterid;
        public int bookshelves;
    }
}
