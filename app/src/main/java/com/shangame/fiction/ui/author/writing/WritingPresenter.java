package com.shangame.fiction.ui.author.writing;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.BookAllDataResponse;
import com.shangame.fiction.net.response.BookDataPageResponse;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2019/7/23
 */
public class WritingPresenter extends RxPresenter<WritingContacts.View> implements WritingContacts.Presenter<WritingContacts.View> {

    @Override
    public void getBookAllData(long userId) {
        Observable<HttpResult<BookAllDataResponse>> observable = ApiManager.getInstance().getBookAllData(userId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<BookAllDataResponse>>() {
            @Override
            public void accept(HttpResult<BookAllDataResponse> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        if (null != result.data.bookdata && result.data.bookdata.size() > 0) {
                            mView.getBookAllDataSuccess(result.data);
                        } else {
                            mView.getBookAllDataEmpty();
                        }
                    } else {
                        mView.getBookAllDataFailure(result.msg);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getBookData(long userId, int page, int pageSize, int maleType) {
        Observable<HttpResult<BookDataPageResponse>> observable = ApiManager.getInstance().getBookData(userId, page, pageSize, maleType);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<BookDataPageResponse>>() {
            @Override
            public void accept(HttpResult<BookDataPageResponse> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        if (null != result.data.pagedata && result.data.pagedata.size() > 0) {
                            mView.getBookDataSuccess(result.data);
                        } else {
                            mView.getBookDataEmpty();
                        }
                    } else {
                        mView.getBookDataFailure(result.msg);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
