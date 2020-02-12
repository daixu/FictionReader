package com.shangame.fiction.ui.my.about;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.VersionCheckResponse;

/**
 * Create by Speedy on 2018/9/26
 */
public interface VersionCheckContracts {

    interface View extends BaseContract.BaseView {
        void checkNewVersionSuccess(VersionCheckResponse response);

        void checkNewVersionFailure(String msg);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void checkNewVersion(long userid, int version);
    }
}
