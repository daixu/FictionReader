package com.shangame.fiction.ui.my.account.coin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.net.response.CoinListResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;

/**
 * Create by Speedy on 2019/1/3
 */
public class CoinListFragment extends BaseFragment implements CoinListContracts.View {

    private int state;

    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView mRecyclerView;
    private CoidAdapter coidAdapter;

    private CoinListPresenter coinListPresenter;
    private int page;
    private long userid;

    public static CoinListFragment newInstance(int state) {
        Bundle args = new Bundle();
        args.putInt("state", state);
        CoinListFragment fragment = new CoinListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        state = getArguments().getInt("state");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        coinListPresenter.detachView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_coin_list, null);
        initRecyclerView(contentView);
        initSmartRefresh(contentView);
        initPresenter();
        return contentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        smartRefreshLayout.autoRefresh();
    }

    private void initRecyclerView(View contentView) {
        mRecyclerView = contentView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.public_line_horizontal));
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        coidAdapter = new CoidAdapter(state);
        mRecyclerView.setAdapter(coidAdapter);
    }

    private void initSmartRefresh(View contentView) {
        smartRefreshLayout = contentView.findViewById(R.id.smartRefreshLayout);
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
        coinListPresenter.getCoinList(userid, state, page, PAGE_SIZE);
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
