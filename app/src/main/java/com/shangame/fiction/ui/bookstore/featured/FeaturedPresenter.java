package com.shangame.fiction.ui.bookstore.featured;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.ChoicenessResponse;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.OthersLookResponse;
import com.shangame.fiction.net.response.PictureConfigResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/13
 */
public class FeaturedPresenter extends RxPresenter<FeaturedContacts.View> implements FeaturedContacts.Presenter<FeaturedContacts.View> {

    @Override
    public void othersLookData(int userId, int maleChannel, int page, int pageSize, int status) {
        Observable<HttpResult<OthersLookResponse>> observable = ApiManager.getInstance().othersLookData(userId, maleChannel, page, pageSize, status);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<OthersLookResponse>>() {
            @Override
            public void accept(HttpResult<OthersLookResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getOthersLookDataSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getFeaturedData(int userId, int pageCount) {
        Observable<HttpResult<ChoicenessResponse>> observable = ApiManager.getInstance().getChoicenessData(userId, pageCount);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<ChoicenessResponse>>() {
            @Override
            public void accept(HttpResult<ChoicenessResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getFeaturedDataSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getPictureConfig(int userId, int maleChannel) {
        Observable<HttpResult<PictureConfigResponse>> observable = ApiManager.getInstance().getPictureConfig(userId, maleChannel);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<PictureConfigResponse>>() {
            @Override
            public void accept(HttpResult<PictureConfigResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getPictureConfigSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }
}
