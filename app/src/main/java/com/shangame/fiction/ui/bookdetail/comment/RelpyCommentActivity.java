package com.shangame.fiction.ui.bookdetail.comment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.response.BookComment;
import com.shangame.fiction.net.response.BookCommentByTypeResponse;
import com.shangame.fiction.net.response.BookDetailCommentResponse;
import com.shangame.fiction.net.response.CommentReplyResponse;
import com.shangame.fiction.net.response.SendCommentResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.widget.GlideApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 回复评论
 * Create by Speedy on 2018/7/25
 */
public class RelpyCommentActivity extends BaseActivity implements View.OnClickListener, CommentContacts.View {

    private static final int PAGE_SIZE = 20;
    private ImageView ivCommentUserIcon;
    private TextView tvCommentUserName;
    private TextView tvCommentContent;
    private TextView tvCommentTime;
    private TextView tvCommentLike;
    private TextView tvCommentCount;
    private TextView tvTotal;
    private RecyclerView recyclerView;
    private CommentReplyAdapter commentReplyAdapter;
    private SmartRefreshLayout smartRefreshLayout;
    private BookComment bookComment;
    private long bookid;
    private CommentPresenter commentPresenter;
    private int mPage = 1;
    private EditText etComment;

    private long replyuserid;

    public static void lunchActivity(Activity activity, BookComment bookComment, long bookid) {
        Intent intent = new Intent(activity, RelpyCommentActivity.class);
        intent.putExtra("BookComment", bookComment);
        intent.putExtra("bookid", bookid);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_comment);
        bookComment = getIntent().getParcelableExtra("BookComment");
        bookid = getIntent().getLongExtra("bookid", 0);
        commentPresenter = new CommentPresenter();
        commentPresenter.attachView(this);
        initView();
        smartRefreshLayout.autoRefresh();
    }

    private void initView() {
        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.comment_detail);

        etComment = findViewById(R.id.etComment);
        findViewById(R.id.tvCommentLike).setOnClickListener(this);
        findViewById(R.id.btnSendComment).setOnClickListener(this);

        ivCommentUserIcon = findViewById(R.id.ivCommentUserIcon);
        tvCommentUserName = findViewById(R.id.tvCommentUserName);
        tvCommentContent = findViewById(R.id.tvCommentContent);
        tvCommentTime = findViewById(R.id.tvCommentTime);
        tvCommentLike = findViewById(R.id.tvCommentLike);
        tvCommentCount = findViewById(R.id.tvCommentCount);

        tvTotal = findViewById(R.id.tvTotal);

        tvCommentUserName.setText(bookComment.nickname);
        tvCommentContent.setText(bookComment.comment);
        tvCommentTime.setText(bookComment.creatortime);
        tvCommentCount.setVisibility(View.INVISIBLE);
        tvCommentLike.setText(String.valueOf(bookComment.pracount));

        if (bookComment.state == 1) {
            Drawable drawable = mActivity.getResources().getDrawable(R.drawable.like_s);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            tvCommentLike.setCompoundDrawables(drawable, null, null, null);
        } else {
            Drawable drawable = mActivity.getResources().getDrawable(R.drawable.like_n);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            tvCommentLike.setCompoundDrawables(drawable, null, null, null);
        }

        // ImageLoader.with(mActivity).loadUserIcon(ivCommentUserIcon,bookComment.headimgurl);

        GlideApp.with(mActivity)
                .load(bookComment.headimgurl)
                .centerCrop()
                .placeholder(R.drawable.default_head)
                .into(ivCommentUserIcon);

        recyclerView = findViewById(R.id.recyclerView);
        commentReplyAdapter = new CommentReplyAdapter(mActivity);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(commentReplyAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.public_line_horizontal));
        recyclerView.addItemDecoration(dividerItemDecoration);

        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                requestCommentReply(mPage++);
            }
        });

        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                requestCommentReply(mPage++);
            }
        });
    }


    private void requestCommentReply(int page) {
        commentPresenter.getCommentReplyList(bookComment.userid, bookid, bookComment.comid, page, PAGE_SIZE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        commentPresenter.detachView();
    }

    @Override
    public void dismissLoading() {
        super.dismissLoading();
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
    }

    @Override
    public void showError(Throwable throwable) {
        super.showError(throwable);
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivPublicBack:
                finish();
                break;
            case R.id.tvCommentLike:
                Map<String, Object> map = new HashMap<>();
                map.put("cid", bookComment.comid);
                map.put("userid", UserInfoManager.getInstance(mActivity).getUserid());
                map.put("bookid", bookid);
                map.put("chapterid", 0);
                map.put("comuserid", bookComment.userid);
                commentPresenter.sendLike(map);
                break;
            case R.id.btnSendComment:
                String comment = etComment.getText().toString();
                if (TextUtils.isEmpty(comment)) {
                    showToast(getString(R.string.comment_empty));
                } else {
                    sendComment(comment);
                }
                break;
            default:
                break;
        }
    }

    private void sendComment(String comment) {
        int userid = UserInfoManager.getInstance(mContext).getUserid();
        int lastIndex = comment.lastIndexOf("#");
        if (comment.startsWith("#") && lastIndex > 3) {
            comment = comment.substring(lastIndex + 1);
        } else {
            replyuserid = 0;
        }

        commentPresenter.sendComment(userid, bookid, bookComment.comid, replyuserid, comment);
    }

    @Override
    public void getBookCommentSuccess(BookDetailCommentResponse bookDetailCommentResponse) {

    }

    @Override
    public void getBookCommentByTypeSuccess(BookCommentByTypeResponse bookCommentByTypeResponse) {

    }

    @Override
    public void getCommentReplyListSuccess(CommentReplyResponse commentReplyResponse) {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();

        tvTotal.setText(getString(R.string.all_reply_count, commentReplyResponse.records));

        if (mPage == 2) {
            commentReplyAdapter.clear();
        }
        commentReplyAdapter.addAll(commentReplyResponse.pagedata);
        commentReplyAdapter.notifyDataSetChanged();
    }

    @Override
    public void sendCommentSuccess(SendCommentResponse sendCommentResponse) {
        showToast(getString(R.string.send_comment_success));
        etComment.setText(null);
        smartRefreshLayout.autoRefresh();
    }

    @Override
    public void sendLikeSuccess() {
        bookComment.pracount++;
        tvCommentLike.setText(String.valueOf(bookComment.pracount));
    }

    class CommentReplyAdapter extends RecyclerView.Adapter<CommentViewHolder> {

        private Activity mActivity;

        private List<CommentReplyResponse.CommentReplyBean> data = new ArrayList<>();

        public CommentReplyAdapter(Activity activity) {
            mActivity = activity;
        }

        public void addAll(List<CommentReplyResponse.CommentReplyBean> list) {
            if (list != null) {
                data.addAll(list);
            }
        }

        public void clear() {
            data.clear();
        }

        @NonNull
        @Override
        public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
            CommentViewHolder commentViewHolder = new CommentViewHolder(view);
            return commentViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull CommentViewHolder viewHolder, int position) {
            final CommentReplyResponse.CommentReplyBean commentReplyBean = data.get(position);

            viewHolder.tvCommentUserName.setText(commentReplyBean.nickname);
            viewHolder.tvCommentTime.setText(commentReplyBean.creatortime);
            viewHolder.tvCommentLike.setText(String.valueOf(commentReplyBean.pracount));
            viewHolder.tvCommentCount.setText(String.valueOf(commentReplyBean.replycount));

            if (commentReplyBean.replyuserid == 0) {
                viewHolder.tvCommentContent.setText(commentReplyBean.comment);
            } else {
                String reply = getString(R.string.reply);
                SpannableStringBuilder builder = new SpannableStringBuilder(reply + commentReplyBean.nickname + commentReplyBean.comment);
                builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), reply.length(), reply.length() + commentReplyBean.nickname.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                viewHolder.tvCommentContent.setText(builder);
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    replyuserid = commentReplyBean.userid;
                    String reply = "#" + getString(R.string.reply) + commentReplyBean.nickname + "# ";
//                    SpannableStringBuilder builder = new SpannableStringBuilder(reply + commentReplyBean.nickname + commentReplyBean.comment);
//                    builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),reply.length(),reply.length()+commentReplyBean.nickname.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//                    etComment.setText(builder);
                    etComment.setText(reply);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
