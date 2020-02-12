package com.shangame.fiction.ui.popup;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BottomPopupView;
import com.shangame.fiction.R;

/**
 * Create by Speedy on 2019/5/9
 */
public class DeleteBookPopupWindow {

    private Activity mActivity;

    private View.OnClickListener deleteOnClickListener;

    public DeleteBookPopupWindow(Activity activity) {
        mActivity = activity;
    }

    public void show(final int count) {
        new XPopup.Builder(mActivity).asCustom(new BottomPopupView(mActivity) {
            @Override
            protected void initPopupContent() {
                super.initPopupContent();
                findViewById(R.id.tvDelete).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        if (deleteOnClickListener != null) {
                            deleteOnClickListener.onClick(v);
                        }
                    }
                });
                TextView tvInfo = findViewById(R.id.tvInfo);
                tvInfo.setText("从书架删除选中的" + count + "本书吗？");
                findViewById(R.id.tvCancel).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            }

            @Override
            protected int getImplLayoutId() {
                return R.layout.popup_delete_book;
            }
        }).show();
    }

    public void setDeleteOnClickListener(View.OnClickListener deleteOnClickListener) {
        this.deleteOnClickListener = deleteOnClickListener;
    }
}
