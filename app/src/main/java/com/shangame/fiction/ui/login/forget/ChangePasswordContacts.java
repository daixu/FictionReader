package com.shangame.fiction.ui.login.forget;

import com.shangame.fiction.core.base.BaseContract;

/**
 * Create by Speedy on 2018/8/29
 */
public interface ChangePasswordContacts {

    interface View extends BaseContract.BaseView{
        void changePassowrdSuccess();
    }


    interface Presenter<V> extends BaseContract.BaserPresenter<V>{
        void changePassowrd(int userid, String loginpass, String smscode);
    }
}
