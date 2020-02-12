package com.shangame.fiction.ui.my.pay.history;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.net.response.PayConsumeResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.widget.RemindFrameLayout;

/**
 * 充值记录 Fragment
 * Create by Speedy on 2018/7/23
 */
public class PayHistoryFragment extends BaseFragment implements PayConsumeContacts.View {

    private static final String TAG = "PayHistoryFragment";

    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;

    private String currentMouth;

    private int page;

    private PayConsurePresenter payConsurePresenter;

    private int type;

    private RemindFrameLayout remindFrameLayout;

    public static PayHistoryFragment getInstance(int type) {
        PayHistoryFragment payHistoryFragment = new PayHistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        payHistoryFragment.setArguments(bundle);
        return payHistoryFragment;
    }

    ;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        payConsurePresenter = new PayConsurePresenter();
        payConsurePresenter.attachView(this);
        type = getArguments().getInt("type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_pay_history, container, false);

        remindFrameLayout = view.findViewById(R.id.remindFrameLayout);

        smartRefreshLayout = view.findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getPayHistory(page);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getPayHistory(page);
            }
        });


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.public_line_horizontal));
        recyclerView.addItemDecoration(dividerItemDecoration);

        myAdapter = new MyAdapter();
        if (type == PayHistoryActivity.CONSUME_HISTORY) {
            remindFrameLayout.setRemindText("暂无消费记录");
        } else if (type == PayHistoryActivity.PAY_HISTORY) {
            remindFrameLayout.setRemindText("暂无充值记录");
        } else {
            remindFrameLayout.setRemindText("暂无充值记录");
        }
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

        recyclerView.setAdapter(myAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        smartRefreshLayout.autoRefresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        payConsurePresenter.detachView();
    }

    private void getPayHistory(int page) {
        int userid = UserInfoManager.getInstance(mContext).getUserid();
        payConsurePresenter.getConsumeHistoryList(userid, type, page, PAGE_SIZE);
    }

    @Override
    public void getConsumeHistoryListSuccess(PayConsumeResponse payConsumeResponse) {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
        if (page == 1) {
            myAdapter.clear();
        }
        myAdapter.addAll(payConsumeResponse.pagedata);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNotNetworkView() {
        super.showNotNetworkView();
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
    }

    @Override
    public void showError(Throwable throwable) {
        super.showError(throwable);
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvMouth;
        TextView tvTitle;
        TextView tvTime;
        TextView tvNum;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvMouth = itemView.findViewById(R.id.tvMouth);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvNum = itemView.findViewById(R.id.tvNum);
        }
    }

    private class MyAdapter extends WrapRecyclerViewAdapter<PayConsumeResponse.PayConsumeBean, MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.pay_history_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            final PayConsumeResponse.PayConsumeBean payConsumeBean = getItem(position);
            holder.tvMouth.setText(payConsumeBean.month);
            holder.tvTitle.setText(payConsumeBean.ramark);
            holder.tvTime.setText(payConsumeBean.daytime);
            holder.tvNum.setText(String.valueOf(payConsumeBean.money));

            if (payConsumeBean.month.equals(currentMouth)) {
                holder.tvMouth.setVisibility(View.GONE);
            } else {
                holder.tvMouth.setVisibility(View.VISIBLE);
                currentMouth = payConsumeBean.month;
            }
        }
    }
}
