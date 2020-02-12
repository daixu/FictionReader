package com.shangame.fiction.ui.welcome.hobby;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.PickHobbyKindResponse;

/**
 * Create by Speedy on 2018/12/29
 */
public class PickHobbyKindContacts {

    interface View extends BaseContract.BaseView{
        void commitPickHobbyKindSuccess(String kinds,PickHobbyKindResponse pickHobbyKindResponse);
        void commitMaleChannelSuccess();
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V>{

        void commitPickHobbyKind(long userid,String kinds);
        void commitMaleChannel(int malechannel);
    }
}
