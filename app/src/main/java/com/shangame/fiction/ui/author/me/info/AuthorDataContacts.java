package com.shangame.fiction.ui.author.me.info;

import com.shangame.fiction.core.base.BaseContract;

import java.util.Map;

/**
 * Create by Speedy on 2019/7/23
 */
public interface AuthorDataContacts {

    interface View extends BaseContract.BaseView {
        void updateAuthorInfoSuccess();

        void updateAuthorInfoFailure(String msg);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void updateAuthorInfo(Map<String, Object> map);
    }
}
