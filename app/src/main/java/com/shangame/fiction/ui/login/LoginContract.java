package com.shangame.fiction.ui.login;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.storage.model.UserInfo;


/**
 * Create by Speedy on 2018/7/19
 */
public interface LoginContract {

    interface View extends BaseContract.BaseView {
        void accountLoginSuccess(UserInfo userInfo);

        void weChatLoginSuccess(UserInfo userInfo);

        void qqLoginSuccess(UserInfo userInfo);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void accountLogin(String username, String password, int agentId);

        void weChatLogin(String code, int agentId);

        void qqLogin(String openId, String accessToken, int agentId);
    }
}
