package com.shangame.fiction.ui.task;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.TaskAwardResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2019/3/28
 */
public class TaskAwardPresenter extends RxPresenter<TaskAwardContacts.View> implements TaskAwardContacts.Presenter<TaskAwardContacts.View> {

    @Override
    public void getTaskAward(long userid, final int taskid, boolean showLoading) {
        if (mView != null && showLoading) {
            mView.showLoading();
        }

        Observable<HttpResult<TaskAwardResponse>> observable = ApiManager.getInstance().getTaskAward(userid, taskid);

        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<TaskAwardResponse>>() {
            @Override
            public void accept(HttpResult<TaskAwardResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        if (result.data != null) {
                            mView.getTaskAwardSuccess(result.data, taskid);
                        }
                    }
                }
            }
        });

        addSubscribe(disposable);
    }
}
