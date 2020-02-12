package com.shangame.fiction.ui.listen.lib;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.AlbumDataResponse;
import com.shangame.fiction.net.response.BookLibraryFilterTypeResponse;

import java.util.Map;

/**
 * Create by Speedy on 2018/8/28
 */
public interface ListenLibraryDetailContracts {

    interface View extends BaseContract.BaseView {
        void getAlbumLibraryTypeSuccess(BookLibraryFilterTypeResponse response);

        void filterAlbumByTypeSuccess(AlbumDataResponse response);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getAlbumLibraryType(int userId, int classId);

        void filterAlbumByType(Map<String, Object> map);
    }
}
