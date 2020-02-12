package com.shangame.fiction.ui.contents;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.ChapterDetailResponse;
import com.shangame.fiction.net.response.ChapterListResponse;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/17
 */
public class BookChapterPresenter extends RxPresenter<BookChapterContact.View> implements BookChapterContact.Presenter<BookChapterContact.View> {

    @Override
    public void getChapterList(long userid, long bookId, int page, int pageSize) {
        Observable<HttpResult<ChapterListResponse>> observable = ApiManager.getInstance().getChapterList(userid, bookId, page, pageSize);
        if (mView != null) {
            mView.showLoading();
        }
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<ChapterListResponse>>() {
            @Override
            public void accept(HttpResult<ChapterListResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.dismissLoading();
                    mView.getChapterListSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getChapterDetail(long userid, long bookid, long cid) {
        Observable<HttpResult<ChapterDetailResponse>> observable = ApiManager.getInstance().getChapterDetail(userid, bookid, cid);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<ChapterDetailResponse>>() {
            @Override
            public void accept(HttpResult<ChapterDetailResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getChapterDetailSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }
}
