package com.shangame.fiction.ui.listen.detail;

import android.util.Log;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.AddToBookResponse;
import com.shangame.fiction.net.response.AlbumChapterDetailResponse;
import com.shangame.fiction.net.response.AlubmDetailResponse;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.TaskAwardResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/13
 */
public class ListenBookDetailPresenter extends RxPresenter<ListenBookDetailContacts.View> implements ListenBookDetailContacts.Presenter<ListenBookDetailContacts.View> {

    @Override
    public void getAlbumDetail(long userId, int albumId) {
        if (null != mView) {
            mView.showLoading();
        }
        Observable<HttpResult<AlubmDetailResponse>> observable = ApiManager.getInstance().getAlbumDetail(userId, albumId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<AlubmDetailResponse>>() {
            @Override
            public void accept(HttpResult<AlubmDetailResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.dismissLoading();
                    mView.getAlbumDetailSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getAlbumChapterDetail(long userId, int albumId, int cid, String deviceId) {
        Observable<HttpResult<AlbumChapterDetailResponse>> observable = ApiManager.getInstance().getAlbumChapterDetail(userId, albumId, cid, deviceId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<AlbumChapterDetailResponse>>() {
            @Override
            public void accept(HttpResult<AlbumChapterDetailResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getAlbumChapterDetailSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void addToBookRack(long userId, final long albumId) {
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
                        mView.addToBookRackSuccess(albumId, result.data.receive);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getTaskAward(long userId, final int taskId, boolean showLoading) {
        if (mView != null && showLoading) {
            mView.showLoading();
        }
        Observable<HttpResult<TaskAwardResponse>> observable = ApiManager.getInstance().getTaskAward(userId, taskId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<TaskAwardResponse>>() {
            @Override
            public void accept(HttpResult<TaskAwardResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        Log.e("hhh", "getTaskAward result.data= " + result.data);
                        if (result.data != null) {
                            mView.getTaskAwardSuccess(result.data, taskId);
                        }
                    }
                }
            }
        });

        addSubscribe(disposable);
    }

    @Override
    public void setAdvertLog(long userId, int albumId) {
        Observable<HttpResult<Object>> observable = ApiManager.getInstance().setAdvertLog(userId, albumId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<Object>>() {
            @Override
            public void accept(HttpResult<Object> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.setAdvertLogSuccess();
                }
            }
        });
        addSubscribe(disposable);
    }
}
