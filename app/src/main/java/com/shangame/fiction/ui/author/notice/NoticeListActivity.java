package com.shangame.fiction.ui.author.notice;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.response.BookNoticeInfoResponse;
import com.shangame.fiction.net.response.NoticeInfoResponse;

public class NoticeListActivity extends BaseActivity implements NoticeListContacts.View, View.OnClickListener {

    private SmartRefreshLayout mSmartRefreshLayout;
    private NoticeListPresenter mPresenter;
    private int mPage = 1;
    private int mType;
    private NoticeListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);

        RecyclerView recyclerNotice = findViewById(R.id.recycler_notice);
        mSmartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        mSmartRefreshLayout.autoRefresh();

        TextView tvTitle = findViewById(R.id.tv_title);

        mType = getIntent().getIntExtra("type", 0);

        if (mType == 0) {
            tvTitle.setText("公告");
        } else {
            tvTitle.setText("新手作者秘籍");
        }

        initPresenter();
        initListener();

        recyclerNotice.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new NoticeListAdapter(mActivity, mType);
        recyclerNotice.setAdapter(mAdapter);
    }

    private void initListener() {
        findViewById(R.id.img_back).setOnClickListener(this);

        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                initData(mPage);
            }
        });

        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPage++;
                initData(mPage);
            }
        });
    }

    private void initData(int page) {
        mPresenter.getNoticeInfo(page, 20, mType);
    }

    private void initPresenter() {
        mPresenter = new NoticeListPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void getNoticeInfoSuccess(NoticeInfoResponse response) {
        mSmartRefreshLayout.finishRefresh();
        mSmartRefreshLayout.finishLoadMore();
        if (mPage == 1) {
            mAdapter.clear();
        }
        mAdapter.addAll(response.pagedata);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getNoticeInfoFailure(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "请求失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getBookNoticeInfoSuccess(BookNoticeInfoResponse response) {

    }

    @Override
    public void getBookNoticeInfoFailure(String msg) {

    }

    @Override
    public void dismissLoading() {
        super.dismissLoading();
        mSmartRefreshLayout.finishRefresh();
    }

    @Override
    public void showError(Throwable throwable) {
        super.showError(throwable);
        mSmartRefreshLayout.finishRefresh();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_back) {
            finish();
        }
    }
}
