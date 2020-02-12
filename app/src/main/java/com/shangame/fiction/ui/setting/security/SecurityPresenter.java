package com.shangame.fiction.ui.setting.security;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/29
 */
public class SecurityPresenter extends RxPresenter<SecurityContracts.View> implements SecurityContracts.Presenter<SecurityContracts.View> {

    @Override
    public void sendSecurityCode(String phone) {
        mView.showLoading();
        Observable<HttpResult<String>> observable = ApiManager.getInstance().sendSecurityCode(phone);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<String>>() {
            @Override
            public void accept(HttpResult<String> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        mView.sendSecurityCodeSuccess();
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void checkCode(final String phone, final String code) {
        mView.showLoading();
        Observable<HttpResult<String>> observable = ApiManager.getInstance().checkCode(phone, code);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<String>>() {
            @Override
            public void accept(HttpResult<String> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        mView.checkCodeSuccess(phone, code);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
