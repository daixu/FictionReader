package com.shangame.fiction.ui.author.works.enter;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.ChapterResponse;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2019/7/23
 */
public class ChapterListPresenter extends RxPresenter<ChapterListContacts.View> implements ChapterListContacts.Presenter<ChapterListContacts.View> {

    @Override
    public void getChapter(long userId, int bookId, int page, int pageSize, int drafts) {
        Observable<HttpResult<ChapterResponse>> observable = ApiManager.getInstance().getChapter(userId, bookId, page, pageSize, drafts);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<ChapterResponse>>() {
            @Override
            public void accept(HttpResult<ChapterResponse> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        if (null != result.data.pagedata && result.data.pagedata.size() > 0) {
                            mView.getChapterSuccess(result.data);
                        } else {
                            mView.getChapterEmpty();
                        }
                    } else {
                        mView.getChapterFailure(result.msg);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void deleteChapter(int cid, int bookId, int volume, long userId) {
        Observable<HttpResult<Object>> observable = ApiManager.getInstance().deleteChapter(cid, bookId, volume, userId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<Object>>() {
            @Override
            public void accept(HttpResult<Object> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        mView.deleteChapterSuccess();
                    } else {
                        mView.deleteChapterFailure(result.msg);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }
}
