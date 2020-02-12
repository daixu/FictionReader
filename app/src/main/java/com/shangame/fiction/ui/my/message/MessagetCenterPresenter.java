package com.shangame.fiction.ui.my.message;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.SystemMessageResponse;
import com.shangame.fiction.net.response.UpdateMessagetResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/27
 */
public class MessagetCenterPresenter extends RxPresenter<MessageCenterContacts.View> implements MessageCenterContacts.Presenter<MessageCenterContacts.View>  {



    @Override
    public void getSystemMessage(int userid, int page, int pageSize) {
        Observable<HttpResult<SystemMessageResponse>> observable =  ApiManager.getInstance().getSystemMessage(userid,page,pageSize);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<SystemMessageResponse>>() {
            @Override
            public void accept(HttpResult<SystemMessageResponse> result) throws Exception {
                if (mView != null) {
                    if(HttpResultManager.verify(result,mView)){
                        mView.getSystemMessageSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getUpdateMessage(int userid, int page, int pageSize) {
        Observable<HttpResult<UpdateMessagetResponse>> observable =  ApiManager.getInstance().getUpdateMessage(userid,page,pageSize);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<UpdateMessagetResponse>>() {
            @Override
            public void accept(HttpResult<UpdateMessagetResponse> result) throws Exception {
                if (mView != null) {
                    if(HttpResultManager.verify(result,mView)){
                        mView.getUpdateMessageSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
