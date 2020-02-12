package com.shangame.fiction.ui.sales.partner;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2019/7/23
 */
public class InvitationCodePresenter extends RxPresenter<InvitationCodeContacts.View> implements InvitationCodeContacts.Presenter<InvitationCodeContacts.View> {

    @Override
    public void bindAgentId(long userId, int agentId) {
        Observable<HttpResult<Object>> observable = ApiManager.getInstance().bindAgentId2(userId, agentId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<Object>>() {
            @Override
            public void accept(HttpResult<Object> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        mView.bindAgentIdSuccess();
                    } else {
                        mView.bindAgentIdFailure(result.msg);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
