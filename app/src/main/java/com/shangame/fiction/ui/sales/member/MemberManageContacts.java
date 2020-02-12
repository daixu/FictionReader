package com.shangame.fiction.ui.sales.member;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.AgentIdInfoResp;
import com.shangame.fiction.net.response.BaseResp;
import com.shangame.fiction.net.response.MemberListResp;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.List;
import java.util.Map;

/**
 * Create by Speedy on 2018/8/22
 */
public interface MemberManageContacts {

    interface View extends BaseContract.BaseView {
        void getMemberListSuccess(MemberListResp.DataBean dataBean);

        void getMemberListFailure(String msg);

        void setUpGradeSuccess(BaseResp resp);

        void setUpGradeFailure(String msg);

        <T> LifecycleTransformer<T> bindToLife();
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getMemberList(Map<String, Object> map);

        void setUpGrade(long userId, int agentId, int agentGrade);
    }
}
