package com.shangame.fiction.ui.author.works;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.BookDataBean;

import java.util.Map;

/**
 * Create by Speedy on 2019/7/23
 */
public interface CreateWorksContacts {

    interface View extends BaseContract.BaseView {
        void addBookSuccess(BookDataBean bean);

        void addBookFailure(String msg);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void addBook(Map<String, Object> map);
    }
}
