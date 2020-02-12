package com.shangame.fiction.ui.bookdetail;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.BookDetailResponse;

/**
 * Create by Speedy on 2019/6/11
 */
public interface ErrorFeedbackContact {

    interface View extends BaseContract.BaseView{
        void feedbackErrorSuccess();
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V>{

        void feedbackError( long bookid, long chapterid,String errortype,String remark);


    }
}
