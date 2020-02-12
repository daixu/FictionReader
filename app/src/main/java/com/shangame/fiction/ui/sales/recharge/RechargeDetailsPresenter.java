package com.shangame.fiction.ui.sales.recharge;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.core.utils.RxUtil;
import com.shangame.fiction.net.BaseSubscriber;
import com.shangame.fiction.net.ExceptionHandle;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.response.RechargeListResp;

import java.util.Map;

/**
 * Create by Speedy on 2018/8/22
 */
public class RechargeDetailsPresenter extends RxPresenter<RechargeDetailsContacts.View> implements RechargeDetailsContacts.Presenter<RechargeDetailsContacts.View> {

    @Override
    public void getRechargeList(Map<String, Object> map) {
        ApiManager.getInstance().getRechargeList(map)
                .compose(RxUtil.<RechargeListResp>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER_BACK_PRESSURE))
                .compose(mView.<RechargeListResp>bindToLife())
                .subscribe(new BaseSubscriber<RechargeListResp>() {
                    @Override
                    public void onNext(RechargeListResp resp) {
                        if (null != resp) {
                            if (resp.isOk()) {
                                if (null != resp.data) {
                                    mView.getRechargeListSuccess(resp.data);
                                } else {
                                    mView.getRechargeListFailure(resp.msg);
                                }
                            } else if (resp.isNotLogin()) {
                                mView.lunchLoginActivity();
                            } else {
                                mView.getRechargeListFailure(resp.msg);
                            }
                        } else {
                            mView.getRechargeListFailure("请求失败");
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
                        mView.getRechargeListFailure("请求失败");
                    }
                });
    }
}
