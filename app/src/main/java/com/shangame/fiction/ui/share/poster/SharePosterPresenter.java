package com.shangame.fiction.ui.share.poster;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.core.utils.RxUtil;
import com.shangame.fiction.net.BaseSubscriber;
import com.shangame.fiction.net.ExceptionHandle;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.GetSharePosterResp;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.TaskAwardResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/22
 */
public class SharePosterPresenter extends RxPresenter<SharePosterContacts.View> implements SharePosterContacts.Presenter<SharePosterContacts.View> {

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

    @Override
    public void getSharePoster(int agentId, int tempId) {
        ApiManager.getInstance().getSharePoster(agentId, tempId)
                .compose(RxUtil.<GetSharePosterResp>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER_BACK_PRESSURE))
                .compose(mView.<GetSharePosterResp>bindToLife())
                .subscribe(new BaseSubscriber<GetSharePosterResp>() {
                    @Override
                    public void onNext(GetSharePosterResp resp) {
                        if (null != resp) {
                            if (resp.isOk()) {
                                if (null != resp.data) {
                                    mView.getSharePosterSuccess(resp.data);
                                } else {
                                    mView.getSharePosterFailure("请求失败");
                                }
                            } else if (resp.isNotLogin()) {
                                mView.lunchLoginActivity();
                            } else {
                                mView.getSharePosterFailure(resp.msg);
                            }
                        } else {
                            mView.getSharePosterFailure("请求失败");
                        }
                    }

                    @Override
                    protected void hideDialog() {
                        mView.dismissLoading();
                    }

                    @Override
                    protected void showDialog() {
                        mView.showLoading();
                    }

                    @Override
                    public void onError(ExceptionHandle.ResponseThrowable e) {
                        mView.getSharePosterFailure("请求失败");
                    }
                });
    }
}
