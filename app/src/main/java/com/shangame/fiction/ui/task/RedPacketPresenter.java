package com.shangame.fiction.ui.task;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.CashConfigResponse;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.RedListResponse;
import com.shangame.fiction.net.response.WeChatCashResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2019/3/30
 */
public class RedPacketPresenter extends RxPresenter<RedPacketContacts.View> implements RedPacketContacts.Presenter<RedPacketContacts.View> {

    @Override
    public void getRedList(long userid, String datatime, int page, int pageSize) {
        if (mView != null) {
            mView.showLoading();
        }

        Observable<HttpResult<RedListResponse>> observable = ApiManager.getInstance().getRedList(userid, datatime, page, pageSize);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<RedListResponse>>() {
            @Override
            public void accept(HttpResult<RedListResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        mView.getRedListSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getCashConfig(long userid) {
        if (mView != null) {
            mView.showLoading();
        }

        Observable<HttpResult<CashConfigResponse>> observable = ApiManager.getInstance().getCashConfig(userid);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<CashConfigResponse>>() {
            @Override
            public void accept(HttpResult<CashConfigResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        mView.getCashconfigSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void weChatCash(long userid, int cashid, String appid) {
        if (mView != null) {
            mView.showLoading();
        }
        Observable<HttpResult<WeChatCashResponse>> observable = ApiManager.getInstance().weChatCash(userid, cashid, appid);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<WeChatCashResponse>>() {
            @Override
            public void accept(HttpResult<WeChatCashResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        mView.weChatCashSuccess(result.data, result.msg);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
