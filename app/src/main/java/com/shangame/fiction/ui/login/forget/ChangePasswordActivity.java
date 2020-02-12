package com.shangame.fiction.ui.login.forget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.manager.ActivityStack;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.login.LoginActivity;

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener,ChangePasswordContacts.View,FindPaswordContacts.View{

    private EditText etNewPassword;
    private EditText etNewPasswordAgain;

    private Button btnSure;

    private String phone;
    private String code;

    private boolean isFindBackPassword;

    private ChangePasswordPresenter changePasswordPresenter;
    private FindPasswordPresenter findPasswordPresenter;




    public static final void luanchActivity(Activity activity,String phone,String code,boolean isFindBackPassword){
        Intent intent = new Intent(activity,ChangePasswordActivity.class);
        intent.putExtra("phone",phone);
        intent.putExtra("code",code);
        intent.putExtra("isFindBackPassword",isFindBackPassword);
        activity.startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        phone = getIntent().getStringExtra("phone");
        code = getIntent().getStringExtra("code");
        isFindBackPassword = getIntent().getBooleanExtra("isFindBackPassword",false);

        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        TextView tvPublicTitle = (TextView) findViewById(R.id.tvPublicTitle);
        if(isFindBackPassword){
            tvPublicTitle.setText(R.string.find_password);
        }else{
            tvPublicTitle.setText(R.string.forget_password);
        }

        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etNewPasswordAgain = (EditText) findViewById(R.id.etNewPasswordAgain);

        btnSure = findViewById(R.id.btnSure);
        btnSure.setOnClickListener(this);

        etNewPasswordAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(s.toString())){
                    btnSure.setEnabled(false);
                }else{
                    btnSure.setEnabled(true);
                }
            }
        });

        changePasswordPresenter = new ChangePasswordPresenter();
        changePasswordPresenter.attachView(this);

        findPasswordPresenter =  new FindPasswordPresenter();
        findPasswordPresenter.attachView(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivPublicBack:
                finish();
                break;
            case R.id.btnSure:
                sure();
                break;
        }
    }


    private void sure() {
        String newPassword = etNewPassword.getText().toString();
        if(TextUtils.isEmpty(newPassword)){
            showToast(getString(R.string.hint_new_password));
            return;
        }

        String newPasswordAgain = etNewPasswordAgain.getText().toString();
        if(TextUtils.isEmpty(newPasswordAgain)){
            showToast(getString(R.string.hint_new_password_again));
            return;
        }
        if(!newPassword.equals(newPasswordAgain)){
            showToast(getString(R.string.password_not_same));
            return;
        }


        if(isFindBackPassword){
            findPasswordPresenter.findPassword(phone,newPassword,code);
        }else{
            int userid = UserInfoManager.getInstance(mContext).getUserid();
            changePasswordPresenter.changePassowrd(userid,newPassword,code);
        }
    }



    @Override
    public void changePassowrdSuccess() {
        findPasswordSuccess();
    }

    @Override
    public void findPasswordSuccess() {
        showToast(getString(R.string.change_password_success));
        //重新登录
        Intent intent = new Intent(mActivity, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        changePasswordPresenter.detachView();
        findPasswordPresenter.detachView();
    }
}
