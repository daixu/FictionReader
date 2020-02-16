package com.shangame.fiction.ui.signin;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.CenterPopupView;
import com.shangame.fiction.R;
import com.shangame.fiction.ad.TTAdManagerHolder;

public class TaskCompletedPopupWindow {
    private Activity mActivity;
    private TTAdNative mAdNative;
    private boolean mHasShowDownloadActive = false;
    private TTRewardVideoAd mttRewardVideoAd;

    private TextView mTextNumber;
    private TextView mTextDesc;

    public TaskCompletedPopupWindow(Activity activity) {
        mActivity = activity;
        //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        mAdNative = TTAdManagerHolder.get().createAdNative(activity);
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(activity);
    }

    public void show(final String desc, final String number) {
        new XPopup.Builder(mActivity).asCustom(new CenterPopupView(mActivity) {
            @Override
            protected int getImplLayoutId() {
                return R.layout.popup_window_task_completed;
            }

            @Override
            protected void initPopupContent() {
                super.initPopupContent();

                findViewById(R.id.ivX).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });

                mTextNumber = findViewById(R.id.tvNumber);
                mTextDesc = findViewById(R.id.tvDesc);

                mTextNumber.setText(number);
                mTextDesc.setText(desc);

                loadAd("921459511", TTAdConstant.VERTICAL);

                findViewById(R.id.text_video).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mttRewardVideoAd != null) {
                            //step6:在获取到广告后展示
                            mttRewardVideoAd.showRewardVideoAd(mActivity);
                            mttRewardVideoAd = null;
                        } else {
                            Log.e("hhh", "请先加载广告");
                            Toast.makeText(mActivity, "请先加载广告", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).show();
    }

    private void loadAd(String codeId, int orientation) {
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setRewardName("金币") //奖励的名称
                .setRewardAmount(3)  //奖励的数量
                .setUserID("user123")//用户id,必传参数
                .setMediaExtra("media_extra") //附加参数，可选
                .setOrientation(orientation) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL
                .build();
        //step5:请求广告
        mAdNative.loadRewardVideoAd(adSlot, new TTAdNative.RewardVideoAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.e("hhh", "message= " + message);
            }

            //视频广告的素材加载完毕，比如视频url等，在此回调后，可以播放在线视频，网络不好可能出现加载缓冲，影响体验。
            @Override
            public void onRewardVideoAdLoad(TTRewardVideoAd ad) {
                Log.e("hhh", "rewardVideoAd loaded");
                mttRewardVideoAd = ad;
//                mttRewardVideoAd.setShowDownLoadBar(false);
                mttRewardVideoAd.setRewardAdInteractionListener(new TTRewardVideoAd.RewardAdInteractionListener() {

                    @Override
                    public void onAdShow() {
                        Log.e("hhh", "rewardVideoAd show");
                    }

                    @Override
                    public void onAdVideoBarClick() {
                        Log.e("hhh", "rewardVideoAd bar click");
                    }

                    @Override
                    public void onAdClose() {
                        Log.e("hhh", "rewardVideoAd close");
                        mTextNumber.setText("hahaha");
                        mTextDesc.setText("desc");
                    }

                    //视频播放完成回调
                    @Override
                    public void onVideoComplete() {
                        Log.e("hhh", "rewardVideoAd complete");
                    }

                    @Override
                    public void onVideoError() {
                        Log.e("hhh", "rewardVideoAd error");
                    }

                    //视频播放完成后，奖励验证回调，rewardVerify：是否有效，rewardAmount：奖励梳理，rewardName：奖励名称
                    @Override
                    public void onRewardVerify(boolean rewardVerify, int rewardAmount, String rewardName) {
                        Log.e("hhh", "verify:" + rewardVerify + " amount:" + rewardAmount + " name:" + rewardName);
                    }

                    @Override
                    public void onSkippedVideo() {
                        Log.e("hhh", "rewardVideoAd has onSkippedVideo");
                    }
                });
                mttRewardVideoAd.setDownloadListener(new TTAppDownloadListener() {
                    @Override
                    public void onIdle() {
                        mHasShowDownloadActive = false;
                    }

                    @Override
                    public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                        if (!mHasShowDownloadActive) {
                            mHasShowDownloadActive = true;
                            Log.e("hhh", "下载中，点击下载区域暂停");
                        }
                    }

                    @Override
                    public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                        Log.e("hhh", "下载暂停，点击下载区域继续");
                    }

                    @Override
                    public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                        Log.e("hhh", "下载失败，点击下载区域重新下载");
                    }

                    @Override
                    public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                        Log.e("hhh", "下载完成，点击下载区域重新下载");
                    }

                    @Override
                    public void onInstalled(String fileName, String appName) {
                        Log.e("hhh", "安装完成，点击下载区域打开");
                    }
                });
            }

            //视频广告加载后，视频资源缓存到本地的回调，在此回调后，播放本地视频，流畅不阻塞。
            @Override
            public void onRewardVideoCached() {
                Log.e("hhh", "rewardVideoAd video cached");
            }
        });
    }
}
