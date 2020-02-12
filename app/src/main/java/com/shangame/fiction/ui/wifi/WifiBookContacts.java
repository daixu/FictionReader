package com.shangame.fiction.ui.wifi;

import com.shangame.fiction.core.base.BaseContract;

/**
 * Create by Speedy on 2019/7/23
 */
public interface WifiBookContacts {

    interface View extends BaseContract.BaseView {
        void setWifiBookSuccess();

        void setWifiBookFailure(String msg);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void setWifiBook(long userId, String bookName);
    }
}
