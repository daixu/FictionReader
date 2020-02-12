package com.shangame.fiction.ui.author.me;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.AuthorInfoResponse;

/**
 * Create by Speedy on 2019/7/23
 */
public interface MeContacts {

    interface View extends BaseContract.BaseView {
        void getAuthorInfoSuccess(AuthorInfoResponse response);

        void getAuthorInfoFailure(String msg);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getAuthorInfo(long userId);
    }
}
