package com.shangame.fiction.ui.bookrack;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.AlbumChapterDetailResponse;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/22
 */
public class BrowseHistoryPresenter extends RxPresenter<BrowseHistoryContacts.View> implements BrowseHistoryContacts.Prestener<BrowseHistoryContacts.View> {

    @Override
    public void getAlbumChapterDetail(long userId, final int albumId, final int cid, String deviceId) {
        Observable<HttpResult<AlbumChapterDetailResponse>> observable = ApiManager.getInstance().getAlbumChapterDetail(userId, albumId, cid, deviceId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<AlbumChapterDetailResponse>>() {
            @Override
            public void accept(HttpResult<AlbumChapterDetailResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getAlbumChapterDetailSuccess(result.data, albumId, cid);
                }
            }
        });
        addSubscribe(disposable);
    }
}
