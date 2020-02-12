package com.shangame.fiction.ui.login.register;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.storage.model.UserInfo;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 注册Presenter
 * Create by Speedy on 2018/7/23
 */
public class RegisterPresenter extends RxPresenter<RegisterContract.View> implements RegisterContract.Presenter<RegisterContract.View> {

    @Override
    public void register(String phone, String logonPass, String smsCode, int regType, int agentId) {
        if (mView != null) {
            mView.showLoading();
        }
        Observable<HttpResult<UserInfo>> observable = ApiManager.getInstance().regUser(phone, logonPass, smsCode, regType, ApiConstant.Channel.ANDROID, agentId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<UserInfo>>() {
            @Override
            public void accept(HttpResult<UserInfo> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        mView.registerSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void phoneCodeLogin(String phone, String smsCode, int agentId) {
        if (mView != null) {
            mView.showLoading();
        }
        Observable<HttpResult<UserInfo>> observable = ApiManager.getInstance().phoneCodeLogin(phone, smsCode, ApiConstant.Channel.ANDROID, agentId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<UserInfo>>() {
            @Override
            public void accept(HttpResult<UserInfo> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        mView.registerSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
