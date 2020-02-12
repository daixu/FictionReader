package com.shangame.fiction.ui.author.data;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.storage.model.UserInfo;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2019/7/23
 */
public class DetailDataPresenter extends RxPresenter<DetailDataContacts.View> implements DetailDataContacts.Prestener<DetailDataContacts.View> {

    @Override
    public void setAuthorInfo(Map<String, Object> map) {
        if (mView != null) {
            mView.showLoading();
        }
        Observable<HttpResult<UserInfo>> observable = ApiManager.getInstance().setAuthorInfo(map);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<UserInfo>>() {
            @Override
            public void accept(HttpResult<UserInfo> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        if (null != result.data) {
                            mView.setAuthorInfoSuccess(result.data);
                        } else {
                            mView.setAuthorInfoSuccess();
                        }
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
