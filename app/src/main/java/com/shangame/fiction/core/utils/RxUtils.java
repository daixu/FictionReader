package com.shangame.fiction.core.utils;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by Speedy on 2018/8/3
 */
public final class RxUtils {

    /**
     * 普通线程切换: IO -> Main
     *
     * @param observable
     * @param <T>
     * @return
     */
    public static final  <T> Observable<T> rxSchedulerHelper(Observable<T> observable){
        return  observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> SingleSource<T> toSimpleSingle(Single<T> upstream){
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
