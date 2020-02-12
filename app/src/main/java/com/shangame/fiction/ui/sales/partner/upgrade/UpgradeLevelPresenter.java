package com.shangame.fiction.ui.sales.partner.upgrade;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.core.utils.RxUtil;
import com.shangame.fiction.net.BaseSubscriber;
import com.shangame.fiction.net.ExceptionHandle;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.BaseResp;
import com.shangame.fiction.net.response.CreatWapOrderResponse;
import com.shangame.fiction.net.response.GetPayMenthodsResponse;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.ui.my.pay.PayWebView;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/22
 */
public class UpgradeLevelPresenter extends RxPresenter<UpgradeLevelContacts.View> implements UpgradeLevelContacts.Presenter<UpgradeLevelContacts.View> {

    private Context mContext;

    public UpgradeLevelPresenter(Context context) {
        mContext = context;
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

    @Override
    public void getPayMethods() {
        if (mView != null) {
            mView.showLoading();
        }
        Observable<HttpResult<GetPayMenthodsResponse>> observable = ApiManager.getInstance().getPayMethods();
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<GetPayMenthodsResponse>>() {
            @Override
            public void accept(HttpResult<GetPayMenthodsResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.dismissLoading();
                    mView.getPayMethodsSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void createWapOrder(Map<String, Object> map, final int payMethod) {
        if (mView != null) {
            mView.showLoading();
        }
        Observable<HttpResult<CreatWapOrderResponse>> observable = ApiManager.getInstance().createWapOrder(map);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<CreatWapOrderResponse>>() {
            @Override
            public void accept(HttpResult<CreatWapOrderResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.dismissLoading();
                    mView.wapCreateOrderSuccess(result.data, payMethod);
                }
            }
        });
        addSubscribe(disposable);
    }

    public void createWapOrder2(String payUrl, Map<String, Object> map, final int payMethod) {
        if (mView != null) {
            mView.showLoading();
        }
        Observable<HttpResult<CreatWapOrderResponse>> observable = ApiManager.getInstance().createWapOrder2(payUrl, map);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<CreatWapOrderResponse>>() {
            @Override
            public void accept(HttpResult<CreatWapOrderResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.dismissLoading();
                    mView.wapCreateOrderSuccess(result.data, payMethod);
                }
            }
        });
        addSubscribe(disposable);
    }

    public void redirectRequest(String url, int payMethod) {
        switch (payMethod) {
            case ApiConstant.PayMethod.WECHAT_SDK:
                break;
            case ApiConstant.PayMethod.BEI_WAP_WECHAT:
                WebView webView = new PayWebView(mContext);
                webView.loadUrl(url);
                break;
            case ApiConstant.PayMethod.BEI_WAP_ALIPAY:
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                break;
            default:
                break;
        }
    }
}
