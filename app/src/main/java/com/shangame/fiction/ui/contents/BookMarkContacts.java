package com.shangame.fiction.ui.contents;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.storage.model.BookMark;

import java.util.List;

/**
 * Create by Speedy on 2018/9/10
 */
public interface BookMarkContacts {

    interface View extends BaseContract.BaseView{
        void addBookMarkSuccess();
        void removeBookMarkSuccess();
        void getBookMarkListSuccess(List<BookMark> bookMarkList);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V>{
        void addBookMark(BookMark bookMark);
        void removeBookMark(BookMark bookMark);
        void getBookMarkList(long userid, long bookid ,int page,int pagesize);
    }
}
