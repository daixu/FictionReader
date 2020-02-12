package com.shangame.fiction.ui.listen.directory;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lxj.xpopup.core.BottomPopupView;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.DirectoryAdapter;
import com.shangame.fiction.net.response.AlbumChapterDetailResponse;
import com.shangame.fiction.net.response.AlbumChapterResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;

import java.util.ArrayList;
import java.util.List;

public class DirectoryPopupWindow extends BottomPopupView implements DirectoryContacts.View {
    private Context mContext;
    private DirectoryAdapter mAdapter;
    private List<AlbumChapterResponse.PageDataBean> mList = new ArrayList<>();

    private DirectoryPresenter mPresenter;
    private int albumId;
    private int chapterId;
    private int pageIndex = 1;
    private int pageSize = 30;
    private TextView mTextTotal;
    private OnClickItemListener onClickItemListener;
    private boolean isEnd = false;

    public DirectoryPopupWindow(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public DirectoryPopupWindow(@NonNull Context context, int albumId, int chapterId) {
        super(context);
        mContext = context;
        this.albumId = albumId;
        this.chapterId = chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
        if (null != mAdapter) {
            mAdapter.setChapterId(chapterId);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
    }

    private void initPresenter() {
        mPresenter = new DirectoryPresenter();
        mPresenter.attachView(this);
    }

    private void loadData(int pageIndex, int pageSize) {
        long userId = UserInfoManager.getInstance(mContext).getUserid();

        mPresenter.getAlbumChapter(userId, albumId, pageIndex, pageSize, 0);
    }

    @Override
    protected void onShow() {
        super.onShow();
        initPresenter();
        pageIndex = 1;
        loadData(pageIndex, pageSize);
    }

    private void initView() {
        mTextTotal = findViewById(R.id.text_total);
        findViewById(R.id.tv_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        RecyclerView recyclerDirectory = findViewById(R.id.recycler_directory);
        recyclerDirectory.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new DirectoryAdapter(R.layout.item_directory, mList, 1);
        mAdapter.setChapterId(chapterId);
        recyclerDirectory.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (isEnd) {
                    mAdapter.loadMoreEnd();
                } else {
                    loadData(pageIndex, pageSize);
                }
            }
        }, recyclerDirectory);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AlbumChapterResponse.PageDataBean bean = mList.get(position);
                onClickItemListener.onItemClick(bean);
                dismiss();
            }
        });
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_window_directory;
    }

    @Override
    public void getAlbumChapterSuccess(AlbumChapterResponse response) {
        if (null == response) {
            return;
        }
        mTextTotal.setText(mContext.getString(R.string.listen_chapter_total, response.records));
        if (null == response.pagedata) {
            return;
        }
        if (pageIndex == 1) {
            mList.clear();
        }
        if (pageSize > response.pagedata.size()) {
            isEnd = true;
        } else {
            isEnd = false;
            pageIndex++;
        }
        mList.addAll(response.pagedata);
        mAdapter.setNewData(mList);
        mAdapter.loadMoreComplete();
    }

    @Override
    public void getAlbumChapterFailure(String msg) {

    }

    @Override
    public void getAlbumChapterDetailSuccess(AlbumChapterDetailResponse response, AlbumChapterResponse.PageDataBean bean) {

    }

    @Override
    public void getAlbumChapterDetailFailure(String msg) {

    }

    @Override
    public void setAdvertLogSuccess() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showNotNetworkView() {

    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showError(Throwable throwable) {

    }

    @Override
    public void lunchLoginActivity() {

    }

    public interface OnClickItemListener {
        void onItemClick(AlbumChapterResponse.PageDataBean bean);
    }

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }
}
