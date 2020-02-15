package com.shangame.fiction.ui.share.poster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.utils.BitmapUtils;
import com.shangame.fiction.core.utils.DensityUtil;
import com.shangame.fiction.core.utils.QrEncodeUtils;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.GetSharePosterResp;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.storage.manager.FileManager;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.popup.ReadRedPacketPopupWindow;
import com.shangame.fiction.ui.share.QQSharer;
import com.shangame.fiction.ui.share.SharePopupWindow;
import com.shangame.fiction.ui.share.WeChatSharer;
import com.shangame.fiction.ui.task.TaskId;
import com.shangame.fiction.ui.task.TaskRewardPopupWindow;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * 邀请好友
 */
@Route(path = "/ss/invite/friends")
public class InviteFriendsActivity extends BaseActivity implements SharePosterContacts.View, View.OnClickListener {
    private ProgressDialog mProgressDialog;
    private String imagePath;
    private ConstraintLayout mLayoutContent;

    private SharePosterPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
        imagePath = FileManager.getInstance(mContext).getCacheDir() + File.separator + "share.jpeg";
        initView();
        initPresenter();
    }

    private void initView() {
        mLayoutContent = findViewById(R.id.layout_content);
        findViewById(R.id.img_back).setOnClickListener(this);
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("邀请好友");
        findViewById(R.id.image_share).setOnClickListener(this);

        ImageView imageQr = findViewById(R.id.image_qr);

        UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();

        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        String url = "http://webapi.anmaa.com/api/agent/set-QrCodeBind?agentid=" + userInfo.agentId + "&channel=" + ApiConstant.Channel.ANDROID;
        Bitmap qrBitmap = QrEncodeUtils.createImage(url, DensityUtil.dip2px(mContext, 150), DensityUtil.dip2px(mContext, 150), logo);
        imageQr.setImageBitmap(qrBitmap);
    }

    private void initPresenter() {
        mPresenter = new SharePosterPresenter();
        mPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        if (null != mProgressDialog && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.image_share:
                sharePoster();
                break;
            default:
                break;
        }
    }

    private void sharePoster() {
        final SharePopupWindow sharePopupWindow = new SharePopupWindow(mActivity, 1);
        sharePopupWindow.setOnShareListener(new SharePopupWindow.OnShareListener() {
            @Override
            public void onShareToWeChat() {
                shareWeChat();
            }

            @Override
            public void onShareToFriendCircle() {
                shareFriendCircle();
            }

            @Override
            public void onShareQq() {
                shareQq();
            }

            @Override
            public void onShareQqZone() {
                shareQqZone();
            }
        });
        sharePopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    private void shareWeChat() {
        downloadImage(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = getImagePath();
                WeChatSharer.shareImageToWeChat(mContext, path);
            }
        });
    }

    private void shareFriendCircle() {
        downloadImage(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = getImagePath();
                WeChatSharer.shareImageToFriendCircle(mContext, path);
            }
        });

    }

    private void shareQq() {
        downloadImage(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = getImagePath();
                QQSharer.shareImageToQQFriend(mActivity, path, new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        showToast("分享成功");
                        long userId = UserInfoManager.getInstance(mContext).getUserid();
                        mPresenter.getTaskAward(userId, TaskId.SHARE_CHAPTER, true);
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
    }

    private void shareQqZone() {
        downloadImage(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = getImagePath();
                UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
                String url = "http://webapi.anmaa.com/api/agent/set-QrCodeBind?agentid=" + userInfo.agentId + "&channel=" + ApiConstant.Channel.ANDROID;
                QQSharer.shareImageToQzone(mActivity, url, "安马文学", "安马文学", path, new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        showToast("分享成功");
                        long userId = UserInfoManager.getInstance(mContext).getUserid();
                        mPresenter.getTaskAward(userId, TaskId.SHARE_CHAPTER, true);
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
    }

    private void downloadImage(final View.OnClickListener onClickListener) {
        final String msg = "正在生成图片……";
        mProgressDialog = ProgressDialog.show(this, null,msg, false);
        mProgressDialog.setProgressStyle(android.R.style.Widget_Material_ProgressBar_Horizontal);
        mProgressDialog.setCancelable(false);

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                OutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(new File(imagePath));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                Bitmap bitmap = BitmapUtils.loadBitmapFromView(mLayoutContent);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream);
                bitmap.recycle();
                return null;
            }

            @Override
            protected void onPreExecute() {
                if (null != mProgressDialog) {
                    mProgressDialog.show();
                }
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (null != mProgressDialog) {
                    mProgressDialog.dismiss();
                }
                onClickListener.onClick(null);
            }
        }.execute();
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    public void getTaskAwardSuccess(TaskAwardResponse response, int taskId) {
        Log.i("hhh", "getTaskAwardSuccess goldType= " + response.goldtype);
        if (response.goldtype == 1) {
            ReadRedPacketPopupWindow redPacketPopupWindow = new ReadRedPacketPopupWindow(mActivity, response);
            redPacketPopupWindow.show();
        } else {
            if (response.number > 0) {
                UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
                userInfo.money = (long) response.number;
                UserInfoManager.getInstance(mContext).saveUserInfo(userInfo);

                Intent intent = new Intent(BroadcastAction.UPDATE_TASK_LIST);
                intent.putExtra(SharedKey.TASK_ID, taskId);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                TaskRewardPopupWindow taskRewardPopupWindow = new TaskRewardPopupWindow(mActivity);
                taskRewardPopupWindow.show(response.desc, response.number + "");
            } else if (response.regtype == 0) {
                showToast("已经领取");
            }
        }
    }

    @Override
    public void getSharePosterSuccess(GetSharePosterResp.DataBean dataBean) {

    }

    @Override
    public void getSharePosterFailure(String msg) {

    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }
}
