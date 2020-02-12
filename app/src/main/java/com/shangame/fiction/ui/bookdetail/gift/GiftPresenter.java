package com.shangame.fiction.ui.bookdetail.gift;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.GetGiftListConfigResponse;
import com.shangame.fiction.net.response.GiveGiftResponse;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.ReceivedGiftResponse;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/29
 */
public class GiftPresenter extends RxPresenter<GiftContracts.View> implements GiftContracts.Presenter<GiftContracts.View> {
    
    
    @Override
    public void getGiftListConfig(long userid) {
        if(mView != null){
            mView.showLoading();
        }
        Observable<HttpResult<GetGiftListConfigResponse>> observable =  ApiManager.getInstance().getGiftListConfig(userid);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<GetGiftListConfigResponse>>() {
            @Override
            public void accept(HttpResult<GetGiftListConfigResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if(HttpResultManager.verify(result,mView)){
                        mView.getGiftListConfigSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void giveGift(long userid, int propid, int pronumber, long bookid) {
        if(mView != null){
            mView.showLoading();
        }
        Observable<HttpResult<GiveGiftResponse>> observable =  ApiManager.getInstance().giveGift(userid,propid,pronumber,bookid);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<GiveGiftResponse>>() {
            @Override
            public void accept(HttpResult<GiveGiftResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if(HttpResultManager.verify(result,mView)){
                        mView.giveGiftSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getReceivedGiftList(long bookid, int page, int pagesize) {
        Observable<HttpResult<ReceivedGiftResponse>> observable =  ApiManager.getInstance().getReceivedGiftList(bookid,page,pagesize);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<ReceivedGiftResponse>>() {
            @Override
            public void accept(HttpResult<ReceivedGiftResponse> result) throws Exception {
                if (mView != null) {
                    if(HttpResultManager.verify(result,mView)){
                        mView.getReceivedGiftListSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
