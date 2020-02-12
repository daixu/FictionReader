package com.shangame.fiction.ui.setting.feedback;

import com.shangame.fiction.core.base.BaseContract;

/**
 * Create by Speedy on 2018/8/29
 */
public interface FeedbackContacts {



    interface View extends BaseContract.BaseView{
        void feedbackSuccess();
    }


    interface Presenter<V> extends BaseContract.BaserPresenter<V>{
        void feedback(int userid,String msg);
    }
}
