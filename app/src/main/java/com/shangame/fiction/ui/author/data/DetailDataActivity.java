package com.shangame.fiction.ui.author.data;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.manager.AppSetting;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.storage.manager.DbManager;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.author.AuthorActivity;
import com.shangame.fiction.ui.setting.personal.area.ProvinceActivity;

import java.util.HashMap;
import java.util.Map;

public class DetailDataActivity extends BaseActivity implements View.OnClickListener, DetailDataContacts.View {
    private String mPenName;
    private String mEmail;
    private String mQq;

    private EditText mEditName;
    private RadioGroup mRadioGroup;
    private EditText mEditIdCard;
    private EditText mEditPhone;
    private TextView mTextAddress;
    private EditText mEditDetailAddress;
    private Button mBtnSubmit;

    private boolean hasName = false;
    private boolean hasSex = false;
    private boolean hasIdCard = false;
    private boolean hasPhone = false;
    private boolean hasAddress = false;
    private boolean hasDetailAddress = false;

    private int mSex;
    private String mProvince;
    private String mCityName;

    private int minLength = 2;
    private int minIdCardLength = 15;

    private DetailDataPresenter mPresenter;

    public final static int MODIFY_AREA_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_data);

        initView();
        initListener();
        initData();

        mPresenter = new DetailDataPresenter();
        mPresenter.attachView(this);
    }

    private void initView() {
        mEditName = findViewById(R.id.edit_name);
        mRadioGroup = findViewById(R.id.radioGroup);
        mEditIdCard = findViewById(R.id.edit_id_card);
        mEditPhone = findViewById(R.id.edit_phone);
        mTextAddress = findViewById(R.id.text_address);
        mEditDetailAddress = findViewById(R.id.edit_detail_address);
        mBtnSubmit = findViewById(R.id.btn_submit);
    }

    private void initListener() {
        initNameListener();
        initIdCardListener();
        initPhoneListener();
        initDetailAddressListener();
        mTextAddress.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
        findViewById(R.id.img_back).setOnClickListener(this);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.e("hhh", "checkedId= " + checkedId);
                switch (checkedId) {
                    case R.id.btn_boy:
                        mSex = 0;
                        break;
                    case R.id.btn_girl:
                        mSex = 1;
                        break;
                    default:
                        break;
                }
                hasSex = true;
            }
        });
    }

    private void initNameListener() {
        mEditName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()) && s.toString().length() >= minLength) {
                    hasName = true;
                }

                setEnabled();
            }
        });
    }

    private void initIdCardListener() {
        mEditIdCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()) && s.toString().length() >= minIdCardLength) {
                    hasIdCard = true;
                }
                setEnabled();
            }
        });
    }

    private void initPhoneListener() {
        mEditPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    hasPhone = true;
                }
                setEnabled();
            }
        });
    }

    private void initDetailAddressListener() {
        mEditDetailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    hasDetailAddress = true;
                }
                setEnabled();
            }
        });
    }

    private void setEnabled() {
        if (hasName && hasSex && hasIdCard && hasPhone && hasAddress && hasDetailAddress) {
            mBtnSubmit.setEnabled(true);
        } else {
            mBtnSubmit.setEnabled(false);
        }
    }

    private void initData() {
        mPenName = getIntent().getStringExtra("penName");
        mEmail = getIntent().getStringExtra("email");
        mQq = getIntent().getStringExtra("qq");

        if (TextUtils.isEmpty(mPenName) || TextUtils.isEmpty(mEmail) || TextUtils.isEmpty(mQq)) {
            Toast.makeText(this, "信息填写不完整", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_address:
                Intent intent = new Intent(mActivity, ProvinceActivity.class);
                intent.putExtra("type", 1);
                if (!TextUtils.isEmpty(mCityName)) {
                    intent.putExtra("cityName", mCityName);
                }
                startActivityForResult(intent, MODIFY_AREA_REQUEST_CODE);
                break;
            case R.id.btn_submit:
                Log.e("hhh", "mSex= " + mSex);
                setAuthorInfo();
                break;
            case R.id.img_back:
                finish();
                break;
            default:
                break;
        }
    }

    private void setAuthorInfo() {
        String phone = mEditPhone.getText().toString().trim();
        String card = mEditIdCard.getText().toString().trim();
        String realName = mEditName.getText().toString().trim();
        String address = mEditDetailAddress.getText().toString().trim();
        int userId = UserInfoManager.getInstance(this).getUserid();
        Map<String, Object> map = new HashMap<>();
        map.put("userid", userId);
        map.put("channel", ApiConstant.Channel.ANDROID);
        map.put("penname", mPenName);
        map.put("mobilephone", phone);
        map.put("card", card);
        map.put("realname", realName);
        map.put("address", address);
        map.put("email", mEmail);
        map.put("qq", mQq);
        map.put("sex", mSex);
        map.put("province", mProvince);
        map.put("city", mCityName);
        mPresenter.setAuthorInfo(map);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == MODIFY_AREA_REQUEST_CODE) {
                hasAddress = true;
                if (null != data) {
                    int fId = data.getIntExtra("fId", -1);
                    mProvince = data.getStringExtra("province");
                    mCityName = data.getStringExtra("cityName");

                    Log.e("hhh", "fId= " + fId + " ,province= " + mProvince + " ,cityName= " + mCityName);

                    String address = "";
                    if (!TextUtils.isEmpty(mProvince)) {
                        address = mProvince;
                    }
                    if (!TextUtils.isEmpty(mCityName)) {
                        address = address + " " + mCityName;
                    }

                    mTextAddress.setText(address);
                }
            }
        }
    }

    @Override
    public void setAuthorInfoSuccess() {
        Toast.makeText(this, "请求成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, AuthorActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void setAuthorInfoSuccess(UserInfo userInfo) {
        AppSetting.getInstance(mContext).putLong(SharedKey.CURRENT_USER_ID, userInfo.userid);
        DbManager.close();
        Toast.makeText(this, "请求成功", Toast.LENGTH_SHORT).show();
        UserInfoManager.getInstance(mContext).saveUserInfo(userInfo);

        Intent intent = new Intent(this, AuthorActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void setAuthorInfoFailure(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "请求失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
