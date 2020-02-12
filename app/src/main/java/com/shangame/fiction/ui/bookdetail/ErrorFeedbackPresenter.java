package com.shangame.fiction.ui.bookdetail;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.BookDetailResponse;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/13
 */
public class ErrorFeedbackPresenter extends RxPresenter<ErrorFeedbackContact.View> implements ErrorFeedbackContact.Presenter<ErrorFeedbackContact.View> {


    @Override
    public void feedbackError(long bookid, long chapterid,String errortype,String remark) {
        Observable<HttpResult<Object>> observable =  ApiManager.getInstance().feedbackError(bookid,chapterid,errortype,remark);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<Object>>() {
            @Override
            public void accept(HttpResult<Object> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if(HttpResultManager.verify(result,mView)){
                        mView.feedbackErrorSuccess();
                    }
                }
            }
        });
        addSubscribe(disposable);
    }







}
