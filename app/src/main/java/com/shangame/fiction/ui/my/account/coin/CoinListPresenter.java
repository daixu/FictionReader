package com.shangame.fiction.ui.my.account.coin;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.CoinListResponse;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2019/1/3
 */
public class CoinListPresenter extends RxPresenter<CoinListContracts.View> implements CoinListContracts.Presenter<CoinListContracts.View> {

    @Override
    public void getCoinList(long userId, int state, int page, int pageSize) {
        Observable<HttpResult<CoinListResponse>> observable = ApiManager.getInstance().getCoinList(userId, state, page, pageSize);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<CoinListResponse>>() {
            @Override
            public void accept(HttpResult<CoinListResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getCoinListSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }


}
