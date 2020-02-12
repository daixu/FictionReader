package com.shangame.fiction.ui.bookstore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.BookNormalAdapter;
import com.shangame.fiction.core.base.BaseFragment;
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

/**
 * 完本
 * Create by Speedy on 2019/3/7
 */
public class BookEndFragment extends BaseFragment implements View.OnClickListener, SearchBookContracts.View {

    private int userid;

    private int status = 1;
    private int bookStoreChannel;

    private SmartRefreshLayout smartRefreshLayout;

    private BookNormalAdapter mAdapter;
    private List<BookInfoEntity> mList = new ArrayList<>();

    private int page = 1;
    private int pageSize = 20;
    private SearchBookPresenter mSearchBookPresenter;

    public static BookEndFragment newInstance(int bookStoreChannel) {
        BookEndFragment fragment = new BookEndFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("bookStoreChannel", bookStoreChannel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookStoreChannel = getArguments().getInt("bookStoreChannel", BookStoreChannel.GIRL);
        userid = UserInfoManager.getInstance(mContext).getUserid();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_end, null);
        initSmartRefreshLayout(view);
        initListView(view);
        initPresenter();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        smartRefreshLayout.autoRefresh();
    }

    private void initSmartRefreshLayout(View view) {
        smartRefreshLayout = view.findViewById(R.id.smartRefreshLayout);
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

    private void initListView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.listView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.public_line_horizontal));
        recyclerView.addItemDecoration(dividerItemDecoration);
        mAdapter = new BookNormalAdapter(R.layout.book_store_item_with_content, null);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BookInfoEntity entity = mList.get(position);
                BookDetailActivity.lunchActivity(mActivity, entity.bookid, ApiConstant.ClickType.FROM_CLICK);
            }
        });
    }

    private void initPresenter() {
        mSearchBookPresenter = new SearchBookPresenter();
        mSearchBookPresenter.attachView(this);
    }

    private void loadBook(int page) {
        mSearchBookPresenter.getBookDataPage(userid, bookStoreChannel, status, page, pageSize);
    }

    @Override
    public void searchBookSuccess(SearchBookResponse searchBookResponse) {

    }

    @Override
    public void loadMoreByTypeBookSuccess(SearchBookResponse searchBookResponse) {

    }

    @Override
    public void getBookDataPageSuccess(SearchBookResponse searchBookResponse) {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
        if (page == 1) {
            mList.clear();
        }
        mList.addAll(searchBookResponse.pagedata);
        mAdapter.setNewData(mList);
    }

    @Override
    public void getSearchHintSuccess(SearchHintResponse searchHintResponse) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivPublicBack) {
            if (null != getActivity()) {
                getActivity().finish();
            }
        }
    }
}
