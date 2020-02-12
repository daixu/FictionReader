package com.shangame.fiction.ui.task;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.TaskRecommendBookResponse;

/**
 * Create by Speedy on 2019/3/30
 */
public interface TaskRecommendBookContacts {

    interface View extends BaseContract.BaseView{
        void getTaskRecommendBookSuccess(TaskRecommendBookResponse taskRecommendBookResponse);
    }


    interface Presenter<V> extends BaseContract.BaserPresenter<V>{
        void getTaskRecommendBook(long userid,int moduleId,int page, int pagesize);
    }
}
