package com.shangame.fiction.ui.listen.rank;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.AlbumRankingResponse;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/22
 *
 * @author hhh
 */
public class ListenRankPresenter extends RxPresenter<ListenRankContacts.View> implements ListenRankContacts.Prestener<ListenRankContacts.View> {

    @Override
    public void getAlbumRank(int userId, int dayType) {
        Observable<HttpResult<AlbumRankingResponse>> observable = ApiManager.getInstance().getAlbumRank(userId, dayType);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<AlbumRankingResponse>>() {
            @Override
            public void accept(HttpResult<AlbumRankingResponse> result) throws Exception {
                if (HttpResultManager.verify(result, mView)) {
                    mView.getAlbumRankSuccess(result.data);
                }
            }
        });
        addSubscribe(disposable);
    }
}
