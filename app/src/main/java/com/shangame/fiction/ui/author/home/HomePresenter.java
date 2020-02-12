package com.shangame.fiction.ui.author.home;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.NoticeInfoResponse;
import com.shangame.fiction.net.response.PictureConfigResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2019/7/23
 */
public class HomePresenter extends RxPresenter<HomeContacts.View> implements HomeContacts.Presenter<HomeContacts.View> {

    @Override
    public void getPicConfig(long userId) {
        Observable<HttpResult<PictureConfigResponse>> observable = ApiManager.getInstance().getPicConfig(userId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<PictureConfigResponse>>() {
            @Override
            public void accept(HttpResult<PictureConfigResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getPicConfigSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }
}
