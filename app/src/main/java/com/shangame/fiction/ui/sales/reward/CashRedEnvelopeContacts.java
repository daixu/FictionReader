package com.shangame.fiction.ui.sales.reward;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.RedPaperResp;
import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * Create by Speedy on 2018/8/22
 */
public interface CashRedEnvelopeContacts {

    interface View extends BaseContract.BaseView {
        void setRedPaperSuccess(RedPaperResp.DataBean dataBean);

        void setRedPaperFailure(String msg);

        <T> LifecycleTransformer<T> bindToLife();
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void setRedPaper(long userId, int agentId);
    }
}
