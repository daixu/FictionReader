package com.shangame.fiction.ui.bookstore;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shangame.fiction.R;

/**
 * Create by Speedy on 2018/7/27
 */
public class SearchViewHolder extends  RecyclerView.ViewHolder{

    public View itemView;
    public ImageView ivCover;
    public TextView tvBookName;
    public TextView tvRead;

    public SearchViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.ivCover = itemView.findViewById(R.id.ivCover);
        this.tvBookName = itemView.findViewById(R.id.tvBookName);
        this.tvRead = itemView.findViewById(R.id.tvRead);
    }
}
