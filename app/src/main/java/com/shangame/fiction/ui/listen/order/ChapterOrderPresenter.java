package com.shangame.fiction.ui.listen.order;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.AlbumChapterFigResponse;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/9/3
 */
public class ChapterOrderPresenter extends RxPresenter<ChapterOrderContracts.View> implements ChapterOrderContracts.Presenter<ChapterOrderContracts.View> {

    @Override
    public void getChapterOrderConfig(long userId, long albumId, long chapterId) {
        Observable<HttpResult<AlbumChapterFigResponse>> observable = ApiManager.getInstance().getAlbumChapterConfig(userId, albumId, chapterId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<AlbumChapterFigResponse>>() {
            @Override
            public void accept(HttpResult<AlbumChapterFigResponse> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        mView.getChapterOrderConfigSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void setAlbumSubChapter(long userId, long albumId, long chapterId, int subNumber, boolean autoPayNextChapter) {
        if (mView != null) {
            mView.showLoading();
        }
        Observable<HttpResult<Object>> observable = ApiManager.getInstance().setAlbumSubChapter(userId, albumId, chapterId, subNumber, autoPayNextChapter);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<Object>>() {
            @Override
            public void accept(HttpResult<Object> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        mView.setAlbumSubChapterSuccess();
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
