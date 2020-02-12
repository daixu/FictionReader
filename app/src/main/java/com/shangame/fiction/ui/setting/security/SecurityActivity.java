package com.shangame.fiction.ui.setting.security;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.manager.AppSetting;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.login.LoginMethod;
import com.shangame.fiction.ui.login.forget.ForgetPasswordActivity;

public class SecurityActivity extends BaseActivity implements View.OnClickListener {

    private static final int BIND_PHONE_REQUEST_CODE = 566;
    private TextView tvBindState;
    private TextView tvPhone;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.account_security);

        tvBindState = findViewById(R.id.tvBindState);
        tvPhone = findViewById(R.id.tvPhone);
        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        findViewById(R.id.modifyPasswordLayout).setOnClickListener(this);

        userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
        findViewById(R.id.bindPhoneLayout).setOnClickListener(this);
        if (TextUtils.isEmpty(userInfo.mobilephone)) {
            tvBindState.setText(R.string.unbind);
        } else {
            tvBindState.setText("去修改");
            tvPhone.setText(userInfo.mobilephone);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivPublicBack) {
            finish();
        } else if (view.getId() == R.id.bindPhoneLayout) {
            startActivityForResult(new Intent(mActivity, BindPhoneActivity.class), BIND_PHONE_REQUEST_CODE);
        } else if (view.getId() == R.id.modifyPasswordLayout) {
            int loginMethod = AppSetting.getInstance(mContext).getInt(SharedKey.LOGIN_METHOD, LoginMethod.ACCOUNT_LOGIN);
            if (loginMethod == LoginMethod.ACCOUNT_LOGIN) {
                Intent intent = new Intent(mActivity, ForgetPasswordActivity.class);
                intent.putExtra("title", getString(R.string.modify_password));
                startActivity(intent);
            } else {
                showToast(getString(R.string.can_change_password));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BIND_PHONE_REQUEST_CODE && resultCode == RESULT_OK) {
            tvBindState.setText(R.string.binded);
            userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
            tvPhone.setText(userInfo.mobilephone);
        }
    }
}
