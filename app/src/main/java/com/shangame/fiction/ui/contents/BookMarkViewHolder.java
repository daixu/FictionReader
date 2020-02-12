package com.shangame.fiction.ui.contents;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shangame.fiction.R;

/**
 * Create by Speedy on 2018/9/10
 */
public class BookMarkViewHolder extends RecyclerView.ViewHolder {

    TextView tvChapterName;
    TextView tvTime;
    TextView tvbookMark;

    public BookMarkViewHolder(View itemView) {
        super(itemView);
        tvChapterName = itemView.findViewById(R.id.tvChapterName);
        tvTime = itemView.findViewById(R.id.tvTime);
        tvbookMark = itemView.findViewById(R.id.tvbookMark);
    }
}
