package com.shangame.fiction.ui.sales.reward;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.core.utils.RxUtil;
import com.shangame.fiction.net.BaseSubscriber;
import com.shangame.fiction.net.ExceptionHandle;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.response.RedPaperResp;

/**
 * Create by Speedy on 2018/8/22
 */
public class CashRedEnvelopePresenter extends RxPresenter<CashRedEnvelopeContacts.View> implements CashRedEnvelopeContacts.Presenter<CashRedEnvelopeContacts.View> {

    @Override
    public void setRedPaper(long userId, int agentId) {
        ApiManager.getInstance().setRedPaper(userId, agentId)
                .compose(RxUtil.<RedPaperResp>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER_BACK_PRESSURE))
                .compose(mView.<RedPaperResp>bindToLife())
                .subscribe(new BaseSubscriber<RedPaperResp>() {
                    @Override
                    public void onNext(RedPaperResp resp) {
                        if (null != resp) {
                            if (resp.isOk()) {
                                if (null != resp.data) {
                                    mView.setRedPaperSuccess(resp.data);
                                } else {
                                    mView.setRedPaperFailure("请求失败");
                                }
                            } else if (resp.isNotLogin()) {
                                mView.lunchLoginActivity();
                            } else {
                                mView.setRedPaperFailure(resp.msg);
                            }
                        } else {
                            mView.setRedPaperFailure("请求失败");
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
                        mView.setRedPaperFailure("请求失败");
                    }
                });
    }
}
