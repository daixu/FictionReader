package com.shangame.fiction.ui.listen.directory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.shangame.fiction.R;
import com.shangame.fiction.ad.TTAdManagerHolder;
import com.shangame.fiction.adapter.DirectoryAdapter;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.utils.DeviceUtils;
import com.shangame.fiction.core.utils.NetworkUtils;
import com.shangame.fiction.net.response.AlbumChapterDetailResponse;
import com.shangame.fiction.net.response.AlbumChapterResponse;
import com.shangame.fiction.net.response.AlbumSelectionResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.listen.ListenPopupWindow;
import com.shangame.fiction.ui.listen.PlayerSong;
import com.shangame.fiction.ui.listen.order.ChapterOrderPopWindow;
import com.shangame.fiction.ui.listen.palyer.Song;
import com.shangame.fiction.ui.listen.play.MusicPlayerActivity;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DirectoryFragment extends BaseFragment implements DirectoryContacts.View, View.OnClickListener {
    private static final int TOP_UP_FOR_BUY_CHAPTER_REQUEST_CODE = 508;
    private DirectoryAdapter mAdapter;
    private List<AlbumChapterResponse.PageDataBean> mList = new ArrayList<>();
    private DirectoryPresenter mPresenter;
    private int albumId;
    private int pageIndex = 1;
    private int pageSize = 30;
    private SmartRefreshLayout mSmartRefreshLayout;
    private TextView mTextTotal;
    private TextView mTvAnthology;
    private TextView mTvSort;
    private SelectionPopupWindow popupWindow;
    private ChapterOrderPopWindow chapterOrderPopWindow;
    private String mRemark;
    private boolean mType = true;
    private boolean isDesc = false;
    private int albumShelves;

    private TTRewardVideoAd mttRewardVideoAd;
    private boolean mHasShowDownloadActive = false;

    private AlbumChapterDetailResponse mResponse;
    private AlbumChapterResponse.PageDataBean mDataBean;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BroadcastAction.UPDATE_LISTEN_CHAPTER.equals(action)) {
                int chapterId = intent.getIntExtra("chapterId", 0);
                if (null != mAdapter) {
                    mAdapter.setChapterId(chapterId);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    public DirectoryFragment() {
    }

    public static DirectoryFragment newInstance(int arg1) {
        DirectoryFragment fragment = new DirectoryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("albumId", arg1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void getAlbumChapterSuccess(AlbumChapterResponse response) {
        mSmartRefreshLayout.finishRefresh();
        mSmartRefreshLayout.finishLoadMore();
        if (null == response) {
            return;
        }
        mTextTotal.setText(getString(R.string.listen_chapter_total, response.records));
        if (null == response.pagedata) {
            return;
        }
        if (pageIndex == 1 || !mType) {
            mList.clear();
        }
        pageIndex++;
        mList.addAll(response.pagedata);
        Song song = PlayerSong.getInstance().getPlayerSong();
        if (null != song) {
            mAdapter.setChapterId(song.id);
        }
        mAdapter.setNewData(mList);
    }

    @Override
    public void getAlbumChapterFailure(String msg) {

    }

    @Override
    public void getAlbumChapterDetailSuccess(AlbumChapterDetailResponse response, AlbumChapterResponse.PageDataBean bean) {
        if (null == response) {
            return;
        }
        mResponse = response;
        mDataBean = bean;
        if (response.advertState == 1) {
            play(response, bean);
        } else {
            alertAdDialog();
        }
    }

    private void play(final AlbumChapterDetailResponse response, final AlbumChapterResponse.PageDataBean bean) {
        if (null == response.play_url) {
            return;
        }
        if (TextUtils.isEmpty(response.play_url.small)) {
            return;
        }
        // 免费章节
        if (response.chargingmode == 0) {
            checkNetType(response, bean);
        } else {
            // 1已订阅
            if (response.buystatus == 1) {
                checkNetType(response, bean);
            } else {
                showChapterOrderPopWindow(albumId, bean.cid, new ChapterOrderPopWindow.OnOrderPayListener() {
                    @Override
                    public void onPaySuccess() {
                        Log.e("hhh", "onPaySuccess");
                        showToast(mActivity.getString(R.string.book_order_success));
                        checkNetType(response, bean);
                    }

                    @Override
                    public void onCancelPay() {
                        Log.e("hhh", "onCancelPay");
                    }
                });
            }
        }
    }

    private void alertAdDialog() {
        loadAd("921459942", TTAdConstant.VERTICAL);

        ListenPopupWindow listenPopupWindow = new ListenPopupWindow(mContext);
        new XPopup.Builder(mContext)
                .dismissOnBackPressed(false)
                .dismissOnTouchOutside(false)
                .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                .asCustom(listenPopupWindow)
                .show();

        listenPopupWindow.setOnAdClickListener(new ListenPopupWindow.OnAdClickListener() {
            @Override
            public void onClick() {
                if (mttRewardVideoAd != null) {
                    //step6:在获取到广告后展示
                    mttRewardVideoAd.showRewardVideoAd(mActivity);
                    mttRewardVideoAd = null;
                } else {
                    Log.e("hhh", "请先加载广告");
                }
            }
        });
    }

    private void checkNetType(final AlbumChapterDetailResponse response, final AlbumChapterResponse.PageDataBean bean) {
        NetworkUtils.NetworkType netWorkType = NetworkUtils.getNetworkType();
        switch (netWorkType) {
            case NETWORK_2G:
            case NETWORK_3G:
            case NETWORK_4G:
            case NETWORK_UNKNOWN:
            case NETWORK_ETHERNET:
                Log.e("hhh", "non wifi");
                alertNonWifi(response, bean);
                break;
            case NETWORK_WIFI:
                Log.e("hhh", "wifi");
                jumpToPlay(response, bean);
                break;
            default:
                break;
        }
    }

    private void showChapterOrderPopWindow(long bookId, long chapterId, ChapterOrderPopWindow.OnOrderPayListener onOrderPayListener) {
        if (chapterOrderPopWindow == null || !chapterOrderPopWindow.isShow()) {

            chapterOrderPopWindow = new ChapterOrderPopWindow(mContext, mActivity, bookId, chapterId);
            chapterOrderPopWindow.setOnOrderPayListener(onOrderPayListener);

            new XPopup.Builder(mContext)
                    .moveUpToKeyboard(false)
                    //.enableDrag(false)
                    .asCustom(chapterOrderPopWindow)
                    .show();
        }
    }

    private void loadAd(String codeId, int orientation) {
        //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        TTAdNative mAdNative = TTAdManagerHolder.get().createAdNative(mContext);
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(mContext);

        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setRewardName("金币")
                .setRewardAmount(1)
                .setUserID("user123")
                .setMediaExtra("media_extra")
                .setOrientation(orientation)
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
                        play(mResponse, mDataBean);
                    }

                    //视频播放完成回调
                    @Override
                    public void onVideoComplete() {
                        Log.e("hhh", "rewardVideoAd complete");
                        long userId = UserInfoManager.getInstance(mContext).getUserid();
                        mPresenter.setAdvertLog(userId, albumId);
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

    private void alertNonWifi(final AlbumChapterDetailResponse response, final AlbumChapterResponse.PageDataBean bean) {
        new AlertDialog.Builder(mContext)
                .setTitle("提示")
                .setMessage("当前为非WIFI环境，是否继续播放？")
                .setPositiveButton("继续播放", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        jumpToPlay(response, bean);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    private void jumpToPlay(AlbumChapterDetailResponse response, AlbumChapterResponse.PageDataBean bean) {
        Intent intent = new Intent(mContext, MusicPlayerActivity.class);
        Song song = new Song();
        song.albumid = albumId;
        song.id = bean.cid;
        song.lastcid = response.lastcid;
        song.nextcid = response.nextcid;
        song.buyStatus = response.buystatus;
        song.albumName = response.albumName;
        song.showCover = response.showCover;
        song.showName = response.showName;
        song.url = response.play_url.small;
        song.duration = bean.duration * 1000;
        song.bookShelves = albumShelves;
        song.readMoney = response.readmoney;
        song.chargingMode = response.chargingmode;
        song.chapterPrice = response.chapterprice;
        song.isVip = response.isvip;
        song.chapterNumber = response.sort;
        intent.putExtra("song", song);
        startActivity(intent);
    }

    @Override
    public void getAlbumChapterDetailFailure(String msg) {

    }

    @Override
    public void setAdvertLogSuccess() {
    }

    public void setBookShelves(int albumShelves) {
        this.albumShelves = albumShelves;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, null);
        Log.e("hhh", "onActivityResult---- ");
        if (requestCode == TOP_UP_FOR_BUY_CHAPTER_REQUEST_CODE) {
            Log.e("hhh", "TOP_UP_FOR_BUY_CHAPTER_REQUEST_CODE---- ");
            UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
            if (chapterOrderPopWindow != null && chapterOrderPopWindow.isShow()) {
                chapterOrderPopWindow.setCurrentMoney(userInfo.money, userInfo.coin);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_directory, container, false);
        initView(contentView);
        initListener();
        return contentView;
    }

    private void initView(View contentView) {
        mTextTotal = contentView.findViewById(R.id.text_total);
        mTvAnthology = contentView.findViewById(R.id.tv_anthology);
        mTvSort = contentView.findViewById(R.id.tv_sort);

        mSmartRefreshLayout = contentView.findViewById(R.id.smartRefreshLayout);
        RecyclerView recyclerView = contentView.findViewById(R.id.recycler_directory);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new DirectoryAdapter(R.layout.item_directory, mList, 0);
        recyclerView.setAdapter(mAdapter);
        popupWindow = new SelectionPopupWindow(mContext, albumId);
        popupWindow.setRemark(mRemark);
    }

    private void initListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AlbumChapterResponse.PageDataBean bean = mAdapter.getItem(position);
                if (null != bean) {
                    long userId = UserInfoManager.getInstance(mContext).getUserid();
                    String deviceId = DeviceUtils.getAndroidID();
                    mPresenter.getAlbumChapterDetail(userId, albumId, bean.cid, deviceId, bean);
                }
            }
        });

        mSmartRefreshLayout.setEnableRefresh(false);

        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (isDesc) {
                    loadData(pageIndex, pageSize, 1);
                } else {
                    loadData(pageIndex, pageSize, 0);
                }
            }
        });
        mTvAnthology.setOnClickListener(this);
        mTvSort.setOnClickListener(this);

        popupWindow.setOnClickItemListener(new SelectionPopupWindow.OnClickItemListener() {

            @Override
            public void onItemClick(AlbumSelectionResponse.SelectModeBean bean, int size) {
                mRemark = bean.remark;
                pageIndex = bean.page;
                pageSize = size;

                if (!TextUtils.isEmpty(mRemark) && "全部".equals(mRemark)) {
                    mType = true;
                } else {
                    mType = false;
                }

                mSmartRefreshLayout.setEnableLoadMore(mType);

                if (isDesc) {
                    loadData(pageIndex, pageSize, 1);
                } else {
                    loadData(pageIndex, pageSize, 0);
                }
            }
        });
    }

    private void loadData(int pageIndex, int pageSize, int orderBy) {
        long userId = UserInfoManager.getInstance(mContext).getUserid();

        mPresenter.getAlbumChapter(userId, albumId, pageIndex, pageSize, orderBy);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != getArguments()) {
            albumId = getArguments().getInt("albumId");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPresenter();
        loadData(1, pageSize, 0);
        initReceiver();
    }

    private void initPresenter() {
        mPresenter = new DirectoryPresenter();
        mPresenter.attachView(this);
    }

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.UPDATE_LISTEN_CHAPTER);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();

        if (null != popupWindow && popupWindow.isShown()) {
            popupWindow.dismiss();
        }
        if (null != chapterOrderPopWindow && chapterOrderPopWindow.isShown()) {
            chapterOrderPopWindow.dismiss();
        }

        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_anthology: {
                popupWindow.setRemark(mRemark);
                new XPopup.Builder(getContext())
                        .moveUpToKeyboard(false)
                        //.enableDrag(false)
                        .asCustom(popupWindow)
                        .show();
            }
            break;
            case R.id.tv_sort: {
                if (mType) {
                    isDesc = !isDesc;
                    pageIndex = 1;
                    if (isDesc) {
                        loadData(pageIndex, pageSize, 1);
                    } else {
                        loadData(pageIndex, pageSize, 0);
                    }
                } else {
                    Collections.reverse(mList);
                    mAdapter.notifyDataSetChanged();
                }
            }
            break;
            default:
                break;
        }
    }
}
