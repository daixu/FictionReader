package com.shangame.fiction.ui.bookstore;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.FriendReadResponse;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.MaleChannelResponse;
import com.shangame.fiction.net.response.PictureConfigResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/13
 */
public class BookStorePresenter extends RxPresenter<BookStoreContacts.View> implements BookStoreContacts.Presenter<BookStoreContacts.View> {

    @Override
    public void getBookData(int userId, int pageCount, int maleChannel) {
        Observable<HttpResult<MaleChannelResponse>> observable = ApiManager.getInstance().getBookData(userId, pageCount, maleChannel);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<MaleChannelResponse>>() {
            @Override
            public void accept(HttpResult<MaleChannelResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getBookDataSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getFriendRead(long userid, int malechannel, int page, int pagesize, int status) {
        Observable<HttpResult<FriendReadResponse>> observable = ApiManager.getInstance().getFriendRead(userid, malechannel, page, pagesize, status);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<FriendReadResponse>>() {
            @Override
            public void accept(HttpResult<FriendReadResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getFriendReadSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getPictureConfig(int userid, int malechannel) {
        Observable<HttpResult<PictureConfigResponse>> observable = ApiManager.getInstance().getPictureConfig(userid, malechannel);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<PictureConfigResponse>>() {
            @Override
            public void accept(HttpResult<PictureConfigResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getPictureConfigSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }

}
