package com.shangame.fiction.ui.task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.utils.BitmapUtils;
import com.shangame.fiction.core.utils.ImageUtils;
import com.shangame.fiction.core.utils.QrEncodeUtils;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.GetInviteUrlResponse;
import com.shangame.fiction.net.response.ShareWinRedResponse;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.storage.manager.FileManager;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.share.QQSharer;
import com.shangame.fiction.ui.share.SharePopupWindow;
import com.shangame.fiction.ui.share.WeChatSharer;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.File;

public class ShareToFriendActivity extends BaseActivity implements View.OnClickListener, ShareWinRedContacts.View, TaskAwardContacts.View {

    private SmartRefreshLayout smartRefreshLayout;
    private ImageView ivQR;
    private ShareWinRedPresenter shareWinRedPresenter;
    private TaskAwardPresenter getRewardPresenter;
    private View dataLayout;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BroadcastAction.SHARE_TO_WECHAT_SUCCESS_ACTION.equals(action)) {
                Toast.makeText(mActivity, getString(R.string.share_success), Toast.LENGTH_SHORT).show();
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                getRewardPresenter.getTaskAward(userId, TaskId.SHARE_APP, true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_to_friend);

        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText("分享好友");

        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        findViewById(R.id.tvShare).setOnClickListener(this);

        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });

        getRewardPresenter = new TaskAwardPresenter();
        getRewardPresenter.attachView(this);

        dataLayout = findViewById(R.id.dataLayout);
        ivQR = findViewById(R.id.ivQR);

        shareWinRedPresenter = new ShareWinRedPresenter();
        shareWinRedPresenter.attachView(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.SHARE_TO_WECHAT_SUCCESS_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, intentFilter);
        smartRefreshLayout.autoRefresh();
    }

    private void loadData() {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        shareWinRedPresenter.getInviteUrl(userId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shareWinRedPresenter.detachView();
        getRewardPresenter.detachView();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    @Override
    public void onClick(View view) {
        if (R.id.ivPublicBack == view.getId()) {
            finish();
        } else if (R.id.tvShare == view.getId()) {
            shareImage();
        }
    }

    private void shareImage() {
        final SharePopupWindow sharePopupWindow = new SharePopupWindow(mActivity, 0);
        sharePopupWindow.setOnShareListener(new SharePopupWindow.OnShareListener() {
            @Override
            public void onShareToWeChat() {
                Bitmap bitmap = BitmapUtils.loadBitmapFromView(dataLayout);
                WeChatSharer.shareImageToWeChat(mContext, bitmap);
            }

            @Override
            public void onShareToFriendCircle() {
                Bitmap bitmap = BitmapUtils.loadBitmapFromView(dataLayout);
                WeChatSharer.shareImageToFriendCircle(mContext, bitmap);
            }

            @Override
            public void onShareQq() {
                Bitmap bitmap = BitmapUtils.loadBitmapFromView(dataLayout);
                String path = FileManager.getInstance(mContext).getCacheDir() + File.separator + "aa.png";
                ImageUtils.save(bitmap, path, Bitmap.CompressFormat.PNG);
                QQSharer.shareImageToQQFriend(mActivity, path, new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        long userId = UserInfoManager.getInstance(mContext).getUserid();
                        getRewardPresenter.getTaskAward(userId, TaskId.SHARE_APP, true);
                    }

                    @Override
                    public void onError(UiError uiError) {
                    }

                    @Override
                    public void onCancel() {
                    }
                });
            }

            @Override
            public void onShareQqZone() {
                Bitmap bitmap = BitmapUtils.loadBitmapFromView(dataLayout);
                String path = FileManager.getInstance(mContext).getCacheDir() + File.separator + "aa.png";
                ImageUtils.save(bitmap, path, Bitmap.CompressFormat.PNG);
                String linkUrl = "https://m.anmaa.com/Download/index?channel=" + ApiConstant.Channel.ANDROID;
                QQSharer.shareImageToQzone(mActivity, linkUrl, path, "", path, new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        long userId = UserInfoManager.getInstance(mContext).getUserid();
                        getRewardPresenter.getTaskAward(userId, TaskId.SHARE_APP, true);
                    }

                    @Override
                    public void onError(UiError uiError) {
                    }

                    @Override
                    public void onCancel() {
                    }
                });
            }
        });
        sharePopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, null);
    }

    @Override
    public void getTaskAwardSuccess(TaskAwardResponse taskAwardResponse, int taskid) {
        if (taskAwardResponse.number > 0) {
            UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
            userInfo.money = (long) taskAwardResponse.number;
            UserInfoManager.getInstance(mContext).saveUserInfo(userInfo);

            Intent intent = new Intent(BroadcastAction.UPDATE_TASK_LIST);
            intent.putExtra(SharedKey.TASK_ID, taskid);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

            TaskRewardPopupWindow taskRewardPopupWindow = new TaskRewardPopupWindow(mActivity);
            taskRewardPopupWindow.show(taskAwardResponse.desc, taskAwardResponse.number + "");
        }
    }

    @Override
    public void getShareRuleSuccess(ShareWinRedResponse shareWinRedResponse) {

    }

    @Override
    public void getInviteUrlSuccess(GetInviteUrlResponse getInviteUrlResponse) {
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Bitmap qrBitmap = QrEncodeUtils.createImage(getInviteUrlResponse.codeurl, ivQR.getWidth(), ivQR.getWidth(), logo);
        ivQR.setImageBitmap(qrBitmap);

        dataLayout.setVisibility(View.VISIBLE);

        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.setEnableRefresh(false);
    }
}
