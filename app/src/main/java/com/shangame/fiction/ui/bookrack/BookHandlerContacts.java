package com.shangame.fiction.ui.bookrack;

import com.shangame.fiction.core.base.BaseContract;

/**
 * Create by Speedy on 2018/8/22
 */
public interface BookHandlerContacts {

    public interface View extends BaseContract.BaseView{
        void removeFromBookRackSuccess();
    }

    public interface Prestener<V> extends BaseContract.BaserPresenter<V>{
        void removeFromBookRack(long userid,String bookidArr);
    }
}
