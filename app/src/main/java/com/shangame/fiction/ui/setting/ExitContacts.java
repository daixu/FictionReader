package com.shangame.fiction.ui.setting;

import com.shangame.fiction.core.base.BaseContract;

/**
 * Create by Speedy on 2018/9/12
 */
public interface ExitContacts {


    interface View extends BaseContract.BaseView{
        void exitSuccess();
    }

    interface Prestener<V> extends BaseContract.BaserPresenter<V>{
        void exit(long userid);
    }
}
