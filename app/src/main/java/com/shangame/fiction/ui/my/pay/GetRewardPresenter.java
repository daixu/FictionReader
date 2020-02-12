//package com.shangame.fiction.ui.my.pay;
//
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.webkit.WebView;
//
//import com.shangame.fiction.core.base.RxPresenter;
//import com.shangame.fiction.net.api.ApiConstant;
//import com.shangame.fiction.net.api.ApiManager;
//import com.shangame.fiction.net.manager.HttpResultManager;
//import com.shangame.fiction.net.response.CreatWapOrderResponse;
//import com.shangame.fiction.net.response.GetPayMenthodsResponse;
//import com.shangame.fiction.net.response.GetRechargeConfigResponse;
//import com.shangame.fiction.net.response.GetRewardResponse;
//import com.shangame.fiction.net.response.HttpResult;
//import com.shangame.fiction.net.response.TaskAwardResponse;
//
//import java.util.Map;
//
//import io.reactivex.Observable;
//import io.reactivex.disposables.Disposable;
//import io.reactivex.functions.Consumer;
//
///**
// * Create by Speedy on 2018/9/12
// */
//public class GetRewardPresenter extends RxPresenter<GetRewardContracts.View> implements GetRewardContracts.Presenter<GetRewardContracts.View>  {
//
//
//    @Override
//    public void getReward(long userid, int sharetype) {
//        if(mView != null){
//            mView.showLoading();
//        }
//        Observable<HttpResult<TaskAwardResponse>> observable =  ApiManager.getInstance().getTaskAward(userid,sharetype);
//        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<TaskAwardResponse>>() {
//            @Override
//            public void accept(HttpResult<TaskAwardResponse> result) throws Exception {
//                if (mView != null && HttpResultManager.verify(result,mView)) {
//                    mView.dismissLoading();
//                    mView.getRewardSuccess(result.data);
//                }
//            }
//        });
//        addSubscribe(disposable);
//    }
//}
