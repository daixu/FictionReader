package com.shangame.fiction.ui.my.pay;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.OrderInfoResponse;
import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * Create by Speedy on 2018/8/23
 */
public interface PayCompleteContacts {

    interface View extends BaseContract.BaseView {
        void getOrderInfoSuccess(OrderInfoResponse response);

        void getOrderInfoFailure(String msg);

        <T> LifecycleTransformer<T> bindToLife();
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getOrderInfo(String orderId);
    }
}
