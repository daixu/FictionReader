package com.shangame.fiction.ui.task;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.net.response.CashConfigResponse;
import com.shangame.fiction.net.response.RedListResponse;
import com.shangame.fiction.net.response.WeChatCashResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.DateBean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RedPacketActivity extends BaseActivity implements View.OnClickListener, RedPacketContacts.View {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月");
    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM");
    private SmartRefreshLayout smartRefreshLayout;
    private double cashMoney;
    private TextView tvMoney;
    private TextView tvMouth;
    private RecyclerView mRecyclerView;
    private MyAdapter myAdapter;
    private RedPacketPresenter redPacketPresenter;
    private int page = 1;

    private long userid;
    private String datatime;

    private DateBean currentDateBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_packet);
        initView();
        initSmartRefreshLayout();
        initRecyclerView();
        initPresenter();
        smartRefreshLayout.autoRefresh();
    }

    private void initView() {
        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText("我的红包");

        findViewById(R.id.tvWithDraw).setOnClickListener(this);
        findViewById(R.id.tvChange).setOnClickListener(this);
        tvMoney = findViewById(R.id.tvMoney);
        tvMouth = findViewById(R.id.tvMouth);
        userid = UserInfoManager.getInstance(mContext).getUserid();

        Date date = new Date(System.currentTimeMillis());
        datatime = simpleDateFormat1.format(date);

        cashMoney = getIntent().getDoubleExtra("cashMoney", 0);
        tvMoney.setText(cashMoney + "");

        tvMouth.setText(simpleDateFormat.format(date));
    }

    private void initSmartRefreshLayout() {
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                loadData(datatime, page);
            }
        });

        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadData(datatime, page);
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.public_line_horizontal));

        mRecyclerView.addItemDecoration(dividerItemDecoration);
        myAdapter = new MyAdapter();
        mRecyclerView.setAdapter(myAdapter);
    }

    private void initPresenter() {
        redPacketPresenter = new RedPacketPresenter();
        redPacketPresenter.attachView(this);
    }

    public void loadData(String datatime, int page) {
        redPacketPresenter.getRedList(userid, datatime, page, PAGE_SIZE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        redPacketPresenter.detachView();
    }

    public void resetPage() {
        page = 1;
    }

    @Override
    public void onClick(View view) {
        long id = view.getId();
        if (R.id.ivPublicBack == id) {
            finish();
        } else if (id == R.id.tvWithDraw) {
            Intent intent = new Intent(mActivity, DrawMoneyActivity.class);
            intent.putExtra("cashMoney", cashMoney);
            startActivity(intent);
        } else if (id == R.id.tvChange) {
            showDate();
        }
    }

    private void showDate() {
        ChangeDateFragment changeDateFragment = new ChangeDateFragment();
        changeDateFragment.show(getSupportFragmentManager(), "ChangeDateFragment");
    }

    @Override
    public void getRedListSuccess(RedListResponse redListResponse) {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
        if (page == 1) {
            myAdapter.clear();
        }
        page++;
        myAdapter.addAll(redListResponse.pagedata);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void getCashconfigSuccess(CashConfigResponse cashConfigResponse) {

    }

    @Override
    public void weChatCashSuccess(WeChatCashResponse response, String msg) {

    }

    public DateBean getCurrentDateBean() {
        return currentDateBean;
    }

    public void setCurrentDateBean(DateBean currentDateBean) {
        this.currentDateBean = currentDateBean;
        tvMouth.setText(currentDateBean.toString());
    }

    class MyAdapter extends WrapRecyclerViewAdapter<RedListResponse.RedItem, MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = getLayoutInflater().inflate(R.layout.red_item, viewGroup, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            RedListResponse.RedItem redItem = getItem(i);
            myViewHolder.tvName.setText(redItem.remark);
            myViewHolder.tvTime.setText(redItem.datatime);
            myViewHolder.tvMoney.setText(redItem.price);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvTime;
        TextView tvMoney;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvMoney = itemView.findViewById(R.id.tvMoney);
        }
    }
}
