package com.shangame.fiction.ui.popup;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.CenterPopupView;
import com.shangame.fiction.R;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.statis.ReadTracer;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.manager.UserSetting;
import com.shangame.fiction.ui.login.LoginActivity;
import com.shangame.fiction.ui.task.TaskId;

/**
 * Create by Speedy on 2019/4/24
 */
public class RedTaskPopupWindow {

    private Activity mActivity;

    private long totalReadTime;

    public RedTaskPopupWindow(Activity activity, long totalReadTime) {
        mActivity = activity;
        this.totalReadTime = totalReadTime;
    }

    public void show() {
        new XPopup.Builder(mActivity).asCustom(new CenterPopupView(mActivity) {
            @Override
            protected int getImplLayoutId() {
                return R.layout.popup_window_red_task;
            }

            @Override
            protected void initPopupContent() {
                super.initPopupContent();
                findViewById(R.id.ivX).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });
                TextView tvReadTime = findViewById(R.id.tvReadTime);
                TextView tvInfo = findViewById(R.id.tvInfo);
                TextView tv30 = findViewById(R.id.tv30);
                TextView tv100 = findViewById(R.id.tv100);
                TextView tv200 = findViewById(R.id.tv200);
                TextView tvInfo10 = findViewById(R.id.tvInfo10);
                ViewSwitcher viewSwitcher = findViewById(R.id.viewSwitcher);

                long userid = UserInfoManager.getInstance(mActivity).getUserid();

                Button btnLogin = findViewById(R.id.btnLogin);
                if (userid == 0) {
                    btnLogin.setVisibility(View.VISIBLE);
                    btnLogin.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                            mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
                        }
                    });
                } else {
                    btnLogin.setVisibility(View.GONE);
                }
                tvReadTime.setText("" + totalReadTime / 60);

                Drawable drawable = getResources().getDrawable(R.drawable.red_task_ok);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

                if (totalReadTime > ReadTracer.READ_200) {
                    tvInfo.setText("明日阅读继续领红包");
                    tv30.setCompoundDrawables(null, drawable, null, null);
                    tv100.setCompoundDrawables(null, drawable, null, null);
                    tv200.setCompoundDrawables(null, drawable, null, null);
                    tv30.setTextColor(getResources().getColor(R.color.task_red_color));
                    tv100.setTextColor(getResources().getColor(R.color.task_red_color));
                    tv200.setTextColor(getResources().getColor(R.color.task_red_color));
                } else if (totalReadTime >= ReadTracer.READ_100) {
                    long time = (ReadTracer.READ_200 - totalReadTime) / 60;
                    tvInfo.setText("还需阅读" + time + "分钟得下个红包");

                    tv30.setCompoundDrawables(null, drawable, null, null);
                    tv100.setCompoundDrawables(null, drawable, null, null);

                    tv30.setTextColor(getResources().getColor(R.color.task_red_color));
                    tv100.setTextColor(getResources().getColor(R.color.task_red_color));
                } else if (totalReadTime >= ReadTracer.READ_30) {
                    long time = (ReadTracer.READ_100 - totalReadTime) / 60;
                    tvInfo.setText("还需阅读" + time + "分钟得下个红包");
                    tv30.setCompoundDrawables(null, drawable, null, null);
                    tv30.setTextColor(getResources().getColor(R.color.task_red_color));
                } else if (totalReadTime >= ReadTracer.READ_10) {
                    long time = (ReadTracer.READ_30 - totalReadTime) / 60;
                    tvInfo.setText("还需阅读" + time + "分钟得下个红包");
                } else {
                    if (userid == 0) {
                        long time = (ReadTracer.READ_10 - totalReadTime) / 60;
                        tvInfo10.setText("还需阅读" + time + "分钟得下个红包");
                        viewSwitcher.showNext();
                    } else {
                        UserSetting userSetting = UserSetting.getInstance(mActivity);
                        int nexttaskid = userSetting.getInt(SharedKey.NEXT_TASK_ID, 0);
                        if (nexttaskid == TaskId.READ_10) {
                            long time = (ReadTracer.READ_10 - totalReadTime) / 60;
                            tvInfo10.setText("还需阅读" + time + "分钟得下个红包");
                            viewSwitcher.showNext();
                        } else {
                            long time = (ReadTracer.READ_30 - totalReadTime) / 60;
                            tvInfo.setText("还需阅读" + time + "分钟得下个红包");
                        }
                    }
                }
            }
        }).show();
    }
}
