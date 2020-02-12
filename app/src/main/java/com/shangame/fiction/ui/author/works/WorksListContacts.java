package com.shangame.fiction.ui.author.works;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.BookDataPageResponse;

/**
 * Create by Speedy on 2019/7/23
 */
public interface WorksListContacts {

    interface View extends BaseContract.BaseView {
        void getBookDataSuccess(BookDataPageResponse response);

        void getBookDataEmpty();

        void getBookDataFailure(String msg);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getBookData(long userId, int page, int pageSize, int maleType);
    }
}
