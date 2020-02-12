package com.shangame.fiction.ui.bookstore;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.ChoicenessResponse;
import com.shangame.fiction.net.response.HomeStoreResponse;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.OthersLookResponse;
import com.shangame.fiction.net.response.PictureConfigResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/13
 */
public class ChoicenessPresenter extends RxPresenter<ChoicenessContacts.View> implements ChoicenessContacts.Presenter<ChoicenessContacts.View> {


    @Override
    public void othersLookData(int userid, int malechannel, int page, int pagesize, int status) {
        Observable<HttpResult<OthersLookResponse>> observable =  ApiManager.getInstance().othersLookData(userid,malechannel,page,pagesize,status);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<OthersLookResponse>>() {
            @Override
            public void accept(HttpResult<OthersLookResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result,mView)) {
                    mView.getOthersLookDataSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getChoicenessData(int userid, int pagecount) {
        Observable<HttpResult<ChoicenessResponse>> observable =  ApiManager.getInstance().getChoicenessData(userid,pagecount);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<ChoicenessResponse>>() {
            @Override
            public void accept(HttpResult<ChoicenessResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result,mView)) {
                    mView.getChoicenessDataSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }


    @Override
    public void getPictureConfig(int userid,int malechannel) {
        Observable<HttpResult<PictureConfigResponse>> observable =  ApiManager.getInstance().getPictureConfig(userid,malechannel);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<PictureConfigResponse>>() {
            @Override
            public void accept(HttpResult<PictureConfigResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result,mView)) {
                    mView.getPictureConfigSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }


}
