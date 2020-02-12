package com.shangame.fiction.ui.listen.directory;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.AlbumChapterDetailResponse;
import com.shangame.fiction.net.response.AlbumChapterResponse;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/13
 */
public class DirectoryPresenter extends RxPresenter<DirectoryContacts.View> implements DirectoryContacts.Presenter<DirectoryContacts.View> {

    @Override
    public void getAlbumChapter(long userId, int albumId, int page, int pageSize, int orderBy) {
        Observable<HttpResult<AlbumChapterResponse>> observable = ApiManager.getInstance().getAlbumChapter(userId, albumId, page, pageSize, orderBy);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<AlbumChapterResponse>>() {
            @Override
            public void accept(HttpResult<AlbumChapterResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getAlbumChapterSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getAlbumChapterDetail(long userId, int albumId, int cid, String deviceId, final AlbumChapterResponse.PageDataBean bean) {
        Observable<HttpResult<AlbumChapterDetailResponse>> observable = ApiManager.getInstance().getAlbumChapterDetail(userId, albumId, cid, deviceId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<AlbumChapterDetailResponse>>() {
            @Override
            public void accept(HttpResult<AlbumChapterDetailResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getAlbumChapterDetailSuccess(result.data, bean);
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
