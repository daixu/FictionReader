package com.shangame.fiction.net.manager;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.core.manager.Logger;
import com.shangame.fiction.core.utils.RxUtils;
import com.shangame.fiction.net.response.HttpResult;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Create by Speedy on 2018/8/13
 */
public final class HttpResultManager {

    private static final int FAILURE_CODE = 0;
    private static final int FAILURE_CODE_1 = 1;
    private static final int SUCCESS_CODE = 1;
    private static final int SUCCESS_CODE_1 = 0;
    private static final int NOT_LOGIN_CODE = 2;

    public static <T> Disposable rxResultHandler(Observable<HttpResult<T>> observable, final BaseContract.BaseView baseView, Consumer<HttpResult<T>> consumer) {
        return RxUtils.rxSchedulerHelper(observable)
                .subscribe(consumer, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Logger.e("HttpResultManager", "accept: ", throwable);
                        if (baseView != null) {
                            baseView.dismissLoading();
                            baseView.showError(throwable);
                        }
                    }
                });
    }

    public static boolean verify(HttpResult httpResult, BaseContract.BaseView baseView) {
        if (httpResult.code == SUCCESS_CODE) {
            return true;
        }
        baseView.dismissLoading();

        if (httpResult.code == FAILURE_CODE) {
            baseView.showToast(httpResult.msg);
            return false;
        } else if (httpResult.code == NOT_LOGIN_CODE) {
            baseView.lunchLoginActivity();
            return false;
        } else {
            baseView.showToast(httpResult.msg);
            return false;
        }
    }

    public static boolean verify1(HttpResult httpResult, BaseContract.BaseView baseView) {
        if (httpResult.code == SUCCESS_CODE_1) {
            return true;
        }
        baseView.dismissLoading();

        if (httpResult.code == FAILURE_CODE_1) {
            baseView.showToast(httpResult.msg);
            return false;
        } else if (httpResult.code == NOT_LOGIN_CODE) {
            baseView.lunchLoginActivity();
            return false;
        } else {
            baseView.showToast(httpResult.msg);
            return false;
        }
    }

}
