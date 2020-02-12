package com.shangame.fiction.ui.my.account.coin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.response.CoinListResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;

public class CoinDetailListActivity extends BaseActivity implements View.OnClickListener, CoinListContracts.View {

    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView mRecyclerView;
    private CoidAdapter coidAdapter;

    private CoinListPresenter coinListPresenter;
    private int page;
    private long userid;

    public static void lunchActivity(Activity activity) {
        Intent intent = new Intent(activity, CoinDetailListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_detail_list);
        initTitle();
        initRecyclerView();
        initSmartRefresh();
        initPresenter();
        smartRefreshLayout.autoRefresh();
    }

    private void initTitle() {
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText("可用赠币");
        findViewById(R.id.ivPublicBack).setOnClickListener(this);
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.public_line_horizontal));
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        coidAdapter = new CoidAdapter(CoinState.VALID);
        mRecyclerView.setAdapter(coidAdapter);
    }

    private void initSmartRefresh() {
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                loadDate(page);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadDate(page);
            }
        });
    }

    private void initPresenter() {
        coinListPresenter = new CoinListPresenter();
        coinListPresenter.attachView(this);
        userid = UserInfoManager.getInstance(mContext).getUserid();
    }

    private void loadDate(int page) {
        coinListPresenter.getCoinList(userid, CoinState.VALID, page, PAGE_SIZE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        coinListPresenter.detachView();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivPublicBack) {
            finish();
        }
    }


    @Override
    public void getCoinListSuccess(CoinListResponse coinListResponse) {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
        if (page == 1) {
            coidAdapter.clear();
        }
        page++;
        coidAdapter.addAll(coinListResponse.pagedata);
        coidAdapter.notifyDataSetChanged();
    }
}
