package com.shangame.fiction.ui.share.poster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.ViewPagerAdapter;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.utils.DensityUtil;
import com.shangame.fiction.core.utils.StatusBarUtil;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.GetSharePosterResp;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.popup.ReadRedPacketPopupWindow;
import com.shangame.fiction.ui.share.QQSharer;
import com.shangame.fiction.ui.share.SharePopupWindow;
import com.shangame.fiction.ui.share.WeChatSharer;
import com.shangame.fiction.ui.task.TaskId;
import com.shangame.fiction.ui.task.TaskRewardPopupWindow;
import com.shangame.fiction.widget.GalleryTransformer;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * 分享海报
 */
@Route(path = "/ss/share/poster")
public class SharePosterActivity extends BaseActivity implements SharePosterContacts.View, View.OnClickListener {
    private SharePosterPresenter mPresenter;

    private Fragment mFragment1;
    private Fragment mFragment2;
    private Fragment mFragment3;
    private Fragment mFragment4;
    private Fragment mFragment5;
    private Fragment mFragment6;

    private int mPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_poster);
        setStatusBar();

        initView();
        initPresenter();
        initListener();
    }

    public void setStatusBar() {
        View viewNeedOffset = findViewById(R.id.view_need_offset);
        StatusBarUtil.setTranslucentForImageView(this, 0, viewNeedOffset);
    }

    private void initView() {
        mFragment1 = Poster1Fragment.newInstance(1);
        mFragment2 = Poster2Fragment.newInstance(2);
        mFragment3 = Poster3Fragment.newInstance(3);
        mFragment4 = Poster4Fragment.newInstance(4);
        mFragment5 = Poster5Fragment.newInstance(5);
        mFragment6 = Poster6Fragment.newInstance(5);

        Fragment[] fragments = new Fragment[]{mFragment1, mFragment2, mFragment3, mFragment4, mFragment5, mFragment6};
        ViewPager viewPager = findViewById(R.id.vp_main_pager);
        String[] tabNames = new String[]{"海报1", "海报2", "海报3", "海报4", "海报5", "海报6"};

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) viewPager.getLayoutParams();
        lp.width = DensityUtil.dip2px(this, 230);
        lp.height = DensityUtil.dip2px(this, 390);
        viewPager.setLayoutParams(lp);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), tabNames, fragments);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setPageMargin(30);
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(false, new GalleryTransformer());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void initPresenter() {
        mPresenter = new SharePosterPresenter();
        mPresenter.attachView(this);
    }

    private void initListener() {
        findViewById(R.id.image_share).setOnClickListener(this);
        findViewById(R.id.img_back).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
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
        Log.e("hhh", dataBean.shareImage);
        WeChatSharer.shareUrlToWx(mContext, dataBean.shareImage, "hhh", "hhhcontent");
    }

    @Override
    public void getSharePosterFailure(String msg) {

    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
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
                UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
                int agentId = userInfo.agentId;
                mPresenter.getSharePoster(agentId, mPosition + 1);
                // shareWeChat();
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
        switch (mPosition) {
            case 0: {
                ((Poster1Fragment) mFragment1).downloadImage(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = ((Poster1Fragment) mFragment1).getImagePath();
                        WeChatSharer.shareImageToWeChat(mContext, path);
                    }
                });
            }
            break;
            case 1: {
                ((Poster2Fragment) mFragment2).downloadImage(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = ((Poster2Fragment) mFragment2).getImagePath();
                        WeChatSharer.shareImageToWeChat(mContext, path);
                    }
                });
            }
            break;
            case 2: {
                ((Poster3Fragment) mFragment3).downloadImage(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = ((Poster3Fragment) mFragment3).getImagePath();
                        WeChatSharer.shareImageToWeChat(mContext, path);
                    }
                });
            }
            break;
            case 3: {
                ((Poster4Fragment) mFragment4).downloadImage(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = ((Poster4Fragment) mFragment4).getImagePath();
                        WeChatSharer.shareImageToWeChat(mContext, path);
                    }
                });
            }
            break;
            case 4: {
                ((Poster5Fragment) mFragment5).downloadImage(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = ((Poster5Fragment) mFragment5).getImagePath();
                        WeChatSharer.shareImageToWeChat(mContext, path);
                    }
                });
            }
            break;
            case 5: {
                ((Poster6Fragment) mFragment6).downloadImage(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = ((Poster6Fragment) mFragment6).getImagePath();
                        WeChatSharer.shareImageToWeChat(mContext, path);
                    }
                });
            }
            break;
            default:
                break;
        }
    }

    private void shareFriendCircle() {
        switch (mPosition) {
            case 0: {
                ((Poster1Fragment) mFragment1).downloadImage(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = ((Poster1Fragment) mFragment1).getImagePath();
                        WeChatSharer.shareImageToFriendCircle(mContext, path);
                    }
                });
            }
            break;
            case 1: {
                ((Poster2Fragment) mFragment2).downloadImage(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = ((Poster2Fragment) mFragment2).getImagePath();
                        WeChatSharer.shareImageToFriendCircle(mContext, path);
                    }
                });
            }
            break;
            case 2: {
                ((Poster3Fragment) mFragment3).downloadImage(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = ((Poster3Fragment) mFragment3).getImagePath();
                        WeChatSharer.shareImageToFriendCircle(mContext, path);
                    }
                });
            }
            break;
            case 3: {
                ((Poster4Fragment) mFragment4).downloadImage(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = ((Poster4Fragment) mFragment4).getImagePath();
                        WeChatSharer.shareImageToFriendCircle(mContext, path);
                    }
                });
            }
            break;
            case 4: {
                ((Poster5Fragment) mFragment5).downloadImage(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = ((Poster5Fragment) mFragment5).getImagePath();
                        WeChatSharer.shareImageToFriendCircle(mContext, path);
                    }
                });
            }
            break;
            case 5: {
                ((Poster6Fragment) mFragment6).downloadImage(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = ((Poster6Fragment) mFragment6).getImagePath();
                        WeChatSharer.shareImageToFriendCircle(mContext, path);
                    }
                });
            }
            break;
            default:
                break;
        }
    }

    private void shareQq() {
        switch (mPosition) {
            case 0: {
                ((Poster1Fragment) mFragment1).downloadImage(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = ((Poster1Fragment) mFragment1).getImagePath();
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
            break;
            case 1: {
                ((Poster2Fragment) mFragment2).downloadImage(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = ((Poster2Fragment) mFragment2).getImagePath();
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
            break;
            case 2: {
                ((Poster3Fragment) mFragment3).downloadImage(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = ((Poster3Fragment) mFragment3).getImagePath();
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
            break;
            case 3: {
                ((Poster4Fragment) mFragment4).downloadImage(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = ((Poster4Fragment) mFragment4).getImagePath();
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
            break;
            case 4: {
                ((Poster5Fragment) mFragment5).downloadImage(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = ((Poster5Fragment) mFragment5).getImagePath();
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
            break;
            case 5: {
                ((Poster6Fragment) mFragment6).downloadImage(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = ((Poster6Fragment) mFragment6).getImagePath();
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
            break;
            default:
                break;
        }
    }

    private void shareQqZone() {
        switch (mPosition) {
            case 0: {
                ((Poster1Fragment) mFragment1).downloadImage(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = ((Poster1Fragment) mFragment1).getImagePath();
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
            break;
            case 1: {
                ((Poster2Fragment) mFragment2).downloadImage(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = ((Poster2Fragment) mFragment2).getImagePath();
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
            break;
            case 2: {
                ((Poster3Fragment) mFragment3).downloadImage(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = ((Poster3Fragment) mFragment3).getImagePath();
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
            break;
            case 3: {
                ((Poster4Fragment) mFragment4).downloadImage(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = ((Poster4Fragment) mFragment4).getImagePath();
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
            break;
            case 4: {
                ((Poster5Fragment) mFragment5).downloadImage(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = ((Poster5Fragment) mFragment5).getImagePath();
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
            break;
            case 5: {
                ((Poster6Fragment) mFragment6).downloadImage(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String path = ((Poster6Fragment) mFragment6).getImagePath();
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
            break;
            default:
                break;
        }
    }
}
