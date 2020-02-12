package com.shangame.fiction.ui.bookrack;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.FreeReadResponse;
import com.shangame.fiction.storage.model.UserInfo;

/**
 * Create by Speedy on 2019/1/7
 */
public interface FreeReadContacts {

    interface View extends BaseContract.BaseView {
        void getFreeReadInfoSuccess(FreeReadResponse freeReadResponse);

        void getFreeReadPermissionSuccess(UserInfo userInfo);

        void setWifiBookSuccess();

        void setWifiBookFailure(String msg);
    }

    interface Prestener<V> extends BaseContract.BaserPresenter<V> {
        void getFreeReadInfo(long userid);

        void getFreeReadPermission(long userid);

        void setWifiBook(long userId, String bookName);
    }
}
