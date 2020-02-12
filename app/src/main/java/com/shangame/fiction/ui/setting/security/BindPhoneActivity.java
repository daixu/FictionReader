package com.shangame.fiction.ui.setting.security;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.setting.personal.PersonalContacts;
import com.shangame.fiction.ui.setting.personal.PersonalPresenter;
import com.shangame.fiction.ui.task.TaskAwardContacts;
import com.shangame.fiction.ui.task.TaskAwardPresenter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class BindPhoneActivity extends BaseActivity implements View.OnClickListener, SecurityContracts.View, PersonalContacts.View, TaskAwardContacts.View {

    private static final int RESEND_DURATION = 60;
    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private EditText etPhoneNumber;
    private EditText etSecurityCode;
    private TextView tvSecurityCode;
    private SecurityPresenter securityPresenter;
    private PersonalPresenter personalPresenter;
    private TaskAwardPresenter taskAwardPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);

        TextView tvPublicTitle = (TextView) findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.bind_phone);

        findViewById(R.id.ivPublicBack).setOnClickListener(this);

        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        etSecurityCode = (EditText) findViewById(R.id.etSecurityCode);
        tvSecurityCode = (TextView) findViewById(R.id.tvSecurityCode);
        tvSecurityCode.setOnClickListener(this);
        findViewById(R.id.btnFinish).setOnClickListener(this);

        securityPresenter = new SecurityPresenter();
        securityPresenter.attachView(this);

        personalPresenter = new PersonalPresenter();
        personalPresenter.attachView(this);

        taskAwardPresenter = new TaskAwardPresenter();
        taskAwardPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        securityPresenter.detachView();
        personalPresenter.detachView();
        taskAwardPresenter.detachView();
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
                securityPresenter.sendSecurityCode(phone);
                break;
            case R.id.btnFinish:
                phone = etPhoneNumber.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    showToast(getString(R.string.hint_register_phone_number));
                    return;
                }

                String securityCode = etSecurityCode.getText().toString();
                if (TextUtils.isEmpty(securityCode)) {
                    showToast(getString(R.string.hint_security_code));
                    return;
                } else if (securityCode.length() != 6) {
                    showToast(getString(R.string.hint_input_auth_code));
                    return;
                }
                commitModifyProfile(phone, securityCode);
                break;
            default:
                break;
        }
    }

    private void commitModifyProfile(String phone, String securityCode) {
        int userid = UserInfoManager.getInstance(mContext).getUserid();
        Map<String, Object> map = new HashMap<>();
        map.put("userid", userid);
        map.put("mobilephone", phone);
        map.put("codenumber", securityCode);
        personalPresenter.modifyProfile(map);
    }

    @Override
    public void sendSecurityCodeSuccess() {
        showToast(getString(R.string.security_code_send_success));

        tvSecurityCode.setEnabled(false);
        tvSecurityCode.setTextColor(getResources().getColor(R.color.secondary_text));
        Disposable disposable = Observable.intervalRange(0, RESEND_DURATION, 1, 1, TimeUnit.SECONDS)
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

    }

    @Override
    public void modifyProfileSuccess(UserInfo userInfo) {
        UserInfoManager.getInstance(mContext).saveUserInfo(userInfo);
        showToast(getString(R.string.hint_bind_phone_success));
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void getTaskAwardSuccess(TaskAwardResponse taskAwardResponse, int taskid) {

    }
}
