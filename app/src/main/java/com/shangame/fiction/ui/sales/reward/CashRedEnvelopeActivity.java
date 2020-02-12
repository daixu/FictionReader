package com.shangame.fiction.ui.sales.reward;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.utils.StatusBarUtil;
import com.shangame.fiction.net.response.RedPaperResp;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.sales.partner.RechargeRewardPopupWindow;
import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * 现金红包
 */
@Route(path = "/ss/sales/reward/red/envelope")
public class CashRedEnvelopeActivity extends BaseActivity implements CashRedEnvelopeContacts.View, View.OnClickListener {

    @Autowired
    double orderPrice;

    private CashRedEnvelopePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_red_envelope);
        setStatusBar();
        ARouter.getInstance().inject(this);

        initView();
        initPresenter();
        initListener();
    }

    private void setStatusBar() {
        View viewNeedOffset = findViewById(R.id.view_need_offset);
        StatusBarUtil.setTranslucentForImageView(this, 0, viewNeedOffset);
    }

    private void initView() {
        final SeekBar seekBarRechargeProgress = findViewById(R.id.seek_bar_recharge_progress);

        seekBarRechargeProgress.setEnabled(false);
        TextView textRechargedAmount = findViewById(R.id.text_recharged_amount);

        String strRechargedAmount = orderPrice + "";
        textRechargedAmount.setText(strRechargedAmount);

        double progress = orderPrice + 335;
        if (progress >= 9700) {
            progress = 9700;
        }
        if (progress <= 335) {
            progress = 335;
        }
        seekBarRechargeProgress.setProgress((int) progress);
    }

    private void initPresenter() {
        mPresenter = new CashRedEnvelopePresenter();
        mPresenter.attachView(this);
    }

    private void initListener() {
        findViewById(R.id.image_recharge_reward).setOnClickListener(this);
        findViewById(R.id.btn_invite).setOnClickListener(this);
        findViewById(R.id.img_back).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void setRedPaperSuccess(RedPaperResp.DataBean dataBean) {
        RechargeRewardPopupWindow redPacketPopupWindow = new RechargeRewardPopupWindow(mActivity, dataBean);
        redPacketPopupWindow.show();
    }

    @Override
    public void setRedPaperFailure(String msg) {
        Log.e("hhh", "setRedPaperFailure msg= " + msg);
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.image_recharge_reward:
                openRechargeReward();
                break;
            case R.id.btn_invite:
                ARouter.getInstance()
                        .build("/ss/share/poster")
                        .navigation();
                break;
            default:
                break;
        }
    }

    private void openRechargeReward() {
        double progress = orderPrice;
        if (progress >= 10000) {
            Log.e("hhh", "弹出充值满10000红包");

            UserInfoManager manager = UserInfoManager.getInstance(mContext);
            long userId = manager.getUserid();
            UserInfo userInfo = manager.getUserInfo();
            int agentId = userInfo.agentId;
            mPresenter.setRedPaper(userId, agentId);
        }
    }
}
