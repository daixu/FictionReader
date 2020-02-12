package com.shangame.fiction.ui.author.works.setting;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.BookDataBean;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.UpLoadImageResponse;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2019/7/23
 */
public class WorksSettingPresenter extends RxPresenter<WorksSettingContacts.View> implements WorksSettingContacts.Presenter<WorksSettingContacts.View> {

    @Override
    public void getBookData(int bookId) {
        Observable<HttpResult<BookDataBean>> observable = ApiManager.getInstance().getBookData(bookId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<BookDataBean>>() {
            @Override
            public void accept(HttpResult<BookDataBean> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        if (null != result.data) {
                            mView.getBookDataSuccess(result.data);
                        } else {
                            mView.getBookDataFailure(result.msg);
                        }
                    } else {
                        mView.getBookDataFailure(result.msg);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void updateBook(Map<String, Object> map) {
        Observable<HttpResult<BookDataBean>> observable = ApiManager.getInstance().updateBook(map);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<BookDataBean>>() {
            @Override
            public void accept(HttpResult<BookDataBean> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        mView.updateBookSuccess();
                    } else {
                        mView.updateBookFailure(result.msg);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void uploadCover(int bookId, String imgPath) {
        if (mView != null) {
            mView.showLoading();
        }
        Observable<HttpResult<UpLoadImageResponse>> observable = ApiManager.getInstance().uploadCover(bookId, imgPath);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<UpLoadImageResponse>>() {
            @Override
            public void accept(HttpResult<UpLoadImageResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        mView.uploadCoverSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
