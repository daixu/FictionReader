package com.shangame.fiction.ui.author.works.enter;

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
public class EditContentPresenter extends RxPresenter<EditContentContacts.View> implements EditContentContacts.Presenter<EditContentContacts.View> {

    @Override
    public void deleteChapter(int cid, int bookId, int volume, long userId) {
        Observable<HttpResult<Object>> observable = ApiManager.getInstance().deleteChapter(cid, bookId, volume, userId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<Object>>() {
            @Override
            public void accept(HttpResult<Object> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        mView.deleteChapterSuccess();
                    } else {
                        mView.deleteChapterFailure(result.msg);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void saveChapter(Map<String, Object> map) {
        Observable<HttpResult<AddChapterResponse>> observable = ApiManager.getInstance().addChapter(map);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<AddChapterResponse>>() {
            @Override
            public void accept(HttpResult<AddChapterResponse> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        mView.saveChapterSuccess(result.data);
                    } else {
                        mView.saveChapterFailure(result.msg);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
