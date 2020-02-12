package com.shangame.fiction.ui.my.comment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.MyCommentResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.bookdetail.BookDetailActivity;
import com.shangame.fiction.widget.GlideApp;
import com.shangame.fiction.widget.RemindFrameLayout;

/**
 * 我的评论 Activity
 * Create by Speedy on 2018/8/2
 */
public class MyCommentActivity extends BaseActivity implements View.OnClickListener, MyCommentContacts.View {

    private SmartRefreshLayout smartRefreshLayout;
    private RemindFrameLayout remindFrameLayout;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;

    private MyCommentPresenter myCommentPresenter;

    private int page;
    private int pageSize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comment);
        initView();
        initPresenter();
        smartRefreshLayout.autoRefresh();
    }

    private void initView() {
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.my_comment);

        findViewById(R.id.ivPublicBack).setOnClickListener(this);

        remindFrameLayout = findViewById(R.id.remindFrameLayout);

        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                loadComment(page);
            }
        });

        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                loadComment(page);
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.public_line_horizontal));
        recyclerView.addItemDecoration(dividerItemDecoration);

        myAdapter = new MyAdapter();
        myAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                if (myAdapter.getItemCount() > 0) {
                    remindFrameLayout.showContentView();
                } else {
                    remindFrameLayout.showRemindView();
                }
            }
        });
        recyclerView.setAdapter(myAdapter);
    }

    private void initPresenter() {
        myCommentPresenter = new MyCommentPresenter();
        myCommentPresenter.attachView(this);
    }

    private void loadComment(int page) {
        int userid = UserInfoManager.getInstance(mContext).getUserid();
        myCommentPresenter.getCommentList(userid, page, pageSize);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myCommentPresenter.detachView();
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
        remindFrameLayout.showRemindView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivPublicBack) {
            finish();
        }
    }

    @Override
    public void getCommentListSuccess(MyCommentResponse myCommentResponse) {
        smartRefreshLayout.finishLoadMore();
        smartRefreshLayout.finishRefresh();
        myAdapter.addAll(myCommentResponse.pagedata);
        myAdapter.notifyDataSetChanged();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivUserIcon;
        TextView tvUsername;
        TextView tvComment;
        TextView tvTime;
        TextView tvLike;
        TextView tvCommentCount;
        ImageView ivBookCover;
        TextView tvBookName;
        TextView tvAuthorName;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivUserIcon = itemView.findViewById(R.id.ivUserIcon);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvLike = itemView.findViewById(R.id.tvLike);
            tvCommentCount = itemView.findViewById(R.id.tvCommentCount);
            ivBookCover = itemView.findViewById(R.id.ivBookCover);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvAuthorName = itemView.findViewById(R.id.tvAuthorName);
        }
    }

    private class MyAdapter extends WrapRecyclerViewAdapter<MyCommentResponse.CommentBean, MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.my_comment_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final MyCommentResponse.CommentBean commentBean = getItem(position);
            holder.tvUsername.setText(commentBean.nickname);
            holder.tvComment.setText(commentBean.comment);
            holder.tvTime.setText(commentBean.creatortime);
            holder.tvLike.setText(String.valueOf(commentBean.pracount));
            holder.tvCommentCount.setText(String.valueOf(commentBean.replycount));
            holder.tvBookName.setText(commentBean.bookname);
            holder.tvAuthorName.setText(commentBean.author);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BookDetailActivity.lunchActivity(mActivity, commentBean.bookid, ApiConstant.ClickType.FROM_CLICK);
                }
            });

            // ImageLoader.with(mActivity).loadUserIcon(holder.ivUserIcon, commentBean.headimgurl);
            GlideApp.with(mActivity)
                    .load(commentBean.headimgurl)
                    .centerCrop()
                    .placeholder(R.drawable.default_head)
                    .into(holder.ivUserIcon);
            ImageLoader.with(mActivity).loadCover(holder.ivBookCover, commentBean.bookcover);
        }
    }
}
