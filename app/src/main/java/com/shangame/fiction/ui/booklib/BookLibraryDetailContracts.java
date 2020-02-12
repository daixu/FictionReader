package com.shangame.fiction.ui.booklib;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.BookLibraryFilterTypeResponse;
import com.shangame.fiction.net.response.LibFilterBookByTypeResponse;

import java.util.Map;

/**
 * Create by Speedy on 2018/8/28
 */
public interface BookLibraryDetailContracts {

    interface View extends BaseContract.BaseView{
        void getFilterTypeSuccess(BookLibraryFilterTypeResponse bookLibraryFilterTypeResponse);
        void filterBookByTypeSuccess(LibFilterBookByTypeResponse libFilterBookByTypeResponse);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V>{
        void getFilterType(int userid ,int classid);
        void filterBookByType(Map<String,Object> map);
    }
}
