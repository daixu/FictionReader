package com.shangame.fiction.ui.my.pay;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.manager.AppSetting;
import com.shangame.fiction.net.response.OrderInfoResponse;
import com.trello.rxlifecycle2.LifecycleTransformer;

public class PayCompleteActivity extends BaseActivity implements View.OnClickListener, PayCompleteContacts.View {
    private LinearLayout mLayoutSuccess;
    private LinearLayout mLayoutFailure;
    private LinearLayout mLayoutQuery;
    private PayCompletePresenter mPresenter;
    private boolean isSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_complete);
        initView();

        AppSetting.getInstance(mContext).putString(SharedKey.PAY_RESULT_URL, "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        Uri uri = intent.getData();
        String orderId = "";
        if (null != uri) {
            orderId = uri.getQueryParameter("orderid");
        }
        if (!TextUtils.isEmpty(orderId)) {
            initPresenter();
            initData(orderId);
        }
    }

    private void initPresenter() {
        mPresenter = new PayCompletePresenter();
        mPresenter.attachView(this);
    }

    private void initData(String orderId) {
        mPresenter.getOrderInfo(orderId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private void initView() {
        mLayoutSuccess = findViewById(R.id.layout_success);
        mLayoutFailure = findViewById(R.id.layout_fail);
        mLayoutQuery = findViewById(R.id.layout_query);
        findViewById(R.id.tv_success_complete).setOnClickListener(this);
        findViewById(R.id.tv_fail_complete).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_success_complete:
                PayResult.getInstance().setPaySuccess(true);
                finish();
                break;
            case R.id.tv_fail_complete:
                PayResult.getInstance().setPaySuccess(true);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isSuccess) {
            PayResult.getInstance().setPaySuccess(true);
        } else {
            PayResult.getInstance().setPaySuccess(true);
        }
    }

    @Override
    public void getOrderInfoSuccess(OrderInfoResponse response) {
        if (response.data.payStatus == 1) {
            isSuccess = true;
            mLayoutQuery.setVisibility(View.GONE);
            mLayoutSuccess.setVisibility(View.VISIBLE);
            mLayoutFailure.setVisibility(View.GONE);
        } else {
            isSuccess = false;
            mLayoutQuery.setVisibility(View.GONE);
            mLayoutSuccess.setVisibility(View.GONE);
            mLayoutFailure.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getOrderInfoFailure(String msg) {
        mLayoutSuccess.setVisibility(View.GONE);
        mLayoutFailure.setVisibility(View.VISIBLE);
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }
}
