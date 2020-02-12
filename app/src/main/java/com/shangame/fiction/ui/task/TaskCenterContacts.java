package com.shangame.fiction.ui.task;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.net.response.TaskListResponse;

/**
 * Create by Speedy on 2019/3/28
 */
public interface TaskCenterContacts {
    interface View extends BaseContract.BaseView{
        void getTaskListSuccess(TaskListResponse taskListResponse);
    }


    interface Presenter<V> extends BaseContract.BaserPresenter<V>{
        void getTaskList(long userid);
    }
}
