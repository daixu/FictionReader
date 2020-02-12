package com.shangame.fiction.ui.sales.partner.upgrade;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;

@Route(path = "/ss/sales/partner/upgrade/diamond")
public class UpgradeDiamondActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_diamond);

        findViewById(R.id.btn_contact_service).setOnClickListener(this);
        findViewById(R.id.img_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_contact_service:
                ARouter.getInstance()
                        .build("/ss/customer/service")
                        .navigation();
                break;
            default:
                break;
        }
    }
}
