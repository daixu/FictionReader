package com.shangame.fiction.ui.bookrack;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.BookRackFilterConfigResponse;
import com.shangame.fiction.net.response.BookRackResponse;

import java.util.Map;

/**
 * Create by Speedy on 2018/8/22
 */
public interface BookFilterContacts {

    public interface View extends BaseContract.BaseView{
        void filterBookSuccess(BookRackResponse bookRackResponse);
        void getFilterConfig(BookRackFilterConfigResponse bookRackFilterConfigResponse);
    }

    public interface Prestener<V> extends BaseContract.BaserPresenter<V>{
        void filterBook(Map<String, Object> map);
        void getFilterConfig(int userid);
    }
}
