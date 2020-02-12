package com.shangame.fiction.ui.author.works;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.BookDataBean;
import com.shangame.fiction.net.response.HttpResult;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2019/7/23
 */
public class CreateWorksPresenter extends RxPresenter<CreateWorksContacts.View> implements CreateWorksContacts.Presenter<CreateWorksContacts.View> {

    @Override
    public void addBook(Map<String, Object> map) {
        Observable<HttpResult<BookDataBean>> observable = ApiManager.getInstance().addBook(map);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<BookDataBean>>() {
            @Override
            public void accept(HttpResult<BookDataBean> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        mView.addBookSuccess(result.data);
                    } else {
                        mView.addBookFailure(result.msg);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
