package com.shangame.fiction.ui.sales.partner;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.AgentDetailResp;
import com.shangame.fiction.net.response.AgentIdInfoResp;
import com.shangame.fiction.net.response.WithdrawResp;
import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * Create by Speedy on 2018/8/22
 */
public interface PartnerManageHomeContacts {

    interface View extends BaseContract.BaseView {
        void getAgentIdInfoSuccess(AgentIdInfoResp.DataBean dataBean);

        void getAgentIdInfoFailure(String msg);

        void withdrawSuccess(WithdrawResp.DataBean dataBean);

        void withdrawFailure(String msg);

        void getAgentIdDetailSuccess(AgentDetailResp.DataBean dataBean);

        void getAgentIdDetailFailure(String msg);

        <T> LifecycleTransformer<T> bindToLife();
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getAgentIdInfo(long userId);

        void withdraw(int agentId, int orderId);

        void getAgentIdDetail(int agentId);
    }
}
