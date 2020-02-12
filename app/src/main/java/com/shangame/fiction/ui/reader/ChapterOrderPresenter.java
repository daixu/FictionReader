package com.shangame.fiction.ui.reader;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.ChapterOrderConfigResponse;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/9/3
 */
public class ChapterOrderPresenter extends RxPresenter<ChapterOrderContracts.View> implements ChapterOrderContracts.Presenter<ChapterOrderContracts.View> {
  
  
    @Override
    public void getChapterOrderConfig(long userid, long bookid, long chapterid) {
        Observable<HttpResult<ChapterOrderConfigResponse>> observable =  ApiManager.getInstance().getChapterOrderConfig(userid,bookid,chapterid);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<ChapterOrderConfigResponse>>() {
            @Override
            public void accept(HttpResult<ChapterOrderConfigResponse> result) throws Exception {
                if (mView != null ) {
                    if(HttpResultManager.verify(result,mView)){
                        mView.getChapterOrderConfigSuccess(result.data);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void bugChapterOrder(long userid, long bookid, long chapterid, int subnumber,boolean autoPayNextChapter) {
        if (mView != null ) {
            mView.showLoading();
        }
        Observable<HttpResult<Object>> observable =  ApiManager.getInstance().bugChapterOrder(userid,bookid,chapterid,subnumber,autoPayNextChapter);
        Disposable disposable = HttpResultManager.rxResultHandler(observable,mView, new Consumer<HttpResult<Object>>() {
            @Override
            public void accept(HttpResult<Object> result) throws Exception {
                if (mView != null ) {
                    mView.dismissLoading();
                    if(HttpResultManager.verify(result,mView)){
                        mView.bugChapterOrderSuccess();
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
