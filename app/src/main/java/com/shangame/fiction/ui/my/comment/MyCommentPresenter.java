package com.shangame.fiction.ui.my.comment;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.MyCommentResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/24
 */
public class MyCommentPresenter extends RxPresenter<MyCommentContacts.View> implements MyCommentContacts.Prestener<MyCommentContacts.View> {


    @Override
    public void getCommentList(int userid, int page, int pagesize) {
        Observable<HttpResult<MyCommentResponse>> observable =  ApiManager.getInstance().getCommentList(userid,page,pagesize);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<MyCommentResponse>>() {
            @Override
            public void accept(HttpResult<MyCommentResponse> result) throws Exception {
                if(mView != null && HttpResultManager.verify(result,mView)){
                    mView.getCommentListSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }
}
