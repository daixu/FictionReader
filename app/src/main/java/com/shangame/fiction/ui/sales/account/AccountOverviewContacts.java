package com.shangame.fiction.ui.sales.account;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.AgentDetailResp;
import com.shangame.fiction.net.response.SumPriceListResp;
import com.shangame.fiction.net.response.WithdrawResp;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.List;
import java.util.Map;

/**
 * Create by Speedy on 2018/8/22
 */
public interface AccountOverviewContacts {

    interface View extends BaseContract.BaseView {
        void getSumPriceListSuccess(SumPriceListResp.DataBean dataBean);

        void getSumPriceListFailure(String msg);

        void getAgentIdDetailSuccess(AgentDetailResp.DataBean dataBean, int orderId);

        void getAgentIdDetailFailure(String msg);

        void withdrawSuccess(WithdrawResp.DataBean dataBean);

        void withdrawFailure(String msg);

        <T> LifecycleTransformer<T> bindToLife();
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getSumPriceList(Map<String, Object> map);

        void getAgentIdDetail(int agentId, int orderId);

        void withdraw(int agentId, int orderId);
    }
}
