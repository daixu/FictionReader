package com.shangame.fiction.ui.login.forget;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.storage.model.UserInfo;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/29
 */
public class FindPasswordPresenter extends RxPresenter<FindPaswordContacts.View> implements FindPaswordContacts.Presenter<FindPaswordContacts.View> {


    @Override
    public void findPassword(String account, String loginpass, String smscode) {
        Observable<HttpResult<UserInfo>> observable =  ApiManager.getInstance().findPassword(account,loginpass,smscode);

        if (mView != null) {
            mView.showLoading();
        }

        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<UserInfo>>() {
            @Override
            public void accept(HttpResult<UserInfo> userInfoHttpResult) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if(HttpResultManager.verify(userInfoHttpResult,mView)){
                        mView.findPasswordSuccess();
                    }
                }
            }
        });

        addSubscribe(disposable);
    }
}
