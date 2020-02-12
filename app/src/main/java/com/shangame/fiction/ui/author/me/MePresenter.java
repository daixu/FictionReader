package com.shangame.fiction.ui.author.me;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.AuthorInfoResponse;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2019/7/23
 */
public class MePresenter extends RxPresenter<MeContacts.View> implements MeContacts.Presenter<MeContacts.View> {

    @Override
    public void getAuthorInfo(long userId) {
        Observable<HttpResult<AuthorInfoResponse>> observable = ApiManager.getInstance().getAuthorInfo(userId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<AuthorInfoResponse>>() {
            @Override
            public void accept(HttpResult<AuthorInfoResponse> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        mView.getAuthorInfoSuccess(result.data);
                    } else {
                        mView.getAuthorInfoFailure(result.msg);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
