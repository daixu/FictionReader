package com.shangame.fiction.ui.reader;

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
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.ReadTimeResponse;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.my.pay.PayWebView;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/9/3
 */
public class GetUserInfoPresenter extends RxPresenter<GetUserInfoContracts.View> implements GetUserInfoContracts.Presenter<GetUserInfoContracts.View> {
    private Context mContext;

    public GetUserInfoPresenter(Context context) {
        mContext = context;
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

    @Override
    public void setAddAdvertLog(long userId, long bookId, final long chapterId, final int repOrType) {
        Observable<HttpResult<Object>> observable = ApiManager.getInstance().setAddAdvertLog(userId, bookId, repOrType);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<Object>>() {
            @Override
            public void accept(HttpResult<Object> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.setAddAdvertLogSuccess(chapterId, repOrType);
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void setReceiveLog(long userId, int taskLogId) {
        Observable<HttpResult<TaskAwardResponse>> observable = ApiManager.getInstance().setReceiveLog(userId, taskLogId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<TaskAwardResponse>>() {
            @Override
            public void accept(HttpResult<TaskAwardResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.setReceiveLogSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void sendReadTime(long userId, int type) {
        Observable<HttpResult<ReadTimeResponse>> observable = ApiManager.getInstance().sendReadTime(userId, 0, type);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<ReadTimeResponse>>() {
            @Override
            public void accept(HttpResult<ReadTimeResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.sendReadTimeSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void sendOfflineReadTime(int type) {
        Observable<HttpResult<ReadTimeResponse>> observable = ApiManager.getInstance().sendOfflineReadTime(0, type);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<ReadTimeResponse>>() {
            @Override
            public void accept(HttpResult<ReadTimeResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.sendReadTimeSuccess(result.data);
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
