package com.shangame.fiction.ui.popup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.ui.task.DrawMoneyActivity;

/**
 * Create by Speedy on 2019/3/20
 */
public class RedPacketPopupWindow {

    private Activity mActivity;

    private TextView tvContinue;
    private TextView tvReadTime;
    private TextView tvMoney;

    private ImageView ivBottom;
    private View headLayout;
    private ImageView ivMoney;
    private ImageView ivKai;

    private PopupMenu.OnDismissListener dismissListener;

    private TaskAwardResponse taskAwardResponse;

    private View.OnClickListener bindWechatClickListener;

    private View redBackLayout;
    private View redBeforeLayout;

    public RedPacketPopupWindow(Activity activity, TaskAwardResponse taskAwardResponse) {
        mActivity = activity;
        this.taskAwardResponse = taskAwardResponse;
    }

    public void show() {
        new XPopup.Builder(mActivity)
                .dismissOnBackPressed(false)
                .dismissOnTouchOutside(false)
                .asCustom(new CenterPopupView(mActivity) {
                    @Override
                    protected void initPopupContent() {
                        super.initPopupContent();
                        tvContinue = findViewById(R.id.tvContinue);

                        redBackLayout = findViewById(R.id.redBackLayout);
                        redBeforeLayout = findViewById(R.id.redBeforeLayout);
                        View ivX = findViewById(R.id.ivX);
                        ivX.setVisibility(View.VISIBLE);
                        ivX.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dismiss();
                                if (dismissListener != null) {
                                    dismissListener.onDismiss(null);
                                }
                            }
                        });

                        TextView tvInfo = findViewById(R.id.tvInfo);
                        tvInfo.setCompoundDrawables(null, null, null, null);

                        tvReadTime = findViewById(R.id.tvReadTime);
                        tvMoney = findViewById(R.id.tvMoney);

                        ivBottom = findViewById(R.id.ivBottom);
                        headLayout = findViewById(R.id.headLayout);
                        TextView tvReadTimeCover = findViewById(R.id.tvReadTimeCover);
                        ivMoney = findViewById(R.id.ivMoney);
                        ivKai = findViewById(R.id.ivKai);

                        ivKai.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startOpenAnim();
                            }
                        });

                        tvReadTime.setText(taskAwardResponse.desc);
                        tvReadTimeCover.setText(taskAwardResponse.desc);
                        tvMoney.setText(taskAwardResponse.number + "");

                        if (taskAwardResponse.regtype == 1) {
                            tvContinue.setText("去提现");
                            tvInfo.setText("已存入零钱，需绑定微信提现");
                            tvContinue.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dismiss();
                                    if (dismissListener != null) {
                                        dismissListener.onDismiss(null);
                                    }
                                    Intent intent = new Intent(mActivity, DrawMoneyActivity.class);
                                    mActivity.startActivity(intent);
                                }
                            });
                        } else {
                            tvContinue.setText("去绑定");
                            tvInfo.setText("已存入零钱，需绑定微信提现");
                            tvContinue.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dismiss();
                                    if (bindWechatClickListener != null) {
                                        bindWechatClickListener.onClick(v);
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    protected int getImplLayoutId() {
                        return R.layout.popup_window_red_packet_login;
                    }

                    @Override
                    protected int getMaxWidth() {
                        return XPopupUtils.getWindowWidth(getContext());
                    }
                }).show();
    }

    private void startOpenAnim() {
        final ObjectAnimator anim0 = ObjectAnimator.ofFloat(redBackLayout, "alpha", 0F, 1f);
        final ObjectAnimator anim1 = ObjectAnimator.ofFloat(ivKai, "alpha", 1F, 0f);
        final ObjectAnimator anim2 = ObjectAnimator.ofFloat(ivMoney, "rotationY", 0F, 720F);
        final ObjectAnimator anim3 = ObjectAnimator.ofFloat(headLayout, "translationY", 0F, -headLayout.getHeight());
        final ObjectAnimator anim4 = ObjectAnimator.ofFloat(ivBottom, "translationY", 0F, ivBottom.getHeight());
        final ObjectAnimator anim5 = ObjectAnimator.ofFloat(tvReadTime, "scaleX", 1F, 1.2F, 1F);
        final ObjectAnimator anim6 = ObjectAnimator.ofFloat(tvReadTime, "scaleY", 1F, 1.2F, 1F);
        final ObjectAnimator anim7 = ObjectAnimator.ofFloat(tvMoney, "scaleX", 1F, 1.2F, 1F);
        final ObjectAnimator anim8 = ObjectAnimator.ofFloat(tvMoney, "scaleY", 1F, 1.2F, 1F);
        final ObjectAnimator anim9 = ObjectAnimator.ofFloat(redBeforeLayout, "alpha", 1F, 0f);
        anim1.setDuration(300);
        anim2.setDuration(1000);

        final AnimatorSet animatorSet1 = new AnimatorSet();
        animatorSet1.playSequentially(anim1, anim2);
        animatorSet1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                ivKai.setVisibility(View.GONE);
                ivMoney.setVisibility(View.GONE);
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

    public void setDismissListener(PopupMenu.OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    public void setBindWechatClickListener(View.OnClickListener bindWechatClickListener) {
        this.bindWechatClickListener = bindWechatClickListener;
    }
}
