package com.shangame.fiction.ui.reader;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shangame.fiction.R;

/**
 * Create by Speedy on 2018/9/5
 */
public class ChapterOrderViewHolder extends RecyclerView.ViewHolder {

    TextView tvOrderTitle;
    TextView tvOrderDiscount;


    public ChapterOrderViewHolder(View itemView) {
        super(itemView);
        tvOrderTitle = itemView.findViewById(R.id.tvOrderTitle);
        tvOrderDiscount = itemView.findViewById(R.id.tvOrderDiscount);
    }
}
