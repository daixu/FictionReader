package com.shangame.fiction.ui.bookdetail.comment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shangame.fiction.R;

/**
 * 评论
 * Create by Speedy on 2018/7/30
 */
public class CommentViewHolder extends RecyclerView.ViewHolder {

    public ImageView ivCommentUserIcon;
    public TextView tvCommentUserName;
    public TextView tvCommentContent;
    public TextView tvCommentTime;
    public TextView tvCommentLike;
    public TextView tvCommentCount;

    public CommentViewHolder(View itemView) {
        super(itemView);
        ivCommentUserIcon = itemView.findViewById(R.id.ivCommentUserIcon);
        tvCommentUserName = itemView.findViewById(R.id.tvCommentUserName);
        tvCommentContent = itemView.findViewById(R.id.tvCommentContent);
        tvCommentTime = itemView.findViewById(R.id.tvCommentTime);
        tvCommentLike = itemView.findViewById(R.id.tvCommentLike);
        tvCommentCount = itemView.findViewById(R.id.tvCommentCount);
    }
}
