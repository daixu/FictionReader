package com.shangame.fiction.ui.share.poster;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.GetSharePosterResp;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * Create by Speedy on 2018/8/22
 */
public interface SharePosterContacts {

    interface View extends BaseContract.BaseView {
        void getTaskAwardSuccess(TaskAwardResponse response, int taskId);

        void getSharePosterSuccess(GetSharePosterResp.DataBean dataBean);

        void getSharePosterFailure(String msg);

        <T> LifecycleTransformer<T> bindToLife();
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getTaskAward(long userId, int taskId, boolean showLoading);

        void getSharePoster(int agentId, int tempId);
    }
}
