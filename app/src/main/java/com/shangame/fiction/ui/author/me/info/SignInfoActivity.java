package com.shangame.fiction.ui.author.me.info;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.response.AuthorInfoResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.setting.personal.area.ProvinceActivity;

import java.util.HashMap;
import java.util.Map;

public class SignInfoActivity extends BaseActivity implements SignInfoContacts.View, View.OnClickListener {

    public final static int MODIFY_AREA_REQUEST_CODE = 101;
    private SignInfoPresenter mPresenter;
    private ImageView mImgBack;
    private TextView mTextOption;
    private EditText mEditName;
    private RadioGroup mRadioGroup;
    private RadioButton mBtnBoy;
    private RadioButton mBtnGirl;
    private EditText mEditIcCard;
    private EditText mEditPhone;
    private TextView mTextAddr;
    private EditText mEditAddress;
    private AuthorInfoResponse mAuthorInfo;
    private String mProvince = "";
    private String mCityName = "";
    private int mSex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_info);

        initView();
        initPresenter();
        initListener();
    }

    private void initView() {
        mImgBack = findViewById(R.id.img_back);
        TextView tvTitle = findViewById(R.id.tv_title);
        mTextOption = findViewById(R.id.text_option);
        mEditName = findViewById(R.id.edit_name);
        mRadioGroup = findViewById(R.id.radioGroup);
        mBtnBoy = findViewById(R.id.btn_boy);
        mBtnGirl = findViewById(R.id.btn_girl);
        mEditIcCard = findViewById(R.id.edit_ic_card);
        mEditPhone = findViewById(R.id.edit_phone);
        mTextAddr = findViewById(R.id.text_addr);
        mEditAddress = findViewById(R.id.edit_address);

        tvTitle.setText("签约信息");
        mTextOption.setVisibility(View.VISIBLE);
        mTextOption.setText("保存");

        mAuthorInfo = getIntent().getParcelableExtra("authorInfo");

        if (null != mAuthorInfo) {
            String realName = mAuthorInfo.realname;
            if (!TextUtils.isEmpty(realName)) {
                mEditName.setText(realName);
            }
            mSex = mAuthorInfo.sex;
            if (mSex == 0) {
                mRadioGroup.check(mBtnBoy.getId());
            } else {
                mRadioGroup.check(mBtnGirl.getId());
            }

            String idCard = mAuthorInfo.card;
            if (!TextUtils.isEmpty(idCard)) {
                mEditIcCard.setText(idCard);
            }

            String phone = mAuthorInfo.mobilephone;
            if (!TextUtils.isEmpty(phone)) {
                mEditPhone.setText(phone);
            }

            String province = mAuthorInfo.province;
            String city = mAuthorInfo.city;
            String addr = "";

            if (!TextUtils.isEmpty(province)) {
                mProvince = province;
                addr = province;
            }
            if (!TextUtils.isEmpty(city)) {
                mCityName = city;
                addr = addr + " " + city;
            }
            if (!TextUtils.isEmpty(addr)) {
                mTextAddr.setText(addr);
            }

            String address = mAuthorInfo.address;
            if (!TextUtils.isEmpty(address)) {
                mEditAddress.setText(address);
            }
        }
    }

    private void initPresenter() {
        mPresenter = new SignInfoPresenter();
        mPresenter.attachView(this);
    }

    private void initListener() {
        mImgBack.setOnClickListener(this);
        mTextAddr.setOnClickListener(this);
        mTextOption.setOnClickListener(this);

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
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void updateSignAuthorSuccess() {
        Toast.makeText(this, "修改成功!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void updateSignAuthorFailure(String msg) {
        Toast.makeText(this, "修改失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back: {
                finish();
            }
            break;
            case R.id.text_addr: {
                Intent intent = new Intent(mActivity, ProvinceActivity.class);
                intent.putExtra("type", 1);
                if (!TextUtils.isEmpty(mCityName)) {
                    intent.putExtra("cityName", mCityName);
                }
                startActivityForResult(intent, MODIFY_AREA_REQUEST_CODE);
            }
            break;
            case R.id.text_option:
                saveInfo();
                break;
            default:
                break;
        }
    }

    private void saveInfo() {
        Map<String, Object> map = new HashMap<>();
        String name = mEditName.getText().toString();
        String idCard = mEditIcCard.getText().toString();
        String phone = mEditPhone.getText().toString();
        String address = mEditAddress.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(mContext, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (idCard.length() < 15) {
            Toast.makeText(mContext, "请输入正确的身份证号码", Toast.LENGTH_SHORT).show();
            return;
        }
        map.put("userid", UserInfoManager.getInstance(mContext).getUserid());
        map.put("realname", name);
        map.put("sex", mSex);
        map.put("card", idCard);
        map.put("mobilephone", phone);
        map.put("city", mCityName);
        map.put("province", mProvince);
        map.put("address", address);
        mPresenter.updateSignAuthor(map);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == MODIFY_AREA_REQUEST_CODE) {
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

                    mTextAddr.setText(address);
                }
            }
        }
    }
}
