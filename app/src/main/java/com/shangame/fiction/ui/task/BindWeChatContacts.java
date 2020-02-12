package com.shangame.fiction.ui.task;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.BindWeChatResponse;

/**
 * Create by Speedy on 2019/4/3
 */
public interface BindWeChatContacts {
    interface View extends BaseContract.BaseView{
        void bindWeChatSuccess(BindWeChatResponse inviteRecordResponse);
    }


    interface Presenter<V> extends BaseContract.BaserPresenter<V>{
        void bindWeChat(long userid, String code, String appid);
    }
}
