package com.shangame.fiction.ui.my.pay;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.manager.AppSetting;

/**
 * Create by Speedy on 2018/9/13
 */
public class PayWebView extends WebView {

    private Context mContext;

    public PayWebView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getSettings().setSafeBrowsingEnabled(false);
        }

        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);

        setWebChromeClient(new WebChromeClient() {

        });

        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlString) {
                Log.e("hhh", "urlString= " + urlString);
                if (urlString.contains("weixin://") || urlString.contains("intent://") || urlString.contains("alipay://")) {
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(urlString));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    return true;
                } else if (urlString.contains("amwx://")) {
                    Log.e("hhh", "amwx urlString= " + urlString);
                    AppSetting.getInstance(mContext).putString(SharedKey.PAY_RESULT_URL, urlString);
                }
                return super.shouldOverrideUrlLoading(view, urlString);
            }
        });
    }
}
