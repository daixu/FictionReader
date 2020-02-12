package com.shangame.fiction.ui.contents;

import android.content.Context;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.storage.db.BookMarkDao;
import com.shangame.fiction.storage.manager.DbManager;
import com.shangame.fiction.storage.model.BookMark;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by Speedy on 2018/9/10
 */
public class BookMarkPresenter extends RxPresenter<BookMarkContacts.View> implements BookMarkContacts.Presenter<BookMarkContacts.View> {

    private Context mContext;

    public BookMarkPresenter(Context context){
        mContext = context;
    }
    
    @Override
    public void addBookMark(BookMark bookMark) {

        Disposable disposable = Flowable.just(bookMark).map(new Function<BookMark, Boolean>() {
                                    @Override
                                    public Boolean apply(BookMark bookMark) throws Exception {
                                        BookMarkDao bookMarkDao =  DbManager.getDaoSession(mContext).getBookMarkDao();
                                        bookMarkDao.insertOrReplace(bookMark);
                                        return true;
                                    }
                                }).subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(Boolean aBoolean) throws Exception {
                                        mView.addBookMarkSuccess();
                                    }
                                });

        addSubscribe(disposable);
    }

    @Override
    public void removeBookMark(final BookMark bookMark) {
        Disposable disposable = Observable.create(new ObservableOnSubscribe<List<BookMark>>() {
            @Override
            public void subscribe(ObservableEmitter<List<BookMark>> emitter) throws Exception {
                BookMarkDao bookMarkDao =  DbManager.getDaoSession(mContext).getBookMarkDao();

//               List<BookMark> list = bookMarkDao.queryBuilder().where(BookMarkDao.Properties.Userid.eq(userid),
//                        BookMarkDao.Properties.Bookid.eq(bookid),
//                        BookMarkDao.Properties.Chapterid.eq(chapterid),
//                        BookMarkDao.Properties.Pid.eq(pid))
//                        .build().list();
//
//               if(list != null && list.size() > 0 ){
//                   bookMarkDao.delete(list.get(0));
//               }
                bookMarkDao.delete(bookMark);

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<BookMark>>() {
                    @Override
                    public void accept(List<BookMark> list) throws Exception {
                        mView.getBookMarkListSuccess(list);
                    }
                });

        addSubscribe(disposable);
    }

    @Override
    public void getBookMarkList(final long userid, final long bookid, int page, int pagesize) {

        Disposable disposable = Observable.create(new ObservableOnSubscribe<List<BookMark>>() {
            @Override
            public void subscribe(ObservableEmitter<List<BookMark>> emitter) throws Exception {
                BookMarkDao bookMarkDao =  DbManager.getDaoSession(mContext).getBookMarkDao();
                List<BookMark> list =  bookMarkDao.queryBuilder().where(BookMarkDao.Properties.Userid.eq(userid),BookMarkDao.Properties.Bookid.eq(bookid)).build().list();
//                List<BookMark> list = bookMarkDao.loadAll();
                emitter.onNext(list);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<BookMark>>() {
                    @Override
                    public void accept(List<BookMark> list) throws Exception {
                        mView.getBookMarkListSuccess(list);
                    }
                });

        addSubscribe(disposable);
    }
}
