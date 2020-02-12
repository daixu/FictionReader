package com.shangame.fiction.ui.sales.partner;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lxj.xpopup.core.CenterPopupView;
import com.shangame.fiction.R;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.login.LoginActivity;

import static com.shangame.fiction.core.base.BaseActivity.LUNCH_LOGIN_ACTIVITY_REQUEST_CODE;

/**
 * 绑定邀请码弹框
 *
 * @author hhh
 */
public class InvitationCodePopupWindow extends CenterPopupView implements InvitationCodeContacts.View {
    private int mInvitationCode;
    private Context mContext;
    private Activity mActivity;
    private InvitationCodePresenter mPresenter;

    public InvitationCodePopupWindow(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public InvitationCodePopupWindow(@NonNull Context context, Activity activity, int invitationCode) {
        super(context);
        this.mContext = context;
        this.mActivity = activity;
        this.mInvitationCode = invitationCode;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_window_invitation_code;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
        initPresenter();
        initListener();
    }

    private void initView() {
        TextView textInvitationCode = findViewById(R.id.text_invitation_code);
        String strInvitationCode = mInvitationCode + "";
        textInvitationCode.setText(strInvitationCode);
        clearClipboard();
    }

    private void initPresenter() {
        mPresenter = new InvitationCodePresenter();
        mPresenter.attachView(this);
    }

    private void initListener() {
        findViewById(R.id.image_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        findViewById(R.id.btn_bind).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                if (userId == 0) {
                    lunchLoginActivity();
                    return;
                }
                mPresenter.bindAgentId(userId, mInvitationCode);
            }
        });
    }

    private void clearClipboard() {
        ClipboardManager manager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null) {
            if (null != manager.getPrimaryClip()) {
                manager.setPrimaryClip(ClipData.newPlainText(null, ""));
            }
        }
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        mPresenter.detachView();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

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
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.putExtra("invitationCode", mInvitationCode);
        mActivity.startActivityForResult(intent, LUNCH_LOGIN_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void bindAgentIdSuccess() {
        Toast.makeText(mContext, "绑定邀请码成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void bindAgentIdFailure(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "绑定邀请码失败", Toast.LENGTH_SHORT).show();
        }
    }
}
