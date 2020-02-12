package com.shangame.fiction.ui.bookstore;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shangame.fiction.R;

/**
 * Create by Speedy on 2018/7/27
 */
public class EditRecommendHolder extends  RecyclerView.ViewHolder{

    public View itemView;

    public ImageView ivCover1;
    public TextView tvBookName1;
    public TextView tvBookAuthor1;

    public ImageView ivCover2;
    public TextView tvBookName2;
    public TextView tvBookAuthor2;

    public ImageView ivCover3;
    public TextView tvBookName3;
    public TextView tvBookAuthor3;

    public EditRecommendHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.ivCover1 = itemView.findViewById(R.id.ivCover1);
        this.tvBookName1 = itemView.findViewById(R.id.tvBookName1);
        this.tvBookAuthor1 = itemView.findViewById(R.id.tvBookAuthor1);

        this.ivCover2 = itemView.findViewById(R.id.ivCover2);
        this.tvBookName2 = itemView.findViewById(R.id.tvBookName2);
        this.tvBookAuthor2 = itemView.findViewById(R.id.tvBookAuthor2);

        this.ivCover3 = itemView.findViewById(R.id.ivCover3);
        this.tvBookName3 = itemView.findViewById(R.id.tvBookName3);
        this.tvBookAuthor3 = itemView.findViewById(R.id.tvBookAuthor3);
    }
}
