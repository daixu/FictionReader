package com.shangame.fiction.ui.my.data;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.makeramen.roundedimageview.RoundedImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.CommentAdapter;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.GetReadStatusResponse;
import com.shangame.fiction.net.response.MyCommentResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.bookdetail.BookDetailActivity;
import com.shangame.fiction.ui.setting.personal.PersonalProfileActivity;
import com.shangame.fiction.widget.GlideApp;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/ss/edit/data")
public class EditDataActivity extends BaseActivity implements EditDataContacts.View, View.OnClickListener {
    private EditDataPresenter mPresenter;
    private int pageIndex = 1;
    private SmartRefreshLayout smartRefreshLayout;
    private List<MyCommentResponse.CommentBean> mData = new ArrayList<>();
    private CommentAdapter mAdapter;

    private TextView mTextDayCount;
    private TextView mTextBookCount;
    private TextView mTextPageCount;
    private TextView mTextHourCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        initView();
        initPresenter();
        initData();
    }

    private void initView() {
        mTextDayCount = findViewById(R.id.text_day_count);
        mTextBookCount = findViewById(R.id.text_book_count);
        mTextPageCount = findViewById(R.id.text_page_count);
        mTextHourCount = findViewById(R.id.text_hour_count);

        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.tv_edit_data).setOnClickListener(this);
        RoundedImageView imageAvatar = findViewById(R.id.image_avatar);
        TextView textUserName = findViewById(R.id.text_user_name);
        UserInfoManager userInfoManager = UserInfoManager.getInstance(mContext);

        // ImageLoader.with(mContext).loadUserIcon(imageAvatar, userInfoManager.getUserInfo().headimgurl, R.drawable.default_head, 60, 60);

        GlideApp.with(mContext)
                .load(userInfoManager.getUserInfo().headimgurl)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.default_head)
                .into(imageAvatar);

        textUserName.setText(userInfoManager.getUserInfo().nickname);

        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageIndex = 1;
                loadComment();
            }
        });

        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageIndex++;
                loadComment();
            }
        });

        RecyclerView recyclerComment = findViewById(R.id.recycler_comment);
        recyclerComment.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new CommentAdapter(R.layout.item_my_comment, mData);

        View emptyView = LayoutInflater.from(this).inflate(R.layout.layout_empty_view_1, null);
        mAdapter.setEmptyView(emptyView);

        recyclerComment.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (null != mData) {
                    MyCommentResponse.CommentBean commentBean = mData.get(position);
                    BookDetailActivity.lunchActivity(mActivity, commentBean.bookid, ApiConstant.ClickType.FROM_CLICK);
                }
            }
        });
    }

    private void initPresenter() {
        mPresenter = new EditDataPresenter();
        mPresenter.attachView(this);
    }

    private void initData() {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        mPresenter.getReadStatus(userId);
        loadComment();
    }

    private void loadComment() {
        long userId = UserInfoManager.getInstance(mContext).getUserid();

        mPresenter.getCommentList(userId, pageIndex, 20);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void showNotNetworkView() {
        super.showNotNetworkView();
        smartRefreshLayout.finishLoadMore();
        smartRefreshLayout.finishRefresh();
    }

    @Override
    public void showError(Throwable throwable) {
        super.showError(throwable);
        smartRefreshLayout.finishLoadMore();
        smartRefreshLayout.finishRefresh();
    }

    @Override
    public void getCommentListSuccess(MyCommentResponse response) {
        smartRefreshLayout.finishLoadMore();
        smartRefreshLayout.finishRefresh();
        if (pageIndex == 1) {
            mData.clear();
        }
        mData.addAll(response.pagedata);
        mAdapter.setNewData(mData);
    }

    @Override
    public void getReadStatusSuccess(GetReadStatusResponse response) {
        if (null != response) {
            mTextDayCount.setText(getString(R.string.read_day, response.readingdays));
            mTextBookCount.setText(getString(R.string.read_ben, response.bookcount));
            mTextPageCount.setText(getString(R.string.read_chapter, response.chaptercount));
            long hour = response.readingtime / 60 / 60;
            mTextHourCount.setText(getString(R.string.read_hour, hour));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_edit_data:
                startActivity(new Intent(mContext, PersonalProfileActivity.class));
                break;
            default:
                break;
        }
    }
}
