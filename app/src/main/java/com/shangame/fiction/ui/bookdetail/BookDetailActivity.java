package com.shangame.fiction.ui.bookdetail;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.ad.ADConfig;
import com.shangame.fiction.ad.TTAdManagerHolder;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.core.manager.Logger;
import com.shangame.fiction.net.response.AdBean;
import com.shangame.fiction.net.response.BookCommentByTypeResponse;
import com.shangame.fiction.net.response.BookDetailCommentResponse;
import com.shangame.fiction.net.response.BookDetailResponse;
import com.shangame.fiction.net.response.CommentReplyResponse;
import com.shangame.fiction.net.response.GetGiftListConfigResponse;
import com.shangame.fiction.net.response.GiveGiftResponse;
import com.shangame.fiction.net.response.ReceivedGiftResponse;
import com.shangame.fiction.net.response.SendCommentResponse;
import com.shangame.fiction.net.response.ShareResponse;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.storage.db.BookBrowseHistoryDao;
import com.shangame.fiction.storage.db.BookReadProgressDao;
import com.shangame.fiction.storage.db.ChapterInfoDao;
import com.shangame.fiction.storage.manager.DbManager;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.manager.UserSetting;
import com.shangame.fiction.storage.manager.VisitorDbManager;
import com.shangame.fiction.storage.model.BookBrowseHistory;
import com.shangame.fiction.storage.model.BookDetail;
import com.shangame.fiction.storage.model.BookReadProgress;
import com.shangame.fiction.storage.model.ChapterInfo;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.bookdetail.comment.CommentActivity;
import com.shangame.fiction.ui.bookdetail.comment.CommentContacts;
import com.shangame.fiction.ui.bookdetail.comment.CommentListAdapter;
import com.shangame.fiction.ui.bookdetail.comment.CommentPresenter;
import com.shangame.fiction.ui.bookdetail.comment.SendCommentActivity;
import com.shangame.fiction.ui.bookdetail.gift.GiftContracts;
import com.shangame.fiction.ui.bookdetail.gift.GiftPopupWindow;
import com.shangame.fiction.ui.bookdetail.gift.GiftPresenter;
import com.shangame.fiction.ui.bookdetail.gift.ReceivedGiftActivity;
import com.shangame.fiction.ui.bookrack.AddToBookRackContacts;
import com.shangame.fiction.ui.bookrack.AddToBookRackPresenter;
import com.shangame.fiction.ui.bookstore.BookListByTypeActivity;
import com.shangame.fiction.ui.bookstore.BookStoreChannel;
import com.shangame.fiction.ui.bookstore.BookStoreType;
import com.shangame.fiction.ui.bookstore.BookWithContentAdapter;
import com.shangame.fiction.ui.bookstore.BookWithTitleAdapter;
import com.shangame.fiction.ui.contents.BookContentsActivity;
import com.shangame.fiction.ui.my.pay.PayCenterActivity;
import com.shangame.fiction.ui.reader.ChapterOrderPopupWindow;
import com.shangame.fiction.ui.reader.ReadActivity;
import com.shangame.fiction.ui.share.QQSharer;
import com.shangame.fiction.ui.share.ShareContracts;
import com.shangame.fiction.ui.share.SharePopupWindow;
import com.shangame.fiction.ui.share.SharePresenter;
import com.shangame.fiction.ui.share.WeChatSharer;
import com.shangame.fiction.ui.task.TaskAwardContacts;
import com.shangame.fiction.ui.task.TaskAwardPresenter;
import com.shangame.fiction.ui.task.TaskId;
import com.shangame.fiction.ui.task.TaskRewardPopupWindow;
import com.shangame.fiction.widget.GiftCarouselSwitcher;
import com.shangame.fiction.widget.RemindFrameLayout;
import com.shangame.fiction.widget.SpaceItemDecoration;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;


/**
 * 书籍详情
 * Create by Speedy on 2018/7/25
 */
public class BookDetailActivity extends BaseActivity implements View.OnClickListener,
        BookDetailContacts.View, CommentContacts.View,
        GiftContracts.View, AddToBookRackContacts.View,
        ShareContracts.View, TaskAwardContacts.View {

    private static final String TAG = "BookDetailActivity";

    private static final int SEND_COMMENT_REQUEST_CODE = 520;
    private static final int TOP_UP_REQUEST_CODE = 503;

    private static final int FROM_DISCOUNTSBUY_CODE = 600;
    private static final int FROM_ADD_TO_BOOK_RACK = 601;
    private static final int FROM_PAYTOUR = 602;
    private static final int FROM_SHARE = 603;
    public long fristChapterId;
    private SmartRefreshLayout smartRefreshLayout;
    private View ivHeadBg;
    private ImageView ivBookCover;
    private TextView tvBookName;
    private TextView tvAuthorName;
    private TextView tvBookType;
    private TextView tvBookstatus;
    private TextView tvWordCount;
    private TextView tvContent;
    private View container;
    private View bottomLayout;
    private TextView tvChapterCount;
    private TextView tvUpdateTime;
    private TextView tvPayTourCount;
    private TextView tvAddToBookrack;
    private TextView tvDiscountsBuy;
    private ImageView ivMore;
    private ImageView ivDot;
    private GiftCarouselSwitcher giftCarouselSwitcher;
    private TextView tvCommentCount;
    private RecyclerView commentListView;
    private CommentListAdapter commentAdapter;
    private TextView tvMustReadTitleName;
    private RecyclerView mustReadRecyclerView;
    private BookWithContentAdapter bookWithContentAdapter;
    private TextView tvTitleName;
    private RecyclerView guessYouLikeGridView;
    private BookWithTitleAdapter bookWithTitleAdapter;
    private BookDetailPresenter bookDetailPresenter;
    private CommentPresenter commentPresenter;
    private long bookid;
    private int clicktype;
    private BookDetailResponse bookDetailResponse;
    private int commentCount;

    private GiftPresenter giftPresenter;
    private GiftPopupWindow giftPopupWindow;

    private AddToBookRackPresenter addToBookRackPresenter;

    private SharePresenter sharePresenter;

    private TaskAwardPresenter taskAwardPresenter;

    private boolean goToReadActivity = false;

    private FrameLayout adContainer;

    private RemindFrameLayout remindFrameLayout;
    private View tvLookAllComment;

    // 头条穿山甲
    private List<TTNativeExpressAd> mTTAdList;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BroadcastAction.SHARE_TO_WECHAT_SUCCESS_ACTION.equals(action) && !goToReadActivity) {
                Toast.makeText(mActivity, getString(R.string.share_success), Toast.LENGTH_SHORT).show();
                long userid = UserInfoManager.getInstance(mContext).getUserid();
                taskAwardPresenter.getTaskAward(userid, TaskId.SHARE_BOOK, true);
            } else if (BroadcastAction.ADD_BOOK_TO_RACK_ACTION.equals(action)) {
                if (intent.hasExtra("bookId")) {
                    bookDetailResponse.bookshelves = 1;
                    tvAddToBookrack.setOnClickListener(null);
                    tvAddToBookrack.setText(R.string.added_to_bookrack);
                    Drawable drawable = getResources().getDrawable(R.drawable.added_bookrack);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    tvAddToBookrack.setCompoundDrawables(drawable, null, null, null);
                }
            }
        }
    };

    public static void lunchActivity(Activity activity, long bookid, int clicktype) {
        Intent intent = new Intent(activity, BookDetailActivity.class);
        intent.putExtra("bookid", bookid);
        intent.putExtra("clicktype", clicktype);
        activity.startActivity(intent);
    }

    public static void lunchActivity(Context context, long bookid, int clicktype) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra("bookid", bookid);
        intent.putExtra("clicktype", clicktype);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        initPresenter();
        initSmartRefreshLayout();
        initParam();
        initView();
        initTextSwitcher();
        int verify = AdBean.getInstance().getVerify();
        if (verify == 0) {
            initCsjAd();
        }
        initReceiver();
        smartRefreshLayout.autoRefresh();
    }

    private void initPresenter() {
        bookDetailPresenter = new BookDetailPresenter();
        bookDetailPresenter.attachView(this);

        commentPresenter = new CommentPresenter();
        commentPresenter.attachView(this);

        giftPresenter = new GiftPresenter();
        giftPresenter.attachView(this);

        addToBookRackPresenter = new AddToBookRackPresenter();
        addToBookRackPresenter.attachView(this);

        sharePresenter = new SharePresenter();
        sharePresenter.attachView(this);

        taskAwardPresenter = new TaskAwardPresenter();
        taskAwardPresenter.attachView(this);
    }

    private void initSmartRefreshLayout() {
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                long userid = UserInfoManager.getInstance(mContext).getUserid();
                loadBookDetail(userid);
                loadComment(userid);
                loadGift();
            }
        });
    }

    private void initParam() {
        bookid = getIntent().getLongExtra("bookid", 0);
        clicktype = getIntent().getIntExtra("clicktype", 0);
    }

    private void initView() {
        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        findViewById(R.id.ivShare).setOnClickListener(this);

        container = findViewById(R.id.container);

        adContainer = findViewById(R.id.adContainer);
        bottomLayout = findViewById(R.id.bottomLayout);

        ivHeadBg = findViewById(R.id.ivHeadBg);
        ivBookCover = findViewById(R.id.ivBookCover);
        tvBookName = findViewById(R.id.tvBookName);
        tvAuthorName = findViewById(R.id.tvAuthorName);
        tvBookType = findViewById(R.id.tvBookType);
        tvBookstatus = findViewById(R.id.tvBookstatus);
        tvWordCount = findViewById(R.id.tvWordCount);
        ivMore = findViewById(R.id.ivMore);
        ivDot = findViewById(R.id.ivDot);
        ivMore.setOnClickListener(this);

        tvContent = findViewById(R.id.tvContent);
        findViewById(R.id.menuLayout).setOnClickListener(this);

        tvChapterCount = findViewById(R.id.tvChapterCount);
        tvUpdateTime = findViewById(R.id.tvUpdateTime);

        tvPayTourCount = findViewById(R.id.tvPayTourCount);
        tvPayTourCount.setOnClickListener(this);

        findViewById(R.id.commentTitleLayout).setOnClickListener(this);
        tvCommentCount = findViewById(R.id.tvCommentCount);
        commentListView = findViewById(R.id.commentListView);
        commentListView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        remindFrameLayout = findViewById(R.id.remindFrameLayout);

        commentAdapter = new CommentListAdapter(mActivity, commentPresenter, bookid);
        commentAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                if (commentAdapter.getItemCount() > 0) {
                    remindFrameLayout.showContentView();
                    tvLookAllComment.setVisibility(View.VISIBLE);
                } else {
                    remindFrameLayout.showRemindView();
                    tvLookAllComment.setVisibility(View.GONE);
                }
            }
        });
        commentListView.setAdapter(commentAdapter);

        findViewById(R.id.payTourLayout).setOnClickListener(this);
        findViewById(R.id.tvWriteComment).setOnClickListener(this);
        tvLookAllComment = findViewById(R.id.tvLookAllComment);
        tvLookAllComment.setOnClickListener(this);
        findViewById(R.id.tvGuessYouLikeMore).setOnClickListener(this);

        DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration2.setDrawable(getResources().getDrawable(R.drawable.divider_empty));

        tvMustReadTitleName = findViewById(R.id.tvMustReadTitleName);
        tvMustReadTitleName.setText(R.string.recommend_book);
        mustReadRecyclerView = findViewById(R.id.mustReadRecyclerView);
        mustReadRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mustReadRecyclerView.addItemDecoration(new SpaceItemDecoration(35));
        bookWithContentAdapter = new BookWithContentAdapter(mActivity);
        mustReadRecyclerView.setAdapter(bookWithContentAdapter);

        tvTitleName = findViewById(R.id.tvTitleName);
        guessYouLikeGridView = findViewById(R.id.guessYouLikeGridView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        guessYouLikeGridView.setLayoutManager(gridLayoutManager);
        guessYouLikeGridView.addItemDecoration(new SpaceItemDecoration(35));
        bookWithTitleAdapter = new BookWithTitleAdapter(mActivity);
        guessYouLikeGridView.setAdapter(bookWithTitleAdapter);

        findViewById(R.id.tvDiscountsBuy).setOnClickListener(this);
        findViewById(R.id.tvStartRead).setOnClickListener(this);

        tvAddToBookrack = findViewById(R.id.tvAddToBookrack);
        tvAddToBookrack.setOnClickListener(this);

        UserSetting setting = UserSetting.getInstance(mContext);
        boolean hasShare = setting.getBoolean(SharedKey.HAS_SHARE_BOOK_INTRO, false);
        if (hasShare) {
            ivDot.setVisibility(View.GONE);
        } else {
            ivDot.setVisibility(View.VISIBLE);
        }

        tvDiscountsBuy = findViewById(R.id.tvDiscountsBuy);
        UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
        if (userInfo.newvip == 1) {
            tvDiscountsBuy.setText("免费畅读");
            tvDiscountsBuy.setEnabled(false);
        }
    }

    private void initTextSwitcher() {
        giftCarouselSwitcher = findViewById(R.id.textSwitcher);
        giftCarouselSwitcher.setOnClickListener(this);

        giftCarouselSwitcher.setInAnimation(this, R.anim.slide_top_in);
        giftCarouselSwitcher.setOutAnimation(this, R.anim.slide_bootom_out);
    }

    private void initCsjAd() {
        adContainer = findViewById(R.id.adContainer);

        initCsjAd(adContainer, ADConfig.CSJAdPositionId.READ_DETAIL_ID);
    }

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.SHARE_TO_WECHAT_SUCCESS_ACTION);
        intentFilter.addAction(BroadcastAction.ADD_BOOK_TO_RACK_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, intentFilter);
    }

    private void loadBookDetail(long userid) {
        bookDetailPresenter.getBookDetail(userid, bookid, clicktype);
    }

    private void loadComment(long userid) {
        commentPresenter.getBookComment(userid, bookid);
    }

    private void loadGift() {
        giftPresenter.getReceivedGiftList(bookid, 1, 10);
    }

    private void initCsjAd(final FrameLayout frameLayout, String codeId) {
        frameLayout.removeAllViews();

        //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        TTAdNative adNative = TTAdManagerHolder.get().createAdNative(this);
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(this);

        mTTAdList = new ArrayList<>();
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
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
                frameLayout.removeAllViews();
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                TTNativeExpressAd mTTAd = ads.get(0);
                mTTAdList.add(mTTAd);
                // mTTAd.setSlideIntervalTime(30 * 1000);
                bindAdListener(mTTAd, frameLayout);
                mTTAd.render();
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
        //dislike设置
        bindDislike(ad, frameLayout);
    }

    /**
     * 设置广告的不喜欢, 注意：强烈建议设置该逻辑，如果不设置dislike处理逻辑，则模板广告中的 dislike区域不响应dislike事件。
     *
     * @param ad
     */
    private void bindDislike(TTNativeExpressAd ad, final FrameLayout frameLayout) {
        //使用默认模板中默认dislike弹出样式
        ad.setDislikeCallback(this, new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onSelected(int position, String value) {
                Log.i("hhh", "点击 " + value);
                //用户选择不喜欢原因后，移除广告展示
                frameLayout.removeAllViews();
            }

            @Override
            public void onCancel() {
                Log.i("hhh", "点击取消");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        bookDetailPresenter.detachView();
        commentPresenter.detachView();
        giftPresenter.detachView();
        addToBookRackPresenter.detachView();
        giftCarouselSwitcher.stopLooping();

        sharePresenter.detachView();
        taskAwardPresenter.detachView();
    }

    @Override
    public void dismissLoading() {
        super.dismissLoading();
        smartRefreshLayout.finishRefresh();
    }

    @Override
    public void showError(Throwable throwable) {
        super.showError(throwable);
        smartRefreshLayout.finishRefresh();
        finish();
    }

    @Override
    public void onClick(View view) {
        long userid = UserInfoManager.getInstance(mContext).getUserid();
        switch (view.getId()) {
            case R.id.ivPublicBack:
                finish();
                break;
            case R.id.ivShare:
                if (userid == 0) {
                    super.lunchLoginActivity(FROM_SHARE);
                } else {
                    shareBook();
                }
                break;
            case R.id.commentTitleLayout:
                CommentActivity.lunchActivity(mActivity, bookDetailResponse.detailsdata, commentCount);
                break;
            case R.id.menuLayout:
                Intent intent = new Intent(mContext, BookContentsActivity.class);
                intent.putExtra("bookid", bookid);
                startActivity(intent);
                break;
            case R.id.ivMore:
                if (tvContent.getMaxLines() == 4) {
                    tvContent.setMaxLines(20);
                    ivMore.setImageResource(R.drawable.up_icon);
                } else {
                    tvContent.setMaxLines(4);
                    ivMore.setImageResource(R.drawable.down_icon);
                }
                break;
            case R.id.payTourLayout:
                if (userid == 0) {
                    lunchLoginActivity(FROM_PAYTOUR);
                } else {
                    giftPresenter.getGiftListConfig(userid);
                }
                break;
            case R.id.textSwitcher:
                Intent intent1 = new Intent(mActivity, ReceivedGiftActivity.class);
                intent1.putExtra("bookid", bookid);
                startActivity(intent1);
                break;
            case R.id.tvWriteComment:
                SendCommentActivity.lunchActivityForResult(mActivity, bookid, 0, 0, SEND_COMMENT_REQUEST_CODE);
                break;
            case R.id.tvLookAllComment:
                CommentActivity.lunchActivity(mActivity, bookDetailResponse.detailsdata, commentCount);
                break;
            case R.id.tvGuessYouLikeMore:
                BookListByTypeActivity.lunchActivity(mActivity, BookStoreType.GuessYouLike, "同类作品推荐", BookStoreChannel.All);
                break;
            case R.id.tvDiscountsBuy:
                userid = UserInfoManager.getInstance(mContext).getUserid();
                if (userid == 0) {
                    super.lunchLoginActivity(FROM_DISCOUNTSBUY_CODE);
                } else {
                    discountsBuy();
                }
                break;
            case R.id.tvStartRead:
                BookReadProgressDao bookReadProgressDao = DbManager.getDaoSession(mContext).getBookReadProgressDao();
                List<BookReadProgress> bookReadProgressList = bookReadProgressDao.queryBuilder().where(BookReadProgressDao.Properties.BookId.eq(bookid)).build().list();

                goToReadActivity = true;
                if (bookReadProgressList != null && bookReadProgressList.size() > 0) {
                    BookReadProgress bookReadProgress = bookReadProgressList.get(0);
                    int pageIndex = bookReadProgress.pageIndex;
                    ReadActivity.lunchActivity(mActivity, bookReadProgress.bookId, bookReadProgress.chapterId, pageIndex, 1);
                } else {
                    ReadActivity.lunchActivity(mActivity, bookid, fristChapterId);
                }
                saveReadHistory();
                break;
            case R.id.tvAddToBookrack:
                if (userid == 0) {
                    super.lunchLoginActivity(FROM_ADD_TO_BOOK_RACK);
                } else {
                    addToBookRackPresenter.addToBookRack(userid, bookid, false);
                }
                break;
            default:
                break;
        }
    }

    private void shareBook() {
        ivDot.setVisibility(View.GONE);
        UserSetting setting = UserSetting.getInstance(mContext);
        setting.putBoolean(SharedKey.HAS_SHARE_BOOK_INTRO, true);
        goToReadActivity = false;

        long userid = UserInfoManager.getInstance(mContext).getUserid();
        sharePresenter.getBookShareInfo(userid, bookid, 0);
    }

    private void discountsBuy() {
        ChapterInfo chapterInfo = new ChapterInfo();
        chapterInfo.bookid = bookid;
        ChapterOrderPopupWindow chapterOrderPopupWindow = new ChapterOrderPopupWindow(mActivity, chapterInfo.bookid, chapterInfo.cid);
        chapterOrderPopupWindow.setOnOrderPayListener(new ChapterOrderPopupWindow.OnOrderPayListener() {
            @Override
            public void onPaySuccess() {
                showToast(mActivity.getString(R.string.book_order_success));
            }

            @Override
            public void onCancelPay() {
                showToast(getString(R.string.cancel_pay));
            }
        });

        chapterOrderPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    private void saveReadHistory() {
        if (bookDetailResponse != null) {
            BookBrowseHistory bookBrowseHistory = new BookBrowseHistory();
            BookDetail bookDetail = bookDetailResponse.detailsdata;
            bookBrowseHistory.bookid = bookDetail.bookid;
            bookBrowseHistory.bookname = bookDetail.bookname;
            bookBrowseHistory.bookcover = bookDetail.bookcover;
            bookBrowseHistory.bookshelves = bookDetailResponse.bookshelves;
            bookBrowseHistory.readTime = System.currentTimeMillis();

            VisitorDbManager.getDaoSession(mContext).getBookBrowseHistoryDao().insertOrReplace(bookBrowseHistory);
        }
    }

    @Override
    public void getBookDetailSuccess(BookDetailResponse bookDetailResponse) {
        smartRefreshLayout.finishRefresh();
        this.bookDetailResponse = bookDetailResponse;

        ImageLoader.with(mActivity)
                .loadCover(ivBookCover, bookDetailResponse.detailsdata.bookcover);

        final RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.default_cover);
        options.transform(new BlurTransformation(100));

        Glide.with(mActivity)
                .asBitmap()
                .load(bookDetailResponse.detailsdata.bookcover)
                .apply(options)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        ivHeadBg.setBackground(new BitmapDrawable(resource));
                    }
                });

        fristChapterId = bookDetailResponse.fristchapter;
        tvBookName.setText(bookDetailResponse.detailsdata.bookname);

        tvAuthorName.setText(bookDetailResponse.detailsdata.author);
        tvBookType.setText(bookDetailResponse.detailsdata.classname);
        tvBookstatus.setText(bookDetailResponse.detailsdata.serstatus);
        tvWordCount.setText(bookDetailResponse.detailsdata.wordnumbers);

        tvContent.setText(getString(R.string.intro_content, bookDetailResponse.detailsdata.synopsis));
        tvChapterCount.setText(getString(R.string.book_chapter_count, bookDetailResponse.detailsdata.chapternumber));
        tvUpdateTime.setText(bookDetailResponse.detailsdata.lastmodifytimes);

        tvPayTourCount.setText(bookDetailResponse.detailsdata.giftnumbers);

        bookWithTitleAdapter.clear();
        bookWithTitleAdapter.addAll(bookDetailResponse.clickbook);
        bookWithTitleAdapter.notifyDataSetChanged();

        tvMustReadTitleName.setText(bookDetailResponse.detailsdata.classname + " 的书还有");
        bookWithContentAdapter.clear();
        bookWithContentAdapter.addAll(bookDetailResponse.likedata);
        bookWithContentAdapter.notifyDataSetChanged();

        if (bookDetailResponse.bookshelves == 1) {
            tvAddToBookrack.setOnClickListener(null);
            tvAddToBookrack.setText(R.string.added_to_bookrack);
            Drawable drawable = getResources().getDrawable(R.drawable.added_bookrack);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            tvAddToBookrack.setCompoundDrawables(drawable, null, null, null);
        }

        container.setVisibility(View.VISIBLE);
        bottomLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void getBookCommentSuccess(BookDetailCommentResponse bookDetailCommentResponse) {
        commentCount = bookDetailCommentResponse.sumreplycount;

        tvCommentCount.setText(getString(R.string.comment_user_count, bookDetailCommentResponse.sumreplycount));

        if (bookDetailCommentResponse.comdata != null) {
            int size = bookDetailCommentResponse.comdata.size();
            if (size > 3) {
                size = 3;
            }
            commentAdapter.clear();
            for (int i = 0; i < size; i++) {
                commentAdapter.add(bookDetailCommentResponse.comdata.get(i));
            }
            commentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getBookCommentByTypeSuccess(BookCommentByTypeResponse bookCommentByTypeResponse) {

    }

    @Override
    public void getCommentReplyListSuccess(CommentReplyResponse commentReplyResponse) {
    }

    @Override
    public void sendCommentSuccess(SendCommentResponse sendCommentResponse) {

    }

    @Override
    public void sendLikeSuccess() {
        long userid = UserInfoManager.getInstance(mContext).getUserid();
        loadComment(userid);
    }

    @Override
    public void getGiftListConfigSuccess(GetGiftListConfigResponse getGiftListConfigResponse) {
        giftPopupWindow = new GiftPopupWindow(mActivity);
        giftPopupWindow.setGiftList(getGiftListConfigResponse.data);
        giftPopupWindow.setCurrentMoney(getGiftListConfigResponse.readmoney);
        giftPopupWindow.setOnPayTourListener(new GiftPopupWindow.OnPayTourListener() {
            @Override
            public void onPayTour(GetGiftListConfigResponse.GiftBean giftBean) {
                int userid = UserInfoManager.getInstance(mActivity).getUserid();
                giftPresenter.giveGift(userid, giftBean.propid, 1, bookid);
            }

            @Override
            public void showTopUpActivity() {
                Intent intent = new Intent(mActivity, PayCenterActivity.class);
                startActivityForResult(intent, TOP_UP_REQUEST_CODE);
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
        tvPayTourCount.setText(String.valueOf(giveGiftResponse.giftnumbers));
        updateUserInfo(giveGiftResponse.readmoney);

        if (giveGiftResponse.receive == 0) {
            long userid = UserInfoManager.getInstance(mContext).getUserid();
            taskAwardPresenter.getTaskAward(userid, TaskId.PLAY_TOUR, false);
        }
    }

    @Override
    public void getReceivedGiftListSuccess(ReceivedGiftResponse receivedGiftResponse) {
        if (receivedGiftResponse.pagedata == null || receivedGiftResponse.pagedata.size() < 1) {
            giftCarouselSwitcher.setVisibility(View.GONE);
        } else {
            giftCarouselSwitcher.setGiftList(mActivity, receivedGiftResponse.pagedata);
            giftCarouselSwitcher.startLooping();
        }
    }

    private void updateUserInfo(final long readmoney) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserInfoManager userInfoManager = UserInfoManager.getInstance(mContext);
                UserInfo userInfo = userInfoManager.getUserInfo();
                userInfo.money = readmoney;
                userInfoManager.saveUserInfo(userInfo);
            }
        }).start();
    }

    @Override
    public void addToBookRackSuccess(boolean finishActivity, long bookid, int receive) {
        tvAddToBookrack.setText(R.string.added_to_bookrack);
        tvAddToBookrack.setEnabled(false);
        Drawable drawable = getResources().getDrawable(R.drawable.added_bookrack);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        tvAddToBookrack.setCompoundDrawables(drawable, null, null, null);

        showToast(getString(R.string.add_to_bookrack_success));

        ChapterInfoDao chapterInfoDao = DbManager.getDaoSession(mContext).getChapterInfoDao();
        List<ChapterInfo> list = chapterInfoDao.queryBuilder().where(ChapterInfoDao.Properties.Bookid.eq(bookid)).list();
        for (ChapterInfo chapterInfo : list) {
            chapterInfo.bookshelves = 1;
            chapterInfoDao.update(chapterInfo);
        }

        BookBrowseHistoryDao bookBrowseHistoryDao = VisitorDbManager.getDaoSession(mContext).getBookBrowseHistoryDao();
        List<BookBrowseHistory> list1 = bookBrowseHistoryDao.queryBuilder().where(BookBrowseHistoryDao.Properties.Bookid.eq(bookid)).list();
        for (BookBrowseHistory bookBrowseHistory : list1) {
            bookBrowseHistory.bookshelves = 1;
            bookBrowseHistoryDao.update(bookBrowseHistory);
        }

        Intent intent = new Intent(BroadcastAction.ADD_BOOK_TO_RACK_ACTION);
        intent.putExtra("bookId", bookid);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        if (receive == 0) {
            long userid = UserInfoManager.getInstance(mContext).getUserid();
            taskAwardPresenter.getTaskAward(userid, TaskId.ADD_BOOK_TO_RACK, true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Tencent.onActivityResultData(requestCode, resultCode, data, null);

        long userid = UserInfoManager.getInstance(mContext).getUserid();

        if (requestCode == SEND_COMMENT_REQUEST_CODE) {
            if (RESULT_OK == resultCode) {
                loadComment(userid);
                int receive = data.getIntExtra("receive", 1);
                if (receive == 0) {
                    taskAwardPresenter.getTaskAward(userid, TaskId.COMMENT_BOOK, false);
                }
            }
        } else if (requestCode == FROM_PAYTOUR) {
            giftPresenter.getGiftListConfig(userid);
        } else if (requestCode == TOP_UP_REQUEST_CODE) {
            UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
            if (giftPopupWindow != null && giftPopupWindow.isShowing()) {
                double money = userInfo.money + userInfo.coin;
                giftPopupWindow.setCurrentMoney(money);
            }
        } else if (requestCode == LUNCH_LOGIN_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                int formCode = data.getIntExtra(SharedKey.FROM_CODE, 0);
                if (formCode == FROM_ADD_TO_BOOK_RACK) {
                    addToBookRackPresenter.addToBookRack(userid, bookid, false);
                } else if (formCode == FROM_SHARE) {
                    shareBook();
                } else if (formCode == FROM_DISCOUNTSBUY_CODE) {
                    discountsBuy();
                }
            }
        }
    }

    @Override
    public void getBookShareInfoSuccess(final ShareResponse shareResponse) {
        final SharePopupWindow sharePopupWindow = new SharePopupWindow(mActivity, 0);
        sharePopupWindow.setOnShareListener(new SharePopupWindow.OnShareListener() {
            @Override
            public void onShareToWeChat() {
                RequestOptions options = new RequestOptions()
                        .override(50, 50);

                Glide.with(mActivity)
                        .asBitmap()
                        .load(shareResponse.imgurl)
                        .apply(options)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                WeChatSharer.shareToWeChat(mActivity, shareResponse.titile, shareResponse.linkurl, resource, shareResponse.content);
                            }
                        });
            }

            @Override
            public void onShareToFriendCircle() {
                RequestOptions options = new RequestOptions()
                        .override(50, 50);

                Glide.with(mActivity)
                        .asBitmap()
                        .load(shareResponse.imgurl)
                        .apply(options)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                WeChatSharer.shareToFriendCircle(mActivity, shareResponse.titile, shareResponse.linkurl, resource, shareResponse.content);
                            }
                        });
            }

            @Override
            public void onShareQq() {
                QQSharer.shareToQQFriend(mActivity, shareResponse.titile, shareResponse.content, shareResponse.linkurl, shareResponse.imgurl, new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        Toast.makeText(mActivity, getString(R.string.share_success), Toast.LENGTH_SHORT).show();
                        long userid = UserInfoManager.getInstance(mContext).getUserid();
                        taskAwardPresenter.getTaskAward(userid, TaskId.SHARE_BOOK, true);
                    }

                    @Override
                    public void onError(UiError uiError) {
                        Toast.makeText(mActivity, getString(R.string.share_failed), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Logger.i(TAG, "onCancel: ");
                    }
                });

            }

            @Override
            public void onShareQqZone() {
                QQSharer.shareToQzone(mActivity, shareResponse.titile, shareResponse.content, shareResponse.linkurl, shareResponse.imgurl, new IUiListener() {
                    @Override
                    public void onComplete(Object o) {
                        Toast.makeText(mActivity, getString(R.string.share_success), Toast.LENGTH_SHORT).show();
                        long userid = UserInfoManager.getInstance(mContext).getUserid();
                        taskAwardPresenter.getTaskAward(userid, TaskId.SHARE_BOOK, true);
                    }

                    @Override
                    public void onError(UiError uiError) {
                        Toast.makeText(mActivity, getString(R.string.share_failed), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Logger.i(TAG, "onCancel: ");
                    }
                });
            }
        });
        sharePopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
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
}
