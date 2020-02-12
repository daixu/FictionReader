package com.shangame.fiction.ui.author.works;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.response.BookDataPageResponse;
import com.shangame.fiction.net.response.PageDataBean;
import com.shangame.fiction.storage.manager.UserInfoManager;

import java.util.ArrayList;
import java.util.List;

public class WorksListActivity extends BaseActivity implements View.OnClickListener, WorksListContacts.View {
    private WorksListAdapter mAdapter;
    private List<PageDataBean> mList = new ArrayList<>();
    private WorksListPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_works_list);

        initView();
        initListener();
        initPresenter();
        initData();
    }

    private void initPresenter() {
        mPresenter = new WorksListPresenter();
        mPresenter.attachView(this);
    }

    private void initData() {
        mPresenter.getBookData(UserInfoManager.getInstance(mContext).getUserid(), 1, 1000, 0);
    }

    private void initView() {
        TextView tvTitle = findViewById(R.id.tv_title);
        RecyclerView recyclerWorks = findViewById(R.id.recycler_works);

        tvTitle.setText("作品列表");

        recyclerWorks.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL
                , false));
        mAdapter = new WorksListAdapter(R.layout.item_works_list, mList);
        recyclerWorks.setAdapter(mAdapter);
    }

    private void initListener() {
        findViewById(R.id.img_back).setOnClickListener(this);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PageDataBean dataBean = mList.get(position);
                Intent intent = new Intent();
                intent.putExtra("bookName", dataBean.bookname);
                intent.putExtra("bookId", dataBean.bookid);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void getBookDataSuccess(BookDataPageResponse response) {
        mList.clear();
        mList.addAll(response.pagedata);
        mAdapter.setNewData(mList);
    }

    @Override
    public void getBookDataEmpty() {

    }

    @Override
    public void getBookDataFailure(String msg) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
