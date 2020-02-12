package com.shangame.fiction.ui.my;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.core.utils.RxUtil;
import com.shangame.fiction.net.BaseSubscriber;
import com.shangame.fiction.net.ExceptionHandle;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.BaseResp;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.storage.model.UserInfo;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/9/13
 */
public class UserInfoPresenter extends RxPresenter<UserInfoContracts.View> implements UserInfoContracts.Presenter<UserInfoContracts.View> {

    @Override
    public void getUserInfo(long userId) {
        Observable<HttpResult<UserInfo>> observable = ApiManager.getInstance().getUserInfo(userId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<UserInfo>>() {
            @Override
            public void accept(HttpResult<UserInfo> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getUserInfoSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void bindAgentId(long userId, int agentId) {
        ApiManager.getInstance().bindAgentId(userId, agentId)
                .compose(RxUtil.<BaseResp>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER_BACK_PRESSURE))
                .compose(mView.<BaseResp>bindToLife())
                .subscribe(new BaseSubscriber<BaseResp>() {
                    @Override
                    public void onNext(BaseResp resp) {
                        if (null != resp) {
                            if (resp.isOk()) {
                                mView.bindAgentIdSuccess(resp);
                            } else if (resp.isNotLogin()) {
                                mView.lunchLoginActivity();
                            } else {
                                mView.bindAgentIdFailure(resp.msg);
                            }
                        } else {
                            mView.bindAgentIdFailure("绑定失败");
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
                        mView.bindAgentIdFailure("绑定失败");
                    }
                });
    }

    @Override
    public void bindQrCode(long userId, int agentId) {
        ApiManager.getInstance().bindQrCode(userId, agentId)
                .compose(RxUtil.<BaseResp>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER_BACK_PRESSURE))
                .compose(mView.<BaseResp>bindToLife())
                .subscribe(new BaseSubscriber<BaseResp>() {
                    @Override
                    public void onNext(BaseResp resp) {
                        if (null != resp) {
                            if (resp.isOk()) {
                                mView.bindQrCodeSuccess(resp);
                            } else if (resp.isNotLogin()) {
                                mView.lunchLoginActivity();
                            } else {
                                mView.bindQrCodeFailure(resp.msg);
                            }
                        } else {
                            mView.bindQrCodeFailure("绑定失败");
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
                        mView.bindQrCodeFailure("绑定失败");
                    }
                });
    }
}
