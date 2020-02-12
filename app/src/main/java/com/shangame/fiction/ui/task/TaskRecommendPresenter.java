package com.shangame.fiction.ui.task;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.TaskRecommendBookResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2019/3/30
 */
public class TaskRecommendPresenter extends RxPresenter<TaskRecommendBookContacts.View> implements TaskRecommendBookContacts.Presenter<TaskRecommendBookContacts.View> {

    @Override
    public void getTaskRecommendBook(long userId, int moduleId, int page, int pageSize) {
        if (mView != null) {
            mView.showLoading();
        }

        Observable<HttpResult<TaskRecommendBookResponse>> observable = ApiManager.getInstance().getTaskRecommendBook(userId, moduleId, page, pageSize);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<TaskRecommendBookResponse>>() {
            @Override
            public void accept(HttpResult<TaskRecommendBookResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        mView.getTaskRecommendBookSuccess(result.data);
                    }
                }
            }
        });

        addSubscribe(disposable);
    }
}
