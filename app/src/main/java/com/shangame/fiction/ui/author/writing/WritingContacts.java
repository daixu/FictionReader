package com.shangame.fiction.ui.author.writing;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.BookAllDataResponse;
import com.shangame.fiction.net.response.BookDataPageResponse;

/**
 * Create by Speedy on 2019/7/23
 */
public interface WritingContacts {

    interface View extends BaseContract.BaseView {
        void getBookAllDataSuccess(BookAllDataResponse response);

        void getBookAllDataEmpty();

        void getBookAllDataFailure(String msg);

        void getBookDataSuccess(BookDataPageResponse response);

        void getBookDataEmpty();

        void getBookDataFailure(String msg);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getBookAllData(long userId);

        void getBookData(long userId, int page, int pageSize, int maleType);
    }
}
