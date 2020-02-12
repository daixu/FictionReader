package com.shangame.fiction.ui.welcome.hobby;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.CoinListResponse;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.PickHobbyKindResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/12/29
 */
public class PickHobbyKindPresenter extends RxPresenter<PickHobbyKindContacts.View> implements PickHobbyKindContacts.Presenter<PickHobbyKindContacts.View> {


    @Override
    public void commitPickHobbyKind(long userid,final String kinds) {
        if(mView != null){
            mView.showLoading();
        }
        Observable<HttpResult<PickHobbyKindResponse>> observable =  ApiManager.getInstance().commitPickHobbyKind(userid,kinds);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<PickHobbyKindResponse>>() {
            @Override
            public void accept(HttpResult<PickHobbyKindResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result,mView)) {
                    mView.dismissLoading();
                    if ( HttpResultManager.verify(result,mView)) {
                        mView.commitPickHobbyKindSuccess(kinds,result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);

    }

    @Override
    public void commitMaleChannel(int malechannel) {
        Observable<HttpResult<Object>> observable =  ApiManager.getInstance().commitMaleChannel(malechannel);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<Object>>() {
            @Override
            public void accept(HttpResult<Object> result) throws Exception {

            }
        });
        addSubscribe(disposable);
    }
}
