package com.shangame.fiction.ui.sales.partner.upgrade;

import android.app.Dialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.response.BaseResp;
import com.shangame.fiction.net.response.CreatWapOrderResponse;
import com.shangame.fiction.net.response.GetPayMenthodsResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.trello.rxlifecycle2.LifecycleTransformer;

@Route(path = "/ss/sales/partner/upgrade/silver")
public class UpgradeSilverActivity extends BaseActivity implements View.OnClickListener, UpgradeLevelContacts.View {

    private UpgradeLevelPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_silver);

        initView();
        initPresenter();
    }

    private void initView() {
        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.btn_upgrade).setOnClickListener(this);
    }

    private void initPresenter() {
        mPresenter = new UpgradeLevelPresenter(this);
        mPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_upgrade:
                upgrade();
                break;
            default:
                break;
        }
    }

    private void upgrade() {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        mPresenter.setUpGrade(userId, 0, 3);
    }

    @Override
    public void setUpGradeSuccess(BaseResp resp) {
        showSuccessDialog();
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_upgrade_success, null);

        ImageView imgClose = view.findViewById(R.id.img_close);
        ImageView imgExperience = view.findViewById(R.id.img_experience);
        ImageView imageType = view.findViewById(R.id.image_type);

        imageType.setImageResource(R.drawable.image_silver_prompt);
        final Dialog dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        if (null != window) {
            window.setBackgroundDrawable(new BitmapDrawable());
            window.setContentView(view);
        }

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        imgExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance()
                        .build("/ss/sales/partner/manage/home")
                        .navigation();
                finish();
            }
        });
    }

    @Override
    public void setUpGradeFailure(String msg) {
        Log.e("hhh", "setUpGradeFailure msg= " + msg);
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getPayMethodsSuccess(GetPayMenthodsResponse response) {

    }

    @Override
    public void wapCreateOrderSuccess(CreatWapOrderResponse response, int payMethod) {

    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }
}
