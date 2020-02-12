package com.shangame.fiction.ui.my.about;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.VersionCheckResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/9/26
 */
public class VersionCheckPresenter extends RxPresenter<VersionCheckContracts.View> implements VersionCheckContracts.Presenter<VersionCheckContracts.View> {

    @Override
    public void checkNewVersion(long userId, int version) {
        Observable<HttpResult<VersionCheckResponse>> observable = ApiManager.getInstance().checkNewVersion(userId, version);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<VersionCheckResponse>>() {
            @Override
            public void accept(HttpResult<VersionCheckResponse> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        mView.checkNewVersionSuccess(result.data);
                    } else {
                        mView.checkNewVersionFailure(result.msg);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
