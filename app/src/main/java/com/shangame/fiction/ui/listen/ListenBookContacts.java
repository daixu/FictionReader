package com.shangame.fiction.ui.listen;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.AlbumDataResponse;
import com.shangame.fiction.net.response.AlbumModuleResponse;
import com.shangame.fiction.net.response.PictureConfigResponse;

/**
 * Create by Speedy on 2018/8/13
 */
public interface ListenBookContacts {

    interface View extends BaseContract.BaseView {
        void getAlbumModuleSuccess(AlbumModuleResponse response);

        void getAlbumModuleFailure(String msg);

        void getAlbumDataSuccess(AlbumDataResponse response);

        void getAlbumDataFailure(String msg);

        void getPictureConfigSuccess(PictureConfigResponse response);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getAlbumModule(long userId, int pageCount);

        void getAlbumData(long userId, int page, int pageSize, int status);

        void getPictureConfig(long userId,int maleChannel);
    }
}
