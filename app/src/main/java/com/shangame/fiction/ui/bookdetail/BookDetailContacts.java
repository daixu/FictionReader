package com.shangame.fiction.ui.bookdetail;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.BookDetailResponse;

/**
 * Create by Speedy on 2018/8/13
 */
public interface BookDetailContacts {

    interface View extends BaseContract.BaseView{
        void getBookDetailSuccess(BookDetailResponse bookDetailResponse);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V>{

        void getBookDetail(long userid, long bookid, int clicktype);


    }
}
