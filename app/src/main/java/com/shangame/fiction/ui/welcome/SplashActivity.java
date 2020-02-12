package com.shangame.fiction.ui.welcome;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.MainThread;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.shangame.fiction.R;
import com.shangame.fiction.ad.ADConfig;
import com.shangame.fiction.ad.TTAdManagerHolder;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.utils.AppUtils;
import com.shangame.fiction.net.response.AdBean;
import com.shangame.fiction.net.response.VersionCheckResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.main.MainFrameWorkActivity;
import com.shangame.fiction.ui.my.about.VersionCheckContracts;
import com.shangame.fiction.ui.my.about.VersionCheckPresenter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 启动页
 * <p>
 * Create by Speedy on 2018/7/17
 */

public class SplashActivity extends BaseActivity implements WeakHandler.IHandler, VersionCheckContracts.View {

    private static final String TAG = "SplashActivity";

    private static final String SKIP_TEXT = "点击跳过 %d";
    //开屏广告加载超时时间,建议大于3000,这里为了冷启动第一次加载到广告并且展示,示例设置了3000ms
    private static final int AD_TIME_OUT = 5000;
    private static final int MSG_GO_MAIN = 1;
    //开屏广告加载发生超时但是SDK没有及时回调结果的时候，做的一层保护。
    private final WeakHandler mHandler = new WeakHandler(this);
    private TTAdNative mTTAdNative;
    private ViewGroup adContainer;
    private TextView skipView;
    private ImageView splashHolder;
    //开屏广告是否已经加载
    private boolean mHasLoaded;
    private VersionCheckPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View contentView = View.inflate(this, R.layout.activity_splash, null);
        setContentView(contentView);

        showInvitationCodeDialog(false);

        adContainer = findViewById(R.id.adContainer);
        skipView = findViewById(R.id.skip_view);
        splashHolder = findViewById(R.id.splash_holder);

        mPresenter = new VersionCheckPresenter();
        mPresenter.attachView(this);

        //step2:创建TTAdNative对象
        mTTAdNative = TTAdManagerHolder.get().createAdNative(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermission();
        } else {
            checkNewVersion();
            // fetchSplashAD(this, adContainer, skipView,  ADConfig.APPID, ADConfig.ADPositionID.SPLASH_ID, this, 0);
            // loadSplashAd();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<>();
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        // 权限都已经有了，那么直接调用SDK
        if (lackedPermission.size() == 0) {
            // loadSplashAd();
            checkNewVersion();
            // fetchSplashAD(this, adContainer, skipView, ADConfig.APPID, ADConfig.ADPositionID.SPLASH_ID, this, 0);
        } else {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
        }
    }

    private void checkNewVersion() {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        int version = AppUtils.getAppVersionCode();
        mPresenter.checkNewVersion(userId, version);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {
            // fetchSplashAD(this, adContainer, skipView, ADConfig.APPID, ADConfig.ADPositionID.SPLASH_ID, this, 0);
            // loadSplashAd();
            checkNewVersion();
        } else {
            // 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
            Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            finish();
        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    //防止用户返回键退出 APP
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void handleMsg(Message msg) {
        if (msg.what == MSG_GO_MAIN) {
            if (!mHasLoaded) {
                Log.d(TAG, "handleMsg onAdTimeOver");
                // showToast("广告已超时，跳到主页面");
                goToMainActivity();
            }
        }
    }

    /**
     * 跳转到主页面
     */
    private void goToMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainFrameWorkActivity.class);
        startActivity(intent);
        // adContainer.removeAllViews();
        finish();
    }

    @Override
    public void checkNewVersionSuccess(VersionCheckResponse response) {
        if (null != response) {
            AdBean.getInstance().setVerify(response.verify);
        }
        int verify = AdBean.getInstance().getVerify();
        if (verify == 0) {
            loadSplashAd();
        } else {
            goToMainActivity();
        }
    }
    /**
     * 加载开屏广告
     */
    private void loadSplashAd() {
        //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(ADConfig.CSJAdPositionId.SPLASH_ID)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .build();
        //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            @MainThread
            public void onError(int code, String message) {
                Log.d(TAG, message);
                mHasLoaded = true;
                showToast(message);
                goToMainActivity();
            }

            @Override
            @MainThread
            public void onTimeout() {
                mHasLoaded = true;
                // showToast("开屏广告加载超时");
                Log.d(TAG, "开屏广告加载超时");
                goToMainActivity();
            }

            @Override
            @MainThread
            public void onSplashAdLoad(TTSplashAd ad) {
                Log.d(TAG, "开屏广告请求成功");
                mHasLoaded = true;
                mHandler.removeCallbacksAndMessages(null);
                if (ad == null) {
                    return;
                }
                //获取SplashView
                View view = ad.getSplashView();
                adContainer.removeAllViews();
                //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕宽
                adContainer.addView(view);
                //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                //ad.setNotAllowSdkCountdown();

                //设置SplashView的交互监听器
                ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {
                        Log.d(TAG, "onAdClicked 开屏广告点击");
                        // showToast("开屏广告点击");
                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        Log.d(TAG, "onAdShow 开屏广告展示");
                        // showToast("开屏广告展示");
                    }

                    @Override
                    public void onAdSkip() {
                        Log.d(TAG, "onAdSkip 开屏广告跳过");
                        // showToast("开屏广告跳过");
                        goToMainActivity();

                    }

                    @Override
                    public void onAdTimeOver() {
                        Log.d(TAG, "onAdTimeOver 开屏广告倒计时结束");
                        // showToast("开屏广告倒计时结束");
                        goToMainActivity();
                    }
                });
            }
        }, AD_TIME_OUT);
    }

    @Override
    public void showError(Throwable throwable) {
        Log.e("hhh", "showError throwable= " + throwable);
        loadSplashAd();
    }

    @Override
    public void checkNewVersionFailure(String msg) {
        Log.e("hhh", "checkNewVersionFailure msg= " + msg);
        loadSplashAd();
    }
}
