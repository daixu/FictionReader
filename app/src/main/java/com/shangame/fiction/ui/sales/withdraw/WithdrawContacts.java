package com.shangame.fiction.ui.sales.withdraw;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.AgentDetailResp;
import com.shangame.fiction.net.response.BaseResp;
import com.shangame.fiction.net.response.WithdrawListResp;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.Map;

/**
 * Create by Speedy on 2018/8/22
 */
public interface WithdrawContacts {

    interface View extends BaseContract.BaseView {
        void getWithdrawListSuccess(WithdrawListResp.DataBean dataBean);

        void getWithdrawListFailure(String msg);

        void withdrawSuccess(BaseResp resp);

        void withdrawFailure(String msg);

        void getAgentIdDetailSuccess(AgentDetailResp.DataBean dataBean, int orderId);

        void getAgentIdDetailFailure(String msg);

        <T> LifecycleTransformer<T> bindToLife();
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getWithdrawList(Map<String, Object> map);

        void withdraw(int agentId, int orderId);

        void getAgentIdDetail(int agentId, int orderId);
    }
}
