package com.shangame.fiction.ui.my.data;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.GetReadStatusResponse;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.MyCommentResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/24
 */
public class EditDataPresenter extends RxPresenter<EditDataContacts.View> implements EditDataContacts.Presenter<EditDataContacts.View> {

    @Override
    public void getReadStatus(long userId) {
        Observable<HttpResult<GetReadStatusResponse>> observable = ApiManager.getInstance().getReadStatus(userId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<GetReadStatusResponse>>() {
            @Override
            public void accept(HttpResult<GetReadStatusResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getReadStatusSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getCommentList(long userId, int page, int pageSize) {
        Observable<HttpResult<MyCommentResponse>> observable = ApiManager.getInstance().getCommentList(userId, page, pageSize);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<MyCommentResponse>>() {
            @Override
            public void accept(HttpResult<MyCommentResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getCommentListSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }
}
