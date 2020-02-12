package com.shangame.fiction.ui.my.account;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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
import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.net.response.PlayTourResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.widget.RemindFrameLayout;

/**
 * 打赏记录 Activity
 * Create by Speedy on 2018/8/2
 */
public class PlayTourHistoryActivity extends BaseActivity implements PlayTourContracts.View,View.OnClickListener {

    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;

    private PlayTourPresenter playTourPresenter;
    private int page;

    private RemindFrameLayout remindFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_tour_history);
        remindFrameLayout = findViewById(R.id.remindFrameLayout);
        playTourPresenter = new PlayTourPresenter();
        playTourPresenter.attachView(this);

        TextView tvPublicTitle = (TextView) findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.play_tour_history);

        findViewById(R.id.ivPublicBack).setOnClickListener(this);

        smartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getHistory(page);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getHistory(page);
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.public_line_horizontal));
        recyclerView.addItemDecoration(dividerItemDecoration);

        myAdapter = new MyAdapter();
        myAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                if(myAdapter.getItemCount() > 0){
                    remindFrameLayout.showContentView();
                }else{
                    remindFrameLayout.showRemindView();
                }
            }
        });
        recyclerView.setAdapter(myAdapter);

        smartRefreshLayout.autoRefresh();
    }


    private void getHistory(int page) {
        int userid = UserInfoManager.getInstance(mContext).getUserid();
        playTourPresenter.getPlayTourList(userid,page,PAGE_SIZE);
    }

    @Override
    public void getPlayTourListSuccess(PlayTourResponse playTourResponse) {
        smartRefreshLayout.finishLoadMore();
        smartRefreshLayout.finishRefresh();
        if(page == 1){
            myAdapter.clear();
        }
        myAdapter.addAll(playTourResponse.pagedata);
        myAdapter.notifyDataSetChanged();
    }


    @Override
    public void showError(Throwable throwable) {
        super.showError(throwable);
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
    }

    @Override
    public void showNotNetworkView() {
        super.showNotNetworkView();
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.ivPublicBack){
            finish();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvGiftName;
        TextView tvBookName;
        TextView tvDate;
        ImageView ivGift;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvGiftName = itemView.findViewById(R.id.tvGiftName);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivGift = itemView.findViewById(R.id.ivGift);
        }
    }

    private class MyAdapter extends WrapRecyclerViewAdapter<PlayTourResponse.PlayTourBean,MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.play_tour_history_item,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            final PlayTourResponse.PlayTourBean playTourBean = getItem(position);
            holder.tvGiftName.setText(playTourBean.propname);
            holder.tvBookName.setText(playTourBean.bookname);
            holder.tvDate.setText(playTourBean.creatortime);

            String playWhat = getString(R.string.pay_tour_what);
            SpannableStringBuilder builder = new SpannableStringBuilder(playWhat+playTourBean.propname );
            builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),playWhat.length(),playWhat.length()+playTourBean.propname.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

            ImageLoader.with(mActivity).loadCover(holder.ivGift,playTourBean.propimage);

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        playTourPresenter.detachView();
    }
}
