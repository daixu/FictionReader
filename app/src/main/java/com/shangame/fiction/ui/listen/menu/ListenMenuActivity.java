package com.shangame.fiction.ui.listen.menu;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.ListenNormalAdapter;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.response.AlbumDataResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 听书完本、完结界面
 */
public class ListenMenuActivity extends BaseActivity implements ListenMenuContacts.View {
    private ListenMenuPresenter mPresenter;
    private SmartRefreshLayout smartRefreshLayout;
    private int pageIndex = 1;
    private int status;
    private ListenNormalAdapter mAdapter;
    private List<AlbumDataResponse.PageDataBean> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_menu);
        initView();
        initPresenter();
        initData(pageIndex);
    }

    private void initView() {
        status = getIntent().getIntExtra("status", 1);
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        switch (status) {
            case 1:
                tvPublicTitle.setText("全本");
                break;
            case 2:
                tvPublicTitle.setText("完结");
                break;
            case 3:
                tvPublicTitle.setText("免费");
                break;
            default:
                break;
        }
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        RecyclerView recyclerListen = findViewById(R.id.recycler_listen);
        recyclerListen.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ListenNormalAdapter(R.layout.item_end_listen, mList);
        recyclerListen.setAdapter(mAdapter);

        findViewById(R.id.ivPublicBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageIndex = 1;
                initData(pageIndex);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                initData(pageIndex);
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AlbumDataResponse.PageDataBean bean = mList.get(position);
                if (null != bean) {
                    ARouter.getInstance()
                            .build("/ss/listen/detail")
                            .withInt("albumId", bean.albumid)
                            .navigation();
                }
            }
        });
    }

    private void initPresenter() {
        mPresenter = new ListenMenuPresenter();
        mPresenter.attachView(this);
    }

    private void initData(int pageIndex) {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        mPresenter.getAlbumData(userId, pageIndex, 10, status);
    }

    @Override
    public void getAlbumDataSuccess(AlbumDataResponse response) {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();

        if (pageIndex == 1) {
            mList.clear();
        }
        pageIndex++;
        mList.addAll(response.pagedata);
        mAdapter.setNewData(mList);
    }

    @Override
    public void getAlbumDataFailure(String msg) {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
