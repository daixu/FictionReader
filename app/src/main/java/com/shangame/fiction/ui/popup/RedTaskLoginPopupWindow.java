package com.shangame.fiction.ui.popup;

import android.app.Activity;
import android.view.View;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.CenterPopupView;
import com.shangame.fiction.R;

/**
 * Create by Speedy on 2019/4/24
 */
public class RedTaskLoginPopupWindow {

    private Activity mActivity;

    private View.OnClickListener onLoginClickListener;

    public RedTaskLoginPopupWindow(Activity activity) {
        mActivity = activity;
    }

    public void show() {
        new XPopup.Builder(mActivity)
                .dismissOnBackPressed(false)
                .dismissOnTouchOutside(false)
                .asCustom(new CenterPopupView(mActivity) {
                    @Override
                    protected int getImplLayoutId() {
                        return R.layout.popup_window_red_task_login;
                    }

                    @Override
                    protected void initPopupContent() {
                        super.initPopupContent();
                        findViewById(R.id.btnLogin).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dismiss();
                                if (onLoginClickListener != null) {
                                    onLoginClickListener.onClick(view);
                                }
                            }
                        });

                        findViewById(R.id.ivX).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dismiss();
                            }
                        });
                    }
                }).show();
    }

    public void setOnLoginClickListener(View.OnClickListener onLoginClickListener) {
        this.onLoginClickListener = onLoginClickListener;
    }
}
