package com.shangame.fiction.ui.task;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.GetInviteUrlResponse;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.ShareWinRedResponse;
import com.shangame.fiction.net.response.TaskAwardResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2019/3/29
 */
public class ShareWinRedPresenter extends RxPresenter<ShareWinRedContacts.View> implements ShareWinRedContacts.Presenter<ShareWinRedContacts.View>{



    @Override
    public void getShareRule(long userid) {
        if (mView != null ) {
            mView.showLoading();
        }

        Observable<HttpResult<ShareWinRedResponse>> observable =  ApiManager.getInstance().getShareRule(userid);

        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<ShareWinRedResponse>>() {
            @Override
            public void accept(HttpResult<ShareWinRedResponse> result) throws Exception {
                if (mView != null ) {
                    mView.dismissLoading();
                    if(HttpResultManager.verify(result,mView)){
                        mView.getShareRuleSuccess(result.data);
                    }
                }
            }
        });

        addSubscribe(disposable);
    }

    @Override
    public void getInviteUrl(long userid) {
        if (mView != null ) {
            mView.showLoading();
        }

        Observable<HttpResult<GetInviteUrlResponse>> observable =  ApiManager.getInstance().getInviteUrl(userid);

        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<GetInviteUrlResponse>>() {
            @Override
            public void accept(HttpResult<GetInviteUrlResponse> result) throws Exception {
                if (mView != null ) {
                    mView.dismissLoading();
                    if(HttpResultManager.verify(result,mView)){
                        mView.getInviteUrlSuccess(result.data);
                    }
                }
            }
        });

        addSubscribe(disposable);
    }
}
