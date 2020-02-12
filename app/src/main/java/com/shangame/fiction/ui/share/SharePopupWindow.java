package com.shangame.fiction.ui.share;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BasePopupWindow;

/**
 * Create by Speedy on 2018/9/27
 */
public class SharePopupWindow extends BasePopupWindow implements View.OnClickListener {

    private OnShareListener onShareListener;

    public SharePopupWindow(Activity activity, int type) {
        super(activity);
        initView(type);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setAnimationStyle(R.style.popup_anim_style);
        setBackgroundAlpha(1f);
    }

    private void initView(int type) {
        contentView = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.popup_window_share, null);
        setContentView(contentView);
        TextView tvPrompt = contentView.findViewById(R.id.tv_prompt);
        if (type == 1) {
            tvPrompt.setVisibility(View.GONE);
        } else {
            tvPrompt.setVisibility(View.VISIBLE);
        }
        contentView.findViewById(R.id.tvWechat).setOnClickListener(this);
        contentView.findViewById(R.id.tvFriendCirle).setOnClickListener(this);
        contentView.findViewById(R.id.tvQQ).setOnClickListener(this);
        contentView.findViewById(R.id.tvQQZone).setOnClickListener(this);
        contentView.findViewById(R.id.tvCancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvCancel) {
            dismiss();
            return;
        }
        if (onShareListener == null) {
            return;
        }
        dismiss();
        switch (v.getId()) {
            case R.id.tvWechat:
                onShareListener.onShareToWeChat();
                break;
            case R.id.tvFriendCirle:
                onShareListener.onShareToFriendCircle();
                break;
            case R.id.tvQQ:
                onShareListener.onShareQq();
                break;
            case R.id.tvQQZone:
                onShareListener.onShareQqZone();
                break;
            default:
                break;
        }
    }

    public void setOnShareListener(OnShareListener onShareListener) {
        this.onShareListener = onShareListener;
    }

    public interface OnShareListener {
        void onShareToWeChat();

        void onShareToFriendCircle();

        void onShareQq();

        void onShareQqZone();
    }
}
