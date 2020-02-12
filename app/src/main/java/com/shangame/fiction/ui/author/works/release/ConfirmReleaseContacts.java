package com.shangame.fiction.ui.author.works.release;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.AddChapterResponse;

import java.util.Map;

/**
 * Create by Speedy on 2019/7/23
 */
public interface ConfirmReleaseContacts {

    interface View extends BaseContract.BaseView {
        void addChapterSuccess(AddChapterResponse bean);

        void addChapterFailure(String msg);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void addChapter(Map<String, Object> map);
    }
}
