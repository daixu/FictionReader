package com.shangame.fiction.ui.login.forget;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.core.utils.RxUtils;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/29
 */
public class ChangePasswordPresenter extends RxPresenter<ChangePasswordContacts.View> implements ChangePasswordContacts.Presenter<ChangePasswordContacts.View>{

    @Override
    public void changePassowrd(int userid, String loginpass, String smscode) {
        Observable<HttpResult<Object>> observable =  ApiManager.getInstance().changePassword(userid,loginpass,smscode);
        Disposable disposable = RxUtils.rxSchedulerHelper(observable)
                .subscribe(new Consumer<HttpResult<Object>>() {
                    @Override
                    public void accept(HttpResult<Object> s) throws Exception {
                        if (mView != null) {
                            mView.dismissLoading();
                            mView.changePassowrdSuccess();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (mView != null) {
                            mView.dismissLoading();
                            mView.showError(throwable);
                        }
                    }
                });

        addSubscribe(disposable);
    }
}
