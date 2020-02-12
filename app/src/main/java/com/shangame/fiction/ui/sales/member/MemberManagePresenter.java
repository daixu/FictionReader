package com.shangame.fiction.ui.sales.member;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.core.utils.RxUtil;
import com.shangame.fiction.net.BaseSubscriber;
import com.shangame.fiction.net.ExceptionHandle;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.response.BaseResp;
import com.shangame.fiction.net.response.MemberListResp;

import java.util.Map;

/**
 * Create by Speedy on 2018/8/22
 */
public class MemberManagePresenter extends RxPresenter<MemberManageContacts.View> implements MemberManageContacts.Presenter<MemberManageContacts.View> {

    @Override
    public void getMemberList(Map<String, Object> map) {
        ApiManager.getInstance().getMemberList(map)
                .compose(RxUtil.<MemberListResp>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER_BACK_PRESSURE))
                .compose(mView.<MemberListResp>bindToLife())
                .subscribe(new BaseSubscriber<MemberListResp>() {
                    @Override
                    public void onNext(MemberListResp resp) {
                        if (null != resp) {
                            if (resp.isOk()) {
                                if (null != resp.data) {
                                    if (null != resp.data.pagedata) {
                                        mView.getMemberListSuccess(resp.data);
                                    } else {
                                        mView.getMemberListFailure("暂无数据");
                                    }
                                } else {
                                    mView.getMemberListFailure(resp.msg);
                                }
                            } else if (resp.isNotLogin()) {
                                mView.lunchLoginActivity();
                            } else {
                                mView.getMemberListFailure(resp.msg);
                            }
                        } else {
                            mView.getMemberListFailure("请求失败");
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
                        mView.getMemberListFailure("请求失败");
                    }
                });
    }

    @Override
    public void setUpGrade(long userId, int agentId, int agentGrade) {
        ApiManager.getInstance().setUpGrade(userId, agentId, agentGrade)
                .compose(RxUtil.<BaseResp>applySchedulers(RxUtil.IO_ON_UI_TRANSFORMER_BACK_PRESSURE))
                .compose(mView.<BaseResp>bindToLife())
                .subscribe(new BaseSubscriber<BaseResp>() {
                    @Override
                    public void onNext(BaseResp resp) {
                        if (null != resp) {
                            if (resp.isOk()) {
                                mView.setUpGradeSuccess(resp);
                            } else if (resp.isNotLogin()) {
                                mView.lunchLoginActivity();
                            } else {
                                mView.setUpGradeFailure(resp.msg);
                            }
                        } else {
                            mView.setUpGradeFailure("请求失败");
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
                        mView.setUpGradeFailure("请求失败");
                    }
                });
    }
}
