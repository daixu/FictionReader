package com.shangame.fiction.ui.bookdetail.gift;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.net.response.GetGiftListConfigResponse;
import com.shangame.fiction.net.response.GiveGiftResponse;
import com.shangame.fiction.net.response.ReceivedGiftResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.my.pay.PayCenterActivity;
import com.shangame.fiction.widget.GlideApp;


public class ReceivedGiftActivity extends BaseActivity implements GiftContracts.View, View.OnClickListener {

    private static final int TOP_UP_REQUEST_CODE = 503;

    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private GiftPresenter giftPresenter;
    private GiftPopupWindow giftPopupWindow;

    private long bookid;
    private int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_gift);
        bookid = getIntent().getLongExtra("bookid", bookid);
        initPresenter();
        initTitle();
        initSmartRefreshLayout();
        initRecyclerView();
        smartRefreshLayout.autoRefresh();
    }

    private void initPresenter() {
        giftPresenter = new GiftPresenter();
        giftPresenter.attachView(this);
    }

    private void initTitle() {
        findViewById(R.id.ivPublicBack).setOnClickListener(this);

        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.gift_wall);

        findViewById(R.id.tvGiveGift).setOnClickListener(this);
    }

    private void initSmartRefreshLayout() {
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                loadData(page);
            }
        });

        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                loadData(page);
            }
        });
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.public_line_horizontal));
        recyclerView.addItemDecoration(dividerItemDecoration);

        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
    }

    private void loadData(int page) {
        giftPresenter.getReceivedGiftList(bookid, page, PAGE_SIZE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        giftPresenter.detachView();
    }

    @Override
    public void showError(Throwable throwable) {
        super.showError(throwable);
        smartRefreshLayout.finishLoadMore();
        smartRefreshLayout.finishRefresh();
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
        smartRefreshLayout.autoRefresh();
    }

    @Override
    public void getReceivedGiftListSuccess(ReceivedGiftResponse receivedGiftResponse) {
        smartRefreshLayout.finishLoadMore();
        smartRefreshLayout.finishRefresh();
        if (page == 1) {
            myAdapter.clear();
        }
        myAdapter.addAll(receivedGiftResponse.pagedata);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivPublicBack) {
            finish();
        } else if (v.getId() == R.id.tvGiveGift) {
            long userid = UserInfoManager.getInstance(mContext).getUserid();
            if (userid == 0) {
                lunchLoginActivity();
            } else {
                giftPresenter.getGiftListConfig(userid);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TOP_UP_REQUEST_CODE) {
            UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
            if (giftPopupWindow != null && giftPopupWindow.isShowing()) {
                giftPopupWindow.setCurrentMoney(userInfo.money);
            }
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivHeadIcon;
        TextView tvUserName;
        TextView tvMessge;
        TextView tvTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivHeadIcon = itemView.findViewById(R.id.ivHeadIcon);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvMessge = itemView.findViewById(R.id.tvMessge);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }

    private class MyAdapter extends WrapRecyclerViewAdapter<ReceivedGiftResponse.ReceivedGift, MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.received_gift_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            ReceivedGiftResponse.ReceivedGift receivedGift = getItem(position);
            holder.tvUserName.setText(receivedGift.nickname);
            holder.tvTime.setText(receivedGift.creatortime);

            String author = "";
            if (!TextUtils.isEmpty(receivedGift.author)) {
                author = receivedGift.author;
            }
            String propName = "";
            if (!TextUtils.isEmpty(receivedGift.propname)) {
                propName = receivedGift.propname;
            }

            SpannableStringBuilder builder = new SpannableStringBuilder(author + " " + propName);
            builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), author.length(), (author + " " + propName).length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.tvMessge.setText(builder);

            // ImageLoader.with(mActivity).loadUserIcon(holder.ivHeadIcon, receivedGift.headimgurl);
            GlideApp.with(mActivity)
                    .load(receivedGift.headimgurl)
                    .centerCrop()
                    .placeholder(R.drawable.default_head)
                    .into(holder.ivHeadIcon);
        }
    }
}
