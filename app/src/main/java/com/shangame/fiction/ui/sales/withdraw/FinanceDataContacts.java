package com.shangame.fiction.ui.sales.withdraw;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.BaseResp;
import com.shangame.fiction.net.response.CardListResp;
import com.shangame.fiction.net.response.RechargeListResp;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.List;
import java.util.Map;

/**
 * Create by Speedy on 2018/8/22
 */
public interface FinanceDataContacts {

    interface View extends BaseContract.BaseView {
        void getCardListSuccess(List<CardListResp.DataBean.CardListBean> dataBean);

        void getCardListFailure(String msg);

        void getCodeSuccess();

        void getCodeFailure(String msg);

        void setAgentIdDetailSuccess(BaseResp resp);

        void setAgentIdDetailFailure(String msg);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getCardList();

        void getCode(String phone);

        void setAgentIdDetail(Map<String, Object> map);
    }
}
