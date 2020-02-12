package com.shangame.fiction.ui.bookstore;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shangame.fiction.R;

/**
 * Create by Speedy on 2018/7/27
 */
public class BookWithHotDemandViewHolder  extends RecyclerView.ViewHolder{

    public View itemView;
    public ImageView ivNo;
    public ImageView ivCover;
    public TextView tvBookName;
    public TextView tvBookAuthor;

    public BookWithHotDemandViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.ivNo = itemView.findViewById(R.id.ivNo);
        this.ivCover = itemView.findViewById(R.id.ivCover);
        this.tvBookName = itemView.findViewById(R.id.tvBookName);
        this.tvBookAuthor = itemView.findViewById(R.id.tvBookAuthor);
    }
}
