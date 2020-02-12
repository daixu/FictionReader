package com.shangame.fiction.ui.bookrack;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.AlbumChapterDetailResponse;
import com.shangame.fiction.net.response.AlbumChapterResponse;
import com.shangame.fiction.net.response.BookRackResponse;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.RecommendBookResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/22
 */
public class BookRackPresenter extends RxPresenter<BookRackContacts.View> implements BookRackContacts.Prestener<BookRackContacts.View> {

    @Override
    public void getBookList(int userid, int malechannel, int page, int pagesize) {
        Observable<HttpResult<BookRackResponse>> observable = ApiManager.getInstance().getBookRackList(userid, malechannel, page, pagesize);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<BookRackResponse>>() {
            @Override
            public void accept(HttpResult<BookRackResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getBookListSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getRecommendBook(long userid, int pagesize) {
        Observable<HttpResult<RecommendBookResponse>> observable = ApiManager.getInstance().getRecommendBook(userid, pagesize);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<RecommendBookResponse>>() {
            @Override
            public void accept(HttpResult<RecommendBookResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getRecommendBookSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }

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
