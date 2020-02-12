package com.shangame.fiction.ui.reader.local;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.ui.reader.local.page.TxtChapter;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2019/8/12
 */
public class ReadPresenter extends RxPresenter<ReadContract.View> implements ReadContract.Presenter<ReadContract.View> {
    @Override
    public void loadCategory(String bookId) {

    }

    @Override
    public void loadChapter(String bookId, List<TxtChapter> bookChapterList) {

    }

    @Override
    public void getTaskAward(long userId, final int taskId, boolean showLoading) {
        if (mView != null && showLoading) {
            mView.showLoading();
        }

        Observable<HttpResult<TaskAwardResponse>> observable = ApiManager.getInstance().getTaskAward(userId, taskId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<TaskAwardResponse>>() {
            @Override
            public void accept(HttpResult<TaskAwardResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        if (result.data != null) {
                            mView.getTaskAwardSuccess(result.data, taskId);
                        }
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
