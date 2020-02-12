package com.shangame.fiction.ui.login.register;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.ui.login.AuthLoginActivity;

/**
 * 注册登录 Activity
 * Create by Speedy on 2018/7/19
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEditPhoneNumber;
    private EditText mEditPassword;
    private EditText mEditInvitationCode;

    private Button mBtnNext;
    private boolean isHidePassword = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.phone_register);

        findViewById(R.id.ivPublicBack).setOnClickListener(this);

        int invitationCode = getIntent().getIntExtra("invitationCode", 0);

        mEditPhoneNumber = findViewById(R.id.edit_phone_number);
        mEditPassword = findViewById(R.id.edit_password);
        mEditInvitationCode = findViewById(R.id.edit_invitation_code);

        if (invitationCode > 0) {
            String strInvitationCode = invitationCode + "";
            mEditInvitationCode.setText(strInvitationCode);
        }
        mEditPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString()) || s.toString().length() < 6) {
                    mBtnNext.setEnabled(false);
                } else {
                    mBtnNext.setEnabled(true);
                }
            }
        });

        mBtnNext = findViewById(R.id.btn_next);
        mBtnNext.setOnClickListener(this);
        findViewById(R.id.image_hide).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivPublicBack:
                finish();
                break;
            case R.id.btn_next:
                register();
                break;
            case R.id.image_hide:
                if (isHidePassword) {
                    //如果选中，显示密码
                    isHidePassword = false;
                    mEditPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    isHidePassword = true;
                    mEditPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            default:
                break;
        }
    }

    private void register() {
        String phoneNumber = mEditPhoneNumber.getText().toString();
        String password = mEditPassword.getText().toString();
        String invitationCode = mEditInvitationCode.getText().toString();

        if (TextUtils.isEmpty(phoneNumber)) {
            showToast(getString(R.string.hint_phone_number));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showToast(getString(R.string.hint_setting_password));
            return;
        }
        if (TextUtils.isEmpty(invitationCode)) {
            invitationCode = "0";
        }

        AuthLoginActivity.lunchActivity(mActivity, phoneNumber, password, invitationCode);
        finish();
    }
}
