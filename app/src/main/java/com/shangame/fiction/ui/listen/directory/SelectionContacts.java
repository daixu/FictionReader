package com.shangame.fiction.ui.listen.directory;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.AlbumSelectionResponse;

/**
 * Create by Speedy on 2018/8/13
 */
public interface SelectionContacts {

    interface View extends BaseContract.BaseView {
        void getAlbumSelectionsSuccess(AlbumSelectionResponse response);

        void getAlbumSelectionsFailure(String msg);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getAlbumSelections(int albumId);
    }
}
