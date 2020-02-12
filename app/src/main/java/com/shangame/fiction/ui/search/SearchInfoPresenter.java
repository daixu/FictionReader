package com.shangame.fiction.ui.search;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.SearchInfoResponse;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/7/25
 */
public class SearchInfoPresenter extends RxPresenter<SearchInfoContracts.View> implements SearchInfoContracts.Presenter<SearchInfoContracts.View> {


    @Override
    public void getSearchInfo(int userid) {
        Observable<HttpResult<SearchInfoResponse>> observable =  ApiManager.getInstance().getSearchInfo(userid);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<SearchInfoResponse>>() {
            @Override
            public void accept(HttpResult<SearchInfoResponse> result) throws Exception {
                if(mView != null && HttpResultManager.verify(result,mView)){
                    mView.getSearchInfoSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }


}
