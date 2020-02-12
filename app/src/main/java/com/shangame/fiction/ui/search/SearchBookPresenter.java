package com.shangame.fiction.ui.search;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.SearchBookResponse;
import com.shangame.fiction.net.response.SearchHintResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/7/25
 */
public class SearchBookPresenter extends RxPresenter<SearchBookContracts.View> implements SearchBookContracts.Presenter<SearchBookContracts.View> {

    @Override
    public void searchBook(long userId, int selType, String keywords, int bookStoreChannel, int page, int pageSize) {
        Observable<HttpResult<SearchBookResponse>> observable = ApiManager.getInstance().getSearchBook(userId, selType, keywords, bookStoreChannel, page, pageSize);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<SearchBookResponse>>() {
            @Override
            public void accept(HttpResult<SearchBookResponse> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        mView.searchBookSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void loadMoreByTypeBook(long userId, int moduleId, int maleChannel, int page, int pageSize) {
        Observable<HttpResult<SearchBookResponse>> observable = ApiManager.getInstance().loadMoreByTypeBook(userId, moduleId, maleChannel, page, pageSize);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<SearchBookResponse>>() {
            @Override
            public void accept(HttpResult<SearchBookResponse> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        mView.loadMoreByTypeBookSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getSearchHint(long userId, String keywords) {
        Observable<HttpResult<SearchHintResponse>> observable = ApiManager.getInstance().getSearchHint(userId, keywords);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<SearchHintResponse>>() {
            @Override
            public void accept(HttpResult<SearchHintResponse> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        mView.getSearchHintSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getBookDataPage(long userId, int maleChannel, int status, int page, int pageSize) {
        Observable<HttpResult<SearchBookResponse>> observable = ApiManager.getInstance().getBookDataPage(userId, maleChannel, status, page, pageSize);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<SearchBookResponse>>() {
            @Override
            public void accept(HttpResult<SearchBookResponse> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        mView.getBookDataPageSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
