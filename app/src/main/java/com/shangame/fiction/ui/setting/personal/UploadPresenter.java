package com.shangame.fiction.ui.setting.personal;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.UpLoadImageResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/9/12
 */
public class UploadPresenter extends RxPresenter<UploadContacts.View> implements UploadContacts.Prestener<UploadContacts.View> {

    @Override
    public void uploadImage(long userId, String imgPath) {
        if (mView != null) {
            mView.showLoading();
        }
        Observable<HttpResult<UpLoadImageResponse>> observable = ApiManager.getInstance().uploadImage(userId, imgPath);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<UpLoadImageResponse>>() {
            @Override
            public void accept(HttpResult<UpLoadImageResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        mView.uploadImageSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
