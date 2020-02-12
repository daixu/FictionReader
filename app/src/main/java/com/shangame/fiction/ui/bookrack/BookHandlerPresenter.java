package com.shangame.fiction.ui.bookrack;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.EmptyResponse;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/22
 */
public class BookHandlerPresenter extends RxPresenter<BookHandlerContacts.View> implements BookHandlerContacts.Prestener<BookHandlerContacts.View> {

    @Override
    public void removeFromBookRack(long userId, String bookIdArr) {
        if (mView != null) {
            mView.showLoading();
        }
        Observable<HttpResult<EmptyResponse>> observable = ApiManager.getInstance().removeFromBookRack(userId, bookIdArr);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<EmptyResponse>>() {
            @Override
            public void accept(HttpResult<EmptyResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        mView.removeFromBookRackSuccess();
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
