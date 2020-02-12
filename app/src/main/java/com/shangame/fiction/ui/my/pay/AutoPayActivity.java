package com.shangame.fiction.ui.my.pay;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import com.shangame.fiction.net.response.AutoPayResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.widget.RemindFrameLayout;


public class AutoPayActivity extends BaseActivity implements View.OnClickListener, AutoPayContracts.View {

    private SmartRefreshLayout smartRefreshLayout;
    private MyAdapter myAdapter;
    private int page;
    private AutoPayPresenter autoPayPresenter;
    private long userId;
    private RemindFrameLayout remindFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_pay);
        remindFrameLayout = findViewById(R.id.remindFrameLayout);

        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText("自动购买设置");

        initSmartRefreshLayout();
        initRecyclerView();
        smartRefreshLayout.autoRefresh();

        autoPayPresenter = new AutoPayPresenter();
        autoPayPresenter.attachView(this);

        userId = UserInfoManager.getInstance(mContext).getUserid();
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
                loadData(page);
            }
        });
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
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

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.public_line_horizontal));
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void loadData(int page) {
        autoPayPresenter.getAutoPayList(userId, page, PAGE_SIZE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        autoPayPresenter.detachView();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivPublicBack) {
            finish();
        }
    }

    @Override
    public void setAutoPaySuccess() {
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void getAutoPayListSuccess(AutoPayResponse autoPayResponse) {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();

        if (page == 1) {
            myAdapter.clear();
        }
        page++;
        myAdapter.addAll(autoPayResponse.pagedata);
        myAdapter.notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        ImageView ivSwitch;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            ivSwitch = itemView.findViewById(R.id.ivSwitch);
        }
    }

    class MyAdapter extends WrapRecyclerViewAdapter<AutoPayResponse.AutoPayItem, MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = getLayoutInflater().inflate(R.layout.auto_pay_item, null);
            MyViewHolder myViewHolder = new MyViewHolder(itemView);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final AutoPayResponse.AutoPayItem autoPayItem = getItem(position);
            holder.tvName.setText(autoPayItem.bookname);
            if (autoPayItem.autorenew == AutoPayState.OFF) {
                holder.ivSwitch.setImageResource(R.drawable.switch_off);
            } else {
                holder.ivSwitch.setImageResource(R.drawable.switch_on);
            }

            holder.ivSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmAlter(autoPayItem);
                }
            });
        }

        private void confirmAlter(final AutoPayResponse.AutoPayItem autoPayItem) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                    .setMessage("确定关闭？")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (autoPayItem.autorenew == AutoPayState.OFF) {
                                autoPayPresenter.setAutoPay(userId, autoPayItem, AutoPayState.ON);
                            } else {
                                autoPayPresenter.setAutoPay(userId, autoPayItem, AutoPayState.OFF);
                            }
                        }
                    });
            builder.create().show();
        }
    }
}
