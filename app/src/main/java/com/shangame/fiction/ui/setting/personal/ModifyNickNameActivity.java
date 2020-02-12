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

public class ModifyNickNameActivity extends BaseActivity implements View.OnClickListener, PersonalContacts.View {

    private EditText etNickName;
    private TextView ivDelete;

    private PersonalPresenter personalPresenter;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_nick_name);

        personalPresenter = new PersonalPresenter();
        personalPresenter.attachView(this);

        TextView tvPublicTitle = (TextView) findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.setting);

        etNickName = (EditText) findViewById(R.id.etNickName);

        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        findViewById(R.id.ivDelete).setOnClickListener(this);
        findViewById(R.id.btnSave).setOnClickListener(this);

        userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
        etNickName.setText(userInfo.nickname);
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
        } else if (view.getId() == R.id.ivDelete) {
            etNickName.setText(null);
        } else if (view.getId() == R.id.btnSave) {
            String nickName = etNickName.getText().toString();
            if (TextUtils.isEmpty(nickName)) {
                showToast(getString(R.string.nickname_empty));
                return;
            }
            userInfo.nickname = nickName;
            commitModifyProfile();
        }
    }

    private void commitModifyProfile() {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", userInfo.userid);
        map.put("nickname", userInfo.nickname);
        personalPresenter.modifyProfile(map);
    }

    @Override
    public void modifyProfileSuccess(UserInfo userInfo) {
        UserInfoManager.getInstance(mContext).saveUserInfo(userInfo);
        setResult(RESULT_OK);
        finish();
    }
}
