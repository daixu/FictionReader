package com.shangame.fiction.core.base;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.trello.rxlifecycle2.components.support.RxFragment;

import java.lang.reflect.Field;

/**
 * Create by Speedy on 2018/7/20
 */
public class BaseFragment extends RxFragment implements BaseContract.BaseView {

    public static final int PAGE_SIZE = 20;

    protected BaseActivity mBaseActivity;
    protected Activity mActivity;
    protected Context mContext;

    public BaseFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
        if (mActivity instanceof BaseActivity) {
            mBaseActivity = (BaseActivity) mActivity;
        }
        mContext = getContext();
    }

    @Override
    public void showLoading() {
        if (mBaseActivity != null) {
            mBaseActivity.showLoading();
        }
    }

    @Override
    public void dismissLoading() {
        if (mBaseActivity != null) {
            mBaseActivity.dismissLoading();
        }
    }

    @Override
    public void showNotNetworkView() {
        if (mBaseActivity != null) {
            mBaseActivity.showNotNetworkView();
        }
    }

    @Override
    public void showToast(String msg) {
        if (mBaseActivity != null) {
            mBaseActivity.showToast(msg);
        }
    }

    @Override
    public void showError(Throwable throwable) {
        if (mBaseActivity != null) {
            mBaseActivity.showError(throwable);
        }
    }

    @Override
    public void lunchLoginActivity() {
        if (mBaseActivity != null) {
            mBaseActivity.lunchLoginActivity();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void lunchLoginActivity(int formCode) {
        if (mBaseActivity != null) {
            mBaseActivity.lunchLoginActivity(formCode);
        }
    }
}
