package com.shangame.fiction.ui.login;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.login.LoginContract.Presenter;
import com.shangame.fiction.ui.login.LoginContract.View;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/7/19
 */
public class LoginPresenter extends RxPresenter<View> implements Presenter<View> {

    @Override
    public void accountLogin(String username, String password, int agentId) {
        if (mView != null) {
            mView.showLoading();
        }

        Observable<HttpResult<UserInfo>> observable = ApiManager.getInstance().accountLogin(username, password, agentId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<UserInfo>>() {
            @Override
            public void accept(HttpResult<UserInfo> userInfoHttpResult) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(userInfoHttpResult, mView)) {
                        mView.accountLoginSuccess(userInfoHttpResult.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void weChatLogin(String code, int agentId) {
        Observable<HttpResult<UserInfo>> observable = ApiManager.getInstance().weChatLogin(code, agentId);
        if (mView != null) {
            mView.showLoading();
        }
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<UserInfo>>() {
            @Override
            public void accept(HttpResult<UserInfo> userInfoHttpResult) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(userInfoHttpResult, mView)) {
                        mView.weChatLoginSuccess(userInfoHttpResult.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void qqLogin(String openId, String accessToken, int agentId) {
        Observable<HttpResult<UserInfo>> observable = ApiManager.getInstance().qqLogin(openId, accessToken, agentId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<UserInfo>>() {
            @Override
            public void accept(HttpResult<UserInfo> userInfoHttpResult) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(userInfoHttpResult, mView)) {
                        mView.qqLoginSuccess(userInfoHttpResult.data);
                    }
                }
            }
        });

        addSubscribe(disposable);
    }
}
