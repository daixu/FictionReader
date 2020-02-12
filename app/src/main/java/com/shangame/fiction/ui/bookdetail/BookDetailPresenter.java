package com.shangame.fiction.ui.bookdetail;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.BookDetailResponse;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/13
 */
public class BookDetailPresenter extends RxPresenter<BookDetailContacts.View> implements BookDetailContacts.Presenter<BookDetailContacts.View> {

    @Override
    public void getBookDetail(long userid, long bookid, int clicktype) {
        Observable<HttpResult<BookDetailResponse>> observable = ApiManager.getInstance().getBookDetail(userid, bookid, clicktype);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<BookDetailResponse>>() {
            @Override
            public void accept(HttpResult<BookDetailResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        mView.getBookDetailSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
