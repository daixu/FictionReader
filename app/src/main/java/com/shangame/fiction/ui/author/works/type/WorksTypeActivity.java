package com.shangame.fiction.ui.author.works.type;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.response.ClassAllFigResponse;

import java.util.ArrayList;
import java.util.List;

public class WorksTypeActivity extends BaseActivity implements WorksTypeContacts.View, View.OnClickListener {
    private WorksTypePresenter mPresenter;
    private int mSex;
    private WorksTypeAdapter mAdapter;

    private List<ClassAllFigResponse.SuperDataBean> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_works_type);

        mSex = getIntent().getIntExtra("sex", 0);

        initView();
        initPresenter();
        initData();
        initListener();
    }

    private void initView() {
        TextView tvTitle = findViewById(R.id.tv_title);
        RecyclerView recyclerType = findViewById(R.id.recycler_type);

        tvTitle.setText("作品类型");

        recyclerType.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        mAdapter = new WorksTypeAdapter(R.layout.item_works_type, mData, this);
        recyclerType.setAdapter(mAdapter);
    }

    private void initPresenter() {
        mPresenter = new WorksTypePresenter();
        mPresenter.attachView(this);
    }

    private void initData() {
        mPresenter.getClassAllFig(mSex);
    }

    private void initListener() {
        findViewById(R.id.img_back).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void getClassAllFigSuccess(ClassAllFigResponse response) {
        mData.clear();
        mData.addAll(response.superdata);
        mAdapter.setNewData(mData);
    }

    @Override
    public void getClassAllFigFailure(String msg) {

    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
