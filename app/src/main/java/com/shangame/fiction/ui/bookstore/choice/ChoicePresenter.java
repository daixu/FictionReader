package com.shangame.fiction.ui.bookstore.choice;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.core.utils.RxUtil;
import com.shangame.fiction.net.BaseSubscriber;
import com.shangame.fiction.net.ExceptionHandle;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.response.NewsResp;

/**
 * Create by Speedy on 2018/8/13
 */
public class ChoicePresenter extends RxPresenter<ChoiceContacts.View> implements ChoiceContacts.Presenter<ChoiceContacts.View> {

    @Override
    public void getNewMediaList(long userId, int page, int pageSize, int maleChannel) {
        ApiManager.getInstance().getNewMediaList(userId, page, pageSize, maleChannel)
                .compose(RxUtil.<NewsResp>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER_BACK_PRESSURE))
                .compose(mView.<NewsResp>bindToLife())
                .subscribe(new BaseSubscriber<NewsResp>() {
                    @Override
                    public void onNext(NewsResp resp) {
                        if (null != resp) {
                            if (resp.isOk()) {
                                if (null != resp.data) {
                                    mView.getNewMediaListSuccess(resp.data);
                                } else {
                                    mView.getNewMediaListFailure(resp.msg);
                                }
                            } else if (resp.isNotLogin()) {
                                mView.lunchLoginActivity();
                            } else {
                                mView.getNewMediaListFailure(resp.msg);
                            }
                        } else {
                            mView.getNewMediaListFailure("请求失败");
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
                        mView.getNewMediaListFailure("请求失败");
                    }
                });
    }
}
