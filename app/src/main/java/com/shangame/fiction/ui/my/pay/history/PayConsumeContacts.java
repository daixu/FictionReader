package com.shangame.fiction.ui.my.pay.history;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.PayConsumeResponse;

/**
 * Create by Speedy on 2018/8/27
 */
public interface PayConsumeContacts {

    interface View extends BaseContract.BaseView{
        void getConsumeHistoryListSuccess(PayConsumeResponse payConsumeResponse);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V>{
        void getConsumeHistoryList(int userid,int type,int page,int pageSize);
    }
}
