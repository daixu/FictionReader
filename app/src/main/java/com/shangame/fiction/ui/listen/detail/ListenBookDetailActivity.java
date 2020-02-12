package com.shangame.fiction.ui.listen.detail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.shangame.fiction.R;
import com.shangame.fiction.ad.TTAdManagerHolder;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.utils.DeviceUtils;
import com.shangame.fiction.core.utils.NetworkUtils;
import com.shangame.fiction.net.response.AlbumChapterDetailResponse;
import com.shangame.fiction.net.response.AlubmDetailResponse;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.storage.db.BookBrowseHistoryDao;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.manager.VisitorDbManager;
import com.shangame.fiction.storage.model.BookBrowseHistory;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.listen.ListenPopupWindow;
import com.shangame.fiction.ui.listen.PlayerSong;
import com.shangame.fiction.ui.listen.directory.DirectoryFragment;
import com.shangame.fiction.ui.listen.order.ChapterOrderPopWindow;
import com.shangame.fiction.ui.listen.palyer.Song;
import com.shangame.fiction.ui.listen.play.MusicPlayerActivity;
import com.shangame.fiction.ui.task.TaskId;
import com.shangame.fiction.ui.task.TaskRewardPopupWindow;
import com.shangame.fiction.ui.wifi.MagicIndicatorAdapter;
import com.shangame.fiction.widget.CircleRotateImageView;
import com.tencent.tauth.Tencent;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * 听书详情界面
 *
 * @author hhh
 */
@Route(path = "/ss/listen/detail")
public class ListenBookDetailActivity extends BaseActivity implements ListenBookDetailContacts.View, View.OnClickListener {
    private static final int TOP_UP_FOR_BUY_CHAPTER_REQUEST_CODE_1 = 507;
    @Autowired
    int albumId;
    private MagicIndicator mMagicIndicator;
    private ViewPager mViewPager;
    private TextView mTextBookName;
    private TextView mTextAuthorName;
    private TextView mTextBookType;
    private TextView mTextBookStatus;
    private ImageView mImageCover;
    private LinearLayout mLayoutDiscountsBuy;
    private View mViewLineBuy;
    private CircleRotateImageView mImagePlayerCover;
    private TextView mTvAddBookRack;
    private TextView mTvDiscountsBuy;
    private TextView mTvStartRead;
    private int chapterId;
    private CoordinatorLayout mLayoutContent;
    private AlubmDetailResponse mDetailResponse;
    private AlubmDetailResponse.DetailsDataBean mBean;
    private ListenBookDetailPresenter mPresenter;
    private DirectoryFragment mDirectoryFragment;
    private RelativeLayout mLayoutTop;
    private LinearLayout mLayoutBottom;
    private DetailFragment mDetailFragment;
    private int mPosition = 0;
    private ChapterOrderPopWindow chapterOrderPopWindow;

    private TTRewardVideoAd mttRewardVideoAd;
    private boolean mHasShowDownloadActive = false;

    private AlbumChapterDetailResponse mResponse;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BroadcastAction.ADD_BOOK_TO_RACK_ACTION.equals(action)) {
                if (intent.hasExtra("bookId")) {
                    if (null != mBean) {
                        mBean.allbumshelves = 1;
                    }
                    mTvAddBookRack.setEnabled(false);
                    mTvAddBookRack.setText(R.string.added_to_bookrack);
                }
            } else if (BroadcastAction.STOP_PLAY_ACTION.equals(action)) {
                if (null != mImagePlayerCover) {
                    mImagePlayerCover.setVisibility(View.GONE);
                    mImagePlayerCover.cancelRotateAnimation();
                }
            } else if (BroadcastAction.START_PLAY_ACTION.equals(action)) {
                if (null != mImagePlayerCover) {
                    mImagePlayerCover.setVisibility(View.VISIBLE);
                    mImagePlayerCover.startRotateAnimation();
                }
            } else if (BroadcastAction.PAUSE_PLAY_ACTION.equals(action)) {
                if (null != mImagePlayerCover) {
                    mImagePlayerCover.cancelRotateAnimation();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_book_detail);
        ARouter.getInstance().inject(this);

        initView();
        initViewPager();
        initPresenter();
        initData();
        initReceiver();
        initListener();
        initMagicIndicator();
    }

    private void initView() {
        mMagicIndicator = findViewById(R.id.magic_indicator);
        mViewPager = findViewById(R.id.view_pager);
        mTextBookName = findViewById(R.id.text_book_name);
        mTextAuthorName = findViewById(R.id.text_author_name);
        mTextBookType = findViewById(R.id.text_book_type);
        mTextBookStatus = findViewById(R.id.text_book_status);
        mImageCover = findViewById(R.id.image_cover);

        mLayoutTop = findViewById(R.id.layout_top);
        mTvAddBookRack = findViewById(R.id.tv_add_book_rack);
        mTvDiscountsBuy = findViewById(R.id.tv_discounts_buy);
        mTvStartRead = findViewById(R.id.tv_start_read);
        mImagePlayerCover = findViewById(R.id.image_player_cover);

        mLayoutDiscountsBuy = findViewById(R.id.layout_discounts_buy);
        mViewLineBuy = findViewById(R.id.view_line_buy);

        mLayoutContent = findViewById(R.id.layout_content);
        mLayoutBottom = findViewById(R.id.layout_bottom);

        mDirectoryFragment = DirectoryFragment.newInstance(albumId);
        mDetailFragment = DetailFragment.newInstance("");
    }

    private void initViewPager() {
        final List<Fragment> list = new ArrayList<>(2);
        list.add(mDirectoryFragment);
        list.add(mDetailFragment);

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0) {
                    return "目录";
                } else {
                    return "详情";
                }
            }

            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }
        };
        mViewPager.setAdapter(adapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                if (position == 1) {
                    mDetailFragment.setData(mDetailResponse);
                    mTvStartRead.setText("立即播放");
                } else {
                    mTvStartRead.setText("开始试听");
                }
            }

            @Override
            public void onPageScrollStateChanged(int stats) {

            }
        });
    }

    private void initPresenter() {
        mPresenter = new ListenBookDetailPresenter();
        mPresenter.attachView(this);
    }

    private void initData() {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        mPresenter.getAlbumDetail(userId, albumId);
    }

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.ADD_BOOK_TO_RACK_ACTION);
        intentFilter.addAction(BroadcastAction.STOP_PLAY_ACTION);
        intentFilter.addAction(BroadcastAction.START_PLAY_ACTION);
        intentFilter.addAction(BroadcastAction.PAUSE_PLAY_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, intentFilter);
    }

    private void initListener() {
        mTvAddBookRack.setOnClickListener(this);
        mTvDiscountsBuy.setOnClickListener(this);
        mTvStartRead.setOnClickListener(this);
        mImagePlayerCover.setOnClickListener(this);
        findViewById(R.id.img_back).setOnClickListener(this);
    }

    private void initMagicIndicator() {
        final List<String> titleList = new ArrayList<>(2);
        titleList.add("目录");
        titleList.add("详情");
        CommonNavigator navigator = new CommonNavigator(mContext);
        MagicIndicatorAdapter adapter = new MagicIndicatorAdapter(mViewPager);
        adapter.setTitleList(titleList);
        navigator.setAdapter(adapter);
        navigator.setAdjustMode(true);
        mMagicIndicator.setNavigator(navigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != PlayerSong.getInstance().getPlayerSong()) {
            Song song = PlayerSong.getInstance().getPlayerSong();

            RequestOptions playerOptions = new RequestOptions()
                    .circleCrop()
                    .placeholder(R.drawable.default_cover)
                    .override(54, 54);
            Glide.with(mContext).load(song.showCover).apply(playerOptions).into(mImagePlayerCover);

            mImagePlayerCover.setVisibility(View.VISIBLE);
            boolean isPlay = PlayerSong.getInstance().isPlay();
            if (isPlay) {
                mImagePlayerCover.startRotateAnimation();
            } else {
                mImagePlayerCover.cancelRotateAnimation();
            }
        } else {
            mImagePlayerCover.setVisibility(View.GONE);
            mImagePlayerCover.cancelRotateAnimation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        if (null != mImagePlayerCover) {
            mImagePlayerCover.cancelRotateAnimation();
        }
    }

    @Override
    public void getAlbumDetailSuccess(AlubmDetailResponse response) {
        mLayoutContent.setVisibility(View.VISIBLE);
        mLayoutBottom.setVisibility(View.VISIBLE);
        if (null != response) {
            mDetailResponse = response;
            if (null != response.detailsdata) {
                mBean = response.detailsdata;
                chapterId = mBean.chapterid;
                mTextBookName.setText(mBean.albumName);
                mTextAuthorName.setText(mBean.author);
                mTextBookType.setText(mBean.classname);
                StringBuilder status = new StringBuilder();
                if (!TextUtils.isEmpty(mBean.serstatus)) {
                    status.append(mBean.serstatus);
                    status.append(" ");
                }
                status.append("共");
                status.append(mBean.chapternumber);
                status.append("集");
                mTextBookStatus.setText(status);

                if (mBean.chargingmode == 1) {
                    mLayoutDiscountsBuy.setVisibility(View.VISIBLE);
                    mViewLineBuy.setVisibility(View.VISIBLE);
                } else {
                    mLayoutDiscountsBuy.setVisibility(View.GONE);
                    mViewLineBuy.setVisibility(View.GONE);
                }

                if (mBean.allbumshelves == 1) {
                    mTvAddBookRack.setText("已加入书架");
                    mTvAddBookRack.setEnabled(false);
                } else {
                    mTvAddBookRack.setText("加入书架");
                    mTvAddBookRack.setEnabled(true);
                }

                mDirectoryFragment.setBookShelves(mBean.allbumshelves);

                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.default_cover)
                        .override(104, 104);
                Glide.with(mContext).load(mBean.bookcover).apply(options).into(mImageCover);

                final RequestOptions bgOptions = new RequestOptions();
                bgOptions.placeholder(R.drawable.default_cover);
                bgOptions.transform(new BlurTransformation(100));

                Glide.with(mActivity)
                        .asBitmap()
                        .load(mBean.bookcover)
                        .apply(bgOptions)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                mLayoutTop.setBackground(new BitmapDrawable(resource));
                            }
                        });
            }
        }
    }

    @Override
    public void getAlbumDetailFailure(String msg) {

    }

    @Override
    public void getAlbumChapterDetailSuccess(final AlbumChapterDetailResponse response) {
        if (null == response) {
            return;
        }
        mResponse = response;
        if (response.advertState == 1) {
            play(response);
        } else {
            alertAdDialog();
        }
    }

    @Override
    public void getAlbumChapterDetailFailure(String msg) {

    }

    @Override
    public void addToBookRackSuccess(long bookId, int receive) {
        showToast(getString(R.string.add_to_bookrack_success));

        Intent intent = new Intent(BroadcastAction.ADD_BOOK_TO_RACK_ACTION);
        intent.putExtra("bookId", bookId);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        BookBrowseHistoryDao bookBrowseHistoryDao = VisitorDbManager.getDaoSession(mContext).getBookBrowseHistoryDao();
        List<BookBrowseHistory> list = bookBrowseHistoryDao.queryBuilder().where(BookBrowseHistoryDao.Properties.Bookid.eq(bookId)).list();

        for (BookBrowseHistory bookBrowseHistory : list) {
            bookBrowseHistory.bookshelves = 1;
            bookBrowseHistoryDao.update(bookBrowseHistory);
        }

        Song song = PlayerSong.getInstance().getPlayerSong();
        if (null != song) {
            if (bookId == song.albumid) {
                song.bookShelves = 1;
            }
        }

        if (receive == 0) {
            long userId = UserInfoManager.getInstance(mContext).getUserid();
            mPresenter.getTaskAward(userId, TaskId.ADD_BOOK_TO_RACK, true);
        }
    }

    @Override
    public void getTaskAwardSuccess(TaskAwardResponse taskAwardResponse, int taskId) {
        if (taskAwardResponse.number > 0) {
            TaskRewardPopupWindow taskRewardPopupWindow = new TaskRewardPopupWindow(mActivity);
            taskRewardPopupWindow.show(taskAwardResponse.desc, taskAwardResponse.number + "");
        }
    }

    @Override
    public void setAdvertLogSuccess() {
    }

    private void play(final AlbumChapterDetailResponse response) {
        if (null == response.play_url) {
            return;
        }
        if (TextUtils.isEmpty(response.play_url.small)) {
            return;
        }
        // 免费章节
        if (response.chargingmode == 0) {
            checkNetType(response);
        } else {
            // 1已订阅
            if (response.buystatus == 1) {
                checkNetType(response);
            } else {
                showChapterOrderPopWindow(albumId, chapterId, new ChapterOrderPopWindow.OnOrderPayListener() {
                    @Override
                    public void onPaySuccess() {
                        Log.e("hhh", "onPaySuccess");
                        showToast(mActivity.getString(R.string.book_order_success));
                        checkNetType(response);
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

        ListenPopupWindow listenPopupWindow = new ListenPopupWindow(this);
        new XPopup.Builder(this)
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

    private void checkNetType(final AlbumChapterDetailResponse response) {
        NetworkUtils.NetworkType netWorkType = NetworkUtils.getNetworkType();
        switch (netWorkType) {
            case NETWORK_2G:
            case NETWORK_3G:
            case NETWORK_4G:
            case NETWORK_UNKNOWN:
            case NETWORK_ETHERNET:
                Log.e("hhh", "non wifi");
                alertNonWifi(response);
                break;
            case NETWORK_WIFI:
                Log.e("hhh", "wifi");
                jumpToPlay(response);
                break;
            default:
                break;
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
                        play(mResponse);
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

    private void alertNonWifi(final AlbumChapterDetailResponse response) {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("当前为非WIFI环境，是否继续播放？")
                .setPositiveButton("继续播放", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        jumpToPlay(response);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    private void jumpToPlay(AlbumChapterDetailResponse response) {
        Intent intent = new Intent(mContext, MusicPlayerActivity.class);
        Song song = new Song();
        song.albumid = albumId;
        song.id = chapterId;
        song.lastcid = response.lastcid;
        song.nextcid = response.nextcid;
        song.buyStatus = response.buystatus;
        song.albumName = response.albumName;
        song.showCover = response.showCover;
        song.showName = response.showName;
        song.url = response.play_url.small;
        song.duration = response.duration * 1000;
        song.autoRenew = response.autorenew;
        song.bookShelves = mBean.allbumshelves;
        song.readMoney = response.readmoney;
        song.chargingMode = response.chargingmode;
        song.chapterPrice = response.chapterprice;
        song.isVip = response.isvip;
        song.chapterNumber = response.sort;
        intent.putExtra("song", song);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, null);
        if (requestCode == TOP_UP_FOR_BUY_CHAPTER_REQUEST_CODE_1) {
            UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
            if (chapterOrderPopWindow != null && chapterOrderPopWindow.isShow()) {
                chapterOrderPopWindow.setCurrentMoney(userInfo.money, userInfo.coin);
            }
        } else {
            if (mPosition == 1) {
                mDirectoryFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back: {
                finish();
            }
            break;
            case R.id.tv_discounts_buy: {
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                if (userId == 0) {
                    super.lunchLoginActivity();
                } else {
                    showOrderPopWindow();
                }
            }
            break;
            case R.id.tv_start_read: {
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                String deviceId = DeviceUtils.getAndroidID();
                mPresenter.getAlbumChapterDetail(userId, albumId, chapterId, deviceId);
            }
            break;
            case R.id.tv_add_book_rack: {
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                mPresenter.addToBookRack(userId, albumId);
            }
            break;
            case R.id.image_player_cover: {
                Intent intent = new Intent(mContext, MusicPlayerActivity.class);
                Song song = PlayerSong.getInstance().getPlayerSong();
                if (null != song) {
                    intent.putExtra("song", song);
                }
                intent.putExtra("type", 1);
                startActivity(intent);
            }
            break;
            default:
                break;
        }
    }

    private void showOrderPopWindow() {
        showChapterOrderPopWindow(albumId, 0, new ChapterOrderPopWindow.OnOrderPayListener() {
            @Override
            public void onPaySuccess() {
                Log.e("hhh", "onPaySuccess");
                showToast(mActivity.getString(R.string.book_order_success));
            }

            @Override
            public void onCancelPay() {
                Log.e("hhh", "onCancelPay");
            }
        });
    }

    private void showChapterOrderPopWindow(long albumId, long chapterId, ChapterOrderPopWindow.OnOrderPayListener onOrderPayListener) {
        if (chapterOrderPopWindow == null || !chapterOrderPopWindow.isShow()) {

            chapterOrderPopWindow = new ChapterOrderPopWindow(this, this, albumId, chapterId);
            chapterOrderPopWindow.setOnOrderPayListener(onOrderPayListener);

            new XPopup.Builder(this)
                    .moveUpToKeyboard(false)
                    //.enableDrag(false)
                    .asCustom(chapterOrderPopWindow)
                    .show();
        }
    }
}
