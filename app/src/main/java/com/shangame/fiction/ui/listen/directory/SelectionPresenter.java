package com.shangame.fiction.ui.listen.directory;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.AlbumSelectionResponse;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/13
 */
public class SelectionPresenter extends RxPresenter<SelectionContacts.View> implements SelectionContacts.Presenter<SelectionContacts.View> {

    @Override
    public void getAlbumSelections(int albumId) {
        Observable<HttpResult<AlbumSelectionResponse>> observable = ApiManager.getInstance().getAlbumSelections(albumId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<AlbumSelectionResponse>>() {
            @Override
            public void accept(HttpResult<AlbumSelectionResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getAlbumSelectionsSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }
}
