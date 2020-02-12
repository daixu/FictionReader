package com.shangame.fiction.ui.bookdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.manager.ActivityStack;
import com.shangame.fiction.net.response.BookCommentByTypeResponse;
import com.shangame.fiction.net.response.BookDetailCommentResponse;
import com.shangame.fiction.net.response.BookDetailResponse;
import com.shangame.fiction.net.response.CommentReplyResponse;
import com.shangame.fiction.net.response.GetGiftListConfigResponse;
import com.shangame.fiction.net.response.GiveGiftResponse;
import com.shangame.fiction.net.response.ReceivedGiftResponse;
import com.shangame.fiction.net.response.SendCommentResponse;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
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
import com.shangame.fiction.ui.bookstore.BookWithTitleAdapter;
import com.shangame.fiction.ui.main.MainFrameWorkActivity;
import com.shangame.fiction.ui.my.pay.PayCenterActivity;
import com.shangame.fiction.ui.task.TaskAwardContacts;
import com.shangame.fiction.ui.task.TaskAwardPresenter;
import com.shangame.fiction.ui.task.TaskId;
import com.shangame.fiction.ui.task.TaskRewardPopupWindow;
import com.shangame.fiction.widget.GiftCarouselSwitcher;
import com.shangame.fiction.widget.SpaceItemDecoration;


/**
 * 书籍详情
 * Create by Speedy on 2018/7/25
 */
public class RecommendBookActivity extends BaseActivity implements View.OnClickListener,
        BookDetailContacts.View, CommentContacts.View,
        GiftContracts.View, AddToBookRackContacts.View
        , TaskAwardContacts.View {

    private static final int SEND_COMMENT_REQUEST_CODE = 520;
    private static final int TOP_UP_REQUEST_CODE = 503;
    public long fristChapterId;
    private SmartRefreshLayout smartRefreshLayout;
    private View container;
    private View bottomLayout;
    private TextView tvStatus;
    private TextView tvNext;
    private TextView tvPayTourCount;
    private GiftCarouselSwitcher giftCarouselSwitcher;
    private TextView tvCommentCount;
    private RecyclerView commentListView;
    private CommentListAdapter commentAdapter;
    private TextView tvTitleName;
    private RecyclerView guessYouLikeGridView;
    private BookWithTitleAdapter bookWithTitleAdapter;
    private BookDetailPresenter bookDetailPresenter;
    private CommentPresenter commentPresenter;
    private long userid;
    private long bookid;
    private int clicktype;
    private BookDetailResponse bookDetailResponse;
    private int commentCount;

    private GiftPresenter giftPresenter;
    private GiftPopupWindow giftPopupWindow;

    private AddToBookRackPresenter addToBookRackPresenter;

    private TaskAwardPresenter taskAwardPresenter;

    public static void lunchActivity(Activity activity, long bookid, int clicktype) {
        Intent intent = new Intent(activity, RecommendBookActivity.class);
        intent.putExtra("bookid", bookid);
        intent.putExtra("clicktype", clicktype);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_book);
        initPresenter();
        initSmartRefreshLayout();
        initParam();
        initView();
        initTextSwitcher();
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

        taskAwardPresenter = new TaskAwardPresenter();
        taskAwardPresenter.attachView(this);
    }

    private void initSmartRefreshLayout() {
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadBookDetail();
                loadComment();
                loadGift();
            }
        });
    }

    private void initParam() {
        userid = UserInfoManager.getInstance(mContext).getUserid();
        bookid = getIntent().getLongExtra("bookid", 0);
        clicktype = getIntent().getIntExtra("clicktype", 0);
    }

    private void initView() {
        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.book_detail);

        container = findViewById(R.id.container);
        bottomLayout = findViewById(R.id.bottomLayout);

        tvStatus = findViewById(R.id.tvStatus);
        tvNext = findViewById(R.id.tvNext);
        tvPayTourCount = findViewById(R.id.tvPayTourCount);
        tvPayTourCount.setOnClickListener(this);

        findViewById(R.id.commentTitleLayout).setOnClickListener(this);
        tvCommentCount = findViewById(R.id.tvCommentCount);
        commentListView = findViewById(R.id.commentListView);

        commentAdapter = new CommentListAdapter(mActivity, commentPresenter, bookid);
        commentListView.setAdapter(commentAdapter);

        findViewById(R.id.payTourLayout).setOnClickListener(this);
        findViewById(R.id.tvWriteComment).setOnClickListener(this);
        findViewById(R.id.tvLookAllComment).setOnClickListener(this);
        findViewById(R.id.tvGuessYouLikeMore).setOnClickListener(this);
        findViewById(R.id.tvGotoBookRack).setOnClickListener(this);

        DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration2.setDrawable(getResources().getDrawable(R.drawable.divider_empty));

        tvTitleName = findViewById(R.id.tvTitleName);
        tvTitleName.setText(R.string.recommend_book);
        guessYouLikeGridView = findViewById(R.id.guessYouLikeGridView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        guessYouLikeGridView.setLayoutManager(gridLayoutManager);
        guessYouLikeGridView.addItemDecoration(new SpaceItemDecoration(35));
        bookWithTitleAdapter = new BookWithTitleAdapter(mActivity);
        guessYouLikeGridView.setAdapter(bookWithTitleAdapter);
    }

    private void initTextSwitcher() {
        giftCarouselSwitcher = findViewById(R.id.textSwitcher);
        giftCarouselSwitcher.setOnClickListener(this);

        giftCarouselSwitcher.setInAnimation(this, R.anim.slide_top_in);
        giftCarouselSwitcher.setOutAnimation(this, R.anim.slide_bootom_out);
    }

    private void loadBookDetail() {
        bookDetailPresenter.getBookDetail(userid, bookid, clicktype);
    }

    private void loadComment() {
        commentPresenter.getBookComment(userid, bookid);
    }

    private void loadGift() {
        giftPresenter.getReceivedGiftList(bookid, 1, 10);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bookDetailPresenter.detachView();
        commentPresenter.detachView();
        giftPresenter.detachView();
        addToBookRackPresenter.detachView();
        taskAwardPresenter.detachView();
        giftCarouselSwitcher.stopLooping();
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
        switch (view.getId()) {
            case R.id.ivPublicBack:
                finish();
                break;
            case R.id.commentTitleLayout:
            case R.id.tvLookAllComment:
                if (bookDetailResponse != null) {
                    CommentActivity.lunchActivity(mActivity, bookDetailResponse.detailsdata, commentCount);
                }
                break;
            case R.id.payTourLayout:
                userid = UserInfoManager.getInstance(mContext).getUserid();
                if (userid == 0) {
                    lunchLoginActivity();
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
            case R.id.tvGotoBookRack:
                ActivityStack.popAllActivity();
                Intent intent = new Intent(mActivity, MainFrameWorkActivity.class);
                intent.putExtra("index", 0);
                startActivity(intent);
                break;
            case R.id.tvGuessYouLikeMore:
                BookListByTypeActivity.lunchActivity(mActivity, BookStoreType.GuessYouLike, getString(R.string.guess_you_like), BookStoreChannel.All);
                break;
            default:
                break;
        }
    }

    @Override
    public void getBookDetailSuccess(BookDetailResponse bookDetailResponse) {
        smartRefreshLayout.finishRefresh();
        this.bookDetailResponse = bookDetailResponse;

        fristChapterId = bookDetailResponse.fristchapter;

        tvPayTourCount.setText(bookDetailResponse.detailsdata.giftnumbers + "");

        bookWithTitleAdapter.clear();
        bookWithTitleAdapter.addAll(bookDetailResponse.likedata);
        bookWithTitleAdapter.notifyDataSetChanged();

        container.setVisibility(View.VISIBLE);
        bottomLayout.setVisibility(View.VISIBLE);

        tvStatus.setText(bookDetailResponse.detailsdata.serstatus);
        if ("已完结".equals(bookDetailResponse.detailsdata.serstatus)) {
            tvNext.setText("敬请期待下一部作品");
        }
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
        loadComment();
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEND_COMMENT_REQUEST_CODE) {
            if (RESULT_OK == resultCode) {
                loadComment();
                int receive = data.getIntExtra("receive", 1);
                if (receive == 0) {
                    long userid = UserInfoManager.getInstance(mContext).getUserid();
                    taskAwardPresenter.getTaskAward(userid, TaskId.COMMENT_BOOK, false);
                }
            }
        } else if (requestCode == TOP_UP_REQUEST_CODE) {
            UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
            if (giftPopupWindow != null && giftPopupWindow.isShowing()) {
                giftPopupWindow.setCurrentMoney(userInfo.money);
            }
        }
    }

    @Override
    public void getTaskAwardSuccess(TaskAwardResponse taskAwardResponse, int taskid) {
        TaskRewardPopupWindow taskRewardPopupWindow = new TaskRewardPopupWindow(mActivity);
        taskRewardPopupWindow.show(taskAwardResponse.desc, taskAwardResponse.number + "");
    }
}
