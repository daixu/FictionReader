package com.shangame.fiction.ui.setting.security;

import com.shangame.fiction.core.base.BaseContract;

/**
 * Create by Speedy on 2018/8/29
 */
public interface SecurityContracts {

    interface View extends BaseContract.BaseView{
        void sendSecurityCodeSuccess();
        void checkCodeSuccess(String phone,String code);
    }


    interface Presenter<V> extends BaseContract.BaserPresenter<V>{
        void sendSecurityCode(String phone);
        void checkCode(String phone,String code);
    }
}
