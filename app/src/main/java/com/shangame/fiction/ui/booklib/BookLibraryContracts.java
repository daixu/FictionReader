package com.shangame.fiction.ui.booklib;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.GetBookLibraryTypeResponse;

/**
 * Create by Speedy on 2018/8/28
 */
public interface BookLibraryContracts {

    interface View extends BaseContract.BaseView{
        void getBookLibraryTypeSuccess(GetBookLibraryTypeResponse getBookLibraryTypeResponse);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V>{
        void getBookLibraryType(int userid, int malechannel);
    }
}
