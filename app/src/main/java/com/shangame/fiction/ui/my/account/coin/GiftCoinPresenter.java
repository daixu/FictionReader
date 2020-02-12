package com.shangame.fiction.ui.my.account.coin;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.CoinSummaryResponse;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2019/1/3
 */
public class GiftCoinPresenter extends RxPresenter<GiftCoinContracts.View> implements GiftCoinContracts.Presenter<GiftCoinContracts.View> {

    @Override
    public void getCoinSummary(long userId) {
        Observable<HttpResult<CoinSummaryResponse>> observable = ApiManager.getInstance().getPropData(userId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<CoinSummaryResponse>>() {
            @Override
            public void accept(HttpResult<CoinSummaryResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getCoinSummarySuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }
}
