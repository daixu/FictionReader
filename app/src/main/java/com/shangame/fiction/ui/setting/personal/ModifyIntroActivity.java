package com.shangame.fiction.ui.setting.personal;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;

import java.util.HashMap;
import java.util.Map;

public class ModifyIntroActivity extends BaseActivity implements View.OnClickListener, PersonalContacts.View {

    private EditText etContent;
    private PersonalPresenter personalPresenter;

    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_intro);

        personalPresenter = new PersonalPresenter();
        personalPresenter.attachView(this);

        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.personal_intro);

        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        findViewById(R.id.btnSave).setOnClickListener(this);

        etContent = findViewById(R.id.etContent);

        userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
        etContent.setText(userInfo.synopsis);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        personalPresenter.detachView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivPublicBack) {
            finish();
        } else if (view.getId() == R.id.btnSave) {
            String intro = etContent.getText().toString();
            userInfo.synopsis = intro;
            if (TextUtils.isEmpty(intro)) {
                showToast(getString(R.string.intro_empty));
                return;
            }
            commitModifyProfile();
        }
    }

    private void commitModifyProfile() {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", userInfo.userid);
        map.put("synopsis", userInfo.synopsis);
        personalPresenter.modifyProfile(map);
    }

    @Override
    public void modifyProfileSuccess(UserInfo userInfo) {
        UserInfoManager.getInstance(mContext).saveUserInfo(userInfo);
        setResult(RESULT_OK);
        finish();
    }
}
