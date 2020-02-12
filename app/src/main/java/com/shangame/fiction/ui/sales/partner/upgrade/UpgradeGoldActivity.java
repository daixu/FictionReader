package com.shangame.fiction.ui.sales.partner.upgrade;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.utils.StatusBarUtil;
import com.shangame.fiction.net.response.BaseResp;
import com.shangame.fiction.net.response.CreatWapOrderResponse;
import com.shangame.fiction.net.response.GetPayMenthodsResponse;
import com.shangame.fiction.net.response.GetRechargeConfigResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.my.pay.PayPopupWindow;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.Map;

@Route(path = "/ss/sales/partner/upgrade/gold")
public class UpgradeGoldActivity extends BaseActivity implements UpgradeLevelContacts.View, View.OnClickListener {
    @Autowired
    int buyDisplay;
    private UpgradeLevelPresenter mPresenter;
    private PayPopupWindow payPopupWindow;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showSuccessDialog();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_gold);
        ARouter.getInstance().inject(this);

        initView();
        initPresenter();
        initReceiver();
    }

    private void initView() {
        Button btnUpgrade = findViewById(R.id.btn_upgrade);

        if (buyDisplay == 0) {
            btnUpgrade.setVisibility(View.GONE);
        } else {
            btnUpgrade.setVisibility(View.VISIBLE);
        }

        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.btn_contact_customer_service).setOnClickListener(this);
        btnUpgrade.setOnClickListener(this);
    }

    private void initPresenter() {
        mPresenter = new UpgradeLevelPresenter(this);
        mPresenter.attachView(this);
    }

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter(BroadcastAction.PUSH_GRADE_SUCCESS_ACTION);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();

        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }

    @Override
    public void setUpGradeSuccess(BaseResp resp) {
        showSuccessDialog();
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_upgrade_success, null);

        ImageView imgClose = view.findViewById(R.id.img_close);
        ImageView imgExperience = view.findViewById(R.id.img_experience);
        ImageView imageType = view.findViewById(R.id.image_type);

        imageType.setImageResource(R.drawable.image_gold_prompt);
        final Dialog dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        if (null != window) {
            window.setBackgroundDrawable(new BitmapDrawable());
            window.setContentView(view);
        }

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        imgExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build("/ss/sales/partner/manage/home")
                        .navigation();
                finish();
            }
        });
    }

    @Override
    public void setUpGradeFailure(String msg) {
        Log.e("hhh", "setUpGradeFailure msg= " + msg);
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getPayMethodsSuccess(GetPayMenthodsResponse response) {
        payPopupWindow = new PayPopupWindow(mActivity);
        payPopupWindow.setPayItemList(response.paydata);
        GetRechargeConfigResponse.RechargeBean currentRechargeBean = new GetRechargeConfigResponse.RechargeBean();
        currentRechargeBean.propid = 1005;
        currentRechargeBean.price = 198;

        payPopupWindow.setRechargeBean(currentRechargeBean);
        payPopupWindow.setOnPayClickListener(new PayPopupWindow.OnPayClickListener() {
            @Override
            public void onPay(Map<String, Object> map, int payMethod) {
                if (payPopupWindow != null && payPopupWindow.isShowing()) {
                    payPopupWindow.dismiss();
                }
                mPresenter.createWapOrder(map, payMethod);
            }

            @Override
            public void onPay2(String payUrl, Map<String, Object> map, int payMethod) {
                if (payPopupWindow != null && payPopupWindow.isShowing()) {
                    payPopupWindow.dismiss();
                }
                mPresenter.createWapOrder2(payUrl, map, payMethod);
            }
        });
        payPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void wapCreateOrderSuccess(CreatWapOrderResponse response, int payMethod) {
        mPresenter.redirectRequest(response.skipurl, payMethod);
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
            case R.id.btn_upgrade:
                // upgrade();
                getPayMethod();
                break;
            case R.id.btn_contact_customer_service:
                ARouter.getInstance()
                        .build("/ss/customer/service")
                        .navigation();
                break;
            default:
                break;
        }
    }

    private void getPayMethod() {
        mPresenter.getPayMethods();
    }

    private void upgrade() {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        mPresenter.setUpGrade(userId, 0, 3);
    }
}
