package com.shangame.fiction.ui.reader;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.storage.model.BookMark;
import com.shangame.fiction.storage.model.BookParagraph;
import com.shangame.fiction.storage.model.ChapterInfo;

import java.util.List;

/**
 * Create by Speedy on 2018/7/26
 */
public interface BookLoadContract {

    interface View extends BaseContract.BaseView {
        void getChapterSuccess(int advertOpen, ChapterInfo chapterInfo, List<BookParagraph> bookParagraphList, int type);

        void getNextChapterSuccess(int advertOpen, ChapterInfo chapterInfo, List<BookParagraph> bookParagraphList);

        void getBeforeChapterSuccess(int advertOpen, ChapterInfo chapterInfo, List<BookParagraph> bookParagraphList);

        void getBookMarkChapterSuccess(int advertOpen, ChapterInfo chapterInfo, List<BookParagraph> bookParagraphList, BookMark bookMark);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getChapter(long userId, long bookId, long chapterId, int type);

        void getNextChapter(long userId, long bookId, long chapterId);

        void getBeforeChapter(long userId, long bookId, long chapterId);

        void jumpToBookMarkChapter(long userId, long bookId, long chapterId, BookMark bookMark);
    }
}
