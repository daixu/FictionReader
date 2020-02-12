package com.shangame.fiction.ui.wifi;

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
public class WifiBookPresenter extends RxPresenter<WifiBookContacts.View> implements WifiBookContacts.Presenter<WifiBookContacts.View> {

    @Override
    public void setWifiBook(long userId, String bookName) {
        Observable<HttpResult<Object>> observable = ApiManager.getInstance().setWifiBook(userId, bookName);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<Object>>() {
            @Override
            public void accept(HttpResult<Object> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        mView.setWifiBookSuccess();
                    } else {
                        mView.setWifiBookFailure(result.msg);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
