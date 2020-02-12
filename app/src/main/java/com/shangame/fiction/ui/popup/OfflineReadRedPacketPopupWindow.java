package com.shangame.fiction.ui.popup;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.shangame.fiction.R;

/**
 * 离线或未登录阅读30分钟红包
 * Create by Speedy on 2019/3/20
 */
public class OfflineReadRedPacketPopupWindow {

    private Activity mActivity;

    private String desc;

    private ImageView ivKai;

    private BasePopupView popupView;

    public OfflineReadRedPacketPopupWindow(Activity activity, String desc) {
        mActivity = activity;
        this.desc = desc;
    }

    public void show(final View.OnClickListener onClickListener) {
        popupView = new XPopup.Builder(mActivity).asCustom(new CenterPopupView(mActivity) {
            @Override
            protected int getImplLayoutId() {
                return R.layout.popup_window_red_packet;
            }

            @Override
            protected int getMaxWidth() {
                return XPopupUtils.getWindowWidth(getContext());
            }

            @Override
            protected void initPopupContent() {
                super.initPopupContent();
                TextView tvReadTimeCover = findViewById(R.id.tvReadTimeCover);

                tvReadTimeCover.setText(desc);
                ivKai = findViewById(R.id.ivKai);
                ivKai.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupView.dismiss();
                        if (onClickListener != null) {
                            onClickListener.onClick(v);
                        }
                    }
                });
            }
        });
        popupView.show();
    }
}
