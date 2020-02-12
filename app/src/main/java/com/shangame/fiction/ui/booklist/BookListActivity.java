package com.shangame.fiction.ui.booklist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.BookListAdapter;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.BookListDetailResponse;
import com.shangame.fiction.storage.model.BookListEnitiy;
import com.shangame.fiction.storage.model.BookListResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 书单
 */
public class BookListActivity extends BaseActivity implements View.OnClickListener, BookListContact.View {

    private SmartRefreshLayout mSmartRefreshLayout;
    private BookListAdapter mAdapter;
    private List<BookListEnitiy> mList = new ArrayList<>();

    private BookListPresenter bookListPresenter;

    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText("书单");
        findViewById(R.id.ivPublicBack).setOnClickListener(this);

        mSmartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                loadData(page);
            }
        });

        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadData(page);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        mAdapter = new BookListAdapter(R.layout.book_list_item, mList);
        recyclerView.setAdapter(mAdapter);

        bookListPresenter = new BookListPresenter();
        bookListPresenter.attachView(this);

        mSmartRefreshLayout.autoRefresh();

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BookListEnitiy bookListEnitiy = mList.get(position);
                Intent intent = new Intent(mActivity, BookListDetailActivity.class);
                intent.putExtra("BookListEntity", bookListEnitiy);
                startActivity(intent);
            }
        });
    }

    private void loadData(int page) {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        bookListPresenter.getBookList(userId, page, PAGE_SIZE);
    }

    @Override
    public void getBookListSuccess(BookListResponse bookListResponse) {
        mSmartRefreshLayout.finishLoadMore();
        mSmartRefreshLayout.finishRefresh();
        if (page == 1) {
            mList.clear();
        }
        page++;
        mList.addAll(bookListResponse.pagedata);
        mAdapter.setNewData(mList);
    }

    @Override
    public void getBookListDetailSuccess(BookListDetailResponse bookListDetailResponse) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivPublicBack) {
            finish();
        }
    }
}
