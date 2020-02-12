package com.shangame.fiction.ui.task;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.shangame.fiction.net.response.InviteRecordResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.widget.GlideApp;
import com.shangame.fiction.widget.RemindFrameLayout;

/**
 * 邀请记录
 */
public class InviteRecordActivity extends BaseActivity implements InviteRecordContacts.View, View.OnClickListener {

    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private int page;
    private String currentMouth;
    private int inviteid;

    private InviteRecordPresenter inviteRecordPresenter;
    private RemindFrameLayout remindFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_record);
        inviteid = getIntent().getIntExtra("inviteid", 0);
        initTitle();
        initSmartRefreshLayout();
        initRecyclerView();
        initPresenter();
        smartRefreshLayout.autoRefresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        inviteRecordPresenter.detachView();
    }

    private void initTitle() {
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText("邀请记录");
        findViewById(R.id.ivPublicBack).setOnClickListener(this);
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
            }
        });
    }

    private void initRecyclerView() {
        remindFrameLayout = findViewById(R.id.remindFrameLayout);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.public_line_horizontal));
        recyclerView.addItemDecoration(dividerItemDecoration);
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);

        myAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                if (myAdapter.getItemCount() > 0) {
                    remindFrameLayout.showContentView();
                } else {
                    remindFrameLayout.showRemindView();
                }
            }
        });
    }

    private void initPresenter() {
        inviteRecordPresenter = new InviteRecordPresenter();
        inviteRecordPresenter.attachView(this);
    }

    private void loadData(int page) {
        long userid = UserInfoManager.getInstance(mContext).getUserid();
        inviteRecordPresenter.getInviteRecords(userid, inviteid, page, PAGE_SIZE);
    }

    @Override
    public void getInviteRecordsSuccess(InviteRecordResponse inviteRecordResponse) {
        smartRefreshLayout.finishLoadMore();
        smartRefreshLayout.finishRefresh();
        if (page == 1) {
            myAdapter.clear();
        }
        page++;
        myAdapter.addAll(inviteRecordResponse.pagedata);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivPublicBack) {
            finish();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHeadIcon;
        TextView tvName;
        TextView tvDate;
        TextView tvState;
        TextView tvMoney;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivHeadIcon = itemView.findViewById(R.id.ivHeadIcon);
            tvName = itemView.findViewById(R.id.tvName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvState = itemView.findViewById(R.id.tvState);
            tvMoney = itemView.findViewById(R.id.tvMoney);
        }
    }

    private class MyAdapter extends WrapRecyclerViewAdapter<InviteRecordResponse.InviteRecord, MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.invite_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            final InviteRecordResponse.InviteRecord inviteRecord = getItem(position);
            holder.tvMoney.setText(inviteRecord.price);
            holder.tvName.setText(inviteRecord.nickname);
            holder.tvDate.setText(inviteRecord.datatime);
            holder.tvState.setText(inviteRecord.remark);
            // ImageLoader.with(mActivity).loadUserIcon(holder.ivHeadIcon, inviteRecord.headimgurl);
            GlideApp.with(mActivity)
                    .load(inviteRecord.headimgurl)
                    .centerCrop()
                    .placeholder(R.drawable.default_head)
                    .into(holder.ivHeadIcon);
        }
    }
}
