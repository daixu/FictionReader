package com.shangame.fiction.ui.bookdetail.comment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.shangame.fiction.core.base.BaseFragment;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.BookCommentByTypeResponse;
import com.shangame.fiction.net.response.BookDetailCommentResponse;
import com.shangame.fiction.net.response.CommentReplyResponse;
import com.shangame.fiction.net.response.SendCommentResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;


public class CommentByTypeFragment extends BaseFragment implements CommentContacts.View{

    private int  type;
    private long  bookid;

    private RecyclerView recyclerView;
    private CommentAdapter hotCommentAdapter;

    private SmartRefreshLayout smartRefreshLayout;

    private CommentPresenter commentPresenter;

    private int page = 1;
    private int pageSize = 20;



    public static CommentByTypeFragment newInstance(int type,long bookid) {
        CommentByTypeFragment fragment = new CommentByTypeFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putLong("bookid", bookid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt("type");
            bookid = getArguments().getLong("bookid");
        }
        commentPresenter = new CommentPresenter();
        commentPresenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment_by_type, container, false);

        recyclerView =  view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));

        DividerItemDecoration  dividerItemDecoration = new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.public_line_horizontal));
        recyclerView.addItemDecoration(dividerItemDecoration);

        hotCommentAdapter = new CommentAdapter(mActivity,commentPresenter,bookid);
        recyclerView.setAdapter(hotCommentAdapter);

        smartRefreshLayout =  view.findViewById(R.id.smartRefreshLayout);
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


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refresh();
    }

    public void refresh(){
        smartRefreshLayout.autoRefresh();
    }


    private void loadComment(int page) {
        int userid = UserInfoManager.getInstance(mContext).getUserid();
        commentPresenter.getBookCommentByType(userid,bookid,type,page,pageSize);
    }


    @Override
    public void getBookCommentSuccess(BookDetailCommentResponse bookDetailCommentResponse) {

    }

    @Override
    public void getBookCommentByTypeSuccess(BookCommentByTypeResponse bookCommentByTypeResponse) {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
        if(page == 1){
            hotCommentAdapter.clear();
        }
        hotCommentAdapter.addAll(bookCommentByTypeResponse.pagedata);
        hotCommentAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(Throwable throwable) {
        super.showError(throwable);
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
    }

    @Override
    public void dismissLoading() {
        super.dismissLoading();
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
    }

    @Override
    public void getCommentReplyListSuccess(CommentReplyResponse commentReplyResponse) {

    }

    @Override
    public void sendCommentSuccess(SendCommentResponse sendCommentResponse) {

    }

    @Override
    public void sendLikeSuccess() {
        page = 1;
        loadComment(page);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        commentPresenter.detachView();
    }
}
