package com.shangame.fiction.ui.booklib;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.GetBookLibraryTypeResponse;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/28
 */
public class BookLibraryPresenter extends RxPresenter<BookLibraryContracts.View> implements BookLibraryContracts.Presenter<BookLibraryContracts.View> {



    @Override
    public void getBookLibraryType(int userid, int malechannel) {
        Observable<HttpResult<GetBookLibraryTypeResponse>> observable =  ApiManager.getInstance().getBookLibraryType(userid,malechannel);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<GetBookLibraryTypeResponse>>() {
            @Override
            public void accept(HttpResult<GetBookLibraryTypeResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result,mView)) {
                    mView.getBookLibraryTypeSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }
}
