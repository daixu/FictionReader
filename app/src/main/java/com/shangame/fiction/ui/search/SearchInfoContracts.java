package com.shangame.fiction.ui.search;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.SearchInfoResponse;

/**
 * Create by Speedy on 2018/7/25
 */
public interface SearchInfoContracts {

    interface View extends BaseContract.BaseView{
        void getSearchInfoSuccess(SearchInfoResponse response);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V>{
        void getSearchInfo(int userid);
    }
}
