package com.shangame.fiction.ui.sales.partner.upgrade;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.BaseResp;
import com.shangame.fiction.net.response.CreatWapOrderResponse;
import com.shangame.fiction.net.response.GetPayMenthodsResponse;
import com.shangame.fiction.net.response.MemberListResp;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.List;
import java.util.Map;

/**
 * Create by Speedy on 2018/8/22
 */
public interface UpgradeLevelContacts {

    interface View extends BaseContract.BaseView {
        void setUpGradeSuccess(BaseResp resp);

        void setUpGradeFailure(String msg);

        void getPayMethodsSuccess(GetPayMenthodsResponse response);

        void wapCreateOrderSuccess(CreatWapOrderResponse response, int payMethod);

        <T> LifecycleTransformer<T> bindToLife();
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void setUpGrade(long userId, int agentId, int agentGrade);

        void getPayMethods();

        void createWapOrder(Map<String, Object> map, int payMethod);
    }
}
