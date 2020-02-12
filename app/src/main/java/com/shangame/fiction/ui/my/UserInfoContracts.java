package com.shangame.fiction.ui.my;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.BaseResp;
import com.shangame.fiction.storage.model.UserInfo;
import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * Create by Speedy on 2018/9/13
 */
public interface UserInfoContracts {

    interface View extends BaseContract.BaseView {
        void getUserInfoSuccess(UserInfo userInfo);

        void bindAgentIdSuccess(BaseResp resp);

        void bindAgentIdFailure(String msg);

        void bindQrCodeSuccess(BaseResp resp);

        void bindQrCodeFailure(String msg);

        <T> LifecycleTransformer<T> bindToLife();
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getUserInfo(long userId);

        void bindAgentId(long userId, int agentId);

        void bindQrCode(long userId, int agentId);
    }
}
