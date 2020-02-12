package com.shangame.fiction.ui.author.works.enter;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.AddChapterResponse;

import java.util.Map;

/**
 * Create by Speedy on 2019/7/23
 */
public interface EditContentContacts {

    interface View extends BaseContract.BaseView {
        void deleteChapterSuccess();

        void deleteChapterFailure(String msg);

        void saveChapterSuccess(AddChapterResponse bean);

        void saveChapterFailure(String msg);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void deleteChapter(int cid, int bookId, int volume, long userId);

        void saveChapter(Map<String, Object> map);
    }
}
