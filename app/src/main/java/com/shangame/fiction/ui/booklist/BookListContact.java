package com.shangame.fiction.ui.booklist;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.storage.model.BookListDetailResponse;
import com.shangame.fiction.storage.model.BookListResponse;

/**
 * Create by Speedy on 2019/3/6
 */
public interface BookListContact {

    public interface View extends BaseContract.BaseView{
        void getBookListSuccess(BookListResponse bookListResponse);
        void getBookListDetailSuccess(BookListDetailResponse bookListDetailResponse);
    }

    public interface Prestener<V> extends BaseContract.BaserPresenter<V>{
        void getBookList(long userid,int page,int pagesize);
        void getBookListDetail(long userid,int mid,int page,int pagesize);
    }
}
