package com.shangame.fiction.ui.my.pay;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.CreatWapOrderResponse;
import com.shangame.fiction.net.response.GetPayMenthodsResponse;
import com.shangame.fiction.net.response.GetRechargeConfigResponse;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.storage.model.UserInfo;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/9/12
 */
public class PayPresenter extends RxPresenter<PayContracts.View> implements PayContracts.Presenter<PayContracts.View> {

    private Context mContext;

    public PayPresenter(Context context) {
        mContext = context;
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
    public void getRechargeConfig(long userId) {
        if (mView != null) {
            mView.showLoading();
        }
        Observable<HttpResult<GetRechargeConfigResponse>> observable = ApiManager.getInstance().getRechargeConfig(userId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<GetRechargeConfigResponse>>() {
            @Override
            public void accept(HttpResult<GetRechargeConfigResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.dismissLoading();
                    mView.getRechargeConfigSuccess(result.data);
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
