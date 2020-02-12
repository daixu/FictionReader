package com.shangame.fiction.ui.login.forget;

import com.shangame.fiction.core.base.BaseContract;

/**
 * Create by Speedy on 2018/8/29
 */
public interface FindPaswordContacts {

    interface View extends BaseContract.BaseView{
        void findPasswordSuccess();
    }


    interface Presenter<V> extends BaseContract.BaserPresenter<V>{
        void findPassword(String account, String loginpass,String smscode);
    }
}
