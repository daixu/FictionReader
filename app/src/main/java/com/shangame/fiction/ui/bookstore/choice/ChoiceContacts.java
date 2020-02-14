package com.shangame.fiction.ui.bookstore.choice;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.NewsResp;
import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * Create by Speedy on 2018/8/13
 */
public interface ChoiceContacts {

    interface View extends BaseContract.BaseView {
        void getNewMediaListSuccess(NewsResp.DataBean dataBean);

        void getNewMediaListFailure(String msg);

        <T> LifecycleTransformer<T> bindToLife();
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getNewMediaList(long userId, int page, int pageSize, int maleChannel);
    }
}
