package com.shangame.fiction.ui.search;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.SearchBookResponse;
import com.shangame.fiction.net.response.SearchHintResponse;

/**
 * Create by Speedy on 2018/7/25
 */
public interface SearchBookContracts {

    interface View extends BaseContract.BaseView {
        void searchBookSuccess(SearchBookResponse response);

        void loadMoreByTypeBookSuccess(SearchBookResponse response);

        void getBookDataPageSuccess(SearchBookResponse response);

        void getSearchHintSuccess(SearchHintResponse response);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void searchBook(long userId, int selType, String keywords, int bookStoreChannel, int page, int pageSize);

        void loadMoreByTypeBook(long userId, int moduleId, int maleChannel, int page, int pageSize);

        void getBookDataPage(long userId, int maleChannel, int status, int page, int pageSize);

        void getSearchHint(long userId, String keywords);
    }
}
