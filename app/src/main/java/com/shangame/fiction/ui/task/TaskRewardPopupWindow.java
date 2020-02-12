package com.shangame.fiction.ui.task;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.CenterPopupView;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.TaskAwardResponse;

/**
 * Create by Speedy on 2019/3/20
 */
public class TaskRewardPopupWindow {

    private Activity mActivity;

    private TextView tvNumber;
    private TextView tvDesc;

    public TaskRewardPopupWindow(Activity activity){
        mActivity = activity;
    }

    public void show(final String desc,final String number){
        new XPopup.Builder(mActivity).asCustom(new CenterPopupView(mActivity){
            @Override
            protected int getImplLayoutId() {
                return R.layout.popup_window_task_reward;
            }

            @Override
            protected void initPopupContent() {
                super.initPopupContent();

                findViewById(R.id.ivX).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });

                tvNumber = findViewById(R.id.tvNumber);
                tvDesc = findViewById(R.id.tvDesc);

                tvNumber.setText(number);
                tvDesc.setText(desc);
            }
        }).show();
    }
}
