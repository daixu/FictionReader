package com.shangame.fiction.ui.setting.feedback;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/29
 */
public class FeedbackPresenter extends RxPresenter<FeedbackContacts.View> implements FeedbackContacts.Presenter<FeedbackContacts.View> {



    @Override
    public void feedback(int userid, String msg) {
        if(mView != null){
            mView.showLoading();
        }
        Observable<HttpResult<Object>> observable =  ApiManager.getInstance().feedback(userid,msg);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<Object>>() {
            @Override
            public void accept(HttpResult<Object> result) throws Exception {
                if(mView != null){
                    mView.dismissLoading();
                    if( HttpResultManager.verify(result,mView)){
                        mView.feedbackSuccess();
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
