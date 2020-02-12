package com.shangame.fiction.ui.listen;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.AlbumDataResponse;
import com.shangame.fiction.net.response.AlbumModuleResponse;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.PictureConfigResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/13
 */
public class ListenBookPresenter extends RxPresenter<ListenBookContacts.View> implements ListenBookContacts.Presenter<ListenBookContacts.View> {

    @Override
    public void getAlbumModule(long userId, int pageCount) {
        Observable<HttpResult<AlbumModuleResponse>> observable = ApiManager.getInstance().getAlbumModule(userId, pageCount);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<AlbumModuleResponse>>() {
            @Override
            public void accept(HttpResult<AlbumModuleResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getAlbumModuleSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getAlbumData(long userId, int page, int pageSize, int status) {
        Observable<HttpResult<AlbumDataResponse>> observable = ApiManager.getInstance().getAlbumData(userId, page, pageSize, status);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<AlbumDataResponse>>() {
            @Override
            public void accept(HttpResult<AlbumDataResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getAlbumDataSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getPictureConfig(long userId, int maleChannel) {
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
