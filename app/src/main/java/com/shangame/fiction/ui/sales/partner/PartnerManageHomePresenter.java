package com.shangame.fiction.ui.sales.partner;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.core.utils.RxUtil;
import com.shangame.fiction.net.BaseSubscriber;
import com.shangame.fiction.net.ExceptionHandle;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.response.AgentDetailResp;
import com.shangame.fiction.net.response.AgentIdInfoResp;
import com.shangame.fiction.net.response.WithdrawResp;

/**
 * Create by Speedy on 2018/8/22
 */
public class PartnerManageHomePresenter extends RxPresenter<PartnerManageHomeContacts.View> implements PartnerManageHomeContacts.Presenter<PartnerManageHomeContacts.View> {

    @Override
    public void getAgentIdInfo(long userId) {
        ApiManager.getInstance().getAgentIdInfo(userId)
                .compose(RxUtil.<AgentIdInfoResp>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER_BACK_PRESSURE))
                .compose(mView.<AgentIdInfoResp>bindToLife())
                .subscribe(new BaseSubscriber<AgentIdInfoResp>() {
                    @Override
                    public void onNext(AgentIdInfoResp resp) {
                        if (null != resp) {
                            if (resp.isOk()) {
                                if (null != resp.data) {
                                    mView.getAgentIdInfoSuccess(resp.data);
                                } else {
                                    mView.getAgentIdInfoFailure(resp.msg);
                                }
                            } else if (resp.isNotLogin()) {
                                mView.lunchLoginActivity();
                            } else {
                                mView.getAgentIdInfoFailure(resp.msg);
                            }
                        } else {
                            mView.getAgentIdInfoFailure("请求失败");
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
                        mView.getAgentIdInfoFailure("请求失败");
                    }
                });
    }

    @Override
    public void withdraw(int agentId, int orderId) {
        ApiManager.getInstance().withdraw(agentId, orderId)
                .compose(RxUtil.<WithdrawResp>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER_BACK_PRESSURE))
                .compose(mView.<WithdrawResp>bindToLife())
                .subscribe(new BaseSubscriber<WithdrawResp>() {
                    @Override
                    public void onNext(WithdrawResp resp) {
                        if (null != resp) {
                            if (resp.isOk()) {
                                if (null != resp.data) {
                                    mView.withdrawSuccess(resp.data);
                                }
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
    public void getAgentIdDetail(int agentId) {
        ApiManager.getInstance().getAgentIdDetail(agentId)
                .compose(RxUtil.<AgentDetailResp>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER_BACK_PRESSURE))
                .compose(mView.<AgentDetailResp>bindToLife())
                .subscribe(new BaseSubscriber<AgentDetailResp>() {
                    @Override
                    public void onNext(AgentDetailResp resp) {
                        if (null != resp) {
                            if (resp.isOk()) {
                                if (null != resp.data) {
                                    mView.getAgentIdDetailSuccess(resp.data);
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
