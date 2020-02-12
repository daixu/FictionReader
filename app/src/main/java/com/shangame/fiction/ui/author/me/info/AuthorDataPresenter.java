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
public class AuthorDataPresenter extends RxPresenter<AuthorDataContacts.View> implements AuthorDataContacts.Presenter<AuthorDataContacts.View> {

    @Override
    public void updateAuthorInfo(Map<String, Object> map) {
        Observable<HttpResult<Object>> observable = ApiManager.getInstance().updateAuthorInfo(map);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<Object>>() {
            @Override
            public void accept(HttpResult<Object> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        mView.updateAuthorInfoSuccess();
                    } else {
                        mView.updateAuthorInfoFailure(result.msg);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
