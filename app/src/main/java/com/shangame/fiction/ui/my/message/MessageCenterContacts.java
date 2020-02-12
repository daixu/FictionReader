package com.shangame.fiction.ui.my.message;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.SystemMessageResponse;
import com.shangame.fiction.net.response.UpdateMessagetResponse;

/**
 * Create by Speedy on 2018/8/27
 */
public interface MessageCenterContacts {

    interface View extends BaseContract.BaseView{
        void getUpdateMessageSuccess(UpdateMessagetResponse updateMessagetResponse);
        void getSystemMessageSuccess(SystemMessageResponse messageResponse);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V>{
        void getSystemMessage(int userid, int page, int pageSize);
        void getUpdateMessage(int userid, int page, int pageSize);
    }
}
