package com.shangame.fiction.ui.bookstore.choice;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.ChoiceListAdapter;
import com.shangame.fiction.core.base.BaseLazyFragment;
import com.shangame.fiction.net.response.NewsResp;
import com.shangame.fiction.storage.db.BookReadProgressDao;
import com.shangame.fiction.storage.manager.DbManager;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.manager.VisitorDbManager;
import com.shangame.fiction.storage.model.BookBrowseHistory;
import com.shangame.fiction.storage.model.BookReadProgress;
import com.shangame.fiction.ui.reader.ReadActivity;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.ArrayList;
import java.util.List;

public class ChoiceItemFragment extends BaseLazyFragment implements ChoiceContacts.View {

    private ChoiceListAdapter mAdapter;
    private List<NewsResp.DataBean.PageDataBean> mData = new ArrayList<>();
    private SmartRefreshLayout mSmartRefreshLayout;
    private RecyclerView mRecyclerView;

    private ChoicePresenter mPresenter;
    private int pageIndex = 1;
    private int maleChannel = 0;

    public static ChoiceItemFragment newInstance(int type) {
        ChoiceItemFragment fragment = new ChoiceItemFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            maleChannel = getArguments().getInt("type", 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_choice_item, container, false);
        initView(contentView);
        initSmartRefreshLayout(contentView);
        return contentView;
    }

    private void initView(View contentView) {
        mRecyclerView = contentView.findViewById(R.id.recycler_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ChoiceListAdapter(mData);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NewsResp.DataBean.PageDataBean dataBean = mData.get(position);
                saveReadHistory(dataBean);
                openBook(dataBean.bookid, dataBean.chapterid);
            }
        });
    }

    private void saveReadHistory(NewsResp.DataBean.PageDataBean dataBean) {
        if (dataBean != null) {
            BookBrowseHistory bookBrowseHistory = new BookBrowseHistory();
            bookBrowseHistory.bookid = dataBean.bookid;
            bookBrowseHistory.bookname = dataBean.bookname;
            bookBrowseHistory.bookcover = dataBean.bookcover;
            bookBrowseHistory.bookshelves = dataBean.bookShelves;
            bookBrowseHistory.readTime = System.currentTimeMillis();

            VisitorDbManager.getDaoSession(mContext).getBookBrowseHistoryDao().insertOrReplace(bookBrowseHistory);
        }
    }

    private void initSmartRefreshLayout(View contentView) {
        mSmartRefreshLayout = contentView.findViewById(R.id.smartRefreshLayout);
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageIndex = 1;
                loadData();
            }
        });

        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });
    }

    private void openBook(int bookId, int chapterId) {
        BookReadProgressDao bookReadProgressDao = DbManager.getDaoSession(mContext).getBookReadProgressDao();
        List<BookReadProgress> bookReadProgressList = bookReadProgressDao.queryBuilder().where(BookReadProgressDao.Properties.BookId.eq(bookId)).build().list();

        if (bookReadProgressList != null && bookReadProgressList.size() > 0) {
            BookReadProgress bookReadProgress = bookReadProgressList.get(0);
            int pageIndex = bookReadProgress.pageIndex;
            ReadActivity.lunchActivity(mActivity, bookReadProgress.bookId, bookReadProgress.chapterId, pageIndex, 1);
        } else {
            ReadActivity.lunchActivity(mActivity, bookId, chapterId);
        }
    }

    private void loadData() {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        mPresenter.getNewMediaList(userId, pageIndex, 20, maleChannel);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != mPresenter) {
            mPresenter.detachView();
        }
    }

    @Override
    protected void onLazyLoad() {
        initPresenter();
        pageIndex = 1;
        loadData();
    }

    private void initPresenter() {
        mPresenter = new ChoicePresenter();
        mPresenter.attachView(this);
    }

    public void scrollToTop() {
        Log.e("hhh", "scrollToTop");
        if (null != mRecyclerView) {
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    @Override
    public void getNewMediaListSuccess(NewsResp.DataBean dataBean) {
        mSmartRefreshLayout.finishRefresh();
        if (null != dataBean) {
            if (pageIndex == 1) {
                mData.clear();
            }
            mSmartRefreshLayout.finishLoadMore();
            pageIndex++;
            mData.addAll(dataBean.pagedata);
            mAdapter.setNewData(mData);
        }
    }

    @Override
    public void getNewMediaListFailure(String msg) {
        Log.e("hhh", "getNewMediaListFailure msg= " + msg);
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }
}
