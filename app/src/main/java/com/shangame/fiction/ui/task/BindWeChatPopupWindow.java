package com.shangame.fiction.ui.task;

import android.view.View;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.CenterPopupView;
import com.shangame.fiction.R;

/**
 * Create by Speedy on 2019/3/25
 */
public class BindWeChatPopupWindow {

    private DrawMoneyActivity mActivity;

    public BindWeChatPopupWindow(DrawMoneyActivity activity) {
        mActivity = activity;
    }

    public void show() {
        new XPopup.Builder(mActivity).asCustom(new CenterPopupView(mActivity) {
            @Override
            protected void initPopupContent() {
                super.initPopupContent();

                findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });
                findViewById(R.id.tvBind).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                        mActivity.toWeChatLogin();
                    }
                });
            }

            @Override
            protected int getImplLayoutId() {
                return R.layout.popup_window_bind_wechat;
            }
        }).show();
    }
}
