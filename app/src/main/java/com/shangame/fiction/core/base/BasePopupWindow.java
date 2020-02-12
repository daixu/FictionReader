package com.shangame.fiction.core.base;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Create by Speedy on 2018/8/1
 */
public abstract class BasePopupWindow extends PopupWindow implements BaseContract.BaseView {

    protected Activity mActivity;
    protected BaseActivity mBaseActivity;
    protected View contentView;

    public BasePopupWindow(Activity activity) {
        this.mActivity = activity;
        if (mActivity instanceof BaseActivity) {
            mBaseActivity = (BaseActivity) mActivity;
        }
        setBackgroundDrawable(new ColorDrawable(0));
        setBackgroundAlpha(0.6f);
        setOutsideTouchable(true);
        setFocusable(true);
    }

    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        mActivity.getWindow().setAttributes(lp);
    }

    public void setContentView(int layoutResID) {
        contentView = LayoutInflater.from(mActivity.getApplicationContext()).inflate(layoutResID, null);
        setContentView(contentView);
    }

    public <T extends View> T findViewById(int id) {
        return contentView.findViewById(id);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        setBackgroundAlpha(1f);
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
}
