package com.shangame.fiction.ui.task;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.TaskListResponse;
import com.shangame.fiction.storage.model.UserInfo;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2019/3/28
 */
public class TaskCenterPresenter  extends RxPresenter<TaskCenterContacts.View> implements TaskCenterContacts.Presenter<TaskCenterContacts.View>{


    @Override
    public void getTaskList(long userid) {
        if (mView != null ) {
            mView.showLoading();
        }

        Observable<HttpResult<TaskListResponse>> observable =  ApiManager.getInstance().getTaskList(userid);

        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<TaskListResponse>>() {
            @Override
            public void accept(HttpResult<TaskListResponse> result) throws Exception {
                if (mView != null ) {
                    mView.dismissLoading();
                    if(HttpResultManager.verify(result,mView)){
                        mView.getTaskListSuccess(result.data);
                    }
                }
            }
        });

        addSubscribe(disposable);
    }
}
