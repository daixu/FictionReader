package com.shangame.fiction.book.render;

import android.support.annotation.NonNull;
import android.util.Log;

import com.shangame.fiction.book.loader.ChapterLoaderObserver;
import com.shangame.fiction.book.page.PageData;
import com.shangame.fiction.storage.model.BookMark;

import java.util.List;

/**
 * Create by Speedy on 2018/8/31
 */
public abstract class RenderAdapter implements ChapterLoaderObserver {

    private ChapterLoaderObserver chapterLoaderObserver;

    public abstract int getPageCount();

    public abstract PageData getItem(int index);

    public abstract List<PageData> getChapterPageData(long chapterId);

    public abstract void addChapterPage(List<PageData> list, int showPageIndex, int type);

    public abstract void addNextChapterPage(List<PageData> list);

    public abstract void addBeforeChapterPage(List<PageData> list);

    public abstract int getPageByProgress(long chapterId, int progress);

    public abstract void jumpToBookMarkPage(List<PageData> list, BookMark bookMark);

    public abstract List<PageData> getPageDataList();

    public abstract void replaceNormalPage(List<PageData> list);

    /**
     * 重置界面
     */
    public abstract void resetPage(PageData pageData);

    public void registerRenderObserver(@NonNull ChapterLoaderObserver chapterLoaderObserver) {
        this.chapterLoaderObserver = chapterLoaderObserver;
    }

    @Override
    public void notifyChapterLoadFinished(int showPageIndex, int type) {
        if (chapterLoaderObserver != null) {
            Log.e("hhh", "type48= " + type);
            chapterLoaderObserver.notifyChapterLoadFinished(showPageIndex, type);
        }
    }

    @Override
    public void notifyNextChapterLoadFinished(int newCount, int oldCount) {
        if (chapterLoaderObserver != null) {
            chapterLoaderObserver.notifyNextChapterLoadFinished(newCount, oldCount);
        }
    }

    @Override
    public void notifyBeforeChapterLoadFinished(int newCount, int oldCount) {
        if (chapterLoaderObserver != null) {
            chapterLoaderObserver.notifyBeforeChapterLoadFinished(newCount, oldCount);
        }
    }

    @Override
    public void notifyResetPageSuccess() {
        if (chapterLoaderObserver != null) {
            chapterLoaderObserver.notifyResetPageSuccess();
        }
    }

    @Override
    public void notifyJumpToBookMarkPage(int index) {
        if (chapterLoaderObserver != null) {
            chapterLoaderObserver.notifyJumpToBookMarkPage(index);
        }
    }

    @Override
    public void notifyReplaceChargePage() {
        if (chapterLoaderObserver != null) {
            chapterLoaderObserver.notifyReplaceChargePage();
        }
    }
}
