package com.shangame.fiction.ui.booklib;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.BookLibraryFilterTypeResponse;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.LibFilterBookByTypeResponse;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/28
 */
public class BookLibraryDetailPresenter extends RxPresenter<BookLibraryDetailContracts.View> implements BookLibraryDetailContracts.Presenter<BookLibraryDetailContracts.View> {

    @Override
    public void getFilterType(int userid, int classid) {
        Observable<HttpResult<BookLibraryFilterTypeResponse>> observable = ApiManager.getInstance().getFilterType(userid, classid);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<BookLibraryFilterTypeResponse>>() {
            @Override
            public void accept(HttpResult<BookLibraryFilterTypeResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getFilterTypeSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void filterBookByType(Map<String, Object> map) {
        Observable<HttpResult<LibFilterBookByTypeResponse>> observable = ApiManager.getInstance().filterBookByType(map);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<LibFilterBookByTypeResponse>>() {
            @Override
            public void accept(HttpResult<LibFilterBookByTypeResponse> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        mView.filterBookByTypeSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
