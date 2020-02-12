package com.shangame.fiction.ui.task;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.InviteRecordResponse;

/**
 * Create by Speedy on 2019/3/29
 */
public interface InviteRecordContacts {
    interface View extends BaseContract.BaseView{
        void getInviteRecordsSuccess(InviteRecordResponse inviteRecordResponse);
    }


    interface Presenter<V> extends BaseContract.BaserPresenter<V>{
        void getInviteRecords(long userid,int inviteid,int page,int pageSize);
    }
}
