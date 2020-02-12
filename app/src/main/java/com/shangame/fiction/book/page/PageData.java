package com.shangame.fiction.book.page;

import com.shangame.fiction.storage.model.BookCover;

import java.util.List;

/**
 * Create by Speedy on 2018/8/18
 */
public class PageData {
    public int index ;//该页在当前章节中的位置,从0开始算起
    public int currentIndex; // 当前页码
    public int chapterPageSize;//当前章节总页数
    public String chapterName = "第一章：花开富贵";
    public List<Line> lineList;
    public float percent;
    public double bookPercent;

    public long bookId;
    public long beforeChapterId;
    public long chapterId;
    public long nextChapterId;

    public int chapterIndex;

    public boolean isVipPage;//标记是否为免费页
    public boolean isADPage;//标记是否为广告页
    public boolean isCoverPage;//标记是否为封面页
    public boolean hasAdPage;
    public boolean autoPayNextChapter = true;
    public long readmoney;
    public long chapterprice;
    public String bookname;
    public BookCover bookCover;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PageData){
            PageData pageData = (PageData) obj;
            if(this.bookId == pageData.bookId && this.chapterId == pageData.chapterId && this.index == pageData.index){
                return true;
            }
        }
        return super.equals(obj);
    }
}
