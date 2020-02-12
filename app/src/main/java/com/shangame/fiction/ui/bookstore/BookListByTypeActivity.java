package com.shangame.fiction.ui.bookstore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
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
import com.shangame.fiction.adapter.BookNormalAdapter;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.SearchBookResponse;
import com.shangame.fiction.net.response.SearchHintResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.BookInfoEntity;
import com.shangame.fiction.ui.bookdetail.BookDetailActivity;
import com.shangame.fiction.ui.search.SearchBookContracts;
import com.shangame.fiction.ui.search.SearchBookPresenter;

import java.util.ArrayList;
import java.util.List;

public class BookListByTypeActivity extends BaseActivity implements View.OnClickListener, SearchBookContracts.View {

    private String title;
    private int bookStoreType;

    private int status;
    private int bookStoreChannel;

    private SmartRefreshLayout smartRefreshLayout;

    private BookNormalAdapter mAdapter;
    private List<BookInfoEntity> mList = new ArrayList<>();

    private int page = 1;
    private int pageSize = 20;
    private SearchBookPresenter mSearchBookPresenter;
    private boolean isFromMenu = false;

    public static void lunchActivity(Activity activity, int bookStoreType, String title, int bookStoreChannel) {
        Intent intent = new Intent(activity, BookListByTypeActivity.class);
        intent.putExtra("BookStoreType", bookStoreType);
        intent.putExtra("title", title);
        intent.putExtra("bookStoreChannel", bookStoreChannel);
        activity.startActivity(intent);
    }

    public static void lunchActivity(Context context, int bookStoreType, String title, int bookStoreChannel) {
        Intent intent = new Intent(context, BookListByTypeActivity.class);
        intent.putExtra("BookStoreType", bookStoreType);
        intent.putExtra("title", title);
        intent.putExtra("bookStoreChannel", bookStoreChannel);
        context.startActivity(intent);
    }

    public static void lunchActivity(Activity activity, String title, int bookStoreChannel, int status) {
        Intent intent = new Intent(activity, BookListByTypeActivity.class);
        intent.putExtra("bookStoreChannel", bookStoreChannel);
        intent.putExtra("isFromMenu", true);
        intent.putExtra("title", title);
        intent.putExtra("status", status);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_by_type);
        initParam();
        initTitle();
        initListView();
        initSmartRefreshLayout();
        initPresenter();
        smartRefreshLayout.autoRefresh();
    }

    private void initParam() {
        isFromMenu = getIntent().getExtras().getBoolean("isFromMenu");
        title = getIntent().getExtras().getString("title");
        status = getIntent().getExtras().getInt("status");
        bookStoreType = getIntent().getExtras().getInt("BookStoreType");
        bookStoreChannel = getIntent().getExtras().getInt("bookStoreChannel");
    }

    private void initTitle() {
        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(title);
    }

    private void initListView() {
        RecyclerView recyclerView = findViewById(R.id.listView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.public_line_horizontal));
        recyclerView.addItemDecoration(dividerItemDecoration);

        mAdapter = new BookNormalAdapter(R.layout.book_store_item_with_content, mList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BookInfoEntity entity = mList.get(position);
                BookDetailActivity.lunchActivity(mActivity, entity.bookid, ApiConstant.ClickType.FROM_CLICK);
            }
        });
    }

    private void initSmartRefreshLayout() {
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                loadBook(page);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                loadBook(page);
            }
        });
    }

    private void initPresenter() {
        mSearchBookPresenter = new SearchBookPresenter();
        mSearchBookPresenter.attachView(this);
    }

    private void loadBook(int page) {
        int userId = UserInfoManager.getInstance(mContext).getUserid();
        if (isFromMenu) {
            mSearchBookPresenter.getBookDataPage(userId, bookStoreChannel, status, page, pageSize);
        } else if (bookStoreType == BookStoreType.GuessYouLike) {
            mSearchBookPresenter.searchBook(userId, bookStoreType, "", bookStoreChannel, page, pageSize);
        } else {
            mSearchBookPresenter.loadMoreByTypeBook(userId, bookStoreType, bookStoreChannel, page, pageSize);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearchBookPresenter.detachView();
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivPublicBack) {
            finish();
        }
    }

    @Override
    public void searchBookSuccess(SearchBookResponse response) {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
        if (page == 1) {
            mList.clear();
        }
        mList.addAll(response.pagedata);
        mAdapter.setNewData(mList);
    }

    @Override
    public void loadMoreByTypeBookSuccess(SearchBookResponse response) {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
        if (page == 1) {
            mList.clear();
        }
        mList.addAll(response.pagedata);
        mAdapter.setNewData(mList);
    }

    @Override
    public void getBookDataPageSuccess(SearchBookResponse response) {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
        if (page == 1) {
            mList.clear();
        }
        mList.addAll(response.pagedata);
        mAdapter.setNewData(mList);
    }

    @Override
    public void getSearchHintSuccess(SearchHintResponse searchHintResponse) {

    }
}
