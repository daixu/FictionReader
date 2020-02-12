package com.shangame.fiction.ui.bookdetail.comment;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.net.response.BookComment;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.widget.GlideApp;

import java.util.HashMap;
import java.util.Map;

/**
 * Create by Speedy on 2018/8/20
 */
public class CommentListAdapter extends WrapRecyclerViewAdapter<BookComment, CommentViewHolder> {

    private Activity mActivity;
    private CommentPresenter commentPresenter;
    private long bookid;

    public CommentListAdapter(Activity activity, CommentPresenter commentPresenter, long bookid) {
        this.mActivity = activity;
        this.commentPresenter = commentPresenter;
        this.bookid = bookid;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mActivity.getLayoutInflater().inflate(R.layout.comment_item, viewGroup, false);
        CommentViewHolder commentViewHolder = new CommentViewHolder(view);
        return commentViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder viewHolder, int position) {
        final BookComment bookComment = getItem(position);

        viewHolder.tvCommentUserName.setText(bookComment.nickname);
        viewHolder.tvCommentContent.setText(bookComment.comment);
        viewHolder.tvCommentContent.setMaxLines(2);
        viewHolder.tvCommentTime.setText(bookComment.creatortime);
        viewHolder.tvCommentCount.setText(String.valueOf(bookComment.replycount));
        viewHolder.tvCommentLike.setText(String.valueOf(bookComment.pracount));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelpyCommentActivity.lunchActivity(mActivity, bookComment, bookid);
            }
        });
        viewHolder.tvCommentCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelpyCommentActivity.lunchActivity(mActivity, bookComment, bookid);
            }
        });

        if (bookComment.state == 1) {
            Drawable drawable = mActivity.getResources().getDrawable(R.drawable.like_s);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            viewHolder.tvCommentLike.setCompoundDrawables(drawable, null, null, null);
        } else {
            Drawable drawable = mActivity.getResources().getDrawable(R.drawable.like_n);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            viewHolder.tvCommentLike.setCompoundDrawables(drawable, null, null, null);
        }

        // ImageLoader.with(mActivity).loadUserIcon(viewHolder.ivCommentUserIcon,bookComment.headimgurl);
        GlideApp.with(mActivity)
                .load(bookComment.headimgurl)
                .centerCrop()
                .placeholder(R.drawable.default_head)
                .into(viewHolder.ivCommentUserIcon);

        viewHolder.tvCommentLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<>();
                map.put("cid", bookComment.comid);
                map.put("userid", UserInfoManager.getInstance(mActivity).getUserid());
                map.put("bookid", bookid);
                map.put("chapterid", 0);
                map.put("comuserid", bookComment.userid);
                commentPresenter.sendLike(map);
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelpyCommentActivity.lunchActivity(mActivity, bookComment, bookid);
            }
        });
    }
}
