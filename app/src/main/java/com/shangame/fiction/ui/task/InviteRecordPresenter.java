package com.shangame.fiction.ui.task;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.InviteRecordResponse;
import com.shangame.fiction.net.response.ShareWinRedResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2019/3/29
 */
public class InviteRecordPresenter extends RxPresenter<InviteRecordContacts.View> implements InviteRecordContacts.Presenter<InviteRecordContacts.View>{


    @Override
    public void getInviteRecords(long userid,int inviteid,int page,int pageSize) {
        if (mView != null ) {
            mView.showLoading();
        }

        Observable<HttpResult<InviteRecordResponse>> observable =  ApiManager.getInstance().getInviteRecords(userid,inviteid,page,pageSize);

        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<InviteRecordResponse>>() {
            @Override
            public void accept(HttpResult<InviteRecordResponse> result) throws Exception {
                if (mView != null ) {
                    mView.dismissLoading();
                    if(HttpResultManager.verify(result,mView)){
                        mView.getInviteRecordsSuccess(result.data);
                    }
                }
            }
        });

        addSubscribe(disposable);
    }
}
