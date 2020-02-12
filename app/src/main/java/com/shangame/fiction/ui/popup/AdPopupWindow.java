package com.shangame.fiction.ui.popup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.lxj.xpopup.core.CenterPopupView;
import com.shangame.fiction.R;

public class AdPopupWindow extends CenterPopupView {
    private View.OnClickListener mVideoListener;
    private View.OnClickListener mRewardListener;

    public AdPopupWindow(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_ad_read;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initListener();
    }

    private void initListener() {
        findViewById(R.id.layout_video).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mVideoListener) {
                    mVideoListener.onClick(v);
                }
            }
        });

        findViewById(R.id.layout_reward).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mRewardListener) {
                    mRewardListener.onClick(v);
                }
            }
        });
    }

    public void setVideoClickListener(View.OnClickListener videoListener) {
        this.mVideoListener = videoListener;
    }

    public void setRewardListener(View.OnClickListener rewardListener) {
        this.mRewardListener = rewardListener;
    }
}
