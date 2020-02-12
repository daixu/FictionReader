package com.shangame.fiction.ui.login;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.manager.AppSetting;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.BindWeChatResponse;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.login.register.RegisterContract;
import com.shangame.fiction.ui.login.register.RegisterPresenter;
import com.shangame.fiction.ui.popup.RedPacketPopupWindow;
import com.shangame.fiction.ui.setting.security.SecurityContracts;
import com.shangame.fiction.ui.setting.security.SecurityPresenter;
import com.shangame.fiction.ui.task.BindWeChatContacts;
import com.shangame.fiction.ui.task.BindWeChatPresenter;
import com.shangame.fiction.ui.task.TaskAwardContacts;
import com.shangame.fiction.ui.task.TaskAwardPresenter;
import com.shangame.fiction.ui.task.TaskId;
import com.shangame.fiction.wxapi.WeChatConstants;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import static com.shangame.fiction.core.constant.SharedKey.AD_FREE;

public class AuthLoginActivity extends BaseActivity implements View.OnClickListener,
        RegisterContract.View, SecurityContracts.View, TaskAwardContacts.View
        , BindWeChatContacts.View {

    private static final int RESEND_DURATION = 60;
    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private EditText etSecurityCode;
    private TextView tvSendHint;
    private TextView tvSecurityCode;
    private Button btnRegisterAndLogin;
    private String phoneNumber;
    private String password;
    private int invitationCode = 0;
    private RegisterPresenter mRegisterPresenter;
    private SecurityPresenter securityPresenter;
    private TaskAwardPresenter taskAwardPresenter;
    private BindWeChatPresenter bindWechatPresenter;
    private IWXAPI api;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (BroadcastAction.WECHAT_BIND_ACTION.equals(intent.getAction())) {
                String code = intent.getStringExtra("code");
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                bindWechatPresenter.bindWeChat(userId, code, WeChatConstants.APP_ID);
            }
        }
    };

    public static void lunchActivity(Activity activity, String phoneNumber, String password, String invitationCode) {
        Intent intent = new Intent(activity, AuthLoginActivity.class);
        intent.putExtra("phoneNumber", phoneNumber);
        intent.putExtra("password", password);
        intent.putExtra("invitationCode", invitationCode);
        activity.startActivity(intent);
    }

    public static void lunchActivity(Activity activity, String phoneNumber) {
        Intent intent = new Intent(activity, AuthLoginActivity.class);
        intent.putExtra("phoneNumber", phoneNumber);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_login);

        phoneNumber = getIntent().getStringExtra("phoneNumber");
        password = getIntent().getStringExtra("password");
        String strInvitationCode = getIntent().getStringExtra("invitationCode");
        try {
            invitationCode = Integer.parseInt(strInvitationCode);
        } catch (Exception e) {
            Log.e("hhh", e.getMessage());
            invitationCode = 0;
        }

        mRegisterPresenter = new RegisterPresenter();
        mRegisterPresenter.attachView(this);

        securityPresenter = new SecurityPresenter();
        securityPresenter.attachView(this);

        taskAwardPresenter = new TaskAwardPresenter();
        taskAwardPresenter.attachView(this);

        bindWechatPresenter = new BindWeChatPresenter();
        bindWechatPresenter.attachView(this);

        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);

        if (TextUtils.isEmpty(password)) {
            tvPublicTitle.setText(R.string.code_login);
        } else {
            tvPublicTitle.setText(R.string.phone_register);
        }

        tvSendHint = findViewById(R.id.tvSendHint);
        etSecurityCode = findViewById(R.id.etSecurityCode);

        tvSecurityCode = findViewById(R.id.tvSecurityCode);
        tvSecurityCode.setOnClickListener(this);
        btnRegisterAndLogin = findViewById(R.id.btnRegistAndLogin);
        btnRegisterAndLogin.setOnClickListener(this);

        etSecurityCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()) && s.toString().length() >= 6) {
                    btnRegisterAndLogin.setEnabled(true);
                } else {
                    btnRegisterAndLogin.setEnabled(false);
                }
            }
        });

        tvSendHint.setText(getString(R.string.security_code_sended, phoneNumber));

        securityPresenter.sendSecurityCode(phoneNumber);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.WECHAT_BIND_ACTION);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        securityPresenter.detachView();
        mRegisterPresenter.detachView();
        bindWechatPresenter.detachView();
        mCompositeDisposable.clear();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvSecurityCode) {
            securityPresenter.sendSecurityCode(phoneNumber);
        } else if (view.getId() == R.id.ivPublicBack) {
            finish();
        } else if (view.getId() == R.id.btnRegistAndLogin) {
            String securityCode = etSecurityCode.getText().toString();
            if (TextUtils.isEmpty(securityCode)) {
                showToast(getString(R.string.hint_security_code));
                return;
            }
            if (securityCode.length() != 6) {
                showToast(getString(R.string.hint_input_auth_code));
                return;
            }
            if (TextUtils.isEmpty(password)) {
                mRegisterPresenter.phoneCodeLogin(phoneNumber, securityCode, invitationCode);
            } else {
                mRegisterPresenter.register(phoneNumber, password, securityCode, ApiConstant.RegisterPlatform.PHONE, invitationCode);
            }
        }
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
    public void registerSuccess(UserInfo userInfo) {
        showToast(getString(R.string.login_success));
        UserInfoManager.getInstance(mContext).saveUserInfo(userInfo);

        JPushInterface.setAlias(mContext, (int) userInfo.userid, String.valueOf(userInfo.userid));

//        ActivityStack.popToSpecifyActivity(LoginActivity.class);
        finish();

        AppSetting.getInstance(mContext).putString(SharedKey.LOGIN_USERNAME, userInfo.mobilephone);

        Intent intent = new Intent(BroadcastAction.REFRESH_BOOK_RACK_ACTION);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        if (userInfo.advertopen == 0) {
            AppSetting.getInstance(mContext).putBoolean(AD_FREE, true);
        } else {
            AppSetting.getInstance(mContext).putBoolean(AD_FREE, false);
        }

        SharedKey.SYS_AGENT_GRADE = userInfo.sysAgentGrade;

        if (userInfo.receive == 0) {
            taskAwardPresenter.getTaskAward(userInfo.userid, TaskId.LOGIN, true);
        }
    }

    @Override
    public void checkCodeSuccess(String phone, String code) {

    }

    @Override
    public void getTaskAwardSuccess(TaskAwardResponse taskAwardResponse, int taskid) {
        RedPacketPopupWindow redPacketPopupWindow = new RedPacketPopupWindow(mActivity, taskAwardResponse);
        redPacketPopupWindow.setBindWechatClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toWeChatLogin();
            }
        });
        redPacketPopupWindow.setDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                setResult(RESULT_OK, getIntent());
                finish();
            }
        });
        redPacketPopupWindow.show();
    }

    public void toWeChatLogin() {
        api = WXAPIFactory.createWXAPI(this, WeChatConstants.APP_ID, true);
        api.registerApp(WeChatConstants.APP_ID);

        if (!api.isWXAppInstalled()) {
            Toast.makeText(mActivity, R.string.not_wechat_app, Toast.LENGTH_SHORT).show();
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = WeChatConstants.State.BIND;
        api.sendReq(req);
    }

    @Override
    public void phoneCodeLoginSuccess(UserInfo userInfo) {
        registerSuccess(userInfo);
    }

    @Override
    public void bindWeChatSuccess(BindWeChatResponse inviteRecordResponse) {
        showToast("绑定微信成功");
        setResult(RESULT_OK, getIntent());
        finish();
    }
}
