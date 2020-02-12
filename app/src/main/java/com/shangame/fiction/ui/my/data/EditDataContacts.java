package com.shangame.fiction.ui.my.data;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.GetReadStatusResponse;
import com.shangame.fiction.net.response.MyCommentResponse;

/**
 * Create by Speedy on 2018/8/24
 */
public interface EditDataContacts {

    interface View extends BaseContract.BaseView{
        void getCommentListSuccess(MyCommentResponse myCommentResponse);

        void getReadStatusSuccess(GetReadStatusResponse getReadStatusResponse);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V>{
        void getCommentList(long userId, int page, int pageSize);

        void getReadStatus(long userId);
    }
}
