package com.shangame.fiction.ui.listen.more;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.AlbumDataResponse;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/13
 */
public class ListenMorePresenter extends RxPresenter<ListenMoreContacts.View> implements ListenMoreContacts.Presenter<ListenMoreContacts.View> {

    @Override
    public void getAlbumModulePage(long userId, int page, int pageSize, int moduleId) {
        Observable<HttpResult<AlbumDataResponse>> observable = ApiManager.getInstance().getAlbumModulePage(userId, page, pageSize, moduleId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<AlbumDataResponse>>() {
            @Override
            public void accept(HttpResult<AlbumDataResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getAlbumModulePageSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }
}
