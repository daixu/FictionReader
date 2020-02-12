package com.shangame.fiction.ui.bookrack;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.AttachPopupView;
import com.lxj.xpopup.core.BasePopupView;
import com.shangame.fiction.R;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.manager.AppSetting;

/**
 * Create by Speedy on 2018/8/1
 */
public class RackPopupWindow implements View.OnClickListener {

    private Activity mActivity;
    private OnRackChangeListener onRackChangeListener;
    private BasePopupView popupView;

    public RackPopupWindow(Activity activity) {
        mActivity = activity;
    }

    public void show(View view) {
        AttachPopupView attachPopupView = new AttachPopupView(mActivity) {
            @Override
            protected int getImplLayoutId() {
                return R.layout.popup_window_rack;
            }

            @Override
            protected void initPopupContent() {
                super.initPopupContent();
                int currentViewType = AppSetting.getInstance(mActivity).getInt(SharedKey.BOOK_RACK_VIEW_TYPE, BookListFragment.VIEW_TYPE_GRID_LAYOUT);
                TextView tvListMode = findViewById(R.id.tvListMode);
                if (currentViewType == BookListFragment.VIEW_TYPE_GRID_LAYOUT) {
                    tvListMode.setText("列表模式");
                } else {
                    tvListMode.setText("封面模式");
                }
                tvListMode.setOnClickListener(RackPopupWindow.this);
                findViewById(R.id.tvManager).setOnClickListener(RackPopupWindow.this);
                findViewById(R.id.tv_local_import).setOnClickListener(RackPopupWindow.this);
                findViewById(R.id.tv_wifi_book).setOnClickListener(RackPopupWindow.this);
            }
        };

        popupView = new XPopup.Builder(mActivity).atView(view).asCustom(attachPopupView);
        popupView.show();
    }

    @Override
    public void onClick(View view) {
        popupView.dismiss();
        if (onRackChangeListener == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.tvListMode:
                onRackChangeListener.onListMode();
                break;
            case R.id.tvManager:
                onRackChangeListener.onManager();
                break;
            case R.id.tv_local_import:
                onRackChangeListener.onLocalImport();
                break;
            case R.id.tv_wifi_book:
                onRackChangeListener.onWifiBook();
                break;
            default:
                break;
        }
    }

    public void setOnRackChangeListener(OnRackChangeListener onRackChangeListener) {
        this.onRackChangeListener = onRackChangeListener;
    }

    public interface OnRackChangeListener {
        void onListMode();

        void onManager();

        void onLocalImport();

        void onWifiBook();
    }
}
