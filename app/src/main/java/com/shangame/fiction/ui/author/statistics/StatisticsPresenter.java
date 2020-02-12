package com.shangame.fiction.ui.author.statistics;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.ReportFromResponse;
import com.shangame.fiction.net.response.TimeConfigResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2019/7/23
 */
public class StatisticsPresenter extends RxPresenter<StatisticsContacts.View> implements StatisticsContacts.Presenter<StatisticsContacts.View> {

    @Override
    public void getTimeConfig(int timeType, int bookId) {
        Observable<HttpResult<TimeConfigResponse>> observable = ApiManager.getInstance().getTimeConfig(timeType, bookId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<TimeConfigResponse>>() {
            @Override
            public void accept(HttpResult<TimeConfigResponse> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        mView.getTimeConfigSuccess(result.data);
                    } else {
                        mView.getTimeConfigFailure(result.msg);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getReportFrom(int timeType, int bookId, int source, int dateType, int years, int times) {
        Observable<HttpResult<ReportFromResponse>> observable = ApiManager.getInstance().getReportFrom(timeType, bookId, source, dateType, years, times);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<ReportFromResponse>>() {
            @Override
            public void accept(HttpResult<ReportFromResponse> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        if (null != result.data && null != result.data.timedata && result.data.timedata.size() > 0) {
                            mView.getReportFromSuccess(result.data);
                        } else {
                            mView.getReportFromEmpty();
                        }
                    } else {
                        mView.getReportFromFailure(result.msg);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
