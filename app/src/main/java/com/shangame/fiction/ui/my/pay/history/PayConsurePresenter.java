package com.shangame.fiction.ui.my.pay.history;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.PayConsumeResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/27
 */
public class PayConsurePresenter extends RxPresenter<PayConsumeContacts.View> implements PayConsumeContacts.Presenter<PayConsumeContacts.View> {


    @Override
    public void getConsumeHistoryList(int userid,int type, int page, int pageSize) {
        Observable<HttpResult<PayConsumeResponse>> observable =  ApiManager.getInstance().getConsumeHistoryList(userid,type,page,pageSize);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<PayConsumeResponse>>() {
            @Override
            public void accept(HttpResult<PayConsumeResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result,mView)) {
                    mView.getConsumeHistoryListSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }
}
