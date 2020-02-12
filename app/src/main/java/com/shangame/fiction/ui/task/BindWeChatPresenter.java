package com.shangame.fiction.ui.task;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.BindWeChatResponse;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2019/4/3
 */
public class BindWeChatPresenter extends RxPresenter<BindWeChatContacts.View> implements BindWeChatContacts.Presenter<BindWeChatContacts.View> {

    @Override
    public void bindWeChat(long userid, String code, String appid) {
        if (mView != null) {
            mView.showLoading();
        }

        Observable<HttpResult<BindWeChatResponse>> observable = ApiManager.getInstance().bindWeChat(userid, code, appid);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<BindWeChatResponse>>() {
            @Override
            public void accept(HttpResult<BindWeChatResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        mView.bindWeChatSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
