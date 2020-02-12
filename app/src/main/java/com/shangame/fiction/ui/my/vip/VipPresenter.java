package com.shangame.fiction.ui.my.vip;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.VipInfoResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/23
 */
public class VipPresenter extends RxPresenter<VipContacts.View> implements VipContacts.Prestener<VipContacts.View>  {




    @Override
    public void getVipInfo(int userid) {
        Observable<HttpResult<VipInfoResponse>> observable =  ApiManager.getInstance().getVipInfo(userid);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<VipInfoResponse>>() {
            @Override
            public void accept(HttpResult<VipInfoResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result,mView)) {
                        mView.getVipInfoSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }


}
