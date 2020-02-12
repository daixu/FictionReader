package com.shangame.fiction.net;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Created by ${HELY} on 17/4/15.
 * 邮箱：heli.lixiong@gmail.com
 */

public abstract class BaseSubscriber<T> implements Subscriber<T> {

    @Override
    public void onError(Throwable e) {
        hideDialog();
        if(e instanceof ExceptionHandle.ResponseThrowable){
            onError((ExceptionHandle.ResponseThrowable)e);
        } else {
            onError(new ExceptionHandle.ResponseThrowable(e, ExceptionHandle.ERROR.UNKNOWN));
        }
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
        showDialog();
    }

    protected abstract void hideDialog();

    protected abstract void showDialog();

    @Override
    public void onComplete() {
        hideDialog();
    }

    public abstract void onError(ExceptionHandle.ResponseThrowable e);
}