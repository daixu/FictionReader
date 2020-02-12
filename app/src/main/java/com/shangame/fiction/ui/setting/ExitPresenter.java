package com.shangame.fiction.ui.setting;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/9/12
 */
public class ExitPresenter extends RxPresenter<ExitContacts.View> implements ExitContacts.Prestener<ExitContacts.View> {

    @Override
    public void exit(long userid) {
        if(mView != null){
            mView.showLoading();
        }
        Observable<HttpResult<Object>> observable =  ApiManager.getInstance().exit(userid);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<Object>>() {
            @Override
            public void accept(HttpResult<Object> result) throws Exception {
                if(mView != null){
                    mView.dismissLoading();
                    if(HttpResultManager.verify(result,mView)){
                        mView.exitSuccess();
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
