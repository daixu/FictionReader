package com.shangame.fiction.ui.author.me.info;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.FinanceDataResponse;
import com.shangame.fiction.net.response.HttpResult;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2019/7/23
 */
public class FinanceInfoPresenter extends RxPresenter<FinanceInfoContacts.View> implements FinanceInfoContacts.Presenter<FinanceInfoContacts.View> {

    @Override
    public void getFinanceData(long userId) {
        Observable<HttpResult<FinanceDataResponse>> observable = ApiManager.getInstance().getFinanceData(userId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<FinanceDataResponse>>() {
            @Override
            public void accept(HttpResult<FinanceDataResponse> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        mView.getFinanceDataSuccess(result.data);
                    } else {
                        mView.getFinanceDataFailure(result.msg);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void setFinanceData(Map<String, Object> map) {
        Observable<HttpResult<Object>> observable = ApiManager.getInstance().setFinance(map);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<Object>>() {
            @Override
            public void accept(HttpResult<Object> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        mView.setFinanceDataSuccess();
                    } else {
                        mView.setFinanceDataFailure(result.msg);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
