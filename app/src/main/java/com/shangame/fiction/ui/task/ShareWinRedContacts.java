package com.shangame.fiction.ui.task;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.GetInviteUrlResponse;
import com.shangame.fiction.net.response.ShareWinRedResponse;
import com.shangame.fiction.net.response.TaskAwardResponse;

/**
 * Create by Speedy on 2019/3/28
 */
public interface ShareWinRedContacts {
    interface View extends BaseContract.BaseView{
        void getShareRuleSuccess(ShareWinRedResponse shareWinRedResponse);
        void getInviteUrlSuccess(GetInviteUrlResponse getInviteUrlResponse);
    }


    interface Presenter<V> extends BaseContract.BaserPresenter<V>{
        void getShareRule(long userid);
        void getInviteUrl(long userid);
    }
}
