package com.shangame.fiction.ui.bookstore;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shangame.fiction.R;

/**
 * Create by Speedy on 2018/7/27
 */
public class BookInfoViewHolder extends RecyclerView.ViewHolder {

    public ImageView ivCover;
    public TextView tvBookName;
    public TextView tvContent;
    public TextView tvBookAuthor;

    public BookInfoViewHolder(View itemView) {
        super(itemView);
        ivCover = itemView.findViewById(R.id.ivCover);
        tvBookName = itemView.findViewById(R.id.tvBookName);
        tvContent = itemView.findViewById(R.id.tvContent);
        tvBookAuthor = itemView.findViewById(R.id.tvBookAuthor);
    }
}
