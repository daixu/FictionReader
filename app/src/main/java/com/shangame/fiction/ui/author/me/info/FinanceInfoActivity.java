package com.shangame.fiction.ui.author.me.info;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.response.FinanceDataResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.author.works.enter.EditBandCardActivity;
import com.shangame.fiction.ui.author.works.enter.EditInfoActivity;

import java.util.HashMap;
import java.util.Map;

public class FinanceInfoActivity extends BaseActivity implements FinanceInfoContacts.View, View.OnClickListener {
    private static final int BANK_CARD_NUMBER_CODE = 101;
    private static final int BRANCH_NAME_CODE = 102;
    private static final int BANK_ACCOUNT_NAME_CODE = 103;
    private FinanceInfoPresenter mPresenter;
    private ImageView mImgBack;
    private TextView mTextOption;
    private TextView mTextBankCardNumber;
    private TextView mTextBankName;
    private TextView mTextBranchName;
    private TextView mTextBankAccountName;
    private String mBankCardNumber;
    private String mBankName;
    private String mBranchName;
    private String mBankAccountName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_info);

        initView();
        initPresenter();
        initData();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private void initView() {
        mImgBack = findViewById(R.id.img_back);
        TextView tvTitle = findViewById(R.id.tv_title);
        mTextOption = findViewById(R.id.text_option);
        mTextBankCardNumber = findViewById(R.id.text_bank_card_number);
        mTextBankName = findViewById(R.id.text_bank_name);
        mTextBranchName = findViewById(R.id.text_branch_name);
        mTextBankAccountName = findViewById(R.id.text_bank_account_name);

        tvTitle.setText("财务信息");
        mTextOption.setVisibility(View.VISIBLE);
        mTextOption.setText("保存");
    }

    private void initPresenter() {
        mPresenter = new FinanceInfoPresenter();
        mPresenter.attachView(this);
    }

    private void initData() {
        mPresenter.getFinanceData(UserInfoManager.getInstance(mContext).getUserid());
    }

    private void initListener() {
        mImgBack.setOnClickListener(this);
        mTextOption.setOnClickListener(this);
        mTextBankCardNumber.setOnClickListener(this);
        mTextBankName.setOnClickListener(this);
        mTextBranchName.setOnClickListener(this);
        mTextBankAccountName.setOnClickListener(this);
    }

    @Override
    public void getFinanceDataSuccess(FinanceDataResponse response) {
        if (!TextUtils.isEmpty(response.bankcard)) {
            mTextBankCardNumber.setText(response.bankcard);
            mBankCardNumber = response.bankcard;
        }
        if (!TextUtils.isEmpty(response.bankname)) {
            mTextBankName.setText(response.bankname);
            mBankName = response.bankname;
        }
        if (!TextUtils.isEmpty(response.zhbankname)) {
            mTextBranchName.setText(response.zhbankname);
            mBranchName = response.zhbankname;
        }
        if (!TextUtils.isEmpty(response.username)) {
            mTextBankAccountName.setText(response.username);
            mBankAccountName = response.username;
        }
    }

    @Override
    public void getFinanceDataFailure(String msg) {
        Log.e("hhh", "getFinanceDataFailure msg= " + msg);
    }

    @Override
    public void setFinanceDataSuccess() {
        Toast.makeText(this, "修改成功!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void setFinanceDataFailure(String msg) {
        Toast.makeText(this, "修改失败", Toast.LENGTH_SHORT).show();
        Log.e("hhh", "setFinanceDataFailure msg= " + msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back: {
                finish();
            }
            break;
            case R.id.text_option: {
                saveInfo();
            }
            break;
            case R.id.text_bank_card_number: {
                Intent intent = new Intent(mContext, EditBandCardActivity.class);
                intent.putExtra("title", "银行卡号");
                String bankCardNumber = mTextBankCardNumber.getText().toString().trim();
                if (!TextUtils.isEmpty(bankCardNumber)) {
                    intent.putExtra("content", bankCardNumber);
                }
                intent.putExtra("hint", "请仔细填写银行卡号，卡号之间不要输入空格，不要输错哦！");
                startActivityForResult(intent, BANK_CARD_NUMBER_CODE);
            }
            break;
            case R.id.text_bank_name: {
                selectBranch();
            }
            break;
            case R.id.text_branch_name: {
                Intent intent = new Intent(mContext, EditInfoActivity.class);
                intent.putExtra("title", "支行名称");
                String branchName = mTextBranchName.getText().toString().trim();
                if (!TextUtils.isEmpty(branchName)) {
                    intent.putExtra("content", branchName);
                }
                intent.putExtra("hint", "例：工商银行湖南长沙砚瓦池分理...");
                startActivityForResult(intent, BRANCH_NAME_CODE);
            }
            break;
            case R.id.text_bank_account_name: {
                Intent intent = new Intent(mContext, EditInfoActivity.class);
                intent.putExtra("title", "银行户名");
                String bankAccountName = mTextBankAccountName.getText().toString().trim();
                if (!TextUtils.isEmpty(bankAccountName)) {
                    intent.putExtra("content", bankAccountName);
                }
                intent.putExtra("hint", "填输入银卡开户名.");
                startActivityForResult(intent, BANK_ACCOUNT_NAME_CODE);
            }
            break;
            default:
                break;
        }
    }

    private void saveInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", UserInfoManager.getInstance(mContext).getUserid());
        map.put("bankcard", mBankCardNumber);
        map.put("bankname", mBankName);
        map.put("username", mBankAccountName);
        map.put("zhbankname", mBranchName);
        map.put("alipay", "");
        map.put("arealname", "");
        mPresenter.setFinanceData(map);
    }

    private void selectBranch() {
        final String[] items = {"中国银行", "工商银行", "农业银行", "建设银行", "邮政储蓄银行", "招商银行",
                "民生银行", "平安银行", "上海浦东发展银行", "交通银行", "中信银行", "光大银行", "浦发银行"};
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("银行名称")
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTextBankName.setText(items[which]);
                        mBankName = items[which];
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case BANK_CARD_NUMBER_CODE: {
                    if (null != data) {
                        mBankCardNumber = data.getStringExtra("content");
                        if (!TextUtils.isEmpty(mBankCardNumber)) {
                            mTextBankCardNumber.setText(mBankCardNumber);
                        }
                    }
                }
                break;
                case BRANCH_NAME_CODE: {
                    if (null != data) {
                        mBranchName = data.getStringExtra("content");
                        if (!TextUtils.isEmpty(mBranchName)) {
                            mTextBranchName.setText(mBranchName);
                        }
                    }
                }
                break;
                case BANK_ACCOUNT_NAME_CODE: {
                    if (null != data) {
                        mBankAccountName = data.getStringExtra("content");
                        if (!TextUtils.isEmpty(mBankAccountName)) {
                            mTextBankAccountName.setText(mBankAccountName);
                        }
                    }
                }
                break;
                default:
                    break;
            }
        }
    }
}
