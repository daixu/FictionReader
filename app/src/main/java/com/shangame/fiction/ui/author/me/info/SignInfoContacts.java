package com.shangame.fiction.ui.author.me.info;

import com.shangame.fiction.core.base.BaseContract;

import java.util.Map;

/**
 * Create by Speedy on 2019/7/23
 */
public interface SignInfoContacts {

    interface View extends BaseContract.BaseView {
        void updateSignAuthorSuccess();

        void updateSignAuthorFailure(String msg);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void updateSignAuthor(Map<String, Object> map);
    }
}
