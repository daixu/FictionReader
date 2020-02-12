package com.shangame.fiction.ui.author.works.release;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.AddChapterResponse;
import com.shangame.fiction.net.response.HttpResult;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2019/7/23
 */
public class ConfirmReleasePresenter extends RxPresenter<ConfirmReleaseContacts.View> implements ConfirmReleaseContacts.Presenter<ConfirmReleaseContacts.View> {

    @Override
    public void addChapter(Map<String, Object> map) {
        Observable<HttpResult<AddChapterResponse>> observable = ApiManager.getInstance().addChapter(map);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<AddChapterResponse>>() {
            @Override
            public void accept(HttpResult<AddChapterResponse> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        mView.addChapterSuccess(result.data);
                    } else {
                        mView.addChapterFailure(result.msg);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
