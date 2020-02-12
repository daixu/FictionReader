package com.shangame.fiction.ui.bookrack;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.FreeReadResponse;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.storage.model.UserInfo;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2019/1/7
 */
public class FreeReadPresenter extends RxPresenter<FreeReadContacts.View> implements FreeReadContacts.Prestener<FreeReadContacts.View> {

    @Override
    public void getFreeReadInfo(long userid) {
        Observable<HttpResult<FreeReadResponse>> observable = ApiManager.getInstance().getFreeReadInfo(userid);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<FreeReadResponse>>() {
            @Override
            public void accept(HttpResult<FreeReadResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getFreeReadInfoSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getFreeReadPermission(long userid) {
        if (mView != null) {
            mView.showLoading();
        }
        Observable<HttpResult<UserInfo>> observable = ApiManager.getInstance().getFreeReadPermission(userid);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<UserInfo>>() {
            @Override
            public void accept(HttpResult<UserInfo> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        mView.getFreeReadPermissionSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

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
