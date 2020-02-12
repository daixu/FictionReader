package com.shangame.fiction.ui.my;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.BaseResp;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.author.AuthorActivity;
import com.shangame.fiction.ui.author.data.InputDataActivity;
import com.shangame.fiction.ui.my.account.MyAccountActivity;
import com.shangame.fiction.ui.my.message.MessageCenterActivity;
import com.shangame.fiction.ui.my.pay.PayCenterActivity;
import com.shangame.fiction.ui.my.scan.ScannerActivity;
import com.shangame.fiction.ui.setting.SettingActivity;
import com.shangame.fiction.ui.signin.SigninPopupWindow;
import com.shangame.fiction.ui.task.TaskCenterActivity;
import com.shangame.fiction.ui.web.WebViewActivity;
import com.shangame.fiction.widget.GlideApp;
import com.trello.rxlifecycle2.LifecycleTransformer;

import static android.app.Activity.RESULT_OK;

/**
 * Create by Speedy on 2018/7/20
 */
public class MyFragment extends BaseFragment implements View.OnClickListener, UserInfoContracts.View {

    private static final String TAG = "MyFragment";
    private static final int SETTING_REQUEST_CODE = 262;
    private static final int TOP_UP_REQUEST_CODE = 503;

    private RoundedImageView mImageUserAvatar;
    private TextView mTextUserName;
    private TextView mTextTodayReadTime;
    private TextView mTextCoin;

    private TextView mTvInvitationCode;
    private View mViewInvitationCode;

    private UserInfoPresenter mPresenter;

    public static MyFragment newInstance() {
        return new MyFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new UserInfoPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        requestUserInfo();
    }

    private void requestUserInfo() {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        if (userId != 0) {
            mPresenter.getUserInfo(userId);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_message: {
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                if (userId == 0) {
                    super.lunchLoginActivity();
                    return;
                }
                startActivity(new Intent(mContext, MessageCenterActivity.class));
            }
            break;
            case R.id.tv_account: {
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                if (userId == 0) {
                    super.lunchLoginActivity();
                    return;
                }
                Intent intent = new Intent(mContext, MyAccountActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.img_recharge: {
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                if (userId == 0) {
                    super.lunchLoginActivity();
                    return;
                }
                Intent rechargeIntent = new Intent(mContext, PayCenterActivity.class);
                startActivity(rechargeIntent);
            }
            break;
            case R.id.tv_sign: {
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                if (userId == 0) {
                    super.lunchLoginActivity();
                    return;
                }
                SigninPopupWindow signinPopupWindow = new SigninPopupWindow(mActivity);
                signinPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            }
            break;
            case R.id.tv_welfare: {
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                if (userId == 0) {
                    super.lunchLoginActivity();
                    return;
                }
                Intent welfareIntent = new Intent(mContext, TaskCenterActivity.class);
                startActivity(welfareIntent);
            }
            break;
            case R.id.tv_service:
                ARouter.getInstance()
                        .build("/ss/customer/service")
                        .navigation();
                break;
            case R.id.userInfoLayout: {
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                if (userId == 0) {
                    super.lunchLoginActivity();
                    return;
                }
                ARouter.getInstance()
                        .build("/ss/edit/data")
                        .navigation();
            }
            break;
            case R.id.img_setting:
                // startActivityForResult(new Intent(mContext, SettingActivity.class), SETTING_REQUEST_CODE);
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
            case R.id.tv_help:
                String url = "https://m.anmaa.com/Mine/AppHelp?channel=" + ApiConstant.Channel.ANDROID;
                WebViewActivity.lunchActivity(mActivity, "帮助中心", url);
                break;
            case R.id.tv_author_platform: {
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                if (userId == 0) {
                    super.lunchLoginActivity();
                    return;
                }
                int authorId = UserInfoManager.getInstance(mContext).getUserInfo().authorid;
                if (authorId > 0) {
                    Intent intent = new Intent(mContext, AuthorActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, InputDataActivity.class);
                    intent.putExtra("authorId", authorId);
                    startActivity(intent);
                }
            }
            break;
            case R.id.image_enter: {
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                if (userId == 0) {
                    super.lunchLoginActivity();
                    return;
                }
                jumpSalesSystem();
            }
            break;
            case R.id.tv_scan: {
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                if (userId == 0) {
                    super.lunchLoginActivity();
                    return;
                }
                Log.e("hhh", "扫一扫");
                Intent scanIntent = new Intent(mContext, ScannerActivity.class);
                startActivityForResult(scanIntent, 201);
            }
            break;
            case R.id.tv_invitation_code: {
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                if (userId == 0) {
                    super.lunchLoginActivity();
                    return;
                }
                showCodeDialog();
            }
            break;
            default:
                break;
        }
    }

    private void jumpSalesSystem() {
        UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
        int agentGrade = userInfo.agentGrade;
        if (agentGrade > 0) {
            ARouter.getInstance()
                    .build("/ss/sales/partner/manage/home")
                    .navigation();
        } else {
            ARouter.getInstance()
                    .build("/ss/sales/partner/upgrade/silver")
                    .navigation();
        }
    }

    private void showCodeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.custom_edit_dialog, null);
        final EditText editDialogContent = view.findViewById(R.id.edit_dialog_content);
        ImageView imagePositive = view.findViewById(R.id.image_positive);
        ImageView imgClose = view.findViewById(R.id.img_close);
        TextView tvDialogTitle = view.findViewById(R.id.tv_dialog_title);

        UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
        final int agentId = userInfo.agentId;
        if (agentId > 0) {
            String dialogContent = agentId + "";
            editDialogContent.setText(dialogContent);
            editDialogContent.setFocusable(false);
            tvDialogTitle.setText("上级邀请码");
        } else {
            editDialogContent.setFocusable(true);
            tvDialogTitle.setText("看书找组织，收益共享");
        }

        final Dialog dialog = builder.create();
        dialog.show();
        final Window window = dialog.getWindow();
        if (null != window) {
            window.setBackgroundDrawable(new BitmapDrawable());
            window.setContentView(view);
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
        imagePositive.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String value = editDialogContent.getText().toString().trim();
                if (TextUtils.isEmpty(value)) {
                    Toast.makeText(mContext, "邀请码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
                if (agentId <= 0) {
                    long userId = UserInfoManager.getInstance(mContext).getUserid();
                    int agentId = Integer.valueOf(value);
                    mPresenter.bindAgentId(userId, agentId);
                }
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (null != window) {
                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                }

                InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (null != inputMethodManager) {
                    inputMethodManager.hideSoftInputFromWindow(editDialogContent.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                }
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BaseActivity.LUNCH_LOGIN_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                initUserInfo();
            }
        } else if (requestCode == SETTING_REQUEST_CODE) {
            mBaseActivity.initDayModel();
        } else if (requestCode == TOP_UP_REQUEST_CODE) {
            UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
            mTextCoin.setText(String.valueOf(userInfo.money));
        } else if (requestCode == 201) {
            if (resultCode == 201) {
                String content = data.getStringExtra("content");
                Log.e("hhh", "content=  " + content);
                if (content.startsWith("http://webapi.anmaa.com/api/agent/set-QrCodeBind?")) {
                    bindQrCode(content);
                } else {
                    Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == 200) {
                String content = data.getStringExtra("content");
                Log.e("hhh", "content= " + content);
                if (content.startsWith("http://webapi.anmaa.com/api/agent/set-QrCodeBind?")) {
                    bindQrCode(content);
                } else {
                    Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_my, container, false);
        mImageUserAvatar = contentView.findViewById(R.id.image_user_avatar);
        mTextUserName = contentView.findViewById(R.id.text_user_name);
        mTextTodayReadTime = contentView.findViewById(R.id.text_today_read_time);
        mTextCoin = contentView.findViewById(R.id.text_coin);

        mTvInvitationCode = contentView.findViewById(R.id.tv_invitation_code);
        mViewInvitationCode = contentView.findViewById(R.id.view_invitation_code);

        contentView.findViewById(R.id.tv_message).setOnClickListener(this);
        contentView.findViewById(R.id.tv_service).setOnClickListener(this);
        contentView.findViewById(R.id.tv_account).setOnClickListener(this);
        contentView.findViewById(R.id.img_recharge).setOnClickListener(this);
        contentView.findViewById(R.id.tv_sign).setOnClickListener(this);
        contentView.findViewById(R.id.userInfoLayout).setOnClickListener(this);
        contentView.findViewById(R.id.tv_welfare).setOnClickListener(this);
        contentView.findViewById(R.id.img_setting).setOnClickListener(this);
        contentView.findViewById(R.id.tv_help).setOnClickListener(this);
        contentView.findViewById(R.id.tv_author_platform).setOnClickListener(this);
        contentView.findViewById(R.id.image_enter).setOnClickListener(this);
        contentView.findViewById(R.id.tv_scan).setOnClickListener(this);
        mTvInvitationCode.setOnClickListener(this);
        return contentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUserInfo();
    }

    private void initUserInfo() {
        UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
        if (userInfo.userid != 0) {
            mTextUserName.setText(userInfo.nickname);
            mTextCoin.setText(String.valueOf(userInfo.money));

            // ImageLoader.with(mActivity).loadUserIcon(mImageUserAvatar, userInfo.headimgurl, R.drawable.default_head, 70, 70);

            GlideApp.with(mContext)
                    .load(userInfo.headimgurl)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.default_head)
                    .into(mImageUserAvatar);

            long minute = userInfo.readTime / 60;
            mTextTodayReadTime.setText(getString(R.string.today_read_count, minute));

            if (userInfo.agentId > 0) {
                mTvInvitationCode.setVisibility(View.GONE);
                mViewInvitationCode.setVisibility(View.GONE);
            } else {
                mTvInvitationCode.setVisibility(View.VISIBLE);
                mViewInvitationCode.setVisibility(View.VISIBLE);
            }
        } else {
            mTvInvitationCode.setVisibility(View.VISIBLE);
            mViewInvitationCode.setVisibility(View.VISIBLE);
            mTextUserName.setText(R.string.to_login);
            mTextTodayReadTime.setText(R.string.no_login_record);
        }
    }

    private void bindQrCode(String content) {
        String agentId = content.substring(content.lastIndexOf("=") + 1);
        Log.e("hhh", "agentId= " + agentId);
        if (!TextUtils.isEmpty(agentId)) {
            long userId = UserInfoManager.getInstance(mContext).getUserid();
            try {
                int intAgentId = Integer.parseInt(agentId);
                mPresenter.bindQrCode(userId, intAgentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void lunchLoginActivity() {
        if (isVisible()) {
            super.lunchLoginActivity();
        }
    }

    @Override
    public void getUserInfoSuccess(UserInfo userInfo) {
        if (userInfo != null && userInfo.userid != 0) {
            UserInfoManager.getInstance(mContext).saveUserInfo(userInfo);
            initUserInfo();
        }
    }

    @Override
    public void bindAgentIdSuccess(BaseResp resp) {
        Toast.makeText(mContext, "绑定邀请码成功", Toast.LENGTH_SHORT).show();
        mTvInvitationCode.setVisibility(View.GONE);
    }

    @Override
    public void bindAgentIdFailure(String msg) {
        Toast.makeText(mContext, "绑定邀请码失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void bindQrCodeSuccess(BaseResp resp) {
        Toast.makeText(mContext, "扫码绑定成功", Toast.LENGTH_SHORT).show();
        mTvInvitationCode.setVisibility(View.GONE);
    }

    @Override
    public void bindQrCodeFailure(String msg) {
        Toast.makeText(mContext, "扫码绑定失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }
}
