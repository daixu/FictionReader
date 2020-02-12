package com.shangame.fiction.ui.listen.more;

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

public class ListenMoreActivity extends BaseActivity implements ListenMoreContacts.View {
    private ListenMorePresenter mPresenter;
    private SmartRefreshLayout smartRefreshLayout;
    private int pageIndex = 1;
    private int moduleId;
    private ListenNormalAdapter mAdapter;
    private List<AlbumDataResponse.PageDataBean> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_more);
        initView();
        initPresenter();
        initData(1);
    }

    private void initView() {
        moduleId = getIntent().getIntExtra("moduleId", 1);
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        switch (moduleId) {
            case 1:
                tvPublicTitle.setText("每日必听");
                break;
            case 2:
                tvPublicTitle.setText("折扣专区");
                break;
            case 3:
                tvPublicTitle.setText("男生精品");
                break;
            case 4:
                tvPublicTitle.setText("女生精品");
                break;
            case 5:
                tvPublicTitle.setText("影视专区");
                break;
            case 6:
                tvPublicTitle.setText("完本畅听");
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
        mPresenter = new ListenMorePresenter();
        mPresenter.attachView(this);
    }

    private void initData(int pageIndex) {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        mPresenter.getAlbumModulePage(userId, pageIndex, 10, moduleId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void getAlbumModulePageSuccess(AlbumDataResponse response) {
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
    public void getAlbumModulePageFailure(String msg) {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
    }

}
