package com.shangame.fiction.ui.bookrack;

import com.shangame.fiction.core.base.BaseContract;

/**
 * Create by Speedy on 2018/8/31
 */
public interface AddToBookRackContacts {

    interface View extends BaseContract.BaseView {
        void addToBookRackSuccess(boolean finishActivity, long bookId, int receive);
    }

    interface Prestener<V> extends BaseContract.BaserPresenter<V> {
        void addToBookRack(long userId, long bookId, boolean finishActivity);

        void addToAlbumRack(long userId, long albumId, boolean finishActivity);
    }
}
