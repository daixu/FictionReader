package com.shangame.fiction.ui.listen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.lxj.xpopup.core.CenterPopupView;
import com.shangame.fiction.R;

public class ListenPopupWindow extends CenterPopupView {
    private OnAdClickListener onAdClickListener;

    public ListenPopupWindow(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_window_listen;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initListener();
    }

    private void initListener() {
        findViewById(R.id.img_watch).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onAdClickListener != null) {
                    onAdClickListener.onClick();
                }
            }
        });
    }

    public void setOnAdClickListener(OnAdClickListener onAdClickListener) {
        this.onAdClickListener = onAdClickListener;
    }

    public interface OnAdClickListener {
        void onClick();
    }
}
