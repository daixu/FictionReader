package com.shangame.fiction.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.MyCommentResponse;
import com.shangame.fiction.widget.GlideApp;

import java.util.List;

public class CommentAdapter extends BaseQuickAdapter<MyCommentResponse.CommentBean, BaseViewHolder> {
    public CommentAdapter(int layoutResId, @Nullable List<MyCommentResponse.CommentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MyCommentResponse.CommentBean item) {
        ImageView imageAvatar = helper.getView(R.id.image_avatar);
        ImageView imageCover = helper.getView(R.id.image_cover);
        // ImageLoader.with(mContext).loadUserIcon(imageAvatar, item.headimgurl, R.drawable.default_head, 30, 30);

        GlideApp.with(mContext)
                .load(item.headimgurl)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.default_head)
                .into(imageAvatar);

        helper.setText(R.id.text_user_name, item.nickname);
        helper.setText(R.id.text_comment, item.comment);
        helper.setText(R.id.text_time, item.creatortime);
        helper.setText(R.id.text_like, item.pracount + "");
        helper.setText(R.id.text_comment_count, item.replycount + "");

        GlideApp.with(mContext)
                .load(item.bookcover)
                .placeholder(R.drawable.default_cover)
                .centerCrop()
                .into(imageCover);

        helper.setText(R.id.text_book_name, item.bookname);
        helper.setText(R.id.text_author_name, item.author);

    }
}
