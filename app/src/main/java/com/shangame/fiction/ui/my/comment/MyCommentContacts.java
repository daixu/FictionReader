package com.shangame.fiction.ui.my.comment;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.MyCommentResponse;

/**
 * Create by Speedy on 2018/8/24
 */
public interface MyCommentContacts {

    interface View extends BaseContract.BaseView {
        void getCommentListSuccess(MyCommentResponse myCommentResponse);
    }

    interface Prestener<V> extends BaseContract.BaserPresenter<V> {
        void getCommentList(int userid, int page, int pagesize);
    }
}
