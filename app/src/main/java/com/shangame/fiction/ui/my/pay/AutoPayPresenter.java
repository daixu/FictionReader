package com.shangame.fiction.ui.my.pay;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.AutoPayResponse;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2019/1/5
 */
public class AutoPayPresenter extends RxPresenter<AutoPayContracts.View> implements AutoPayContracts.Presenter<AutoPayContracts.View> {

    @Override
    public void getAutoPayList(long userid, int page, int pageSize) {
        Observable<HttpResult<AutoPayResponse>> observable = ApiManager.getInstance().getAutoPayList(userid, page, pageSize);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<AutoPayResponse>>() {
            @Override
            public void accept(HttpResult<AutoPayResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.dismissLoading();
                    mView.getAutoPayListSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void setAutoPay(long userid, final AutoPayResponse.AutoPayItem autoPayItem, final int autorenew) {
        if (mView != null) {
            mView.showLoading();
        }
        Observable<HttpResult<Object>> observable = ApiManager.getInstance().setAutoPay(userid, autoPayItem.bookid, autorenew);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<Object>>() {
            @Override
            public void accept(HttpResult<Object> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.dismissLoading();
                    autoPayItem.autorenew = autorenew;
                    mView.setAutoPaySuccess();
                }
            }
        });
        addSubscribe(disposable);
    }
}
