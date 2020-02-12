package com.shangame.fiction.ui.popup;

import android.app.Activity;
import android.view.View;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.CenterPopupView;
import com.shangame.fiction.R;

/**
 * Create by Speedy on 2019/2/20
 */
public class FreeReadPopupWindow {

    private Activity mActivity;

    public FreeReadPopupWindow(Activity activity) {
        mActivity = activity;
    }

    public void show() {
        new XPopup.Builder(mActivity).asCustom(new CenterPopupView(mActivity) {
            @Override
            protected void initPopupContent() {
                super.initPopupContent();

                findViewById(R.id.ivX).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });

            }

            @Override
            protected int getImplLayoutId() {
                return R.layout.popup_window_free_read;
            }
        }).show();
    }
}
