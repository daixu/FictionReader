package com.shangame.fiction.ui.author.notice;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.BookNoticeInfoResponse;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.NoticeInfoResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2019/7/23
 */
public class NoticeListPresenter extends RxPresenter<NoticeListContacts.View> implements NoticeListContacts.Presenter<NoticeListContacts.View> {

    @Override
    public void getNoticeInfo(int page, int pageSize, int noticeType) {
        Observable<HttpResult<NoticeInfoResponse>> observable = ApiManager.getInstance().getNoticeInfo(page, pageSize, noticeType);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<NoticeInfoResponse>>() {
            @Override
            public void accept(HttpResult<NoticeInfoResponse> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        mView.getNoticeInfoSuccess(result.data);
                    } else {
                        mView.getNoticeInfoFailure(result.msg);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getBookNoticeInfo(int page, int pageSize, int bookId) {
        Observable<HttpResult<BookNoticeInfoResponse>> observable = ApiManager.getInstance().getBookNoticeInfo(page, pageSize, bookId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<BookNoticeInfoResponse>>() {
            @Override
            public void accept(HttpResult<BookNoticeInfoResponse> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        mView.getBookNoticeInfoSuccess(result.data);
                    } else {
                        mView.getBookNoticeInfoFailure(result.msg);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
