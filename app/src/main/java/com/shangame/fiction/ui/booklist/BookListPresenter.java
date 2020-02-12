package com.shangame.fiction.ui.booklist;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.storage.model.BookListDetailResponse;
import com.shangame.fiction.storage.model.BookListResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2019/3/6
 */
public class BookListPresenter extends RxPresenter<BookListContact.View> implements BookListContact.Prestener<BookListContact.View> {


    @Override
    public void getBookList(long userid, int page, int pagesize) {
        Observable<HttpResult<BookListResponse>> observable =  ApiManager.getInstance().getBookList(userid,page,pagesize);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<BookListResponse>>() {
            @Override
            public void accept(HttpResult<BookListResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if(HttpResultManager.verify(result,mView)){
                        mView.getBookListSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getBookListDetail(long userid, int mid, int page, int pagesize) {
        Observable<HttpResult<BookListDetailResponse>> observable =  ApiManager.getInstance().getBookListDetail(userid,mid,page,pagesize);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<BookListDetailResponse>>() {
            @Override
            public void accept(HttpResult<BookListDetailResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if(HttpResultManager.verify(result,mView)){
                        mView.getBookListDetailSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
