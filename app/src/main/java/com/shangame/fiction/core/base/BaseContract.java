package com.shangame.fiction.core.base;

/**
 * Create by Speedy on 2018/7/19
 */
public interface BaseContract {
    interface BaserPresenter<V> {
        void attachView(V view);
        void detachView();
    }

    interface BaseView {
        void showLoading();

        void dismissLoading();

        void showNotNetworkView();

        void showToast(String msg);

        void showError(Throwable throwable);

        void lunchLoginActivity();
    }
}
