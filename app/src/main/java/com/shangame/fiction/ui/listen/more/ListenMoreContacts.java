package com.shangame.fiction.ui.listen.more;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.AlbumDataResponse;

/**
 * Create by Speedy on 2018/8/13
 */
public interface ListenMoreContacts {

    interface View extends BaseContract.BaseView {
        void getAlbumModulePageSuccess(AlbumDataResponse response);

        void getAlbumModulePageFailure(String msg);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getAlbumModulePage(long userId, int page, int pageSize, int moduleId);
    }
}
