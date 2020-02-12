package com.shangame.fiction.ui.task;

import android.view.View;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.CenterPopupView;
import com.shangame.fiction.R;

/**
 * Create by Speedy on 2019/3/25
 */
public class ChnageBindWechatPopupWindow {

    private DrawMoneyActivity mActivity;

    public ChnageBindWechatPopupWindow(DrawMoneyActivity activity) {
        mActivity = activity;
    }

    public void show() {
        new XPopup.Builder(mActivity).asCustom(new CenterPopupView(mActivity) {
            @Override
            protected int getImplLayoutId() {
                return R.layout.popup_window_change_bind_wechat;
            }

            @Override
            protected void initPopupContent() {
                super.initPopupContent();

                findViewById(R.id.tvCancel).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });
                findViewById(R.id.tvChangeBind).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                        mActivity.toWeChatLogin();
                    }
                });

            }
        }).show();
    }
}
