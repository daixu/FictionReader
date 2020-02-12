package com.shangame.fiction.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.config.AppConfig;
import com.shangame.fiction.core.config.QQConfig;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.manager.ActivityStack;
import com.shangame.fiction.core.manager.AppSetting;
import com.shangame.fiction.core.utils.BrightnessUtils;
import com.shangame.fiction.storage.manager.DbManager;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.main.MainFrameWorkActivity;
import com.shangame.fiction.ui.my.about.AboutActivity;
import com.shangame.fiction.ui.my.message.MessageCenterActivity;
import com.shangame.fiction.ui.my.pay.AutoPayActivity;
import com.shangame.fiction.ui.setting.feedback.SuggestFeedbackActivity;
import com.shangame.fiction.ui.setting.personal.PersonalProfileActivity;
import com.shangame.fiction.ui.setting.security.SecurityActivity;
import com.tencent.tauth.Tencent;

import cn.jpush.android.api.JPushInterface;

import static com.shangame.fiction.core.constant.SharedKey.AD_FREE;

/**
 * 设置
 * Create by Speedy on 2018/8/7
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener, ExitContacts.View {

    private ImageView nightModeSwitch;
    private boolean nightMode;

    private ExitPresenter exitPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.setting);

        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        findViewById(R.id.account_safety_layout).setOnClickListener(this);
        findViewById(R.id.personal_profile_layout).setOnClickListener(this);
        findViewById(R.id.message_notification_layout).setOnClickListener(this);
        nightModeSwitch = findViewById(R.id.nightModeSwitch);
        nightModeSwitch.setOnClickListener(this);
        findViewById(R.id.autoBuyLayout).setOnClickListener(this);
        findViewById(R.id.suggest_feedback_layout).setOnClickListener(this);
        findViewById(R.id.about_me_layout).setOnClickListener(this);

        Button btnExit = findViewById(R.id.btnExit);
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        if (userId == 0) {
            btnExit.setVisibility(View.GONE);
        } else {
            btnExit.setVisibility(View.VISIBLE);
        }
        btnExit.setOnClickListener(this);

        nightMode = AppSetting.getInstance(mContext).getBoolean(SharedKey.NIGHT_MODE, false);
        if (nightMode) {
            nightModeSwitch.setImageResource(R.drawable.switch_on);
        } else {
            nightModeSwitch.setImageResource(R.drawable.switch_off);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exitPresenter != null) {
            exitPresenter.detachView();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivPublicBack:
                finish();
                break;
            case R.id.account_safety_layout: {
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                if (userId == 0) {
                    super.lunchLoginActivity();
                    return;
                }
                startActivity(new Intent(mContext, SecurityActivity.class));
            }
            break;
            case R.id.personal_profile_layout: {
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                if (userId == 0) {
                    super.lunchLoginActivity();
                    return;
                }
                startActivity(new Intent(mContext, PersonalProfileActivity.class));
            }
            break;
            case R.id.message_notification_layout: {
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                if (userId == 0) {
                    super.lunchLoginActivity();
                    return;
                }
                startActivity(new Intent(mContext, MessageCenterActivity.class));
            }
            break;
            case R.id.nightModeSwitch:
                changDayMode();
                break;
            case R.id.autoBuyLayout: {
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                if (userId == 0) {
                    super.lunchLoginActivity();
                    return;
                }
                startActivity(new Intent(mContext, AutoPayActivity.class));
            }
            break;
            case R.id.suggest_feedback_layout: {
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                if (userId == 0) {
                    super.lunchLoginActivity();
                    return;
                }
                startActivity(new Intent(mContext, SuggestFeedbackActivity.class));
            }
            break;
            case R.id.about_me_layout:
                startActivity(new Intent(mContext, AboutActivity.class));
                break;
            case R.id.btnExit:
                exit();
                break;
            default:
                break;
        }
    }

    private void changDayMode() {
        nightMode = !nightMode;
        AppSetting.getInstance(mContext).putBoolean(SharedKey.NIGHT_MODE, nightMode);
        if (nightMode) {
            nightModeSwitch.setImageResource(R.drawable.switch_on);
            int brightness = BrightnessUtils.getScreenBrightness(mActivity);
            AppSetting.getInstance(mContext).putInt(SharedKey.ACTIVITY_BRIGHTNESS, brightness);
            BrightnessUtils.setActivityBrightness(mActivity, AppConfig.NIGHT_BRIGHTNESS);
        } else {
            nightModeSwitch.setImageResource(R.drawable.switch_off);
            int brightness = AppSetting.getInstance(mContext).getInt(SharedKey.ACTIVITY_BRIGHTNESS, AppConfig.SUN_BRIGHTNESS);
            BrightnessUtils.setActivityBrightness(mActivity, brightness);
        }
    }

    private void exit() {
        exitPresenter = new ExitPresenter();
        exitPresenter.attachView(this);
        UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
        if (userInfo.regtype == 2) {
            Tencent mTencent = Tencent.createInstance(QQConfig.appId, this.getApplicationContext());
            mTencent.logout(mContext);
        }
        exitPresenter.exit(userInfo.userid);
    }

    @Override
    public void exitSuccess() {
        AppSetting.getInstance(mContext).putBoolean(AD_FREE, false);
        DbManager.getDaoSession(mContext).getUserInfoDao().deleteAll();
        DbManager.getDaoSession(mContext).getChapterInfoDao().deleteAll();
        ActivityStack.popAllActivity();
        startActivity(new Intent(mActivity, MainFrameWorkActivity.class));
        showToast(getString(R.string.exit_suceesee));
        JPushInterface.stopPush(mContext);
    }
}
