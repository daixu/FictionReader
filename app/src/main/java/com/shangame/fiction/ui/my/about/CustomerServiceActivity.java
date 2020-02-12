package com.shangame.fiction.ui.my.about;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;

@Route(path = "/ss/customer/service")
public class CustomerServiceActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service);

        initView();
    }

    private void initView() {
        findViewById(R.id.img_back).setOnClickListener(this);
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("客服联系方式");

        findViewById(R.id.btn_copy).setOnClickListener(this);
        findViewById(R.id.btn_call_phone).setOnClickListener(this);
        findViewById(R.id.btn_contact).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_copy:
                copy("anma52099");
                break;
            case R.id.btn_call_phone:
                call("0731-85865669");
                break;
            case R.id.btn_contact:
                joinQq();
                break;
            default:
                break;
        }
    }

    private void copy(String copyStr) {
        //获取剪贴板管理器
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", copyStr);
        // 将ClipData内容放到系统剪贴板里。
        if (null != cm) {
            cm.setPrimaryClip(mClipData);
        }
        Toast.makeText(this, "复制成功", Toast.LENGTH_SHORT).show();
        // joinWeChat();
    }

    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void joinQq() {
        try {
            String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=508408961&version=1";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "检查到您手机没有安装QQ，请安装后使用该功能", Toast.LENGTH_SHORT).show();
        }
    }

    private void joinWeChat() {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "检查到您手机没有安装微信，请安装后使用该功能", Toast.LENGTH_SHORT).show();
        }
    }
}
