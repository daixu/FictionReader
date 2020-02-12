package com.shangame.fiction.ui.sales.recharge;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.RechargeListResp;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.Map;

/**
 * Create by Speedy on 2018/8/22
 */
public interface RechargeDetailsContacts {

    interface View extends BaseContract.BaseView {
        void getRechargeListSuccess(RechargeListResp.DataBean dataBean);

        void getRechargeListFailure(String msg);

        <T> LifecycleTransformer<T> bindToLife();
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getRechargeList(Map<String, Object> map);
    }
}
