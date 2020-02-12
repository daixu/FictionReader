package com.shangame.fiction.book.loader;

/**
 * 章节加载观察者
 * Create by Speedy on 2018/8/31
 */
public interface ChapterLoaderObserver {

    /**
     * 通知下一章加载完成
     */
    void notifyChapterLoadFinished(int showPageIndex, int type);


    void notifyNextChapterLoadFinished(int newCount, int oldCount);

    /**
     * 通知上一章加载完毕
     *
     * @param newCount
     * @param oldCount
     */
    void notifyBeforeChapterLoadFinished(int newCount, int oldCount);

    void notifyResetPageSuccess();

    void notifyJumpToBookMarkPage(int index);

    void notifyReplaceChargePage();
}
