package com.shangame.fiction.ui.reader;

import android.content.Context;
import android.util.Log;

import com.shangame.fiction.ad.ADConfig;
import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.manager.AppSetting;
import com.shangame.fiction.core.manager.Logger;
import com.shangame.fiction.core.utils.RxUtils;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.ChapterDetailResponse;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.storage.db.BookParagraphDao;
import com.shangame.fiction.storage.db.ChapterInfoDao;
import com.shangame.fiction.storage.manager.DbManager;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.BookMark;
import com.shangame.fiction.storage.model.BookParagraph;
import com.shangame.fiction.storage.model.ChapterInfo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by Speedy on 2018/7/26
 */
public class BookLoadPresenter extends RxPresenter<BookLoadContract.View> implements BookLoadContract.Presenter<BookLoadContract.View> {

    private static final String TAG = "BookLoadPresenter";

    private Context mContext;

    public BookLoadPresenter(Context context) {
        this.mContext = context;
    }

    @Override
    public void getChapter(final long userid, final long bookid, final long chapterid, final int type) {
        queryChapter(bookid, chapterid, new OnQueryChapterCallback() {
            @Override
            public void onQuerySuccess(ChapterDetailResponse chapterDetailResponse) {
                Log.e("hhh", "type54= " + type);
                mView.getChapterSuccess(chapterDetailResponse.advertopen, chapterDetailResponse.chaptermode, chapterDetailResponse.textdata, type);
            }

            @Override
            public void onQueryFailed() {
                long userid = UserInfoManager.getInstance(mContext).getUserid();
                Log.e("hhh", "type60= " + type);
                requestChapter(userid, bookid, chapterid, type);
            }
        });
    }

    @Override
    public void getNextChapter(final long userid, final long bookid, final long chapterid) {
        queryChapter(bookid, chapterid, new OnQueryChapterCallback() {
            @Override
            public void onQuerySuccess(ChapterDetailResponse chapterDetailResponse) {
                mView.getNextChapterSuccess(chapterDetailResponse.advertopen, chapterDetailResponse.chaptermode, chapterDetailResponse.textdata);
            }

            @Override
            public void onQueryFailed() {
                long userid = UserInfoManager.getInstance(mContext).getUserid();
                requestNextChapter(userid, bookid, chapterid);
            }
        });
    }

    public void requestNextChapter(long userid, long bookid, final long chapterid) {
        Observable<HttpResult<ChapterDetailResponse>> observable = ApiManager.getInstance().getChapterDetail(userid, bookid, chapterid);
        Disposable disposable = RxUtils.rxSchedulerHelper(observable)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<HttpResult<ChapterDetailResponse>>() {
                    @Override
                    public void accept(HttpResult<ChapterDetailResponse> result) throws Exception {
                        updateADConfig(result.data);
                        if (mView != null) {
                            if (HttpResultManager.verify(result, mView)) {
                                List<BookParagraph> list = parseParagraphFromResponse(result.data);
                                mView.getNextChapterSuccess(result.data.advertopen, result.data.chaptermode, list);

                                saveChapterInfo(mContext, result.data.advertopen, result.data.chaptermode);
                                saveBookParagraph(mContext, chapterid, result.data.textdata);

                                AppSetting.getInstance(mContext).putInt(SharedKey.NO_AD_TIME, result.data.advertmin);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.e("HttpResultManager", "accept: ", throwable);
                        if (mView != null) {
                            mView.dismissLoading();
                            mView.showError(throwable);
                        }
                    }
                });

        addSubscribe(disposable);
    }

    @Override
    public void getBeforeChapter(final long userid, final long bookid, final long chapterid) {
        queryChapter(bookid, chapterid, new OnQueryChapterCallback() {
            @Override
            public void onQuerySuccess(ChapterDetailResponse chapterDetailResponse) {
                mView.getBeforeChapterSuccess(chapterDetailResponse.advertopen, chapterDetailResponse.chaptermode, chapterDetailResponse.textdata);
            }

            @Override
            public void onQueryFailed() {
                long userid = UserInfoManager.getInstance(mContext).getUserid();
                requestBeforeChapter(userid, bookid, chapterid);

            }
        });
    }

    public void requestBeforeChapter(long userid, long bookid, final long chapterid) {
        Observable<HttpResult<ChapterDetailResponse>> observable = ApiManager.getInstance().getChapterDetail(userid, bookid, chapterid);

        Disposable disposable = RxUtils.rxSchedulerHelper(observable)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<HttpResult<ChapterDetailResponse>>() {
                    @Override
                    public void accept(HttpResult<ChapterDetailResponse> result) throws Exception {
                        updateADConfig(result.data);
                        if (mView != null) {
                            if (HttpResultManager.verify(result, mView)) {
                                List<BookParagraph> list = parseParagraphFromResponse(result.data);
                                mView.getBeforeChapterSuccess(result.data.advertopen, result.data.chaptermode, list);

                                saveChapterInfo(mContext, result.data.advertopen, result.data.chaptermode);
                                saveBookParagraph(mContext, chapterid, result.data.textdata);

                                AppSetting.getInstance(mContext).putInt(SharedKey.NO_AD_TIME, result.data.advertmin);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.e("HttpResultManager", "accept: ", throwable);
                        if (mView != null) {
                            mView.dismissLoading();
                            mView.showError(throwable);
                        }
                    }
                });

        addSubscribe(disposable);
    }

    @Override
    public void jumpToBookMarkChapter(final long userid, final long bookid, final long chapterid, final BookMark bookMark) {
        queryChapter(bookid, chapterid, new OnQueryChapterCallback() {
            @Override
            public void onQuerySuccess(ChapterDetailResponse chapterDetailResponse) {
                mView.getBookMarkChapterSuccess(chapterDetailResponse.advertopen, chapterDetailResponse.chaptermode, chapterDetailResponse.textdata, bookMark);
            }

            @Override
            public void onQueryFailed() {
                long userid = UserInfoManager.getInstance(mContext).getUserid();
                requestBookMarkChapter(userid, bookid, chapterid, bookMark);
            }
        });
    }

    public void requestBookMarkChapter(long userid, long bookid, long chapterid, final BookMark bookMark) {
        Observable<HttpResult<ChapterDetailResponse>> observable = ApiManager.getInstance().getChapterDetail(userid, bookid, chapterid);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<ChapterDetailResponse>>() {
            @Override
            public void accept(HttpResult<ChapterDetailResponse> result) throws Exception {
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        List<BookParagraph> list = parseParagraphFromResponse(result.data);
                        mView.getBookMarkChapterSuccess(result.data.advertopen, result.data.chaptermode, list, bookMark);
                        AppSetting.getInstance(mContext).putInt(SharedKey.NO_AD_TIME, result.data.advertmin);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    public void queryChapter(final long bookId, final long chapterId, final OnQueryChapterCallback onQueryChapterCallback) {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<ChapterDetailResponse>() {
            @Override
            public void subscribe(ObservableEmitter<ChapterDetailResponse> emitter) throws Exception {
                ChapterDetailResponse chapterDetailResponse = queryCacheChapter(bookId, chapterId);
                if (chapterDetailResponse != null) {
                    emitter.onNext(chapterDetailResponse);
                } else {
                    emitter.onError(new Exception("无缓存数据"));
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ChapterDetailResponse>() {
                    @Override
                    public void accept(ChapterDetailResponse chapterDetailResponse) throws Exception {
                        updateADConfig(chapterDetailResponse);
                        onQueryChapterCallback.onQuerySuccess(chapterDetailResponse);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        onQueryChapterCallback.onQueryFailed();

                    }
                });
        addSubscribe(disposable);
    }

    public void requestChapter(long userid, long bookid, final long chapterid, final int type) {
        Observable<HttpResult<ChapterDetailResponse>> observable = ApiManager.getInstance().getChapterDetail(userid, bookid, chapterid);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<ChapterDetailResponse>>() {

            @Override
            public void accept(HttpResult<ChapterDetailResponse> result) throws Exception {
                updateADConfig(result.data);
                if (mView != null) {
                    if (HttpResultManager.verify(result, mView)) {
                        List<BookParagraph> list = parseParagraphFromResponse(result.data);
                        Log.e("hhh", "type239= " + type);
                        mView.getChapterSuccess(result.data.advertopen, result.data.chaptermode, list, type);

//                        if(result.data.chaptermode.chargingmode == ApiConstant.ChapterAuth.FREEE_READ || result.data.chaptermode.buystatus ==  ApiConstant.PayStatus.PAID) {
//                            saveChapterInfo(mContext, result.data.chaptermode);
//                            saveBookParagraph(mContext, result.data.textdata);
//                        }
                        saveChapterInfo(mContext, result.data.advertopen, result.data.chaptermode);
                        saveBookParagraph(mContext, chapterid, result.data.textdata);

                        AppSetting.getInstance(mContext).putInt(SharedKey.NO_AD_TIME, result.data.advertmin);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    public ChapterDetailResponse queryCacheChapter(long bookid, final long chapterid) {
        ChapterDetailResponse chapterDetailResponse = new ChapterDetailResponse();
        ChapterInfoDao chapterInfoDao = DbManager.getDaoSession(mContext).getChapterInfoDao();
        List<ChapterInfo> chapterInfoList = chapterInfoDao.queryBuilder()
                .where(ChapterInfoDao.Properties.Bookid.eq(bookid), ChapterInfoDao.Properties.Cid.eq(chapterid))
                .list();

        if (chapterInfoList != null && chapterInfoList.size() > 0) {
            chapterDetailResponse.chaptermode = chapterInfoList.get(0);
            chapterDetailResponse.advertopen = chapterInfoList.get(0).advertopen;
            chapterDetailResponse.advertpage = chapterInfoList.get(0).advertpage;
            if (chapterDetailResponse.chaptermode != null) {
                BookParagraphDao bookParagraphDao = DbManager.getDaoSession(mContext).getBookParagraphDao();
                chapterDetailResponse.textdata = bookParagraphDao.queryBuilder()
                        .where(BookParagraphDao.Properties.BookId.eq(bookid), BookParagraphDao.Properties.ChapterId.eq(chapterid))
                        .orderAsc(BookParagraphDao.Properties.Pid)
                        .list();
                if (chapterDetailResponse.textdata != null && chapterDetailResponse.textdata.size() > 0) {
                    return chapterDetailResponse;
                }
            }
        }
        return null;
    }

    private void updateADConfig(ChapterDetailResponse chapterDetailResponse) {
        if (chapterDetailResponse.advertopen == 0) {
            ADConfig.removeAD = true;
        } else {
            ADConfig.removeAD = false;
        }
        ADConfig.AD_INTERVAL_PAGE = chapterDetailResponse.advertpage;
    }

    public static List<BookParagraph> parseParagraphFromResponse(ChapterDetailResponse chapterDetailResponse) {
        List<BookParagraph> bookParagraphList = chapterDetailResponse.textdata;
        for (BookParagraph bookParagraph : bookParagraphList) {
            bookParagraph.chapterId = chapterDetailResponse.chaptermode.cid;
            bookParagraph.bookId = chapterDetailResponse.chaptermode.bookid;
        }
        return bookParagraphList;
    }

    public void saveChapterInfo(final Context context, final int advertopen, ChapterInfo chapterInfo) {
        Observable.just(chapterInfo).map(new Function<ChapterInfo, Boolean>() {
            @Override
            public Boolean apply(ChapterInfo chapterInfo) throws Exception {
                ChapterInfoDao chapterInfoDao = DbManager.getDaoSession(context).getChapterInfoDao();
                chapterInfo.advertopen = advertopen;
                chapterInfoDao.insertOrReplace(chapterInfo);
                return true;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Logger.i("BookLoadPresenter", "accept: saveChapterInfo success = " + aBoolean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.e("BookLoadPresenter", "accept: ", throwable);
                    }
                });
    }

    public void saveBookParagraph(final Context context, final long chapterid, List<BookParagraph> bookParagraphList) {
        Observable.just(bookParagraphList).map(new Function<List<BookParagraph>, Boolean>() {
            @Override
            public Boolean apply(List<BookParagraph> bookParagraphs) throws Exception {
                BookParagraphDao bookParagraphDao = DbManager.getDaoSession(context).getBookParagraphDao();
                //删除就数据
                List<BookParagraph> list = bookParagraphDao.queryBuilder()
                        .where(BookParagraphDao.Properties.ChapterId.eq(chapterid))
                        .list();
                if (list != null && list.size() > 0) {
                    bookParagraphDao.deleteInTx(list);
                }
                bookParagraphDao.insertInTx(bookParagraphs, true);
                return true;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Logger.i("BookLoadPresenter", "accept: saveChapter success = " + aBoolean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.e("BookLoadPresenter", "accept: ", throwable);
                    }
                });
    }

    public List<BookParagraph> queryBookParagraph(long bookid, final long chapterid) {
        BookParagraphDao bookParagraphDao = DbManager.getDaoSession(mContext).getBookParagraphDao();
        List<BookParagraph> list = bookParagraphDao.queryBuilder()
                .where(BookParagraphDao.Properties.BookId.eq(bookid), BookParagraphDao.Properties.ChapterId.eq(chapterid))
                .orderAsc(BookParagraphDao.Properties.Pid)
                .list();
        return list;
    }

    public void deleteChargeChapter(long bookid, long chapterid) {
        ChapterInfoDao chapterInfoDao = DbManager.getDaoSession(mContext).getChapterInfoDao();
        List<ChapterInfo> chapterInfoList = chapterInfoDao.queryBuilder()
                .where(ChapterInfoDao.Properties.Bookid.eq(bookid), ChapterInfoDao.Properties.Cid.notEq(chapterid))
                .list();

        for (ChapterInfo chapterInfo : chapterInfoList) {
            chapterInfoDao.delete(chapterInfo);
        }
    }

    private interface OnQueryChapterCallback {
        void onQuerySuccess(ChapterDetailResponse chapterDetailResponse);

        void onQueryFailed();
    }
}
