package com.shangame.fiction.ui.my.about;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shangame.fiction.BuildConfig;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.config.AppConfig;
import com.shangame.fiction.core.utils.AppUtils;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.VersionCheckResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.web.WebViewActivity;

public class AboutActivity extends BaseActivity implements View.OnClickListener, VersionCheckContracts.View {

    private TextView tvVersion;
    private TextView tvUpdateVersion;

    private VersionCheckPresenter versionCheckPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        versionCheckPresenter = new VersionCheckPresenter();
        versionCheckPresenter.attachView(this);

        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.about_me);

        tvVersion = findViewById(R.id.tvVersion);
        tvUpdateVersion = findViewById(R.id.tvUpdateVersion);

        String version = AppUtils.getAppVersionName() + "." + BuildConfig.CHANNEL_ID;
        tvVersion.setText(getString(R.string.version_code, version));

        tvUpdateVersion.setOnClickListener(this);
        findViewById(R.id.tvCopyright).setOnClickListener(this);
        findViewById(R.id.tvPrivate).setOnClickListener(this);

        findViewById(R.id.ivLogo).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AppConfig.forceDebug = true;
                showToast("已开启调试模块");
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivPublicBack) {
            finish();
        } else if (v.getId() == R.id.tvCopyright) {
            String url = "https://m.anmaa.com/Mine/Agreement?channel=" + ApiConstant.Channel.ANDROID;
            WebViewActivity.lunchActivity(mActivity, "服务协议", url);
        } else if (v.getId() == R.id.tvPrivate) {
            String url2 = "https://m.anmaa.com/Mine/Privacy?channel=" + ApiConstant.Channel.ANDROID;
            WebViewActivity.lunchActivity(mActivity, "隐私协议", url2);
        } else if (v.getId() == R.id.tvUpdateVersion) {
            long userId = UserInfoManager.getInstance(mContext).getUserid();
            int version = AppUtils.getAppVersionCode();
            versionCheckPresenter.checkNewVersion(userId, version);
        }
    }

    @Override
    public void checkNewVersionSuccess(VersionCheckResponse versionCheckResponse) {
        if (versionCheckResponse.updatetype == 0) {
            showToast(getString(R.string.newest_version));
        } else {
            VersionUpdateDialog versionUpdateDialog = VersionUpdateDialog.newInstance(mActivity, versionCheckResponse);
            final String fileName = versionCheckResponse.remark + ".apk";
            versionUpdateDialog.setUpdateVersionListener(new VersionUpdateDialog.UpdateVersionListener() {
                @Override
                public void updateVersion(String url) {
                    DownloadAPKDialog downloadAPKDialog = DownloadAPKDialog.newIntance(url, fileName);
                    downloadAPKDialog.show(getFragmentManager(), "DownloadAPKDialog");
                }
            });
            versionUpdateDialog.show(getFragmentManager(), "VersionUpdateDialog");
        }
    }

    @Override
    public void checkNewVersionFailure(String msg) {

    }
}
