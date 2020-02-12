package com.shangame.fiction.ui.author.writing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.net.response.BookAllDataResponse;
import com.shangame.fiction.net.response.BookDataBean;
import com.shangame.fiction.net.response.BookDataPageResponse;
import com.shangame.fiction.net.response.PageDataBean;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.author.notice.ReviewInfoActivity;
import com.shangame.fiction.ui.author.works.CreateWorksFirstActivity;
import com.shangame.fiction.ui.author.works.enter.EditChapterActivity;
import com.shangame.fiction.ui.author.works.setting.WorksSettingActivity;

import java.util.ArrayList;
import java.util.List;

public class WritingFragment extends BaseFragment implements WritingContacts.View, View.OnClickListener {
    private ImageView mImgOption;
    private ImageView mImgBack;
    private WritingPresenter mPresenter;
    private WritingAdapter mAdapter;

    private List<BookDataBean> mData = new ArrayList<>();
    private List<PageDataBean> mListData = new ArrayList<>();

    public WritingFragment() {
        // Required empty public constructor
    }

    public static WritingFragment newInstance() {
        WritingFragment fragment = new WritingFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_writing, container, false);
        initView(contentView);
        initPresenter();
        initListener();
        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initListener() {
        mImgOption.setOnClickListener(this);
        mImgBack.setOnClickListener(this);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.img_setting: {
                        Intent intent = new Intent(mContext, WorksSettingActivity.class);
                        intent.putExtra("bookId", mListData.get(position).bookid);
                        mContext.startActivity(intent);
                    }
                    break;
                    case R.id.btn_edit: {
                        Intent intent = new Intent(mContext, EditChapterActivity.class);
                        intent.putExtra("bookId", mListData.get(position).bookid);
                        intent.putExtra("bookName", mListData.get(position).bookname);
                        mContext.startActivity(intent);
                    }
                    break;
                    case R.id.text_label: {
                        Intent intent = new Intent(mContext, ReviewInfoActivity.class);
                        intent.putExtra("bookId", mListData.get(position).bookid);
                        mContext.startActivity(intent);
                    }
                    break;
                    default:
                        break;
                }
            }
        });
    }

    private void initView(View contentView) {
        TextView tvTitle = contentView.findViewById(R.id.tv_title);
        tvTitle.setText("写作");
        mImgOption = contentView.findViewById(R.id.img_option);
        mImgOption.setVisibility(View.VISIBLE);

        mImgBack = contentView.findViewById(R.id.img_back);

        RecyclerView recyclerWorks = contentView.findViewById(R.id.recycler_works);
        mAdapter = new WritingAdapter(R.layout.item_writing, null);
        recyclerWorks.setAdapter(mAdapter);
        recyclerWorks.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        View emptyView = LayoutInflater.from(this.getActivity()).inflate(R.layout.layout_empty_view, null);
        emptyView.findViewById(R.id.btn_create).setOnClickListener(this);

        mAdapter.setEmptyView(emptyView);
    }

    private void initPresenter() {
        mPresenter = new WritingPresenter();
        mPresenter.attachView(this);
    }

    private void initData() {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        // mPresenter.getBookAllData(userId);
        mPresenter.getBookData(userId, 1, 1000, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                if (null != getActivity()) {
                    getActivity().finish();
                }
                break;
            case R.id.img_option:
            case R.id.btn_create:
                Intent intent = new Intent(mContext, CreateWorksFirstActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void getBookAllDataSuccess(BookAllDataResponse response) {
        Log.e("hhh", "response= " + response);
        mData.clear();
        mData.addAll(response.bookdata);
        // mAdapter.setNewData(mData);
    }

    @Override
    public void getBookAllDataEmpty() {
        Log.e("hhh", "getBookAllDataEmpty");
    }

    @Override
    public void getBookAllDataFailure(String msg) {
        Log.e("hhh", "msg= " + msg);
    }

    @Override
    public void getBookDataSuccess(BookDataPageResponse response) {
        Log.e("hhh", "response= " + response);
        mListData.clear();
        mListData.addAll(response.pagedata);
        mAdapter.setNewData(mListData);
    }

    @Override
    public void getBookDataEmpty() {
        Log.e("hhh", "getBookDataEmpty");
    }

    @Override
    public void getBookDataFailure(String msg) {
        Log.e("hhh", "msg= " + msg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
