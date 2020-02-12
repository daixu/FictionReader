package com.shangame.fiction.ui.author.works.enter;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.ChapterResponse;

/**
 * Create by Speedy on 2019/7/23
 */
public interface ChapterListContacts {

    interface View extends BaseContract.BaseView {
        void getChapterSuccess(ChapterResponse response);

        void getChapterEmpty();

        void getChapterFailure(String msg);

        void deleteChapterSuccess();

        void deleteChapterFailure(String msg);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getChapter(long userId, int bookId, int page, int pageSize, int drafts);

        void deleteChapter(int cid, int bookId, int volume, long userId);
    }
}
