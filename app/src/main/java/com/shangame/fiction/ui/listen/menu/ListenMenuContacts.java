package com.shangame.fiction.ui.listen.menu;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.AlbumDataResponse;

/**
 * Create by Speedy on 2018/8/13
 */
public interface ListenMenuContacts {

    interface View extends BaseContract.BaseView {
        void getAlbumDataSuccess(AlbumDataResponse response);

        void getAlbumDataFailure(String msg);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getAlbumData(long userId, int page, int pageSize, int status);
    }
}
