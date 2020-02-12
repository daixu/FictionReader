package com.shangame.fiction.ui.sales.partner;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.RedPaperResp;
import com.shangame.fiction.ui.task.DrawMoneyActivity;

public class RechargeRewardPopupWindow {
    private Activity mActivity;
    private LinearLayout mLayoutComplete;
    private ImageView mImageOpen;
    private ImageView mImageMoney;
    private FrameLayout mLayoutRedPackage;
    private FrameLayout mLayoutHead;
    private ImageView mImageBottom;
    private TextView mTextMoney;
    private TextView mTvPrompt;
    private RedPaperResp.DataBean mDataBean;

    public RechargeRewardPopupWindow(Activity activity, RedPaperResp.DataBean dataBean) {
        mActivity = activity;
        mDataBean = dataBean;
    }

    public void show() {
        new XPopup.Builder(mActivity).asCustom(new CenterPopupView(mActivity) {
            @Override
            protected void initPopupContent() {
                super.initPopupContent();
                mLayoutComplete = findViewById(R.id.layout_complete);
                mImageOpen = findViewById(R.id.image_open);
                mImageMoney = findViewById(R.id.image_money);
                mLayoutRedPackage = findViewById(R.id.layout_red_package);
                mLayoutHead = findViewById(R.id.layout_head);
                mImageBottom = findViewById(R.id.image_bottom);
                mTextMoney = findViewById(R.id.text_money);
                mTvPrompt = findViewById(R.id.tv_prompt);

                TextView tvContinue = findViewById(R.id.tv_continue);
                tvContinue.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });

                if (null != mDataBean) {
                    String strMoney = mDataBean.redMoney + "";
                    mTextMoney.setText(strMoney);
                }

                TextView tvInfo = findViewById(R.id.tv_info);
                tvInfo.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        mActivity.startActivity(new Intent(mActivity, DrawMoneyActivity.class));
                    }
                });

                mImageMoney.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startOpenAnim();
                    }
                });
            }

            @Override
            protected int getImplLayoutId() {
                return R.layout.popup_window_recharge_reward;
            }

            @Override
            protected int getMaxWidth() {
                return XPopupUtils.getWindowWidth(getContext());
            }
        }).show();
    }

    private void startOpenAnim() {
        final ObjectAnimator anim0 = ObjectAnimator.ofFloat(mLayoutComplete, "alpha", 0F, 1f);
        final ObjectAnimator anim1 = ObjectAnimator.ofFloat(mImageOpen, "alpha", 1F, 0f);

        final ObjectAnimator anim2 = ObjectAnimator.ofFloat(mImageMoney, "rotationY", 0F, 720F);
        final ObjectAnimator anim3 = ObjectAnimator.ofFloat(mLayoutHead, "translationY", 0F, -mLayoutHead.getHeight());
        final ObjectAnimator anim4 = ObjectAnimator.ofFloat(mImageBottom, "translationY", 0F, mImageBottom.getHeight());
        final ObjectAnimator anim5 = ObjectAnimator.ofFloat(mTvPrompt, "scaleX", 1F, 1.2F, 1F);
        final ObjectAnimator anim6 = ObjectAnimator.ofFloat(mTvPrompt, "scaleY", 1F, 1.2F, 1F);
        final ObjectAnimator anim7 = ObjectAnimator.ofFloat(mTextMoney, "scaleX", 1F, 1.2F, 1F);
        final ObjectAnimator anim8 = ObjectAnimator.ofFloat(mTextMoney, "scaleY", 1F, 1.2F, 1F);
        final ObjectAnimator anim9 = ObjectAnimator.ofFloat(mLayoutRedPackage, "alpha", 1F, 0f);
        anim1.setDuration(300);
        anim2.setDuration(1000);

        final AnimatorSet animatorSet1 = new AnimatorSet();
        animatorSet1.playSequentially(anim1, anim2);
        animatorSet1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mImageOpen.setVisibility(View.GONE);
                mImageMoney.setVisibility(View.GONE);
            }
        });

        final AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.setDuration(300);
        animatorSet2.setInterpolator(new AccelerateInterpolator());
        animatorSet2.playTogether(anim3, anim4, anim9);

        final AnimatorSet animatorSet3 = new AnimatorSet();
        animatorSet3.setDuration(500);
        animatorSet3.playTogether(anim0, anim5, anim6, anim7, anim8);

        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(animatorSet1, animatorSet2, animatorSet3);
        animatorSet.start();
    }
}
