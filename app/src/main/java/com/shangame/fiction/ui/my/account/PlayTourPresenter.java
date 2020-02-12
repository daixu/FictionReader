package com.shangame.fiction.ui.my.account;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.PlayTourResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/27
 */
public class PlayTourPresenter extends RxPresenter<PlayTourContracts.View> implements PlayTourContracts.Presenter<PlayTourContracts.View> {


    @Override
    public void getPlayTourList(int userid, int page, int pageSize) {
        Observable<HttpResult<PlayTourResponse>> observable =  ApiManager.getInstance().getPlayTourList(userid,page,pageSize);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<PlayTourResponse>>() {
            @Override
            public void accept(HttpResult<PlayTourResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result,mView)) {
                    mView.getPlayTourListSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }
}
