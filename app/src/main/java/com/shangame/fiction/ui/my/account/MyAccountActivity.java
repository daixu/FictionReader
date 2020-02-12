package com.shangame.fiction.ui.my.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.my.account.coin.GiftCoinActivity;
import com.shangame.fiction.ui.my.pay.PayCenterActivity;
import com.shangame.fiction.ui.my.pay.history.PayHistoryActivity;

/**
 * 我的账户 Activity
 * Create by Speedy on 2018/8/2
 */
public class MyAccountActivity extends BaseActivity implements View.OnClickListener{

    private TextView tvAccountBalance;
    private TextView tvGiftBalance;

    private static final int TOP_UP_REQUEST_CODE =503;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        initView();
    }
    private void initView() {
        TextView tvPublicTitle = (TextView) findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.my_account);

        tvAccountBalance = (TextView) findViewById(R.id.tvAccountBalance);
        tvGiftBalance = (TextView) findViewById(R.id.tvGiftBalance);
        UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
        tvAccountBalance.setText(String.valueOf(userInfo.money));
        tvGiftBalance.setText(String.valueOf(userInfo.coin));

        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        findViewById(R.id.topUpLayout).setOnClickListener(this);
        findViewById(R.id.giftBalance).setOnClickListener(this);
        findViewById(R.id.topUpAndConsumeHistoryLayout).setOnClickListener(this);
        findViewById(R.id.payTourLayout).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.ivPublicBack){
            finish();
        }else  if(id == R.id.giftBalance){
            Intent intent = new Intent(mContext, GiftCoinActivity.class);
            startActivity(intent);
        }else  if(id == R.id.topUpLayout){
            Intent intent = new Intent(mContext, PayCenterActivity.class);
            startActivityForResult(intent,TOP_UP_REQUEST_CODE);
        }else  if(id == R.id.topUpAndConsumeHistoryLayout){
            Intent intent = new Intent(mContext, PayHistoryActivity.class);
            startActivity(intent);
        }else  if(id == R.id.payTourLayout){
            Intent intent = new Intent(mContext, PlayTourHistoryActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == TOP_UP_REQUEST_CODE){
            UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
            tvAccountBalance.setText(String.valueOf(userInfo.money));
            tvGiftBalance.setText(String.valueOf(userInfo.coin));
        }
    }
}
