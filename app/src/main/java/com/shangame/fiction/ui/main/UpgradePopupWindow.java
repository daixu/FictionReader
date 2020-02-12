package com.shangame.fiction.ui.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lxj.xpopup.core.CenterPopupView;
import com.shangame.fiction.R;
import com.shangame.fiction.core.constant.SharedKey;

/**
 * 绑定邀请码弹框
 *
 * @author hhh
 */
public class UpgradePopupWindow extends CenterPopupView {
    private int mSysAgentGrade;

    public UpgradePopupWindow(@NonNull Context context) {
        super(context);
    }

    public UpgradePopupWindow(@NonNull Context context, int sysAgentGrade) {
        super(context);
        this.mSysAgentGrade = sysAgentGrade;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_upgrade_success;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
        initListener();
    }

    private void initView() {
        SharedKey.SYS_AGENT_GRADE = 0;
        ImageView imageType = findViewById(R.id.image_type);

        switch (mSysAgentGrade) {
            case 1:
                imageType.setImageResource(R.drawable.image_diamond_prompt);
                break;
            case 2:
                imageType.setImageResource(R.drawable.image_gold_prompt);
                break;
            case 3:
                imageType.setImageResource(R.drawable.image_silver_prompt);
                break;
            default:
                break;
        }
    }

    private void initListener() {
        findViewById(R.id.img_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        findViewById(R.id.img_experience).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                ARouter.getInstance()
                        .build("/ss/sales/partner/manage/home")
                        .navigation();
            }
        });
    }
}
