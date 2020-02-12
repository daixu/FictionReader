package com.shangame.fiction.ui.login.register;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.storage.model.UserInfo;

/**
 * 注册Contact
 * Create by Speedy on 2018/7/23
 */
public interface RegisterContract {

    interface View extends BaseContract.BaseView {
        void registerSuccess(UserInfo userInfo);

        void phoneCodeLoginSuccess(UserInfo userInfo);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void register(String phone, String logonPass, String smsCode, int regType, int agentId);

        void phoneCodeLogin(String phone, String smsCode, int agentId);
    }
}
