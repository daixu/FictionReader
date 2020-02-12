package com.shangame.fiction.ui.listen.directory;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lxj.xpopup.core.BottomPopupView;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.DirectorySelectionAdapter;
import com.shangame.fiction.net.response.AlbumSelectionResponse;
import com.shangame.fiction.widget.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class SelectionPopupWindow extends BottomPopupView implements SelectionContacts.View {
    private SelectionPresenter mPresenter;
    private int albumId;
    private Context mContext;
    private DirectorySelectionAdapter mAdapter;
    private List<AlbumSelectionResponse.SelectModeBean> mList = new ArrayList<>();
    private int size;
    private String mRemark;
    private OnClickItemListener onClickItemListener;

    public SelectionPopupWindow(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public SelectionPopupWindow(@NonNull Context context, int albumId) {
        super(context);
        this.mContext = context;
        this.albumId = albumId;
    }

    public void setRemark(String remark) {
        this.mRemark = remark;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_window_selection;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
        initPresenter();
        loadData();
    }

    private void initPresenter() {
        mPresenter = new SelectionPresenter();
        mPresenter.attachView(this);
    }

    private void loadData() {
        mPresenter.getAlbumSelections(albumId);
    }

    @Override
    protected void doAfterShow() {
        super.doAfterShow();

        if (TextUtils.isEmpty(mRemark)) {
            mRemark = "全部";
        }
        mAdapter.setRemark(mRemark);
        mAdapter.notifyDataSetChanged();
        Log.e("hhh", "mRemark= " + mRemark);
    }

    private void initView() {
        findViewById(R.id.image_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        RecyclerView recyclerDirectory = findViewById(R.id.recycler_directory);
        recyclerDirectory.setLayoutManager(new GridLayoutManager(mContext, 4));
        recyclerDirectory.addItemDecoration(new SpaceItemDecoration(20));
        mAdapter = new DirectorySelectionAdapter(R.layout.item_selection, mList);
        recyclerDirectory.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AlbumSelectionResponse.SelectModeBean bean = mList.get(position);
                onClickItemListener.onItemClick(bean, size);
                dismiss();
            }
        });
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        mPresenter.detachView();
    }

    @Override
    public void getAlbumSelectionsSuccess(AlbumSelectionResponse response) {
        if (null != response) {
            size = response.pagesize;
            mList.clear();
            mList.addAll(response.selectmode);
            if (TextUtils.isEmpty(mRemark)) {
                mRemark = "全部";
            }
            mAdapter.setRemark(mRemark);
            mAdapter.setNewData(mList);
        }
    }

    @Override
    public void getAlbumSelectionsFailure(String msg) {

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
        void onItemClick(AlbumSelectionResponse.SelectModeBean bean, int pageSize);
    }

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }
}
