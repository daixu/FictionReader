package com.shangame.fiction.ui.share;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.utils.BitmapUtils;
import com.shangame.fiction.core.utils.ImageUtils;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.storage.manager.FileManager;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.task.TaskAwardContacts;
import com.shangame.fiction.ui.task.TaskAwardPresenter;
import com.shangame.fiction.ui.task.TaskId;
import com.shangame.fiction.ui.task.TaskRewardPopupWindow;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.File;

public class ShareDownloadAppActivity extends BaseActivity implements View.OnClickListener, TaskAwardContacts.View {

    private static final String TAG = "Share";

    private View shareLayout;

    private TaskAwardPresenter getRewardPresenter;

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
        setContentView(R.layout.activity_share_download_app);
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText("每日分享");
        shareLayout = findViewById(R.id.shareLayout);
        findViewById(R.id.tvShare).setOnClickListener(this);
        findViewById(R.id.ivPublicBack).setOnClickListener(this);

        getRewardPresenter = new TaskAwardPresenter();
        getRewardPresenter.attachView(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.SHARE_TO_WECHAT_SUCCESS_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getRewardPresenter.detachView();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvShare) {
            shareImage();
        } else if (v.getId() == R.id.ivPublicBack) {
            finish();
        }
    }

    private void shareImage() {
        final SharePopupWindow sharePopupWindow = new SharePopupWindow(mActivity, 0);
        sharePopupWindow.setOnShareListener(new SharePopupWindow.OnShareListener() {
            @Override
            public void onShareToWeChat() {
                Bitmap bitmap = BitmapUtils.loadBitmapFromView(shareLayout);
                WeChatSharer.shareImageToWeChat(mContext, bitmap);
            }

            @Override
            public void onShareToFriendCircle() {
                Bitmap bitmap = BitmapUtils.loadBitmapFromView(shareLayout);
                WeChatSharer.shareImageToFriendCircle(mContext, bitmap);
            }

            @Override
            public void onShareQq() {
                Bitmap bitmap = BitmapUtils.loadBitmapFromView(shareLayout);
                String path = FileManager.getInstance(mContext).getCacheDir() + File.separator + "aa.png";
                ImageUtils.save(bitmap, path, Bitmap.CompressFormat.PNG);
                QQSharer.shareImageToQQFriend(mActivity, path, new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        Log.i(TAG, "onComplete: ");
                        long userId = UserInfoManager.getInstance(mContext).getUserid();
                        getRewardPresenter.getTaskAward(userId, TaskId.SHARE_APP, true);
                    }

                    @Override
                    public void onError(UiError uiError) {
                        Log.i(TAG, "onError: ");
                    }

                    @Override
                    public void onCancel() {
                        Log.i(TAG, "onCancel: ");
                    }
                });
            }

            @Override
            public void onShareQqZone() {
                Bitmap bitmap = BitmapUtils.loadBitmapFromView(shareLayout);
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
                        Log.i(TAG, "onError: ");
                    }

                    @Override
                    public void onCancel() {
                        Log.i(TAG, "onCancel: ");
                    }
                });
            }
        });
        sharePopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void getTaskAwardSuccess(TaskAwardResponse taskAwardResponse, int taskId) {
        if (taskAwardResponse.number > 0) {
            UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
            userInfo.money = (long) taskAwardResponse.number;
            UserInfoManager.getInstance(mContext).saveUserInfo(userInfo);

            Intent intent = new Intent(BroadcastAction.UPDATE_TASK_LIST);
            intent.putExtra(SharedKey.TASK_ID, taskId);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

            TaskRewardPopupWindow taskRewardPopupWindow = new TaskRewardPopupWindow(mActivity);
            taskRewardPopupWindow.show(taskAwardResponse.desc, taskAwardResponse.number + "");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, null);
    }
}
