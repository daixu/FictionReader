package com.shangame.fiction.ui.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.SearchSellWellAdapter;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.SearchBookResponse;
import com.shangame.fiction.net.response.SearchHintResponse;
import com.shangame.fiction.net.response.SearchInfoResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.bookdetail.BookDetailActivity;
import com.shangame.fiction.widget.RemindFrameLayout;
import com.shangame.fiction.widget.SearchBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索
 * Create by Speedy on 2018/7/25
 */
public class SearchBookActivity extends BaseActivity implements SearchInfoContracts.View, SearchBookContracts.View {

    private View searchInfoLayout;
    private HotSearchAdapter hotSearchAdapter;
    private SearchSellWellAdapter mSearchSellWellAdapter;

    private HistorySearchAdapter historySearchAdapter;

    private SearchInfoPresenter mSearchInfoPresenter;

    private SearchBookPresenter mSearchBookPresenter;

    private RecyclerView searchListView1;
    private SearchHintAdapter searchHintAdapter;

    private TextView tvOpenAll;

    private RecyclerView searchListView2;
    private View mViewLine;
    private SearchAdapter searchAdapter;

    private RemindFrameLayout remindFrameLayout;

    private int userId;

    private List<SearchInfoResponse.AlbumsDataBean> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);
        initSearchView();
        initHotSearch();
        initHistorySearch();
        initSearch();
        initRemindLayout();
        initPresenter();
        loadSearchInfo();
    }

    private void initSearchView() {
        searchInfoLayout = findViewById(R.id.searchInfoLayout);

        SearchBar searchBar = findViewById(R.id.search_bar);
        searchBar.setOnCancelLinstener(new SearchBar.OnCancelLinstener() {
            @Override
            public void onCancel() {
                finish();
            }
        });

        searchBar.setOnQueryTextListener(new SearchBar.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String queryText) {
                if (TextUtils.isEmpty(queryText)) {
                    searchInfoLayout.setVisibility(View.VISIBLE);
                    searchListView1.setVisibility(View.GONE);
                    tvOpenAll.setVisibility(View.GONE);
                    searchListView2.setVisibility(View.GONE);
                    mViewLine.setVisibility(View.GONE);
                    remindFrameLayout.showContentView();
                } else {
                    searchInfoLayout.setVisibility(View.GONE);
                    searchListView1.setVisibility(View.VISIBLE);
                    tvOpenAll.setVisibility(View.GONE);
                    searchListView2.setVisibility(View.VISIBLE);
                    mViewLine.setVisibility(View.VISIBLE);

                    mSearchBookPresenter.getSearchHint(userId, queryText);
                }
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String queryText) {
//                mSearchBookPresenter.searchBook(userid,0, queryText,BookStoreChannel.All,page,pageSize);
                return false;
            }
        });
    }

    private void initHotSearch() {
        RecyclerView hotSearchRecyclerView = findViewById(R.id.hotSearchRecyclerView);
        hotSearchRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        hotSearchAdapter = new HotSearchAdapter();
        hotSearchRecyclerView.setAdapter(hotSearchAdapter);

        RecyclerView recyclerSellWell = findViewById(R.id.recycler_sell_well);
        recyclerSellWell.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mSearchSellWellAdapter = new SearchSellWellAdapter(R.layout.hot_search_item, mList);
        recyclerSellWell.setAdapter(mSearchSellWellAdapter);

        mSearchSellWellAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SearchInfoResponse.AlbumsDataBean bean = mList.get(position);
                if (null != bean) {
                    ARouter.getInstance()
                            .build("/ss/listen/detail")
                            .withInt("albumId", bean.bookid)
                            .navigation();
                }
            }
        });
    }

    private void initHistorySearch() {
        RecyclerView searchHistoryRecyclerView = findViewById(R.id.searchHistoryRecyclerView);
        searchHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.public_line_horizontal));
        searchHistoryRecyclerView.addItemDecoration(dividerItemDecoration);

        historySearchAdapter = new HistorySearchAdapter();
        searchHistoryRecyclerView.setAdapter(historySearchAdapter);
    }

    private void initSearch() {
        searchListView1 = findViewById(R.id.searchListView1);
        searchListView1.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.public_line_horizontal));
        searchListView1.addItemDecoration(dividerItemDecoration);
        searchHintAdapter = new SearchHintAdapter(mActivity);
        searchListView1.setAdapter(searchHintAdapter);

        tvOpenAll = findViewById(R.id.tvOpenAll);
        tvOpenAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchHintAdapter.isShowAll()) {
                    searchHintAdapter.setShowAll(false);
                    tvOpenAll.setText("展开全部书架作品(" + searchHintAdapter.getData().size() + ")");
                } else {
                    searchHintAdapter.setShowAll(true);
                    tvOpenAll.setText("收起");
                }
            }
        });
        mViewLine = findViewById(R.id.view_line);

        searchListView2 = findViewById(R.id.searchListView2);
        searchListView2.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        searchListView2.addItemDecoration(dividerItemDecoration);
        searchAdapter = new SearchAdapter(mActivity);
        searchListView2.setAdapter(searchAdapter);

        searchAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                if (searchAdapter.getItemCount() > 0) {
                    remindFrameLayout.showContentView();
                } else {
                    remindFrameLayout.showRemindView();
                }
            }
        });
    }

    private void initRemindLayout() {
        remindFrameLayout = findViewById(R.id.remindFrameLayout);
    }

    private void initPresenter() {
        mSearchBookPresenter = new SearchBookPresenter();
        mSearchBookPresenter.attachView(this);

        mSearchInfoPresenter = new SearchInfoPresenter();
        mSearchInfoPresenter.attachView(this);
    }

    private void loadSearchInfo() {
        userId = UserInfoManager.getInstance(mContext).getUserid();
        mSearchInfoPresenter.getSearchInfo(userId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearchInfoPresenter.detachView();
        mSearchBookPresenter.detachView();
    }

    @Override
    public void getSearchInfoSuccess(SearchInfoResponse response) {
        hotSearchAdapter.addAll(response.retdata);
        hotSearchAdapter.notifyDataSetChanged();

        mList.clear();
        mList.addAll(response.albumsdata);
        mSearchSellWellAdapter.setNewData(mList);

        historySearchAdapter.addAll(response.histdata);
        historySearchAdapter.notifyDataSetChanged();
    }

    @Override
    public void searchBookSuccess(SearchBookResponse response) {
        searchInfoLayout.setVisibility(View.GONE);
        searchListView1.setVisibility(View.VISIBLE);
        searchAdapter.clear();
        searchAdapter.addAll(response.pagedata);
        searchAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadMoreByTypeBookSuccess(SearchBookResponse response) {
    }

    @Override
    public void getBookDataPageSuccess(SearchBookResponse response) {
    }

    @Override
    public void getSearchHintSuccess(SearchHintResponse response) {
        searchAdapter.clear();
        searchHintAdapter.clear();

        searchAdapter.addAll(response.bookdata);
        searchHintAdapter.addAll(response.shelverdata);

        searchHintAdapter.setShowAll(false);
        searchHintAdapter.notifyDataSetChanged();

        if (searchHintAdapter.getData().size() > 1) {
            tvOpenAll.setText("展开全部书架作品(" + searchHintAdapter.getData().size() + ")");
            tvOpenAll.setVisibility(View.VISIBLE);
        } else {
            tvOpenAll.setVisibility(View.GONE);
        }
        searchAdapter.notifyDataSetChanged();
    }

    private class HotSearchViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIndex;
        TextView tvBookName;

        public HotSearchViewHolder(View itemView) {
            super(itemView);
            ivIndex = itemView.findViewById(R.id.ivIndex);
            tvBookName = itemView.findViewById(R.id.tvBookName);
        }
    }

    private class HotSearchAdapter extends WrapRecyclerViewAdapter<SearchInfoResponse.HotSearchBean, HotSearchViewHolder> {

        @NonNull
        @Override
        public HotSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.hot_search_item, parent, false);
            return new HotSearchViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull HotSearchViewHolder holder, int position) {
            final SearchInfoResponse.HotSearchBean hotSearchBean = getItem(position);
            if (null != hotSearchBean) {
                holder.tvBookName.setText(hotSearchBean.bookname);
                switch (position) {
                    case 0:
                        holder.ivIndex.setImageResource(R.drawable.top_1);
                        break;
                    case 1:
                        holder.ivIndex.setImageResource(R.drawable.top_2);
                        break;
                    case 2:
                        holder.ivIndex.setImageResource(R.drawable.top_3);
                        break;
                    case 3:
                        holder.ivIndex.setImageResource(R.drawable.top_4);
                        break;
                    case 4:
                        holder.ivIndex.setImageResource(R.drawable.top_5);
                        break;
                    case 5:
                        holder.ivIndex.setImageResource(R.drawable.top_6);
                        break;
                    default:
                        holder.ivIndex.setVisibility(View.GONE);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        BookDetailActivity.lunchActivity(mActivity, hotSearchBean.bookid, ApiConstant.ClickType.FROM_SEARCH);
                    }
                });
            }
        }
    }

    private class HistorySearchViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookName;

        public HistorySearchViewHolder(View itemView) {
            super(itemView);
            tvBookName = itemView.findViewById(R.id.tvBookName);
        }
    }

    private class HistorySearchAdapter extends WrapRecyclerViewAdapter<SearchInfoResponse.HistoryDataBean, HistorySearchViewHolder> {

        @NonNull
        @Override
        public HistorySearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.history_search_item, parent, false);
            return new HistorySearchViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull HistorySearchViewHolder holder, int position) {
            final SearchInfoResponse.HistoryDataBean historyDataBean = getItem(position);
            if (null != historyDataBean) {
                holder.tvBookName.setText(historyDataBean.bookname);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (historyDataBean.booktype == 1) {
                            ARouter.getInstance()
                                    .build("/ss/listen/detail")
                                    .withInt("albumId", historyDataBean.bookid)
                                    .navigation();
                        } else {
                            BookDetailActivity.lunchActivity(mActivity, historyDataBean.bookid, ApiConstant.ClickType.FROM_SEARCH);
                        }
                    }
                });
            }
        }
    }
}
