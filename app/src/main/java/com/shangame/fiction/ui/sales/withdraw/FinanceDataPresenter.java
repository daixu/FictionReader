package com.shangame.fiction.ui.sales.withdraw;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.core.utils.RxUtil;
import com.shangame.fiction.net.BaseSubscriber;
import com.shangame.fiction.net.ExceptionHandle;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.response.BaseResp;
import com.shangame.fiction.net.response.CardListResp;

import java.util.Map;

/**
 * Create by Speedy on 2018/8/22
 */
public class FinanceDataPresenter extends RxPresenter<FinanceDataContacts.View> implements FinanceDataContacts.Presenter<FinanceDataContacts.View> {

    @Override
    public void getCardList() {
        ApiManager.getInstance().getCardList()
                .compose(RxUtil.<CardListResp>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER_BACK_PRESSURE))
                .subscribe(new BaseSubscriber<CardListResp>() {
                    @Override
                    public void onNext(CardListResp resp) {
                        if (null != resp) {
                            if (resp.isOk()) {
                                if (null != resp.data) {
                                    if (null != resp.data.cardList) {
                                        mView.getCardListSuccess(resp.data.cardList);
                                    } else {
                                        mView.getCardListFailure("暂无数据");
                                    }
                                } else {
                                    mView.getCardListFailure(resp.msg);
                                }
                            } else if (resp.isNotLogin()) {
                                mView.lunchLoginActivity();
                            } else {
                                mView.getCardListFailure(resp.msg);
                            }
                        } else {
                            mView.getCardListFailure("请求失败");
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
                        mView.getCardListFailure("请求失败");
                    }
                });
    }

    @Override
    public void getCode(String phone) {
        ApiManager.getInstance().getCode(phone)
                .compose(RxUtil.<BaseResp>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER_BACK_PRESSURE))
                .subscribe(new BaseSubscriber<BaseResp>() {
                    @Override
                    public void onNext(BaseResp resp) {
                        if (null != resp) {
                            if (resp.isOk()) {
                                mView.getCodeSuccess();
                            } else if (resp.isNotLogin()) {
                                mView.lunchLoginActivity();
                            } else {
                                mView.getCodeFailure(resp.msg);
                            }
                        } else {
                            mView.getCodeFailure("请求失败");
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
                        mView.getCodeFailure("请求失败");
                    }
                });
    }

    @Override
    public void setAgentIdDetail(Map<String, Object> map) {
        ApiManager.getInstance().setAgentIdDetail(map)
                .compose(RxUtil.<BaseResp>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER_BACK_PRESSURE))
                .subscribe(new BaseSubscriber<BaseResp>() {
                    @Override
                    public void onNext(BaseResp resp) {
                        if (null != resp) {
                            if (resp.isOk()) {
                                mView.setAgentIdDetailSuccess(resp);
                            } else if (resp.isNotLogin()) {
                                mView.lunchLoginActivity();
                            } else {
                                mView.setAgentIdDetailFailure(resp.msg);
                            }
                        } else {
                            mView.setAgentIdDetailFailure("请求失败");
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
                        mView.setAgentIdDetailFailure("请求失败");
                    }
                });
    }
}
