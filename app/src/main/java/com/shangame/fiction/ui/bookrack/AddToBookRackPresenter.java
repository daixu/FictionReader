package com.shangame.fiction.ui.bookrack;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.AddToBookResponse;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/31
 */
public class AddToBookRackPresenter extends RxPresenter<AddToBookRackContacts.View> implements AddToBookRackContacts.Prestener<AddToBookRackContacts.View> {

    @Override
    public void addToBookRack(long userId, final long bookId, final boolean finishActivity) {
        if (mView != null) {
            mView.showLoading();
        }
        Observable<HttpResult<AddToBookResponse>> observable = ApiManager.getInstance().addToBookRack(userId, bookId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<AddToBookResponse>>() {
            @Override
            public void accept(HttpResult<AddToBookResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        mView.addToBookRackSuccess(finishActivity, bookId, result.data.receive);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void addToAlbumRack(long userId, final long albumId, final boolean finishActivity) {
        if (mView != null) {
            mView.showLoading();
        }
        Observable<HttpResult<AddToBookResponse>> observable = ApiManager.getInstance().addAlbumBookRack(userId, albumId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<AddToBookResponse>>() {
            @Override
            public void accept(HttpResult<AddToBookResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        mView.addToBookRackSuccess(finishActivity, albumId, result.data.receive);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
