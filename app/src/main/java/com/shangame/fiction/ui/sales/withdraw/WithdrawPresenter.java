package com.shangame.fiction.ui.sales.withdraw;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.core.utils.RxUtil;
import com.shangame.fiction.net.BaseSubscriber;
import com.shangame.fiction.net.ExceptionHandle;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.response.AgentDetailResp;
import com.shangame.fiction.net.response.BaseResp;
import com.shangame.fiction.net.response.WithdrawListResp;

import java.util.Map;

/**
 * Create by Speedy on 2018/8/22
 */
public class WithdrawPresenter extends RxPresenter<WithdrawContacts.View> implements WithdrawContacts.Presenter<WithdrawContacts.View> {

    @Override
    public void getWithdrawList(Map<String, Object> map) {
        ApiManager.getInstance().getWithdrawList(map)
                .compose(RxUtil.<WithdrawListResp>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER_BACK_PRESSURE))
                .compose(mView.<WithdrawListResp>bindToLife())
                .subscribe(new BaseSubscriber<WithdrawListResp>() {
                    @Override
                    public void onNext(WithdrawListResp resp) {
                        if (null != resp) {
                            if (resp.isOk()) {
                                if (null != resp.data) {
                                    mView.getWithdrawListSuccess(resp.data);
                                } else {
                                    mView.getWithdrawListFailure(resp.msg);
                                }
                            } else if (resp.isNotLogin()) {
                                mView.lunchLoginActivity();
                            } else {
                                mView.getWithdrawListFailure(resp.msg);
                            }
                        } else {
                            mView.getWithdrawListFailure("请求失败");
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
                        mView.getWithdrawListFailure("请求失败");
                    }
                });
    }

    @Override
    public void withdraw(int agentId, int orderId) {
        ApiManager.getInstance().withdraw(agentId, orderId)
                .compose(RxUtil.<BaseResp>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER_BACK_PRESSURE))
                .compose(mView.<BaseResp>bindToLife())
                .subscribe(new BaseSubscriber<BaseResp>() {
                    @Override
                    public void onNext(BaseResp resp) {
                        if (null != resp) {
                            if (resp.isOk()) {
                                mView.withdrawSuccess(resp);
                            } else if (resp.isNotLogin()) {
                                mView.lunchLoginActivity();
                            } else {
                                mView.withdrawFailure(resp.msg);
                            }
                        } else {
                            mView.withdrawFailure("提现失败");
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
                        mView.withdrawFailure("提现失败");
                    }
                });
    }

    @Override
    public void getAgentIdDetail(int agentId, final int orderId) {
        ApiManager.getInstance().getAgentIdDetail(agentId)
                .compose(RxUtil.<AgentDetailResp>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER_BACK_PRESSURE))
                .compose(mView.<AgentDetailResp>bindToLife())
                .subscribe(new BaseSubscriber<AgentDetailResp>() {
                    @Override
                    public void onNext(AgentDetailResp resp) {
                        if (null != resp) {
                            if (resp.isOk()) {
                                if (null != resp.data) {
                                    mView.getAgentIdDetailSuccess(resp.data, orderId);
                                } else {
                                    mView.getAgentIdDetailFailure("提现失败");
                                }
                            } else if (resp.isNotLogin()) {
                                mView.lunchLoginActivity();
                            } else {
                                mView.getAgentIdDetailFailure(resp.msg);
                            }
                        } else {
                            mView.getAgentIdDetailFailure("提现失败");
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
                        mView.getAgentIdDetailFailure("提现失败");
                    }
                });
    }
}
