package com.shangame.fiction.ui.setting.personal.area;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.CityResponse;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.ProvinceResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/27
 */
public class AreaPresenter extends RxPresenter<AreaContracts.View> implements AreaContracts.Presenter<AreaContracts.View> {


    @Override
    public void getProvinceList() {
        if(mView != null){
            mView.showLoading();
        }
        Observable<HttpResult<ProvinceResponse>> observable =  ApiManager.getInstance().getProvinceList();
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<ProvinceResponse>>() {
            @Override
            public void accept(HttpResult<ProvinceResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if(HttpResultManager.verify(result,mView)){
                        mView.getProvinceListSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }



    @Override
    public void getCityList(int fId) {
        if(mView != null){
            mView.showLoading();
        }
        Observable<HttpResult<CityResponse>> observable =  ApiManager.getInstance().getCityList(fId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<CityResponse>>() {
            @Override
            public void accept(HttpResult<CityResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if(HttpResultManager.verify(result,mView)){
                        mView.getCityListSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
