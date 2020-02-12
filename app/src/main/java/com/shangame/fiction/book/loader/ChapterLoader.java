package com.shangame.fiction.book.loader;

/**
 * 书籍章节加载器
 */
public interface ChapterLoader {

    /**
     * 加载下一章节
     */
    void loadNextChapter(long bookId, long chapterId, boolean showLoading, int type, String method);

    /**
     * 加载上一章节
     */
    void loadBeforeChapter(long bookId, long chapterId);

}