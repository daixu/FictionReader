package com.shangame.fiction.storage.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Create by Speedy on 2018/9/15
 */
@Entity
public class BookReadProgress {
    @Id
    public long bookId;
    public long chapterId;
    public int pageIndex;
    public int chapterIndex;
    @Generated(hash = 1525828018)
    public BookReadProgress(long bookId, long chapterId, int pageIndex,
            int chapterIndex) {
        this.bookId = bookId;
        this.chapterId = chapterId;
        this.pageIndex = pageIndex;
        this.chapterIndex = chapterIndex;
    }
    @Generated(hash = 964768386)
    public BookReadProgress() {
    }
    public long getBookId() {
        return this.bookId;
    }
    public void setBookId(long bookId) {
        this.bookId = bookId;
    }
    public long getChapterId() {
        return this.chapterId;
    }
    public void setChapterId(long chapterId) {
        this.chapterId = chapterId;
    }
    public int getPageIndex() {
        return this.pageIndex;
    }
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
    public int getChapterIndex() {
        return this.chapterIndex;
    }
    public void setChapterIndex(int chapterIndex) {
        this.chapterIndex = chapterIndex;
    }

    @Override
    public String toString() {
        return "BookReadProgress{" +
                "bookId=" + bookId +
                ", chapterId=" + chapterId +
                ", pageIndex=" + pageIndex +
                ", chapterIndex=" + chapterIndex +
                '}';
    }
}
