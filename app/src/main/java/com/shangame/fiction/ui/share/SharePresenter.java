package com.shangame.fiction.ui.share;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.ShareResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/9/28
 */
public class SharePresenter extends RxPresenter<ShareContracts.View> implements ShareContracts.Presenter<ShareContracts.View> {

    @Override
    public void getBookShareInfo(long userId, long bookId, long chapterId) {
        if (mView != null) {
            mView.showLoading();
        }
        Observable<HttpResult<ShareResponse>> observable = ApiManager.getInstance().getBookShareInfo(userId, bookId, chapterId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<ShareResponse>>() {
            @Override
            public void accept(HttpResult<ShareResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        mView.getBookShareInfoSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
