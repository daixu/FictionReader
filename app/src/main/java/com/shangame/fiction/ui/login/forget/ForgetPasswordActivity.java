package com.shangame.fiction.ui.login.forget;

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
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.setting.security.SecurityContracts;
import com.shangame.fiction.ui.setting.security.SecurityPresenter;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 忘记密码(找回密码) Activity
 * Create by Speedy on 2018/7/19
 */
public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener, SecurityContracts.View {

    private static final int RESEND_DURATION = 60;
    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private TextView tvPublicTitle;
    private String title;
    private EditText etPhoneNumber;
    private EditText etSecurityCode;
    private TextView tvSecurityCode;
    private Button btnNextStep;
    private SecurityPresenter securityPresenter;
    private boolean isFindBackPassword;//标记是否为找回密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        tvPublicTitle = findViewById(R.id.tvPublicTitle);
        title = getIntent().getStringExtra("title");

        if (TextUtils.isEmpty(title)) {
            tvPublicTitle.setText(R.string.find_password);
            isFindBackPassword = true;
        } else {
            tvPublicTitle.setText(title);
            isFindBackPassword = false;
        }

        findViewById(R.id.ivPublicBack).setOnClickListener(this);

        btnNextStep = findViewById(R.id.btnNextStep);
        btnNextStep.setOnClickListener(this);

        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etSecurityCode = findViewById(R.id.etSecurityCode);
        etSecurityCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()) && s.toString().length() == 6) {
                    btnNextStep.setEnabled(true);
                } else {
                    btnNextStep.setEnabled(false);
                }
            }
        });

        tvSecurityCode = findViewById(R.id.tvSecurityCode);
        tvSecurityCode.setOnClickListener(this);

        securityPresenter = new SecurityPresenter();
        securityPresenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        securityPresenter.detachView();
        mCompositeDisposable.clear();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivPublicBack:
                finish();
                break;
            case R.id.tvSecurityCode:
                String phone = etPhoneNumber.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    showToast(getString(R.string.hint_register_phone_number));
                    return;
                }
                if (!isFindBackPassword) {
                    UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
                    if (!userInfo.mobilephone.equals(phone)) {
                        showToast(getString(R.string.hint_phone_number_illegal));
                        return;
                    }
                }
                tvSecurityCode.setEnabled(false);//禁止再次点击
                securityPresenter.sendSecurityCode(phone);
                break;
            case R.id.btnNextStep:
                nextStop();
                break;
            default:
                break;
        }
    }

    private void nextStop() {
        String phone = etPhoneNumber.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            showToast(getString(R.string.hint_register_phone_number));
            return;
        }

        String code = etSecurityCode.getText().toString();
        if (TextUtils.isEmpty(code)) {
            showToast(getString(R.string.hint_security_code));
            return;
        }

        if (code.length() != 6) {
            showToast(getString(R.string.hint_input_auth_code));
            return;
        }
        if (!isFindBackPassword) {
            UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
            if (!userInfo.mobilephone.equals(phone)) {
                showToast(getString(R.string.hint_phone_number_illegal));
                return;
            }
        }
        securityPresenter.checkCode(phone, code);
    }

    @Override
    public void sendSecurityCodeSuccess() {
        showToast(getString(R.string.security_code_send_success));
        tvSecurityCode.setEnabled(false);
        tvSecurityCode.setTextColor(getResources().getColor(R.color.secondary_text));

        Disposable disposable = Observable.intervalRange(0, RESEND_DURATION, 1, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        tvSecurityCode.setText(getString(R.string.resend_code, RESEND_DURATION - aLong.intValue()));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }, new Action() {

                    @Override
                    public void run() throws Exception {
                        tvSecurityCode.setText(getString(R.string.reobtain_security_code));
                        tvSecurityCode.setTextColor(getResources().getColor(R.color.colorPrimary));
                        tvSecurityCode.setEnabled(true);
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void checkCodeSuccess(String phone, String code) {
        ChangePasswordActivity.luanchActivity(mActivity, phone, code, isFindBackPassword);
        finish();
    }

}
