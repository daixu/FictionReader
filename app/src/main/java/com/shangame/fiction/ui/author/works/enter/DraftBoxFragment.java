package com.shangame.fiction.ui.author.works.enter;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.net.response.ChapterResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 草稿箱
 */
public class DraftBoxFragment extends BaseFragment implements ChapterListContacts.View {

    private ChapterListPresenter mPresenter;
    private ChapterItemAdapter mAdapter;
    private List<ChapterResponse.PageDataBean> mData = new ArrayList<>();
    private int mBookId;
    private String mBookName;
    private SmartRefreshLayout mSmartRefreshLayout;
    private int mPageIndex = 1;

    public DraftBoxFragment() {
        // Required empty public constructor
    }

    public static DraftBoxFragment newInstance(int bookId, String bookName) {
        DraftBoxFragment fragment = new DraftBoxFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("bookId", bookId);
        bundle.putString("bookName", bookName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_draft_box, container, false);

        if (null != getArguments()) {
            mBookId = getArguments().getInt("bookId");
            mBookName = getArguments().getString("bookName");
        }
        initView(contentView);
        initListener();
        initPresenter();
        // loadData();
        return contentView;
    }

    private void initView(View contentView) {
        mSmartRefreshLayout = contentView.findViewById(R.id.smartRefreshLayout);
        RecyclerView recyclerView = contentView.findViewById(R.id.recycler_chapter_list);

        mAdapter = new ChapterItemAdapter(R.layout.item_chapter_item, mData, 0);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);

        contentView.findViewById(R.id.layout_chapter_count).setVisibility(View.GONE);
    }

    private void initListener() {
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPageIndex = 1;
                loadData(1);
            }
        });
        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPageIndex++;
                loadData(mPageIndex);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ChapterResponse.PageDataBean bean = mData.get(position);
                if (view.getId() == R.id.text_delete) {
                    deleteChapter(bean);
                }
                if (view.getId() == R.id.content) {
                    Intent intent = new Intent(mContext, EditContentActivity.class);
                    intent.putExtra("bookName", mBookName);
                    intent.putExtra("bookId", mBookId);
                    intent.putExtra("bean", bean);
                    startActivity(intent);
                }
            }
        });
    }

    private void initPresenter() {
        mPresenter = new ChapterListPresenter();
        mPresenter.attachView(this);
    }

    private void loadData(int pageIndex) {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        mPresenter.getChapter(userId, mBookId, pageIndex, 20, 1);
    }

    private void deleteChapter(ChapterResponse.PageDataBean bean) {
        int cid = bean.cid;
        int volume = 0;
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        mPresenter.deleteChapter(cid, mBookId, volume, userId);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void getChapterSuccess(ChapterResponse response) {
        mSmartRefreshLayout.finishLoadMore();
        mSmartRefreshLayout.finishRefresh();
        if (mPageIndex == 1) {
            mData.clear();
        }
        mData.addAll(response.pagedata);
        mAdapter.setNewData(mData);
    }

    @Override
    public void getChapterEmpty() {
        mSmartRefreshLayout.finishRefresh();
        mSmartRefreshLayout.finishLoadMore();
    }

    @Override
    public void getChapterFailure(String msg) {
        mSmartRefreshLayout.finishRefresh();
        mSmartRefreshLayout.finishLoadMore();
    }

    @Override
    public void deleteChapterSuccess() {
        Toast.makeText(mContext, "删除成功!", Toast.LENGTH_SHORT).show();
        loadData(1);
    }

    @Override
    public void deleteChapterFailure(String msg) {
        Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNotNetworkView() {
        super.showNotNetworkView();
        mSmartRefreshLayout.finishRefresh();
        mSmartRefreshLayout.finishLoadMore();
    }

    @Override
    public void showError(Throwable throwable) {
        super.showError(throwable);
        mSmartRefreshLayout.finishRefresh();
        mSmartRefreshLayout.finishLoadMore();
    }
}
