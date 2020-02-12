package com.shangame.fiction.ui.bookrack;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.BookRackFilterConfigResponse;
import com.shangame.fiction.net.response.BookRackResponse;
import com.shangame.fiction.net.response.HttpResult;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/28
 */
public class BookFilterPresenter extends RxPresenter<BookFilterContacts.View> implements BookFilterContacts.Prestener<BookFilterContacts.View> {

    @Override
    public void getFilterConfig(int userid) {
        Observable<HttpResult<BookRackFilterConfigResponse>> observable = ApiManager.getInstance().getFilterConfig(userid);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<BookRackFilterConfigResponse>>() {
            @Override
            public void accept(HttpResult<BookRackFilterConfigResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        mView.getFilterConfig(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void filterBook(Map<String, Object> map) {
        Observable<HttpResult<BookRackResponse>> observable = ApiManager.getInstance().getFilterBook(map);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<BookRackResponse>>() {
            @Override
            public void accept(HttpResult<BookRackResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        mView.filterBookSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
