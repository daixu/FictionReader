package com.shangame.fiction.ui.sales.withdraw;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lxj.xpopup.core.BottomPopupView;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.AgentDetailResp;
import com.shangame.fiction.net.response.BaseResp;
import com.shangame.fiction.net.response.CardListResp;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.login.LoginActivity;
import com.shangame.fiction.widget.PublicLoadDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class FinanceDataWindow extends BottomPopupView implements View.OnClickListener, FinanceDataContacts.View {
    private static final int RESEND_DURATION = 60;
    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private EditText mEditName;
    private EditText mEditPhone;
    private EditText mEditCode;
    private TextView mTextGetCode;
    private EditText mEditBankCardNumber;
    private TextView mTextBankName;
    private EditText mEditBranchName;
    private EditText mEditIdCard;
    private FinanceDataPresenter mPresenter;
    private PublicLoadDialog mProgressDialog;

    private AgentDetailResp.DataBean mDataBean;

    private String mBankName;

    private Context mContext;

    public FinanceDataWindow(@NonNull Context context, AgentDetailResp.DataBean dataBean) {
        super(context);
        this.mContext = context;
        this.mDataBean = dataBean;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_window_finance_data;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
        initListener();
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        mPresenter.detachView();
    }

    private void initView() {
        mEditName = findViewById(R.id.edit_name);
        mEditPhone = findViewById(R.id.edit_phone);
        mEditCode = findViewById(R.id.edit_code);
        mTextGetCode = findViewById(R.id.text_get_code);
        mEditBankCardNumber = findViewById(R.id.edit_bank_card_number);
        mTextBankName = findViewById(R.id.text_bank_name);
        mEditBranchName = findViewById(R.id.edit_branch_name);
        mEditIdCard = findViewById(R.id.edit_id_card);
        findViewById(R.id.btn_success).setOnClickListener(this);
        findViewById(R.id.img_select).setOnClickListener(this);
        mTextBankName.setOnClickListener(this);
        mTextGetCode.setOnClickListener(this);

        if (null != mDataBean) {
            mEditName.setText(mDataBean.userName);
            mEditPhone.setText(mDataBean.iPhone);
            mEditBankCardNumber.setText(mDataBean.card);
            mTextBankName.setText(mDataBean.cardName);
            mEditBranchName.setText(mDataBean.zhCardName);
            mEditIdCard.setText(mDataBean.identitycard);

            mBankName = mDataBean.cardName;
        }
    }

    private void initListener() {
        mPresenter = new FinanceDataPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_get_code:
                getCode();
                break;
            case R.id.text_bank_name:
            case R.id.img_select:
                getCardList();
                break;
            case R.id.btn_success:
                success();
                break;
            default:
                break;
        }
    }

    private void getCode() {
        String phone = mEditPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(mContext, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        mPresenter.getCode(phone);
    }

    private void getCardList() {
        mPresenter.getCardList();
    }

    private void success() {
        String name = mEditName.getText().toString().trim();
        String phone = mEditPhone.getText().toString().trim();
        String code = mEditCode.getText().toString().trim();
        String bankCardNumber = mEditBankCardNumber.getText().toString();
        String bankName = mBankName;
        String branchName = mEditBranchName.getText().toString().trim();
        String idCard = mEditIdCard.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(mContext, "请输入姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(mContext, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(mContext, "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(bankCardNumber)) {
            Toast.makeText(mContext, "请输入银行卡号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(bankName)) {
            Toast.makeText(mContext, "请选择银行名称", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(branchName)) {
            Toast.makeText(mContext, "请输入支行名称", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(idCard)) {
            Toast.makeText(mContext, "请输入身份证号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (idCard.length() != 15 && idCard.length() != 18) {
            Toast.makeText(mContext, "请输入正确的身份证号码", Toast.LENGTH_SHORT).show();
            return;
        }

        UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
        int agentId = 0;
        if (null != userInfo) {
            agentId = userInfo.agentId;
        }
        Map<String, Object> map = new HashMap<>(8);
        map.put("agentId", agentId);
        map.put("userName", name);
        map.put("iPhone", phone);
        map.put("card", bankCardNumber);
        map.put("cardName", bankName);
        map.put("zhCardName", branchName);
        map.put("identitycard", idCard);
        map.put("smscode", code);
        mPresenter.setAgentIdDetail(map);
    }

    @Override
    public void getCardListSuccess(List<CardListResp.DataBean.CardListBean> dataBean) {
        Log.e("hhh", "getCardListSuccess dataBean= " + dataBean);
        final String[] items = new String[dataBean.size()];
        for (int i = 0; i < dataBean.size(); i++) {
            items[i] = dataBean.get(i).cardName;
        }
        AlertDialog dialog = new AlertDialog.Builder(mContext)
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
    public void getCardListFailure(String msg) {
        Log.e("hhh", "getCardListFailure msg= " + msg);
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getCodeSuccess() {
        Toast.makeText(mContext, R.string.security_code_send_success, Toast.LENGTH_SHORT).show();

        mTextGetCode.setEnabled(false);
        mTextGetCode.setTextColor(ContextCompat.getColor(mContext, R.color.secondary_text));

        Disposable disposable = Observable.intervalRange(0, RESEND_DURATION, 1, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mTextGetCode.setText(mContext.getString(R.string.resend_code, RESEND_DURATION - aLong.intValue()));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }, new Action() {

                    @Override
                    public void run() throws Exception {
                        mTextGetCode.setText(R.string.reobtain_security_code);
                        mTextGetCode.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                        mTextGetCode.setEnabled(true);
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getCodeFailure(String msg) {
        Log.e("hhh", "getCodeFailure msg= " + msg);
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setAgentIdDetailSuccess(BaseResp resp) {
        Log.e("hhh", "setAgentIdDetailSuccess resp= " + resp);
        Toast.makeText(mContext, "添加信息成功", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    @Override
    public void setAgentIdDetailFailure(String msg) {
        Log.e("hhh", "setAgentIdDetailFailure msg= " + msg);
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showLoading() {
        if (mProgressDialog == null) {
            mProgressDialog = new PublicLoadDialog(mContext);
        }

        mProgressDialog.show();
    }

    @Override
    public void dismissLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            try {
                mProgressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showNotNetworkView() {

    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showError(Throwable throwable) {

    }

    @Override
    public void lunchLoginActivity() {
        dismissLoading();
        Intent intent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);
    }
}
