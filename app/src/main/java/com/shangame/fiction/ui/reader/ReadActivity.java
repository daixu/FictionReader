package com.shangame.fiction.ui.reader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.bytedance.sdk.openadsdk.TTRewardVideoAd;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.shangame.fiction.R;
import com.shangame.fiction.ad.ADConfig;
import com.shangame.fiction.ad.FeedAdBean;
import com.shangame.fiction.ad.TTAdManagerHolder;
import com.shangame.fiction.book.config.PageConfig;
import com.shangame.fiction.book.loader.ChapterLoader;
import com.shangame.fiction.book.page.Line;
import com.shangame.fiction.book.page.PageData;
import com.shangame.fiction.book.parser.ChapterParserListener;
import com.shangame.fiction.book.parser.TextChapterParser;
import com.shangame.fiction.book.test.PageFlipView;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.config.AppConfig;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.manager.AppSetting;
import com.shangame.fiction.core.utils.ImageUtils;
import com.shangame.fiction.core.utils.ScreenUtils;
import com.shangame.fiction.core.utils.TimeUtils;
import com.shangame.fiction.guide.ReadGuide;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.AdBean;
import com.shangame.fiction.net.response.CreatWapOrderResponse;
import com.shangame.fiction.net.response.GetGiftListConfigResponse;
import com.shangame.fiction.net.response.GetPayMenthodsResponse;
import com.shangame.fiction.net.response.GetRechargeConfigResponse;
import com.shangame.fiction.net.response.GiveGiftResponse;
import com.shangame.fiction.net.response.ReadTimeResponse;
import com.shangame.fiction.net.response.ReceivedGiftResponse;
import com.shangame.fiction.net.response.ShareResponse;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.statis.AppStatis;
import com.shangame.fiction.statis.ReadTracer;
import com.shangame.fiction.storage.db.BookBrowseHistoryDao;
import com.shangame.fiction.storage.db.BookReadProgressDao;
import com.shangame.fiction.storage.db.ChapterInfoDao;
import com.shangame.fiction.storage.manager.DbManager;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.manager.UserSetting;
import com.shangame.fiction.storage.manager.VisitorDbManager;
import com.shangame.fiction.storage.model.BookBrowseHistory;
import com.shangame.fiction.storage.model.BookMark;
import com.shangame.fiction.storage.model.BookParagraph;
import com.shangame.fiction.storage.model.BookReadProgress;
import com.shangame.fiction.storage.model.Chapter;
import com.shangame.fiction.storage.model.ChapterInfo;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.bookdetail.ErrorFeedbackActivity;
import com.shangame.fiction.ui.bookdetail.RecommendBookActivity;
import com.shangame.fiction.ui.bookdetail.gift.GiftContracts;
import com.shangame.fiction.ui.bookdetail.gift.GiftPopupWindow;
import com.shangame.fiction.ui.bookdetail.gift.GiftPresenter;
import com.shangame.fiction.ui.bookrack.AddToBookRackContacts;
import com.shangame.fiction.ui.bookrack.AddToBookRackPresenter;
import com.shangame.fiction.ui.contents.BookContentsPopupWindow;
import com.shangame.fiction.ui.contents.BookMarkContacts;
import com.shangame.fiction.ui.contents.BookMarkPresenter;
import com.shangame.fiction.ui.contents.OnBookMarkCheckedLinstener;
import com.shangame.fiction.ui.contents.OnChapterCheckedListener;
import com.shangame.fiction.ui.my.pay.PayCenterActivity;
import com.shangame.fiction.ui.my.pay.PayPopupWindow;
import com.shangame.fiction.ui.my.pay.PayResult;
import com.shangame.fiction.ui.popup.AdPopupWindow;
import com.shangame.fiction.ui.popup.NewUserReadRemindPopupWindow;
import com.shangame.fiction.ui.popup.OfflineReadRedPacketPopupWindow;
import com.shangame.fiction.ui.popup.ReadRedPacketPopupWindow;
import com.shangame.fiction.ui.popup.RedTaskPopupWindow;
import com.shangame.fiction.ui.popup.ShareReadImagePreviewPopupWindow;
import com.shangame.fiction.ui.share.QQSharer;
import com.shangame.fiction.ui.share.ShareContracts;
import com.shangame.fiction.ui.share.SharePopupWindow;
import com.shangame.fiction.ui.share.SharePresenter;
import com.shangame.fiction.ui.share.WeChatSharer;
import com.shangame.fiction.ui.task.TaskAwardContacts;
import com.shangame.fiction.ui.task.TaskAwardPresenter;
import com.shangame.fiction.ui.task.TaskId;
import com.shangame.fiction.ui.task.TaskRewardPopupWindow;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import notchtools.geek.com.notchtools.NotchTools;

import static com.shangame.fiction.ad.ADConfig.CSJAdPositionId.READ_PAGE_ID;
import static com.shangame.fiction.core.constant.SharedKey.AD_FREE;
import static com.shangame.fiction.core.constant.SharedKey.IS_NO_AD;

/**
 * 阅读界面
 * <p>
 * Create by Speedy on 2018/7/23
 */
public class ReadActivity extends BaseActivity implements GestureDetector.OnGestureListener,
        View.OnClickListener, BookLoadContract.View, AddToBookRackContacts.View,
        GiftContracts.View, BookMarkContacts.View, ShareContracts.View,
        TaskAwardContacts.View, GetUserInfoContracts.View {

    public static final int TOP_UP_FOR_GIFT_REQUEST_CODE = 503;
    public static final int TOP_UP_FOR_BUY_CHAPTER_REQUEST_CODE = 505;
    public static final int FROM_OPEN_RED_TASK_REQUEST_CODE = 666;
    private static final String TAG = "ReadActivity";
    private static final int FROM_ADD_TO_BOOK_RACK = 251;
    private static final int FROM_SHARE = 252;
    private static final int FROM_ADD_GIFT = 253;
    private static final int FROM_READ_PACKAGE = 258;
    private static int unUserTaskId;
    private static Toast mProgressToast;
    private PageFlipView mPageFlipView;
    private GestureDetector mGestureDetector;
    private View readHeadLayout;
    private View mLayoutHeadTop;
    private View mLayoutReadTop;
    private View mLayoutReadContent;
    private View mLayoutRedTop;
    private View readSettingLayout;
    private View ivDot;
    private ImageView ivRed;
    private TextView tvBookName;
    private UserSetting setting;
    private int baColor;
    private SeekBar mSeekBar;
    private TextView tvDayMode;
    private TextView tvAddToBookRack;
    private PageConfig mPageConfig;
    private long bookId;
    private long chapterId;
    private int mType;
    private PageRenderAdapter renderAdapter;
    private BookLoadPresenter bookLoadPresenter;
    private GetUserInfoPresenter mPresenter;
    private TextChapterParser textBookParser;
    private AddToBookRackPresenter addToBookRackPresenter;
    private GiftPresenter giftPresenter;
    private GiftPopupWindow giftPopupWindow;
    private BookContentsPopupWindow bookContentsPopupWindow;
    private BookMarkPresenter bookMarkPresenter;
    private BookMark bookMark;
    private BatteryReceiver batteryReceiver;
    private int showPageIndex;
    private ChapterOrderPopupWindow chapterOrderPopupWindow;
    private boolean hasAddToBookRack = true;//标记是否已经加入书架
    private SharePresenter sharePresenter;
    private AppSetting appSetting;
    private ReadTracer readTracer;
    private TaskAwardPresenter taskAwardPresenter;
    private int adPageCount = 10;
    private FrameLayout contentADContainer;
    private FrameLayout bottomADContainer;
    private int index1;
    private List<TTFeedAd> mTTAdList = new ArrayList<>();

    private TTAdNative mAdNative;
    private AdSlot mAdSlot;
    private TTFeedAd mNativeExpressAd;
    private boolean mHasShowDownloadActive = false;
    private TTRewardVideoAd mttRewardVideoAd;

    private long mCurrentChapterId;
    private PayPopupWindow payPopupWindow;

    private long mAdChapterId;
    private long mAdVideoChapterId;
    private int mTaskLogId;
    private ChapterInfo mChapterInfo;
    private ReadRedPacketPopupWindow mRedPacketPopupWindow;
    private int mAdType;
    private AdPopupWindow mAdPopup;

    private ThreadFactory nameThreadFactory = new ThreadFactoryBuilder().setNameFormat("").build();
    private ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MICROSECONDS, new LinkedBlockingQueue<Runnable>(1024), nameThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            long userid = UserInfoManager.getInstance(mContext).getUserid();
            if (BroadcastAction.SHARE_TO_WECHAT_SUCCESS_ACTION.equals(action)) {
                Toast.makeText(mActivity, getString(R.string.share_success), Toast.LENGTH_SHORT).show();
                taskAwardPresenter.getTaskAward(userid, TaskId.SHARE_CHAPTER, true);
            } else if (BroadcastAction.OFFLINE_READ_RED_PACKET.equals(action)) {
                String desc = intent.getStringExtra("desc");
                int taskId = intent.getIntExtra(SharedKey.TASK_ID, TaskId.READ_30);
                unUserTaskId = taskId;

                OfflineReadRedPacketPopupWindow redPacketPopupWindow = new OfflineReadRedPacketPopupWindow(mActivity, desc);
                redPacketPopupWindow.show(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lunchLoginActivity(FROM_READ_PACKAGE);
                    }
                });
            } else if (BroadcastAction.READ_RED_PACKET.equals(action)) {
                Log.i("hhh", "30分钟广播");
                int taskId = intent.getIntExtra(SharedKey.TASK_ID, TaskId.READ_30);
                unUserTaskId = taskId;
                taskAwardPresenter.getTaskAward(userid, taskId, false);
            }
        }
    };

    public static void lunchActivity(Activity activity, long bookId, long chapterId, int pageIndex, int type) {
        Intent intent = new Intent(activity, ReadActivity.class);
        intent.putExtra("bookId", bookId);
        intent.putExtra("chapterId", chapterId);
        intent.putExtra("pageIndex", pageIndex);
        intent.putExtra("type", type);
        activity.startActivity(intent);
    }

    public static void lunchActivity(Activity activity, long bookId, long chapterId) {
        Intent intent = new Intent(activity, ReadActivity.class);
        intent.putExtra("bookId", bookId);
        intent.putExtra("chapterId", chapterId);
        activity.startActivity(intent);
    }

    public static void lunchActivity(Activity activity, long bookId, long chapterId, BookMark bookMark) {
        Intent intent = new Intent(activity, ReadActivity.class);
        intent.putExtra("bookId", bookId);
        intent.putExtra("chapterId", chapterId);
        intent.putExtra("BookMark", bookMark);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        setContentView(R.layout.activity_read_1);
        initParam();
        initPageConfig();
        initHeader();
        initBookView();
        initSetting();
        restoreConfig();
        hideMenuLayout();
        initPresenter();
        initBatteryReceiver();
        initReceiver();

        int verify = AdBean.getInstance().getVerify();
        if (verify == 0) {
            initCsjAd();
        }
        loadData();
        initAdUpload();
    }

    private void initParam() {
        bookId = getIntent().getLongExtra("bookId", 0);
        chapterId = getIntent().getLongExtra("chapterId", 0);
        showPageIndex = getIntent().getIntExtra("pageIndex", 0);
        mType = getIntent().getIntExtra("type", 0);
        if (getIntent().hasExtra("BookMark")) {
            bookMark = getIntent().getParcelableExtra("BookMark");
        }

        textBookParser = new TextChapterParser(mActivity);

        appSetting = AppSetting.getInstance(mContext);
        appSetting.putBoolean(SharedKey.IS_READ_ACTIVITY_NORMAL_FINISH, false);

        readTracer = new ReadTracer(mContext);

        Intent intent = new Intent(BroadcastAction.REFRESH_BOOK_RACK_RANK);
        intent.putExtra("bookId", bookId);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void initPageConfig() {
        mPageConfig = PageConfig.getInstance(mContext);
    }

    private void initHeader() {
        readHeadLayout = findViewById(R.id.readHeadLayout);
        readHeadLayout.findViewById(R.id.ivBack).setOnClickListener(this);
        readHeadLayout.findViewById(R.id.ivGift).setOnClickListener(this);
        readHeadLayout.findViewById(R.id.ivDian).setOnClickListener(this);

        mLayoutHeadTop = readHeadLayout.findViewById(R.id.layout_head_top);

        mLayoutReadTop = findViewById(R.id.layout_read_top);
        mLayoutReadContent = findViewById(R.id.layout_read_content);
        mLayoutRedTop = findViewById(R.id.layout_red_top);

        if (Build.VERSION.SDK_INT > 20) {
            getWindow().getDecorView().setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                @SuppressLint("NewApi")
                @Override
                public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                    boolean isNotchScreen = NotchTools.getFullScreenTools().isNotchScreen(getWindow());
                    Log.e("hhh", "isNotchScreen= " + isNotchScreen);
                    if (isNotchScreen) {
                        mLayoutHeadTop.setVisibility(View.VISIBLE);
                        mLayoutRedTop.setVisibility(View.VISIBLE);
                        mLayoutReadTop.setVisibility(View.VISIBLE);
                        mLayoutReadContent.setBackgroundResource(mPageConfig.backgroundColor);
                    } else {
                        mLayoutHeadTop.setVisibility(View.GONE);
                        mLayoutRedTop.setVisibility(View.GONE);
                        mLayoutReadTop.setVisibility(View.GONE);
                    }
                    getWindow().getDecorView().setOnApplyWindowInsetsListener(null);
                    return view.onApplyWindowInsets(windowInsets);
                }
            });
        } else {
            mLayoutHeadTop.setVisibility(View.GONE);
            mLayoutRedTop.setVisibility(View.GONE);
            mLayoutReadTop.setVisibility(View.GONE);
        }

        tvAddToBookRack = readHeadLayout.findViewById(R.id.tvAddToBookrack);
        readHeadLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        tvBookName = readHeadLayout.findViewById(R.id.tvBookName);
        ivDot = readHeadLayout.findViewById(R.id.ivDot);
        setting = UserSetting.getInstance(mContext);
        boolean hasShareRead = setting.getBoolean(SharedKey.HAS_SHARE_READ, false);
        if (hasShareRead) {
            ivDot.setVisibility(View.GONE);
        } else {
            ivDot.setVisibility(View.VISIBLE);
        }
    }

    private void initBookView() {
        ivRed = findViewById(R.id.ivRed);
        ivRed.setOnClickListener(this);

        mPageFlipView = findViewById(R.id.bookView);
        mGestureDetector = new GestureDetector(this, this);

        mPageFlipView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (readHeadLayout.getVisibility() == View.VISIBLE) {
                    hideMenuLayout();
                    return true;
                } else {
                    return false;
                }
            }
        });

        mPageFlipView.setOnChangePageListener(new PageFlipView.OnChangePageListener() {
            @Override
            public void onChangePage(final PageData pageData) {
                readTracer.flipPage();
                if (pageData != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mTTAdList.size() > 0) {
                                boolean isNoAd = AppSetting.getInstance(mContext).getBoolean(IS_NO_AD, false);
                                if (index1 >= mTTAdList.size()) {
                                    index1 = 0;
                                    if (!isNoAd) {
                                        try {
                                            int verify = AdBean.getInstance().getVerify();
                                            if (verify == 0) {
                                                loadCsjAd();
                                            }
                                        } catch (Exception e) {
                                            Log.e("hhh", "Exception");
                                        }
                                    }
                                }
                                if ((pageData.isADPage || pageData.hasAdPage) && !isNoAd) {
                                    Log.e("hhh", "isADPage");
                                    if (contentADContainer.getChildCount() > 0) {
                                        mNativeExpressAd = mTTAdList.get(0);
                                        // mNativeExpressAd.render();

                                        FeedAdBean.getInstance().setFeedAd(mTTAdList.get(0));
                                        contentADContainer.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    if (contentADContainer.getVisibility() == View.VISIBLE) {
                                        contentADContainer.setVisibility(View.GONE);
                                        contentADContainer.removeAllViews();
                                    }
                                    if (contentADContainer.getChildCount() == 0) {
                                        if (index1 < mTTAdList.size()) {
                                            FeedAdBean.getInstance().setFeedAd(mTTAdList.get(index1));
                                        } else {
                                            FeedAdBean.getInstance().setFeedAd(mTTAdList.get(0));
                                        }
                                        index1++;
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });

        renderAdapter = new PageRenderAdapter(mContext);
        mPageFlipView.setRenderAdapter(renderAdapter);

        mPageFlipView.setChapterLoader(new ChapterLoader() {
            @Override
            public void loadNextChapter(long bookId, long chapterId, boolean showLoading, int type, String method) {
                Log.e("hhh", "type425= " + type + " ,method= " + method);
                if (chapterId == 0) {
                    if (type == 0) {
                        RecommendBookActivity.lunchActivity(mActivity, bookId, ApiConstant.ClickType.FROM_CLICK);
                    } else {
                        Log.e("hhh", "最后一章");
                    }
                } else {
                    if (showLoading) {
                        ReadActivity.super.showLoading();
                    }
                    long userid = UserInfoManager.getInstance(mContext).getUserid();
                    bookLoadPresenter.getNextChapter(userid, bookId, chapterId);
                }
            }

            @Override
            public void loadBeforeChapter(long bookId, long chapterId) {
                if (chapterId == 0) {
                    Toast.makeText(mContext, mContext.getString(R.string.hint_first_page), Toast.LENGTH_SHORT).show();
                } else {
                    ReadActivity.super.showLoading();
                    long userId = UserInfoManager.getInstance(mContext).getUserid();
                    bookLoadPresenter.getBeforeChapter(userId, bookId, chapterId);
                }
            }
        });

        mPageFlipView.setOnPayChapterClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final long userId = UserInfoManager.getInstance(mContext).getUserid();
                if (userId == 0) {
                    final PageData pageData = mPageFlipView.getCurrentPageData();
                    mCurrentChapterId = pageData.chapterId;
                    ReadActivity.super.lunchLoginActivity();
                } else {
                    final PageData pageData = mPageFlipView.getCurrentPageData();
                    showChapterOrderPopupWindow(pageData.bookId, pageData.chapterId, new ChapterOrderPopupWindow.OnOrderPayListener() {
                        @Override
                        public void onPaySuccess() {
                            showToast(mActivity.getString(R.string.book_order_success));

                            ChapterInfoDao chapterInfoDao = DbManager.getDaoSession(mContext).getChapterInfoDao();
                            Log.e("hhh", "bookId= " + pageData.bookId + "chapterId= " + pageData.chapterId);
                            ChapterInfo chapterInfo = chapterInfoDao.queryBuilder()
                                    .where(ChapterInfoDao.Properties.Bookid.eq(pageData.bookId), ChapterInfoDao.Properties.Cid.eq(pageData.chapterId)).unique();
                            Log.e("hhh", "chapterInfo= " + chapterInfo);

                            mPresenter.getUserInfo(userId);

                            if (null != chapterInfo) {
                                chapterInfo.buystatus = ApiConstant.PayStatus.PAID;
                                chapterInfoDao.insertOrReplace(chapterInfo);
                            }

                            List<BookParagraph> bookParagraphList = bookLoadPresenter.queryBookParagraph(pageData.bookId, pageData.chapterId);
                            bookLoadPresenter.deleteChargeChapter(pageData.bookId, pageData.chapterId);
                            textBookParser.parseChapter(chapterInfo, bookParagraphList, new ChapterParserListener() {
                                @Override
                                public void parseFinish(List<PageData> list) {
                                    renderAdapter.replaceChargePage(list);
                                }
                            });
                        }

                        @Override
                        public void onCancelPay() {
                        }
                    });
                }
            }
        });

        mPageFlipView.setOnPayMoreChapterClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final long userId = UserInfoManager.getInstance(mContext).getUserid();
                if (userId == 0) {
                    ReadActivity.super.lunchLoginActivity();
                } else {
                    final PageData pageData = mPageFlipView.getCurrentPageData();
                    showChapterOrderPopupWindow(pageData.bookId, pageData.chapterId, new ChapterOrderPopupWindow.OnOrderPayListener() {
                        @Override
                        public void onPaySuccess() {
                            ChapterInfoDao chapterInfoDao = DbManager.getDaoSession(mContext).getChapterInfoDao();
                            Log.e("hhh", "bookId= " + pageData.bookId + "chapterId= " + pageData.chapterId);
                            ChapterInfo chapterInfo = chapterInfoDao.queryBuilder()
                                    .where(ChapterInfoDao.Properties.Bookid.eq(pageData.bookId), ChapterInfoDao.Properties.Cid.eq(pageData.chapterId)).unique();
                            Log.e("hhh", "chapterInfo= " + chapterInfo);

                            mPresenter.getUserInfo(userId);

                            if (null != chapterInfo) {
                                chapterInfo.buystatus = ApiConstant.PayStatus.PAID;
                                chapterInfoDao.insertOrReplace(chapterInfo);
                            }

                            List<BookParagraph> bookParagraphList = bookLoadPresenter.queryBookParagraph(pageData.bookId, pageData.chapterId);
                            bookLoadPresenter.deleteChargeChapter(pageData.bookId, pageData.chapterId);
                            textBookParser.parseChapter(chapterInfo, bookParagraphList, new ChapterParserListener() {
                                @Override
                                public void parseFinish(List<PageData> list) {
                                    renderAdapter.replaceChargePage(list);
                                }
                            });
                        }

                        @Override
                        public void onCancelPay() {
                        }
                    });
                }
            }
        });

        mPageFlipView.setOnAutoPayNextChapterListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageData pageData = mPageFlipView.getCurrentPageData();
                pageData.autoPayNextChapter = !pageData.autoPayNextChapter;
                mPageFlipView.refreshPage();
            }
        });

        mPageFlipView.setOnMenuRegionClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readHeadLayout.getVisibility() == View.GONE) {
                    showMenuLayout();
                } else {
                    hideMenuLayout();
                }
            }
        });

        mPageFlipView.setOnOpenMenuRegionClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readHeadLayout.getVisibility() == View.GONE) {
                    showMenuLayout();
                } else {
                    hideMenuLayout();
                }
            }
        });

        mPageFlipView.setOnOpenVideoRegionClickListener1(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageData pageData = mPageFlipView.getCurrentPageData();
                boolean isADPage = false;
                boolean hasAdPage = false;
                if (null != pageData) {
                    isADPage = pageData.isADPage;
                    hasAdPage = pageData.hasAdPage;
                    mAdVideoChapterId = pageData.chapterId;
                }
                boolean isNoAd = AppSetting.getInstance(mContext).getBoolean(IS_NO_AD, false);
                if (!isNoAd && (isADPage || hasAdPage)) {
                    loadAd("921459511", TTAdConstant.VERTICAL);
                    if (mttRewardVideoAd != null) {
                        //step6:在获取到广告后展示
                        mttRewardVideoAd.showRewardVideoAd(ReadActivity.this);
                        mttRewardVideoAd = null;
                    } else {
                        Log.e("hhh", "请先加载广告");
                    }
                }
            }
        });
    }

    private void initSetting() {
        readSettingLayout = findViewById(R.id.readSettingLayout);
        readSettingLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        mSeekBar = readSettingLayout.findViewById(R.id.seekBar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    PageData pageData = mPageFlipView.getCurrentPageData();
                    showProgressToast(progress, pageData.chapterName);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                hideMenuLayout();
                mPageFlipView.setProgress(seekBar.getProgress());
            }
        });

        readSettingLayout.findViewById(R.id.tvFontSetting).setOnClickListener(this);
        readSettingLayout.findViewById(R.id.beforeChapter).setOnClickListener(this);
        readSettingLayout.findViewById(R.id.nextChapter).setOnClickListener(this);
        readSettingLayout.findViewById(R.id.tvMenu).setOnClickListener(this);
        tvDayMode = readSettingLayout.findViewById(R.id.tvDayMode);
        tvDayMode.setOnClickListener(this);

        //夜间模式按钮的状态
        toggleNightMode();
    }

    private void restoreConfig() {
        baColor = mPageConfig.backgroundColor;
        setStatusBarColor(baColor);
    }

    private void hideMenuLayout() {
        setStatusBarColor(baColor);
        readHeadLayout.setVisibility(View.GONE);
        readSettingLayout.setVisibility(View.GONE);
    }

    private void initPresenter() {
        bookLoadPresenter = new BookLoadPresenter(this);
        bookLoadPresenter.attachView(this);

        mPresenter = new GetUserInfoPresenter(this);
        mPresenter.attachView(this);

        addToBookRackPresenter = new AddToBookRackPresenter();
        addToBookRackPresenter.attachView(this);

        giftPresenter = new GiftPresenter();
        giftPresenter.attachView(this);

        bookMarkPresenter = new BookMarkPresenter(mContext);
        bookMarkPresenter.attachView(this);

        taskAwardPresenter = new TaskAwardPresenter();
        taskAwardPresenter.attachView(this);

        sharePresenter = new SharePresenter();
        sharePresenter.attachView(this);
    }

    private void initBatteryReceiver() {
        batteryReceiver = new BatteryReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);
    }

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.SHARE_TO_WECHAT_SUCCESS_ACTION);
        intentFilter.addAction(BroadcastAction.READ_RED_PACKET);
        intentFilter.addAction(BroadcastAction.OFFLINE_READ_RED_PACKET);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, intentFilter);
    }

    private void initCsjAd() {
        initMiddleAd();
        initBottomAd();
    }

    private void loadData() {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        if (bookMark == null) {
            Log.e("hhh", "type806= " + mType);
            bookLoadPresenter.getChapter(userId, bookId, chapterId, mType);
        } else {
            bookLoadPresenter.jumpToBookMarkChapter(userId, bookId, chapterId, bookMark);
        }
    }

    private void initAdUpload() {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        mPresenter.setAddAdvertLog(userId, bookId, 0, 1);
    }

    private void loadCsjAd() {
        mAdNative.loadFeedAd(mAdSlot, new TTAdNative.FeedAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.i("hhh", "load error 5 : " + code + ", " + message);
                contentADContainer.removeAllViews();
            }

            @Override
            public void onFeedAdLoad(List<TTFeedAd> ads) {
                Log.i("hhh", "load success 2");
                if (ads == null || ads.size() == 0) {
                    return;
                }
                // mTTAdList.clear();
                mTTAdList.addAll(ads);

                if (null != mTTAdList && !mTTAdList.isEmpty()) {
                    if (index1 < mTTAdList.size()) {
                        FeedAdBean.getInstance().setFeedAd(mTTAdList.get(index1));
                    } else {
                        FeedAdBean.getInstance().setFeedAd(mTTAdList.get(0));
                    }
                }
            }
        });
    }

    private void showChapterOrderPopupWindow(long bookId, long chapterId, ChapterOrderPopupWindow.OnOrderPayListener onOrderPayListener) {
        if (chapterOrderPopupWindow == null || !chapterOrderPopupWindow.isShowing()) {

            chapterOrderPopupWindow = new ChapterOrderPopupWindow(mActivity, bookId, chapterId);
            chapterOrderPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    // initWindow();
                }
            });
            chapterOrderPopupWindow.setOnOrderPayListener(onOrderPayListener);

            chapterOrderPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        }
    }

    private void showMenuLayout() {
        setStatusBarColor(android.R.color.black);
        tvBookName.setText(mPageFlipView.getCurrentPageData().bookname);
        tvBookName.setSelected(true);
        int progress = mPageFlipView.getProgress();
        mSeekBar.setProgress(progress);
        readHeadLayout.setVisibility(View.VISIBLE);
        readSettingLayout.setVisibility(View.VISIBLE);
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
                        long userId = UserInfoManager.getInstance(mContext).getUserid();
                        mPresenter.sendReadTime(userId, 1);

                        DbManager.getDaoSession(mContext).getChapterInfoDao().deleteAll();
                        bookLoadPresenter.getChapter(userId, bookId, mAdVideoChapterId, mType);
                    }

                    //视频播放完成回调
                    @Override
                    public void onVideoComplete() {
                        Log.e("hhh", "rewardVideoAd complete");
                        long userId = UserInfoManager.getInstance(mContext).getUserid();
                        if (userId == 0) {
                            mPresenter.sendOfflineReadTime(1);
                        } else {
                            mPresenter.sendReadTime(userId, 1);
                        }

                        DbManager.getDaoSession(mContext).getChapterInfoDao().deleteAll();
                        bookLoadPresenter.getChapter(userId, bookId, mAdVideoChapterId, mType);
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

    private void showProgressToast(int progress, String chapterName) {
        if (mProgressToast == null) {
            mProgressToast = new Toast(mContext);
            View view = getLayoutInflater().inflate(R.layout.read_progress_toast, null);
            TextView tvChapterName = view.findViewById(R.id.tvChapterName);
            TextView tvProgress = view.findViewById(R.id.tvProgress);
            if (!TextUtils.isEmpty(chapterName)) {
                tvChapterName.setText(chapterName);
            }
            tvProgress.setText(String.valueOf(progress) + "%");
            mProgressToast.setGravity(Gravity.CENTER, 12, 20);//setGravity用来设置Toast显示的位置，相当于xml中的android:gravity或android:layout_gravity
            mProgressToast.setDuration(Toast.LENGTH_SHORT);
            mProgressToast.setView(view);
        } else {
            TextView tvProgress = mProgressToast.getView().findViewById(R.id.tvProgress);
            TextView tvChapterName = mProgressToast.getView().findViewById(R.id.tvChapterName);
            if (!TextUtils.isEmpty(chapterName)) {
                tvChapterName.setText(chapterName);
            }
            tvProgress.setText(String.valueOf(progress) + "%");
        }
        mProgressToast.show();
    }

    private void toggleNightMode() {
        if (mPageConfig.dayMode == PageConfig.DayMode.SUN_MODE) {
            tvDayMode.setText(R.string.sun_mode);
            Drawable drawable = getResources().getDrawable(R.drawable.sun);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvDayMode.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        } else {
            tvDayMode.setText(R.string.night_mode);
            Drawable drawable = getResources().getDrawable(R.drawable.moon);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvDayMode.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        }
    }

    private void initMiddleAd() {
        contentADContainer = findViewById(R.id.contentADContainer);
        mPageFlipView.setLayoutView(contentADContainer);

        //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        mAdNative = TTAdManagerHolder.get().createAdNative(this);
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(this);

        mTTAdList = new ArrayList<>();
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        mAdSlot = new AdSlot.Builder()
                .setCodeId(READ_PAGE_ID) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(3) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(350, 0) //期望模板广告view的size,单位dp
                .setImageAcceptedSize(640, 320)
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        try {
            mAdNative.loadFeedAd(mAdSlot, new TTAdNative.FeedAdListener() {
                @Override
                public void onError(int code, String message) {
                    Log.i("hhh", "load error 5 : " + code + ", " + message);
                    contentADContainer.removeAllViews();
                }

                @Override
                public void onFeedAdLoad(List<TTFeedAd> ads) {
                    Log.i("hhh", "load success 1");
                    if (ads == null || ads.size() == 0) {
                        return;
                    }
                    mTTAdList.clear();
                    mTTAdList.addAll(ads);

                    if (null != mTTAdList && !mTTAdList.isEmpty()) {
                        FeedAdBean.getInstance().setFeedAd(mTTAdList.get(0));
                    }
                }
            });
        } catch (Exception e) {
            Log.e("hhh", "Exception");
        }
    }

    private void initBottomAd() {
        bottomADContainer = findViewById(R.id.bottomADContainer);

        bottomADContainer.removeAllViews();

        //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        TTAdNative adNative = TTAdManagerHolder.get().createAdNative(this);
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(this);

        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(ADConfig.CSJAdPositionId.READ_PAGE_BOTTOM_ID) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(350, 0) //期望模板广告view的size,单位dp
                .setImageAcceptedSize(640, 320)
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        adNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.i("hhh", "load error 1 : " + code + ", " + message);
                bottomADContainer.removeAllViews();
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                TTNativeExpressAd ad = ads.get(0);
                // ad.setSlideIntervalTime(30 * 1000);
                bindAdListener(ad, bottomADContainer);
                ad.render();
            }
        });
    }

    private void bindAdListener(TTNativeExpressAd ad, final FrameLayout frameLayout) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                Log.i("hhh", "广告被点击");
            }

            @Override
            public void onAdShow(View view, int type) {
                Log.i("hhh", "广告展示");
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Log.i("hhh", msg + " code:" + code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                //返回view的宽高 单位 dp
                Log.i("hhh", "渲染成功");
                frameLayout.removeAllViews();
                frameLayout.addView(view);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppStatis.getInstance(mContext).startRead();
        ReadGuide.showGuide(mActivity);
        NewUserReadRemindPopupWindow.showGuide(mActivity);
        readTracer.startRead();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPageFlipView.onResume();
        ReadParameter.getInstance().setResume(true);

        String payResultUrl = AppSetting.getInstance(mContext).getString(SharedKey.PAY_RESULT_URL, "");
        if (payResultUrl.contains("amwx://")) {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(payResultUrl));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }

        if (mAdType == 1) {
            boolean isPaySuccess = PayResult.getInstance().isPaySuccess();
            Log.e("hhh", "isPaySuccess= " + isPaySuccess);
            if (isPaySuccess) {
                AppSetting.getInstance(mContext).putBoolean(AD_FREE, true);
                if (null != mAdPopup && mAdPopup.isShow()) {
                    mAdPopup.dismiss();
                }
                DbManager.getDaoSession(mContext).getChapterInfoDao().deleteAll();
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                if (mAdChapterId != 0) {
                    bookLoadPresenter.getChapter(userId, bookId, mAdChapterId, mType);
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        mPageFlipView.onPause();
        PageData pageData = mPageFlipView.getCurrentPageData();
        if (pageData != null) {
            appSetting.putLong(SharedKey.BOOK_ID, pageData.bookId);
            appSetting.putLong(SharedKey.CHAPTER_ID, pageData.chapterId);
            appSetting.putInt(SharedKey.PAGE_INDEX, pageData.index);
            saveBookReadProgress();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        PageData pageData = mPageFlipView.getCurrentPageData();
        if (pageData != null) {
            AppStatis.getInstance(mContext).endRead(pageData.bookId, pageData.chapterId);
        }
        readTracer.stopRead();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(batteryReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        bookLoadPresenter.detachView();
        addToBookRackPresenter.detachView();
        giftPresenter.detachView();
        bookMarkPresenter.detachView();
        taskAwardPresenter.detachView();
        sharePresenter.detachView();
        mPresenter.detachView();

        if (giftPopupWindow != null && giftPopupWindow.isShowing()) {
            giftPopupWindow.dismiss();
        }
        if (bookContentsPopupWindow != null && bookContentsPopupWindow.isVisible()) {
            bookContentsPopupWindow.dismiss();
        }

        // saveBookReadProgress();
        appSetting.putBoolean(SharedKey.IS_READ_ACTIVITY_NORMAL_FINISH, true);
    }

    private void saveBookReadProgress() {
        final PageData pageData = mPageFlipView.getCurrentPageData();
        if (pageData != null) {
            Flowable.just(pageData).map(new Function<PageData, Boolean>() {
                @Override
                public Boolean apply(PageData pageData) throws Exception {
                    BookReadProgress bookReadProgress = new BookReadProgress();
                    bookReadProgress.bookId = pageData.bookId;
                    bookReadProgress.chapterId = pageData.chapterId;
                    boolean isNoAd = AppSetting.getInstance(mContext).getBoolean(IS_NO_AD, false);
                    if (pageData.isADPage && !isNoAd) {
                        bookReadProgress.pageIndex = pageData.index - 1;
                    } else {
                        bookReadProgress.pageIndex = pageData.index;
                    }
                    bookReadProgress.chapterIndex = pageData.chapterIndex;
                    BookReadProgressDao bookReadProgressDao = DbManager.getDaoSession(mContext).getBookReadProgressDao();
                    bookReadProgressDao.insertOrReplace(bookReadProgress);

                    updateBookBrowseHistory(pageData);

                    return true;
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean aBoolean) throws Exception {
                    Intent intent = new Intent(BroadcastAction.REFRESH_BOOK_RACK_ACTION);
                    intent.putExtra("bookId", bookId);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    Log.e("hhh", "saveBookReadProgress ", throwable);
                }
            });
        }
    }

    private void updateBookBrowseHistory(PageData pageData) {
        if (!pageData.isCoverPage && pageData.isADPage) {
            BookBrowseHistoryDao bookBrowseHistoryDao = VisitorDbManager.getDaoSession(mContext).getBookBrowseHistoryDao();
            BookBrowseHistory bookBrowseHistory = bookBrowseHistoryDao.loadByRowId(pageData.bookId);
            bookBrowseHistory.readTime = System.currentTimeMillis();
            bookBrowseHistory.chapternumber = pageData.chapterIndex;
            bookBrowseHistoryDao.update(bookBrowseHistory);
        }
    }

    private void initWindow() {
        if (Build.VERSION.SDK_INT > 16 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            if (Build.VERSION.SDK_INT >= 28) {
                WindowManager.LayoutParams attributes = getWindow().getAttributes();
                attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                getWindow().setAttributes(attributes);
            }
            // 设置页面全屏显示
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                mPageFlipView.showNextPage();
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                mPageFlipView.showBeforePage();
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onClick(View view) {
        final long userid = UserInfoManager.getInstance(mContext).getUserid();
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.ivRed:
                long totalReadTime = readTracer.getTotalReadTime();
                RedTaskPopupWindow redTaskPopupWindow = new RedTaskPopupWindow(mActivity, totalReadTime);
                redTaskPopupWindow.show();
                break;
            case R.id.ivGift:
                hideMenuLayout();
                if (userid == 0) {
                    lunchLoginActivity(FROM_ADD_GIFT);
                } else {
                    giftPresenter.getGiftListConfig(userid);
                }
                break;
            case R.id.ivDian:
                BookOptionPopupWindow bookOptionPopupWindow = new BookOptionPopupWindow(mActivity);
                bookOptionPopupWindow.setCallback(new BookOptionPopupWindow.Callback() {

                    @Override
                    public void addBookMark() {
                        hideMenuLayout();
                        PageData pageData = mPageFlipView.getCurrentPageData();
                        if (pageData != null && pageData.lineList.size() > 0) {
                            Line firstLine = pageData.lineList.get(0);

                            Log.i("hhh", "pageData.index= " + pageData.index);
                            BookMark bookMark = new BookMark();
                            bookMark.markId = bookId + "_" + firstLine.chapterId;
                            bookMark.userid = userid;
                            bookMark.bookid = bookId;
                            //TODO 添加书签时，将当前页码进行保存
                            bookMark.index = (long) pageData.index;
                            bookMark.chapterid = firstLine.chapterId;
                            bookMark.pid = firstLine.paragraphId;
                            bookMark.content = firstLine.text;
                            bookMark.title = pageData.chapterName;
                            bookMark.createtime = TimeUtils.date2String(new Date(), new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()));

                            bookMarkPresenter.addBookMark(bookMark);
                        }
                    }

                    @Override
                    public void toShare() {
                        ivDot.setVisibility(View.GONE);
                        hideMenuLayout();
                        long userId = UserInfoManager.getInstance(mContext).getUserid();
                        if (userId == 0) {
                            lunchLoginActivity(FROM_SHARE);
                        } else {
                            shareImage();
                        }
                    }

                    @Override
                    public void toError() {
                        hideMenuLayout();
                        Intent intent = new Intent(mContext, ErrorFeedbackActivity.class);
                        PageData pageData = mPageFlipView.getCurrentPageData();
                        if (pageData != null) {
                            intent.putExtra("bookid", bookId);
                            intent.putExtra("chapterid", chapterId);
                            intent.putExtra("bookName", pageData.bookname);
                            intent.putExtra("chapterName", pageData.chapterName);
                            startActivity(intent);
                        }
                    }
                });
                bookOptionPopupWindow.showAsDropDown(view, 0, 45);
                break;
            case R.id.tvAddToBookrack:
                addToBookRackPresenter.addToBookRack(userid, bookId, false);
                break;

            case R.id.beforeChapter:
                hideMenuLayout();
                mPageFlipView.jumpToBeforeChapter();
                break;
            case R.id.nextChapter:
                hideMenuLayout();
                mPageFlipView.jumpToNextChapter();
                break;
            case R.id.tvMenu:
                hideMenuLayout();
                showBookContentsPopupWindow();
                break;
            case R.id.tvDayMode:
                changeDayMode();
                break;
            case R.id.tvFontSetting:
                showPageSettingPopupWindow(view);
                break;
            default:
                break;
        }
    }

    private void shareImage() {
        long userId = UserInfoManager.getInstance(mContext).getUserid();

        PageData pageData = mPageFlipView.getCurrentPageData();
        if (pageData != null) {
            Log.e("hhh", "pageData.isVipPage= " + pageData.isVipPage);
            if (pageData.isVipPage) {
                Toast.makeText(this, "该章节为收费章节，不可分享", Toast.LENGTH_SHORT).show();
            } else {
                sharePresenter.getBookShareInfo(userId, bookId, 0);
            }
        } else {
            sharePresenter.getBookShareInfo(userId, bookId, 0);
        }
    }

    private void showBookContentsPopupWindow() {
        long currentChapterId = 0;
        PageData pageData = mPageFlipView.getCurrentPageData();
        if (pageData != null) {
            currentChapterId = pageData.chapterId;
        }

        bookContentsPopupWindow = BookContentsPopupWindow.newInstance(bookId, currentChapterId);
        bookContentsPopupWindow.setOnChapterCheckedListener(new OnChapterCheckedListener() {
            @Override
            public void checkedChapter(Chapter chapter, int type) {
                bookContentsPopupWindow.dismiss();
                showPageIndex = 0;//显示每章的第一页
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                Log.e("hhh", "type1255= " + type);
                bookLoadPresenter.getChapter(userId, bookId, chapter.cid, type);
            }
        });
        bookContentsPopupWindow.setOnBookMarkCheckedLinstener(new OnBookMarkCheckedLinstener() {
            @Override
            public void checkedBookMark(BookMark bookMark) {
                bookContentsPopupWindow.dismiss();
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                bookLoadPresenter.jumpToBookMarkChapter(userId, bookId, bookMark.chapterid, bookMark);
            }
        });

        bookContentsPopupWindow.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // initWindow();
            }
        });

        bookContentsPopupWindow.show(getSupportFragmentManager(), "BookContentsPopupWindow");
    }

    private void changeDayMode() {
        if (mPageConfig.dayMode == PageConfig.DayMode.SUN_MODE) {
            mPageConfig.dayMode = PageConfig.DayMode.NIGHT_MODE;
            mPageConfig.saveDayMode(PageConfig.DayMode.NIGHT_MODE);
            AppSetting.getInstance(mActivity).putInt(SharedKey.ACTIVITY_BRIGHTNESS, PageConfig.DayMode.NIGHT_MODE);
            AppSetting.getInstance(mActivity).putInt(SharedKey.READ_DAY_TPYE, AppConfig.NIGHT_BRIGHTNESS);

            Drawable drawable = getResources().getDrawable(R.drawable.moon);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvDayMode.setCompoundDrawables(null, drawable, null, null);
            tvDayMode.setText(getString(R.string.night_mode));

            AppSetting.getInstance(mActivity).putInt(SharedKey.READ_BG, mPageConfig.getBackgroundColor());

            mPageConfig.saveBackgroundColor(PageConfig.BackgroundColor.NIGHT_COLOR);
            baColor = PageConfig.BackgroundColor.NIGHT_COLOR;
            setStatusBarColor(baColor);
            mLayoutReadContent.setBackgroundResource(baColor);
            mPageFlipView.updateConfig();
            textBookParser.updateConfig();

        } else {
            mPageConfig.dayMode = PageConfig.DayMode.SUN_MODE;
            mPageConfig.saveDayMode(PageConfig.DayMode.SUN_MODE);
            AppSetting.getInstance(mActivity).putInt(SharedKey.ACTIVITY_BRIGHTNESS, PageConfig.DayMode.SUN_MODE);
            AppSetting.getInstance(mActivity).putInt(SharedKey.ACTIVITY_BRIGHTNESS, AppConfig.SUN_BRIGHTNESS);

            Drawable drawable = getResources().getDrawable(R.drawable.sun);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvDayMode.setCompoundDrawables(null, drawable, null, null);
            tvDayMode.setText(getString(R.string.sun_mode));

            baColor = AppSetting.getInstance(mActivity).getInt(SharedKey.READ_BG, PageConfig.BackgroundColor.COLOR_5);
            mPageConfig.saveBackgroundColor(baColor);
            setStatusBarColor(baColor);
            mLayoutReadContent.setBackgroundResource(baColor);
            mPageFlipView.updateConfig();
            textBookParser.updateConfig();
        }
    }

    private void showPageSettingPopupWindow(View view) {
        PageSettingPopupWindow pageSettingPopupWindow = new PageSettingPopupWindow(this);
        pageSettingPopupWindow.setSettingCallback(new PageSettingPopupWindow.SettingCallback() {

            @Override
            public void setActivityLight(int light) {
                ScreenUtils.setActivityLight(mActivity, light);
                mPageConfig.saveSceenLight(light);
                mPageFlipView.updateConfig();
                textBookParser.updateConfig();
            }

            @Override
            public void setBackgroud(int color) {
                if (mPageConfig.dayMode == PageConfig.DayMode.NIGHT_MODE) {
                    changeDayMode();
                }
                setStatusBarColor(color);
                mLayoutReadContent.setBackgroundResource(color);
                mPageConfig.saveBackgroundColor(color);
                mPageFlipView.updateConfig();
                textBookParser.updateConfig();
            }

            @Override
            public void setLineSpace(int space) {
                mPageConfig.saveLineSpace(space);
                textBookParser.updateConfig();
                mPageFlipView.resetPage();
            }

            @Override
            public void setFontSize(int size) {
                mPageConfig.saveFontSize(size);
                textBookParser.updateConfig();
                mPageFlipView.resetPage();
            }
        });
        pageSettingPopupWindow.initConfig(mPageConfig);
        pageSettingPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Log.i("hhh", "onDismiss: ");
                // initWindow();
            }
        });

        pageSettingPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void getGiftListConfigSuccess(GetGiftListConfigResponse getGiftListConfigResponse) {
        giftPopupWindow = new GiftPopupWindow(mActivity);
        giftPopupWindow.setGiftList(getGiftListConfigResponse.data);
        giftPopupWindow.setCurrentMoney(getGiftListConfigResponse.readmoney);
        giftPopupWindow.setOnPayTourListener(new GiftPopupWindow.OnPayTourListener() {
            @Override
            public void onPayTour(GetGiftListConfigResponse.GiftBean giftBean) {
                int userId = UserInfoManager.getInstance(mActivity).getUserid();
                giftPresenter.giveGift(userId, giftBean.propid, 1, bookId);
            }

            @Override
            public void showTopUpActivity() {
                Intent intent = new Intent(mActivity, PayCenterActivity.class);
                startActivityForResult(intent, TOP_UP_FOR_GIFT_REQUEST_CODE);
            }
        });
        giftPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void giveGiftSuccess(GiveGiftResponse giveGiftResponse) {
        showToast(getString(R.string.give_gift_success));
        if (giftPopupWindow != null && giftPopupWindow.isShowing()) {
            giftPopupWindow.dismiss();
        }
        updateUserInfo(giveGiftResponse.readmoney);
        if (giveGiftResponse.receive == 0) {
            long userId = UserInfoManager.getInstance(mContext).getUserid();
            taskAwardPresenter.getTaskAward(userId, TaskId.PLAY_TOUR, false);
        }
    }

    @Override
    public void getReceivedGiftListSuccess(ReceivedGiftResponse receivedGiftResponse) {

    }

    private void updateUserInfo(final long readMoney) {
        singleThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                UserInfoManager userInfoManager = UserInfoManager.getInstance(mContext);
                UserInfo userInfo = userInfoManager.getUserInfo();
                userInfo.money = readMoney;
                userInfoManager.saveUserInfo(userInfo);
            }
        });
    }

    @Override
    public void getChapterSuccess(final int advertOpen, final ChapterInfo chapterInfo, final List<BookParagraph> bookParagraphList, final int type) {
        dismissLoading();
        ChapterInfoDao chapterInfoDao = DbManager.getDaoSession(mContext).getChapterInfoDao();
        chapterInfo.advertopen = advertOpen;
        chapterInfoDao.insertOrReplace(chapterInfo);
        Log.e("hhh", "getChapterSuccess");
        if (chapterInfo.bookshelves == 0) {
            hasAddToBookRack = false;
            tvAddToBookRack.setVisibility(View.VISIBLE);
            tvAddToBookRack.setOnClickListener(this);
        } else {
            hasAddToBookRack = true;
            tvAddToBookRack.setVisibility(View.GONE);
        }

        textBookParser.parseChapter(chapterInfo, bookParagraphList, new ChapterParserListener() {
            @Override
            public void parseFinish(List<PageData> list) {
                Log.e("hhh", "type1434= " + type);
                renderAdapter.addChapterPage(list, showPageIndex, type);
            }
        });

        if (chapterInfo.popopen == 1) {
            mChapterInfo = chapterInfo;
            loadVideoAd("921459511", TTAdConstant.VERTICAL, chapterInfo.cid, 1);
            mAdPopup = new AdPopupWindow(this);
            new XPopup.Builder(this)
                    .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                    .dismissOnBackPressed(false)
                    .dismissOnTouchOutside(false)
                    .asCustom(mAdPopup)
                    .show();
            mAdPopup.setRewardListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdChapterId = chapterInfo.cid;
                    mAdType = 1;
                    mAdPopup.dismiss();
                    getPayMethod();
                }
            });
            mAdPopup.setVideoClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdType = 2;
                    if (mttRewardVideoAd != null) {
                        //step6:在获取到广告后展示
                        mttRewardVideoAd.showRewardVideoAd(ReadActivity.this);
                        mttRewardVideoAd = null;
                        mAdPopup.dismiss();
                    } else {
                        Log.e("hhh", "请先加载广告");
                        Toast.makeText(ReadActivity.this, "请先加载广告", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void loadVideoAd(String codeId, int orientation, final long chapterId, final int type) {
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
                        long userId = UserInfoManager.getInstance(mContext).getUserid();
                        if (type == 1) {
                            mPresenter.setAddAdvertLog(userId, bookId, chapterId, 0);
                        } else {
                            mPresenter.setReceiveLog(userId, mTaskLogId);
                        }
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

    private void getPayMethod() {
        mPresenter.getPayMethods();
    }

    @Override
    public void getNextChapterSuccess(final int advertOpen, final ChapterInfo chapterInfo, final List<BookParagraph> bookParagraphList) {
        readTracer.flipPage();
        dismissLoading();
        Log.e("hhh", "getNextChapterSuccess");
        ChapterInfoDao chapterInfoDao = DbManager.getDaoSession(mContext).getChapterInfoDao();
        chapterInfo.advertopen = advertOpen;
        chapterInfoDao.insertOrReplace(chapterInfo);
        if (chapterInfo.bookshelves == 0) {
            hasAddToBookRack = false;
            tvAddToBookRack.setVisibility(View.VISIBLE);
            tvAddToBookRack.setOnClickListener(this);
        } else {
            hasAddToBookRack = true;
            tvAddToBookRack.setVisibility(View.GONE);
        }
        textBookParser.parseChapter(chapterInfo, bookParagraphList, new ChapterParserListener() {
            @Override
            public void parseFinish(List<PageData> list) {
                renderAdapter.addNextChapterPage(list);
            }
        });

        if (chapterInfo.popopen == 1) {
            mChapterInfo = chapterInfo;
            loadVideoAd("921459511", TTAdConstant.VERTICAL, chapterInfo.cid, 1);
            mAdPopup = new AdPopupWindow(this);
            new XPopup.Builder(this)
                    .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                    .dismissOnBackPressed(false)
                    .dismissOnTouchOutside(false)
                    .asCustom(mAdPopup)
                    .show();
            mAdPopup.setRewardListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdChapterId = chapterInfo.cid;
                    mAdType = 1;
                    mAdPopup.dismiss();
                    getPayMethod();
                }
            });
            mAdPopup.setVideoClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mttRewardVideoAd != null) {
                        //step6:在获取到广告后展示
                        mttRewardVideoAd.showRewardVideoAd(ReadActivity.this);
                        mttRewardVideoAd = null;
                        mAdPopup.dismiss();
                    } else {
                        Log.e("hhh", "请先加载广告");
                        Toast.makeText(ReadActivity.this, "请先加载广告", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void getBeforeChapterSuccess(final int advertOpen, final ChapterInfo chapterInfo, final List<BookParagraph> bookParagraphList) {
        readTracer.flipPage();
        dismissLoading();
        Log.e("hhh", "getBeforeChapterSuccess");
        ChapterInfoDao chapterInfoDao = DbManager.getDaoSession(mContext).getChapterInfoDao();
        chapterInfo.advertopen = advertOpen;
        chapterInfoDao.insertOrReplace(chapterInfo);
        if (chapterInfo.bookshelves == 0) {
            hasAddToBookRack = false;
            tvAddToBookRack.setVisibility(View.VISIBLE);
            tvAddToBookRack.setOnClickListener(this);
        } else {
            hasAddToBookRack = true;
            tvAddToBookRack.setVisibility(View.GONE);
        }

        textBookParser.parseChapter(chapterInfo, bookParagraphList, new ChapterParserListener() {
            @Override
            public void parseFinish(List<PageData> list) {
                renderAdapter.addBeforeChapterPage(list);
            }
        });

        if (chapterInfo.popopen == 1) {
            mChapterInfo = chapterInfo;
            loadVideoAd("921459511", TTAdConstant.VERTICAL, chapterInfo.cid, 1);
            mAdPopup = new AdPopupWindow(this);
            new XPopup.Builder(this)
                    .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                    .dismissOnBackPressed(false)
                    .dismissOnTouchOutside(false)
                    .asCustom(mAdPopup)
                    .show();
            mAdPopup.setRewardListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdChapterId = chapterInfo.cid;
                    mAdType = 1;
                    mAdPopup.dismiss();
                    getPayMethod();
                }
            });
            mAdPopup.setVideoClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mttRewardVideoAd != null) {
                        //step6:在获取到广告后展示
                        mttRewardVideoAd.showRewardVideoAd(ReadActivity.this);
                        mttRewardVideoAd = null;
                        mAdPopup.dismiss();
                    } else {
                        Log.e("hhh", "请先加载广告");
                        Toast.makeText(ReadActivity.this, "请先加载广告", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void getBookMarkChapterSuccess(final int advertOpen, final ChapterInfo chapterInfo, final List<BookParagraph> bookParagraphList, final BookMark bookMark) {
        readTracer.flipPage();
        dismissLoading();
        ChapterInfoDao chapterInfoDao = DbManager.getDaoSession(mContext).getChapterInfoDao();
        chapterInfo.advertopen = advertOpen;
        chapterInfoDao.insertOrReplace(chapterInfo);
        if (chapterInfo.bookshelves == 0) {
            tvAddToBookRack.setVisibility(View.VISIBLE);
            tvAddToBookRack.setOnClickListener(this);
        } else {
            tvAddToBookRack.setVisibility(View.GONE);
        }

        textBookParser.parseChapter(chapterInfo, bookParagraphList, new ChapterParserListener() {
            @Override
            public void parseFinish(List<PageData> list) {
//                renderAdapter.addNextChapterPage(list);
                // TODO 书签跳转
                Log.i("hhh", "bookMark.index= " + (bookMark.index));
                int index = Long.valueOf(bookMark.index).intValue();
                renderAdapter.addChapterPage(list, index, 0);
            }
        });

        if (chapterInfo.popopen == 1) {
            mChapterInfo = chapterInfo;
            loadVideoAd("921459511", TTAdConstant.VERTICAL, chapterInfo.cid, 1);
            mAdPopup = new AdPopupWindow(this);
            new XPopup.Builder(this)
                    .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                    .dismissOnBackPressed(false)
                    .dismissOnTouchOutside(false)
                    .asCustom(mAdPopup)
                    .show();
            mAdPopup.setRewardListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdChapterId = chapterInfo.cid;
                    mAdType = 1;
                    mAdPopup.dismiss();
                    getPayMethod();
                }
            });
            mAdPopup.setVideoClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mttRewardVideoAd != null) {
                        //step6:在获取到广告后展示
                        mttRewardVideoAd.showRewardVideoAd(ReadActivity.this);
                        mttRewardVideoAd = null;
                        mAdPopup.dismiss();
                    } else {
                        Log.e("hhh", "请先加载广告");
                        Toast.makeText(ReadActivity.this, "请先加载广告", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void addToBookRackSuccess(boolean finishActivity, long bookId, int receive) {
        tvAddToBookRack.setVisibility(View.GONE);
        hasAddToBookRack = true;
        hideMenuLayout();
        showToast(getString(R.string.add_to_bookrack_success));
        Intent intent = new Intent(BroadcastAction.ADD_BOOK_TO_RACK_ACTION);
        intent.putExtra("bookId", bookId);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        ChapterInfoDao chapterInfoDao = DbManager.getDaoSession(mContext).getChapterInfoDao();
        List<ChapterInfo> list = chapterInfoDao.queryBuilder().where(ChapterInfoDao.Properties.Bookid.eq(bookId)).list();
        for (ChapterInfo chapterInfo : list) {
            chapterInfo.bookshelves = 1;
            chapterInfoDao.update(chapterInfo);
        }

        BookBrowseHistoryDao bookBrowseHistoryDao = VisitorDbManager.getDaoSession(mContext).getBookBrowseHistoryDao();
        List<BookBrowseHistory> list1 = bookBrowseHistoryDao.queryBuilder().where(BookBrowseHistoryDao.Properties.Bookid.eq(bookId)).list();

        for (BookBrowseHistory bookBrowseHistory : list1) {
            bookBrowseHistory.bookshelves = 1;
            bookBrowseHistoryDao.update(bookBrowseHistory);
        }

        if (receive == 0) {
            long userid = UserInfoManager.getInstance(mContext).getUserid();
            taskAwardPresenter.getTaskAward(userid, TaskId.ADD_BOOK_TO_RACK, true);
        }

        if (finishActivity) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Tencent.onActivityResultData(requestCode, resultCode, data, null);

        if (requestCode == TOP_UP_FOR_GIFT_REQUEST_CODE) {
            UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
            if (giftPopupWindow != null && giftPopupWindow.isShowing()) {
                double money = userInfo.money + userInfo.coin;
                giftPopupWindow.setCurrentMoney(money);
            }
        } else if (requestCode == LUNCH_LOGIN_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                readTracer.updateReadTracer();
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                int formCode = data.getIntExtra(SharedKey.FROM_CODE, 0);
                if (formCode == FROM_ADD_TO_BOOK_RACK) {
                    hasAddToBookRack = true;
                    addToBookRackPresenter.addToBookRack(userId, bookId, true);
                } else if (formCode == FROM_READ_PACKAGE) {
                    taskAwardPresenter.getTaskAward(userId, unUserTaskId, false);
                } else if (formCode == FROM_SHARE) {
                    shareImage();
                } else if (formCode == FROM_OPEN_RED_TASK_REQUEST_CODE) {
                    long totalReadTime = readTracer.getTotalReadTime();

                    RedTaskPopupWindow redTaskPopupWindow = new RedTaskPopupWindow(mActivity, totalReadTime);
                    redTaskPopupWindow.show();
                } else if (formCode == FROM_ADD_GIFT) {
                    giftPresenter.getGiftListConfig(userId);
                } else {
                    bookLoadPresenter.getChapter(userId, bookId, mCurrentChapterId, mType);
                }
            }
        } else if (requestCode == TOP_UP_FOR_BUY_CHAPTER_REQUEST_CODE) {
            UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
            if (chapterOrderPopupWindow != null && chapterOrderPopupWindow.isShowing()) {
                chapterOrderPopupWindow.setCurrentMoney(userInfo.money, userInfo.coin);
            }
        }
    }

    @Override
    public void getBookShareInfoSuccess(final ShareResponse shareResponse) {
        final ShareReadImagePreviewPopupWindow shareReadImagePreviewPopupWindow = new ShareReadImagePreviewPopupWindow(mActivity);
        final String path = shareReadImagePreviewPopupWindow.getImagePath();
        shareReadImagePreviewPopupWindow.setOnDownloadClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = ImageUtils.saveImageToAlbum(mContext, path);
                if (success) {
                    showToast("图片已保存至相册");
                } else {
                    showToast("图片保存失败");
                }
            }
        });

        shareReadImagePreviewPopupWindow.setOnShareClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSharePopupWindow(shareResponse, path);
                shareReadImagePreviewPopupWindow.dismiss();
            }
        });
        shareReadImagePreviewPopupWindow.setShareResponse(shareResponse);

        PageData pageData = mPageFlipView.getCurrentPageData();
        shareReadImagePreviewPopupWindow.setChapterInfo(pageData.chapterName, pageData.bookId, pageData.chapterId);
        shareReadImagePreviewPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    private void showSharePopupWindow(final ShareResponse shareResponse, final String path) {
        final SharePopupWindow sharePopupWindow = new SharePopupWindow(mActivity, 0);
        sharePopupWindow.setOnShareListener(new SharePopupWindow.OnShareListener() {
            @Override
            public void onShareToWeChat() {
                WeChatSharer.shareImageToWeChat(mContext, path);
            }

            @Override
            public void onShareToFriendCircle() {
                WeChatSharer.shareImageToFriendCircle(mContext, path);
            }

            @Override
            public void onShareQq() {
                QQSharer.shareImageToQQFriend(mActivity, path, new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        showToast("分享成功");
                        long userId = UserInfoManager.getInstance(mContext).getUserid();
                        taskAwardPresenter.getTaskAward(userId, TaskId.SHARE_CHAPTER, true);
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
                QQSharer.shareImageToQzone(mActivity, shareResponse.linkurl, shareResponse.titile, shareResponse.content, path, new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        showToast("分享成功");
                        long userId = UserInfoManager.getInstance(mContext).getUserid();
                        taskAwardPresenter.getTaskAward(userId, TaskId.SHARE_CHAPTER, true);
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
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            boolean b = mPageFlipView.handleClick(event.getX(), event.getY());
            if (!b) {
                try {
                    mPageFlipView.onFingerUp(event.getX(), event.getY());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        } else {
            boolean b = mGestureDetector.onTouchEvent(event);
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (mTTAdList.size() == 0) {
                    return b;
                }
                PageData pageData = mPageFlipView.getCurrentPageData();
                if (pageData != null && (pageData.isADPage || pageData.hasAdPage)) {
                    contentADContainer.setVisibility(View.GONE);
                    contentADContainer.removeAllViews();

                    if (index1 < mTTAdList.size()) {
                        index1++;
                    } else {
                        index1 = 0;
                    }
                }
            }
            return b;
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        try {
            mPageFlipView.onFingerDown(e.getX(), e.getY());
        } catch (Exception e1) {
            Log.e("hhh", "exception= " + e1);
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        mPageFlipView.onFingerMove(e2.getX(), e2.getY());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void addBookMarkSuccess() {
        showToast(getString(R.string.add_bookmark_success));
    }

    @Override
    public void removeBookMarkSuccess() {

    }

    @Override
    public void getBookMarkListSuccess(List<BookMark> bookMarkList) {

    }

    @Override
    public void getTaskAwardSuccess(TaskAwardResponse taskAwardResponse, int taskId) {
        Log.i("hhh", "getTaskAwardSuccess goldType= " + taskAwardResponse.goldtype);
        if (taskAwardResponse.goldtype == 1) {
            mRedPacketPopupWindow = new ReadRedPacketPopupWindow(mActivity, taskAwardResponse);
            mRedPacketPopupWindow.show();

            mTaskLogId = taskAwardResponse.taskLogId;

            loadVideoAd("921459511", TTAdConstant.VERTICAL, 0, 2);

            mRedPacketPopupWindow.setVideoClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mttRewardVideoAd != null) {
                        //step6:在获取到广告后展示
                        mttRewardVideoAd.showRewardVideoAd(ReadActivity.this);
                        mttRewardVideoAd = null;
                    } else {
                        Log.e("hhh", "请先加载广告");
                        Toast.makeText(ReadActivity.this, "请先加载广告", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            if (taskAwardResponse.number > 0) {
                UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
                userInfo.money = (long) taskAwardResponse.number;
                UserInfoManager.getInstance(mContext).saveUserInfo(userInfo);

                Intent intent = new Intent(BroadcastAction.UPDATE_TASK_LIST);
                intent.putExtra(SharedKey.TASK_ID, taskId);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                TaskRewardPopupWindow taskRewardPopupWindow = new TaskRewardPopupWindow(mActivity);
                taskRewardPopupWindow.show(taskAwardResponse.desc, taskAwardResponse.number + "");
            } else if (taskAwardResponse.regtype == 0) {
                showToast("已经领取");
            }
        }
    }

    @Override
    public void getUserInfoSuccess(UserInfo userInfo) {
        if (userInfo != null && userInfo.userid != 0) {
            UserInfoManager.getInstance(mContext).saveUserInfo(userInfo);
        }
    }

    @Override
    public void getPayMethodsSuccess(GetPayMenthodsResponse response) {
        payPopupWindow = new PayPopupWindow(mActivity);
        payPopupWindow.setPayItemList(response.paydata);
        GetRechargeConfigResponse.RechargeBean currentRechargeBean = new GetRechargeConfigResponse.RechargeBean();
        currentRechargeBean.propid = 1007;
        currentRechargeBean.price = 6;

        payPopupWindow.setRechargeBean(currentRechargeBean);
        payPopupWindow.setOnPayClickListener(new PayPopupWindow.OnPayClickListener() {
            @Override
            public void onPay(Map<String, Object> map, int payMethod) {
                if (payPopupWindow != null && payPopupWindow.isShowing()) {
                    payPopupWindow.dismiss();
                }
                mPresenter.createWapOrder(map, payMethod);
            }

            @Override
            public void onPay2(String payUrl, Map<String, Object> map, int payMethod) {
                if (payPopupWindow != null && payPopupWindow.isShowing()) {
                    payPopupWindow.dismiss();
                }
                mPresenter.createWapOrder2(payUrl, map, payMethod);
            }
        });
        payPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        payPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                bookLoadPresenter.getChapter(userId, bookId, mAdChapterId, mType);
            }
        });
    }

    @Override
    public void wapCreateOrderSuccess(CreatWapOrderResponse response, int payMethod) {
        mPresenter.redirectRequest(response.skipurl, payMethod);
    }

    @Override
    public void setAddAdvertLogSuccess(long chapterId, int repOrType) {
        DbManager.getDaoSession(mContext).getChapterInfoDao().deleteAll();
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        if (mAdChapterId != 0) {
            bookLoadPresenter.getChapter(userId, bookId, mAdChapterId, mType);
        }
    }

    @Override
    public void setReceiveLogSuccess(TaskAwardResponse response) {
        mRedPacketPopupWindow.updateNumber(response.number);
    }

    @Override
    public void sendReadTimeSuccess(ReadTimeResponse response) {
        if (response.videoOpen == 0) {
            AppSetting.getInstance(mContext).putBoolean(SharedKey.IS_NO_AD, true);
            FeedAdBean.getInstance().setCloseAd(true);
            DbManager.getDaoSession(mContext).getChapterInfoDao().deleteAll();
            long userId = UserInfoManager.getInstance(mContext).getUserid();
            if (mAdVideoChapterId != 0) {
                bookLoadPresenter.getChapter(userId, bookId, mAdVideoChapterId, mType);
            }
        }
    }

    private class ThreadFactoryBuilder implements ThreadFactory {

        private String mNameFormat;

        private ThreadFactoryBuilder() {
        }

        private ThreadFactoryBuilder setNameFormat(String nameFormat) {
            this.mNameFormat = nameFormat;
            return this;
        }

        public ThreadFactoryBuilder build() {
            return this;
        }

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            return new Thread(runnable, mNameFormat + "-Thread-");
        }
    }
}
