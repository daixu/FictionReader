package com.shangame.fiction.ui.author.notice;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.response.BookNoticeInfoResponse;
import com.shangame.fiction.net.response.NoticeInfoResponse;

import java.util.ArrayList;
import java.util.List;

public class ReviewInfoActivity extends BaseActivity implements NoticeListContacts.View, View.OnClickListener {
    private SmartRefreshLayout mSmartRefreshLayout;
    private NoticeListPresenter mPresenter;
    private int mPage = 1;
    private int mBookId;
    private ReviewInfoAdapter mAdapter;

    private List<BookNoticeInfoResponse.PageDataBean> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);

        RecyclerView recyclerNotice = findViewById(R.id.recycler_notice);
        mSmartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        mSmartRefreshLayout.autoRefresh();
        mBookId = getIntent().getIntExtra("bookId", 0);

        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("消息通知");

        initPresenter();
        recyclerNotice.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ReviewInfoAdapter(R.layout.item_notice_list, mList);
        recyclerNotice.setAdapter(mAdapter);
        initListener();
    }

    private void initPresenter() {
        mPresenter = new NoticeListPresenter();
        mPresenter.attachView(this);
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

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BookNoticeInfoResponse.PageDataBean dataBean = mList.get(position);
                Intent intent = new Intent(mContext, NoticeActivity.class);
                intent.putExtra("dataBean", dataBean);
                intent.putExtra("type", 2);
                startActivity(intent);
            }
        });
    }

    private void initData(int page) {
        mPresenter.getBookNoticeInfo(page, 20, mBookId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
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
    public void getNoticeInfoSuccess(NoticeInfoResponse response) {
    }

    @Override
    public void getNoticeInfoFailure(String msg) {
    }

    @Override
    public void getBookNoticeInfoSuccess(BookNoticeInfoResponse response) {
        mSmartRefreshLayout.finishRefresh();
        mSmartRefreshLayout.finishLoadMore();
        if (mPage == 1) {
            mList.clear();
        }
        mList.addAll(response.pagedata);
        mAdapter.setNewData(mList);
    }

    @Override
    public void getBookNoticeInfoFailure(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "请求失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_back) {
            finish();
        }
    }
}
