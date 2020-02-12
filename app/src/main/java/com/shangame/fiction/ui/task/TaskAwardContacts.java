package com.shangame.fiction.ui.task;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.net.response.TaskListResponse;

/**
 * Create by Speedy on 2019/3/28
 */
public interface TaskAwardContacts {
    interface View extends BaseContract.BaseView{
        void getTaskAwardSuccess(TaskAwardResponse taskAwardResponse,int taskid);
    }


    interface Presenter<V> extends BaseContract.BaserPresenter<V>{
        void getTaskAward(long userid,int taskid,boolean showLoading);
    }
}
