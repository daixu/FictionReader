package com.shangame.fiction.ui.author.me.info;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2019/7/23
 */
public class SignInfoPresenter extends RxPresenter<SignInfoContacts.View> implements SignInfoContacts.Presenter<SignInfoContacts.View> {

    @Override
    public void updateSignAuthor(Map<String, Object> map) {
        Observable<HttpResult<Object>> observable = ApiManager.getInstance().updateSignAuthor(map);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<Object>>() {
            @Override
            public void accept(HttpResult<Object> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        mView.updateSignAuthorSuccess();
                    } else {
                        mView.updateSignAuthorFailure(result.msg);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
