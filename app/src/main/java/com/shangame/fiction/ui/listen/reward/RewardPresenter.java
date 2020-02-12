package com.shangame.fiction.ui.listen.reward;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.core.manager.Logger;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.GetGiftListConfigResponse;
import com.shangame.fiction.net.response.GiveGiftResponse;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.ReceivedGiftResponse;
import com.shangame.fiction.net.response.TaskAwardResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/29
 */
public class RewardPresenter extends RxPresenter<RewardContracts.View> implements RewardContracts.Presenter<RewardContracts.View> {

    @Override
    public void getGiftConfig(long userId) {
        if (mView != null) {
            mView.showLoading();
        }
        Observable<HttpResult<GetGiftListConfigResponse>> observable = ApiManager.getInstance().getGiftListConfig(userId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<GetGiftListConfigResponse>>() {
            @Override
            public void accept(HttpResult<GetGiftListConfigResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        mView.getGiftConfigSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void rewardGift(long userId, int proPid, int proNumber, long bookId) {
        if (mView != null) {
            mView.showLoading();
        }
        Observable<HttpResult<GiveGiftResponse>> observable = ApiManager.getInstance().giveGift(userId, proPid, proNumber, bookId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<GiveGiftResponse>>() {
            @Override
            public void accept(HttpResult<GiveGiftResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        mView.rewardGiftSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getReceivedGift(long bookId, int page, int pageSize) {
        Observable<HttpResult<ReceivedGiftResponse>> observable = ApiManager.getInstance().getReceivedGiftList(bookId, page, pageSize);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<ReceivedGiftResponse>>() {
            @Override
            public void accept(HttpResult<ReceivedGiftResponse> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        mView.getReceivedGiftSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getTaskAward(long userId, final int taskId, boolean showLoading) {
        if (mView != null && showLoading) {
            mView.showLoading();
        }
        Observable<HttpResult<TaskAwardResponse>> observable = ApiManager.getInstance().getTaskAward(userId, taskId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<TaskAwardResponse>>() {
            @Override
            public void accept(HttpResult<TaskAwardResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        Logger.e("hhh", "getTaskAward result.data= " + result.data);
                        if (result.data != null) {
                            mView.getTaskAwardSuccess(result.data, taskId);
                        }
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
