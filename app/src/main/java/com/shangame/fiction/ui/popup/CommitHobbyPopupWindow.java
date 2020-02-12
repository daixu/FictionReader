package com.shangame.fiction.ui.popup;

import android.app.Activity;
import android.os.Handler;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.CenterPopupView;
import com.shangame.fiction.R;

/**
 * Create by Speedy on 2018/12/24
 */
public class CommitHobbyPopupWindow {

    private int mBookCount;
    private Activity mActivity;
    private BasePopupView popupView;

    private PopupWindow.OnDismissListener onDismissListener;

    public CommitHobbyPopupWindow(Activity activity, int bookCount) {
        this.mBookCount = bookCount;
        mActivity = activity;
        popupView = new XPopup.Builder(mActivity).asCustom(new CenterPopupView(mActivity) {
            @Override
            protected int getImplLayoutId() {
                return R.layout.commit_hobby_popup_window;
            }

            @Override
            protected void initPopupContent() {
                super.initPopupContent();

                TextView tvBookCount = findViewById(R.id.tvBookCount);

                tvBookCount.setText("为您准备" + mBookCount + "本好书");

            }
        });
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public void dismiss() {
        popupView.dismiss();
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
        }
    }

    public void show() {
        popupView.show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                popupView.dismiss();
            }
        }, 1000);
    }
}
