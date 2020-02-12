package com.shangame.fiction.core.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.shangame.fiction.R;
import com.shangame.fiction.core.config.AppConfig;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.manager.ActivityStack;
import com.shangame.fiction.core.manager.AppSetting;
import com.shangame.fiction.core.utils.BrightnessUtils;
import com.shangame.fiction.core.utils.StatusBarUtil;
import com.shangame.fiction.ui.login.LoginActivity;
import com.shangame.fiction.ui.sales.partner.InvitationCodePopupWindow;
import com.shangame.fiction.widget.PublicLoadDialog;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.net.SocketTimeoutException;


/**
 * Create by Speedy on 2018/7/19
 *
 * @author hhh
 */
public class BaseActivity extends RxAppCompatActivity implements BaseContract.BaseView {

    public static final int LUNCH_LOGIN_ACTIVITY_REQUEST_CODE = 435;

    public static final int PAGE_SIZE = 20;
    private static Toast mToast;
    protected Activity mActivity;
    protected Context mContext;
    protected boolean useThemestatusBarColor = false;//是否使用特殊的标题栏背景颜色，android5.0以上可以设置状态栏背景色，如果不使用则使用透明色值
    protected boolean useStatusBarColor = true;//是否使用状态栏文字和图标为暗色，如果状态栏采用了白色系，则需要使状态栏和图标为暗色，android6.0以上可以设置
    private PublicLoadDialog mProgressDialog;

    private boolean isShowDialog = true;

    /**
     * 设置状态栏透明
     */
    @TargetApi(19)
    public static void setTranslucentStatus(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            // 两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            //导航栏颜色也可以正常设置
            window.setNavigationBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            attributes.flags |= flagTranslucentStatus;
            //int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            // attributes.flags |= flagTranslucentNavigation; window.setAttributes(attributes);
        }
    }

    public void showInvitationCodeDialog(boolean showDialog) {
        this.isShowDialog = showDialog;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        mContext = getApplicationContext();
        ActivityStack.pushActivity(this);
//        initDayModel();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (isShowDialog) {
            bindInvitationCode();
        }
    }

    private void bindInvitationCode() {
        Log.e("hhh", "bindInvitationCode");
        // 获取系统剪贴板
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (null != clipboard) {
            // 添加剪贴板数据改变监听器
            if (clipboard.hasPrimaryClip()) {
                ClipData clipData = clipboard.getPrimaryClip();
                if (clipData != null && clipData.getItemCount() > 0) {
                    CharSequence addedText = clipboard.getPrimaryClip().getItemAt(0).getText();
                    String addedTextString = String.valueOf(addedText).trim();
                    Log.e("hhh", "addedTextString= " + addedTextString);
                    if (addedTextString.length() > 5 && addedTextString.length() < 9) {
                        try {
                            int invitationCode = Integer.parseInt(addedTextString);
                            Log.e("hhh", "invitationCode= " + invitationCode);

                            InvitationCodePopupWindow customPopup = new InvitationCodePopupWindow(this, this, invitationCode);
                            new XPopup.Builder(this)
                                    .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                                    .dismissOnBackPressed(false)
                                    .dismissOnTouchOutside(false)
                                    .asCustom(customPopup)
                                    .show();
                        } catch (NumberFormatException e) {
                            Log.e("eee", e.getMessage());
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStack.popActivity(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initThemeStyle();
    }

    private void initThemeStyle() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            StatusBarUtil.setColor(this, getResources().getColor(R.color.white));
        }
    }

    public void initDayModel() {
        boolean nightMode = AppSetting.getInstance(mContext).getBoolean(SharedKey.NIGHT_MODE, false);
        if (nightMode) {
            BrightnessUtils.setActivityBrightness(mActivity, AppConfig.NIGHT_BRIGHTNESS);
        } else {
            int brightness = AppSetting.getInstance(mContext).getInt(SharedKey.ACTIVITY_BRIGHTNESS, 0);
            int activityBrightness = BrightnessUtils.getActivityBrightness(mActivity);
            if (brightness != 0 && brightness > activityBrightness) {
                BrightnessUtils.setActivityBrightness(mActivity, brightness);
            }
        }
    }

    public void setStatusBarColor(int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(colorId));
        }
    }

    @Override
    public void showLoading() {
        if (mProgressDialog == null) {
            mProgressDialog = new PublicLoadDialog(mActivity);
        }

        if (mActivity != null && !mActivity.isFinishing()) {
            mProgressDialog.show();
        }
    }

    @Override
    public void dismissLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            try {
                mProgressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showNotNetworkView() {
        dismissLoading();
        showToast("网络异常，请检查网络设置。");
    }

    @Override
    public void showToast(String msg) {
        if (android.text.TextUtils.isEmpty(msg)) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    @Override
    public void showError(Throwable throwable) {
        dismissLoading();
        if (throwable instanceof SocketTimeoutException || throwable instanceof ConnectTimeoutException) {
            showToast(getString(R.string.connect_time_out));
        } else if (throwable instanceof IOException) {
            showToast(getString(R.string.network_error));
        } else {
            String error = throwable.getMessage();
            if (TextUtils.isEmpty(error)) {
                showToast(getString(R.string.request_error));
            } else {
                showToast(error);
            }
        }
    }

    @Override
    public void lunchLoginActivity() {
        dismissLoading();
        Intent mIntent = new Intent(mContext, LoginActivity.class);
        mActivity.startActivityForResult(mIntent, LUNCH_LOGIN_ACTIVITY_REQUEST_CODE);
    }

    public void lunchLoginActivity(int formCode) {
        dismissLoading();
        Intent mIntent = new Intent(mContext, LoginActivity.class);
        mIntent.putExtra(SharedKey.FROM_CODE, formCode);
        mActivity.startActivityForResult(mIntent, LUNCH_LOGIN_ACTIVITY_REQUEST_CODE);
    }
}
