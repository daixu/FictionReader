package com.shangame.fiction.ui.listen.lib;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.AlbumDataResponse;
import com.shangame.fiction.net.response.BookLibraryFilterTypeResponse;
import com.shangame.fiction.net.response.HttpResult;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/28
 */
public class ListenLibraryDetailPresenter extends RxPresenter<ListenLibraryDetailContracts.View> implements ListenLibraryDetailContracts.Presenter<ListenLibraryDetailContracts.View> {

    @Override
    public void getAlbumLibraryType(int userId, int classId) {
        Observable<HttpResult<BookLibraryFilterTypeResponse>> observable = ApiManager.getInstance().getAlbumLibraryType(userId, classId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<BookLibraryFilterTypeResponse>>() {
            @Override
            public void accept(HttpResult<BookLibraryFilterTypeResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getAlbumLibraryTypeSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void filterAlbumByType(Map<String, Object> map) {
        Observable<HttpResult<AlbumDataResponse>> observable = ApiManager.getInstance().filterAlbumByType(map);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<AlbumDataResponse>>() {
            @Override
            public void accept(HttpResult<AlbumDataResponse> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        mView.filterAlbumByTypeSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
