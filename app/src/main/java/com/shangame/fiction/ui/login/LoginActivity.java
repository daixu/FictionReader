package com.shangame.fiction.ui.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.config.QQConfig;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.manager.AppSetting;
import com.shangame.fiction.core.manager.Logger;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.BindWeChatResponse;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.storage.manager.DbManager;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.login.forget.ForgetPasswordActivity;
import com.shangame.fiction.ui.login.register.RegisterActivity;
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
import com.shangame.fiction.ui.web.WebViewActivity;
import com.shangame.fiction.wxapi.WeChatConstants;
import com.tencent.connect.common.Constants;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import static com.shangame.fiction.core.constant.SharedKey.AD_FREE;


/**
 * 登录 Activity
 * Create by Speedy on 2018/7/19
 */
public class LoginActivity extends BaseActivity implements LoginContract.View, View.OnClickListener,
        SecurityContracts.View, RegisterContract.View,
        TaskAwardContacts.View, BindWeChatContacts.View {

    private static final int RESEND_DURATION = 60;

    private static final String TAG = "LoginActivity";
    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<View> viewList = new ArrayList<>(2);
    private EditText etAccountPhone;
    private ImageView ivHide;
    private Button btnLogin;
    private EditText etCodePhone;
    private Button btnPhoneLogin;
    private EditText etPassword;
    private LoginPresenter mLoginPresenter;
    private IWXAPI api;
    private Tencent mTencent;
    private SecurityPresenter securityPresenter;
    private RegisterPresenter mRegisterPresenter;
    private EditText etSecurityCode;
    private TextView tvSecurityCode;

    private int mInvitationCode;

    private TaskAwardPresenter taskAwardPresenter;

    private BindWeChatPresenter bindWechatPresenter;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BroadcastAction.WECHAT_LOGION_ACTION.equals(action)) {
                String code = intent.getStringExtra("code");
                mLoginPresenter.weChatLogin(code, mInvitationCode);
            } else if (BroadcastAction.WECHAT_BIND_ACTION.equals(action)) {
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                String code = intent.getStringExtra("code");
                bindWechatPresenter.bindWeChat(userId, code, WeChatConstants.APP_ID);
            }
        }
    };
    private boolean isHidePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initPresenter();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.WECHAT_LOGION_ACTION);
        intentFilter.addAction(BroadcastAction.WECHAT_BIND_ACTION);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, intentFilter);
    }

    private void initView() {
        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        findViewById(R.id.tvCopyright).setOnClickListener(this);
        findViewById(R.id.tvPrivate).setOnClickListener(this);
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.login);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        View accountLoginView = getLayoutInflater().inflate(R.layout.account_login, null);
        View phoneLoginView = getLayoutInflater().inflate(R.layout.phone_login, null);

        viewList.add(phoneLoginView);
        viewList.add(accountLoginView);

        initViewPager();

        initAccountLoginView(accountLoginView);
        initPhoneLoginView(phoneLoginView);
        initThirdLoginView();
        mInvitationCode = getIntent().getIntExtra("invitationCode", 0);
    }

    private void initPresenter() {
        mLoginPresenter = new LoginPresenter();
        mLoginPresenter.attachView(this);

        taskAwardPresenter = new TaskAwardPresenter();
        taskAwardPresenter.attachView(this);

        bindWechatPresenter = new BindWeChatPresenter();
        bindWechatPresenter.attachView(this);
    }

    private void initViewPager() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    String phone = etAccountPhone.getText().toString();
                    if (!TextUtils.isEmpty(phone)) {
                        etCodePhone.setText(phone);
                        etCodePhone.setSelection(phone.length());
                    }
                } else {
                    String phone = etCodePhone.getText().toString();
                    if (!TextUtils.isEmpty(phone)) {
                        etAccountPhone.setText(phone);
                        etAccountPhone.setSelection(phone.length());
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            /**
             * 返回要显示的view,即要显示的视图
             */
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = viewList.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0) {
                    return getString(R.string.code_login);
                } else {
                    return getString(R.string.passoword_login);
                }
            }
        });

        tabLayout.setupWithViewPager(viewPager);
    }

    private void initAccountLoginView(View accountLoginView) {
        etAccountPhone = accountLoginView.findViewById(R.id.etAccountPhone);
        etPassword = accountLoginView.findViewById(R.id.etPassword);

        btnLogin = accountLoginView.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        etAccountPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString()) || TextUtils.isEmpty(etPassword.getText().toString())) {
                    btnLogin.setEnabled(false);
                } else {
                    btnLogin.setEnabled(true);
                }
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString()) || s.toString().length() < 6 || TextUtils.isEmpty(etAccountPhone.getText().toString())) {
                    btnLogin.setEnabled(false);
                } else {
                    btnLogin.setEnabled(true);
                }
            }
        });

        accountLoginView.findViewById(R.id.tvForgetPassword).setOnClickListener(this);
        accountLoginView.findViewById(R.id.tvRegister).setOnClickListener(this);
        ivHide = accountLoginView.findViewById(R.id.ivHide);
        ivHide.setOnClickListener(this);

        String phone = AppSetting.getInstance(mContext).getString(SharedKey.LOGIN_USERNAME, "");
        etAccountPhone.setText(phone);
        etAccountPhone.setSelection(phone.length());
    }

    private void initPhoneLoginView(View phoneLoginView) {
        etCodePhone = phoneLoginView.findViewById(R.id.etCodePhone);
        btnPhoneLogin = phoneLoginView.findViewById(R.id.btnPhoneLogin);
        btnPhoneLogin.setOnClickListener(this);

        String phone = AppSetting.getInstance(mContext).getString(SharedKey.LOGIN_USERNAME, "");
        etCodePhone.setText(phone);
        etCodePhone.setSelection(phone.length());

        securityPresenter = new SecurityPresenter();
        securityPresenter.attachView(this);

        mRegisterPresenter = new RegisterPresenter();
        mRegisterPresenter.attachView(this);

        tvSecurityCode = phoneLoginView.findViewById(R.id.tvSecurityCode);
        tvSecurityCode.setOnClickListener(this);

        etSecurityCode = phoneLoginView.findViewById(R.id.etSecurityCode);

        etSecurityCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!android.text.TextUtils.isEmpty(s.toString()) && s.toString().length() >= 6) {
                    btnPhoneLogin.setEnabled(true);
                } else {
                    btnPhoneLogin.setEnabled(false);
                }
            }
        });
    }

    private void initThirdLoginView() {
        findViewById(R.id.tvWechat).setOnClickListener(this);
        findViewById(R.id.tvQQ).setOnClickListener(this);
        findViewById(R.id.tvWeibo).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginPresenter.detachView();
        taskAwardPresenter.detachView();
        bindWechatPresenter.detachView();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }

    private void verifyQQLoginExpiresTime() {
        int loginMethod = AppSetting.getInstance(mContext).getInt(SharedKey.LOGIN_METHOD, LoginMethod.ACCOUNT_LOGIN);
        if (loginMethod == LoginMethod.QQ_LOGIN) {
            toQQLogin();
        }
    }

    private void toQQLogin() {
        mTencent = Tencent.createInstance(QQConfig.appId, this.getApplicationContext());

        mTencent.login(this, "get_user_info", new LoginListener());
    }

    @Override
    public void accountLoginSuccess(UserInfo userInfo) {
        AppSetting.getInstance(mContext).putLong(SharedKey.CURRENT_USER_ID, userInfo.userid);
        DbManager.getDaoSession(mContext).getChapterInfoDao().deleteAll();
        DbManager.close();
        UserInfoManager.getInstance(mContext).saveUserInfo(userInfo);
        checkTaskAward(userInfo);
        AppSetting.getInstance(mContext).putString(SharedKey.LOGIN_USERNAME, userInfo.mobilephone);
        JPushInterface.setAlias(mContext, (int) userInfo.userid, String.valueOf(userInfo.userid));

        Intent intent = new Intent(BroadcastAction.REFRESH_BOOK_RACK_ACTION);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        if (userInfo.advertopen == 0) {
            AppSetting.getInstance(mContext).putBoolean(AD_FREE, true);
        } else {
            AppSetting.getInstance(mContext).putBoolean(AD_FREE, false);
        }

        SharedKey.SYS_AGENT_GRADE = userInfo.sysAgentGrade;
    }

    @Override
    public void weChatLoginSuccess(UserInfo userInfo) {
        AppSetting.getInstance(mContext).putLong(SharedKey.CURRENT_USER_ID, userInfo.userid);
        DbManager.getDaoSession(mContext).getChapterInfoDao().deleteAll();
        DbManager.close();
        UserInfoManager.getInstance(mContext).saveUserInfo(userInfo);
        checkTaskAward(userInfo);

        Intent intent = new Intent(BroadcastAction.REFRESH_BOOK_RACK_ACTION);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        if (userInfo.advertopen == 0) {
            AppSetting.getInstance(mContext).putBoolean(AD_FREE, true);
        } else {
            AppSetting.getInstance(mContext).putBoolean(AD_FREE, false);
        }

        JPushInterface.setAlias(mContext, (int) userInfo.userid, String.valueOf(userInfo.userid));

        SharedKey.SYS_AGENT_GRADE = userInfo.sysAgentGrade;
    }

    @Override
    public void qqLoginSuccess(UserInfo userInfo) {
        AppSetting.getInstance(mContext).putLong(SharedKey.CURRENT_USER_ID, userInfo.userid);
        DbManager.getDaoSession(mContext).getChapterInfoDao().deleteAll();
        DbManager.close();
        UserInfoManager.getInstance(mContext).saveUserInfo(userInfo);
        checkTaskAward(userInfo);
        AppSetting.getInstance(mContext).putInt(SharedKey.LOGIN_METHOD, LoginMethod.QQ_LOGIN);
        JPushInterface.setAlias(mContext, (int) userInfo.userid, String.valueOf(userInfo.userid));

        Intent intent = new Intent(BroadcastAction.REFRESH_BOOK_RACK_ACTION);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        if (userInfo.advertopen == 0) {
            AppSetting.getInstance(mContext).putBoolean(AD_FREE, true);
        } else {
            AppSetting.getInstance(mContext).putBoolean(AD_FREE, false);
        }

        SharedKey.SYS_AGENT_GRADE = userInfo.sysAgentGrade;
    }

    private void checkTaskAward(UserInfo userInfo) {
        if (userInfo.receive == 0) {
            taskAwardPresenter.getTaskAward(userInfo.userid, TaskId.LOGIN, true);
        } else {
            showToast(getString(R.string.login_success));
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSecurityCode:
                String phoneNumber = etCodePhone.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)) {
                    showToast(getString(R.string.hint_phone_number));
                    return;
                }
                securityPresenter.sendSecurityCode(phoneNumber);
                break;
            case R.id.tvCopyright:
                String url = "https://m.anmaa.com/Mine/Agreement?channel=" + ApiConstant.Channel.ANDROID;
                WebViewActivity.lunchActivity(mActivity, "服务协议", url);
                break;
            case R.id.tvPrivate:
                String url2 = "https://m.anmaa.com/Mine/Privacy?channel=" + ApiConstant.Channel.ANDROID;
                WebViewActivity.lunchActivity(mActivity, "隐私协议", url2);
                break;
            case R.id.ivPublicBack:
                finish();
                break;
            case R.id.btnLogin:
                String username = etAccountPhone.getText().toString();
                String password = etPassword.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    showToast(getString(R.string.username_empty));
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    showToast(getString(R.string.password_empty));
                    return;
                }
                if (password.length() < 6) {
                    showToast(getString(R.string.password_lenght_error));
                    return;
                }
                mLoginPresenter.accountLogin(username, password, mInvitationCode);
                break;
            case R.id.btnPhoneLogin:
                String securityCode = etSecurityCode.getText().toString();
                if (android.text.TextUtils.isEmpty(securityCode)) {
                    showToast(getString(R.string.hint_security_code));
                    return;
                }
                if (securityCode.length() != 6) {
                    showToast(getString(R.string.hint_input_auth_code));
                    return;
                }

                phoneNumber = etCodePhone.getText().toString();
                mRegisterPresenter.phoneCodeLogin(phoneNumber, securityCode, mInvitationCode);
                break;
            case R.id.tvForgetPassword:
                Intent intent = new Intent(mActivity, ForgetPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.ivHide:
                if (isHidePassword) {
                    //如果选中，显示密码
                    isHidePassword = false;
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivHide.setImageResource(R.drawable.login_open);
                } else {
                    //否则隐藏密码
                    isHidePassword = true;
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivHide.setImageResource(R.drawable.login_hide);
                }
                break;
            case R.id.tvRegister:
                intent = new Intent(mActivity, RegisterActivity.class);
                intent.putExtra("invitationCode", mInvitationCode);
                startActivity(intent);
                break;
            case R.id.tvWechat:
                toWeChatLogin(WeChatConstants.State.LOGIN);
                break;
            case R.id.tvQQ:
                toQQLogin();
                break;
            case R.id.tvWeibo:
                showToast("暂未实现");
                break;
            default:
                break;
        }
    }

    private void toWeChatLogin(String state) {
        api = WXAPIFactory.createWXAPI(this, WeChatConstants.APP_ID, true);
        api.registerApp(WeChatConstants.APP_ID);

        if (!api.isWXAppInstalled()) {
            Toast.makeText(mActivity, R.string.not_wechat_app, Toast.LENGTH_SHORT).show();
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = state;
        api.sendReq(req);
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
    public void registerSuccess(UserInfo userInfo) {
        showToast(getString(R.string.login_success));

        UserInfoManager.getInstance(mContext).saveUserInfo(userInfo);

        JPushInterface.setAlias(mContext, (int) userInfo.userid, String.valueOf(userInfo.userid));

        AppSetting.getInstance(mContext).putString(SharedKey.LOGIN_USERNAME, userInfo.mobilephone);

        Intent intent = new Intent(BroadcastAction.REFRESH_BOOK_RACK_ACTION);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        checkTaskAward(userInfo);

        if (userInfo.advertopen == 0) {
            AppSetting.getInstance(mContext).putBoolean(AD_FREE, true);
        } else {
            AppSetting.getInstance(mContext).putBoolean(AD_FREE, false);
        }

        SharedKey.SYS_AGENT_GRADE = userInfo.sysAgentGrade;
    }

    @Override
    public void phoneCodeLoginSuccess(UserInfo userInfo) {
        SharedKey.SYS_AGENT_GRADE = userInfo.sysAgentGrade;
    }

    @Override
    public void getTaskAwardSuccess(TaskAwardResponse taskAwardResponse, int taskid) {
        RedPacketPopupWindow redPacketPopupWindow = new RedPacketPopupWindow(mActivity, taskAwardResponse);
        redPacketPopupWindow.setBindWechatClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toWeChatLogin(WeChatConstants.State.BIND);
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

    @Override
    public void bindWeChatSuccess(BindWeChatResponse inviteRecordResponse) {
        showToast("绑定微信成功");
        setResult(RESULT_OK, getIntent());
        finish();
    }

    private void parseQQLogin(JSONObject jsonObject) {
        String openid = jsonObject.optString(Constants.PARAM_OPEN_ID);
        String accessToken = jsonObject.optString(Constants.PARAM_ACCESS_TOKEN);
        String expires = jsonObject.optString(Constants.PARAM_EXPIRES_IN);
        if (!TextUtils.isEmpty(accessToken) && !TextUtils.isEmpty(expires)
                && !TextUtils.isEmpty(openid)) {
            if (mTencent != null) {
                mTencent.setAccessToken(accessToken, expires);
                mTencent.setOpenId(openid);
            }
        }
        mLoginPresenter.qqLogin(openid, accessToken, mInvitationCode);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode, resultCode, data, new LoginListener());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class LoginListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            JSONObject jsonObject = (JSONObject) o;
            parseQQLogin(jsonObject);
        }

        @Override
        public void onError(UiError uiError) {
            Logger.i(TAG, "onError: " + uiError.errorCode);
            Logger.i(TAG, "onError: " + uiError.errorDetail);
            Logger.i(TAG, "onError: " + uiError.errorMessage);
            showToast(uiError.errorMessage);
        }

        @Override
        public void onCancel() {
            showToast(getString(R.string.cancel_login));
            AppSetting.getInstance(mContext).putInt(SharedKey.LOGIN_METHOD, LoginMethod.ACCOUNT_LOGIN);
        }
    }
}
