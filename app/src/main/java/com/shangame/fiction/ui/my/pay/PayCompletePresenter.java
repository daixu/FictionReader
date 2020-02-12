package com.shangame.fiction.ui.my.pay;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.core.utils.RxUtil;
import com.shangame.fiction.net.BaseSubscriber;
import com.shangame.fiction.net.ExceptionHandle;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.BaseResp;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.OrderInfoResponse;
import com.shangame.fiction.net.response.VipInfoResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/23
 */
public class PayCompletePresenter extends RxPresenter<PayCompleteContacts.View> implements PayCompleteContacts.Presenter<PayCompleteContacts.View>  {

    @Override
    public void getOrderInfo(String orderId) {
        ApiManager.getInstance().getOrderInfo(orderId)
                .compose(RxUtil.<OrderInfoResponse>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER_BACK_PRESSURE))
                .compose(mView.<OrderInfoResponse>bindToLife())
                .subscribe(new BaseSubscriber<OrderInfoResponse>() {
                    @Override
                    public void onNext(OrderInfoResponse resp) {
                        if (null != resp) {
                            if (resp.isOk()) {
                                mView.getOrderInfoSuccess(resp);
                            } else if (resp.isNotLogin()) {
                                mView.lunchLoginActivity();
                            } else {
                                mView.getOrderInfoFailure(resp.msg);
                            }
                        } else {
                            mView.getOrderInfoFailure("请求失败");
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
                        mView.getOrderInfoFailure("请求失败");
                    }
                });
    }
}
