package com.shangame.fiction.ui.author.notice;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shangame.fiction.R;

/**
 * Create by Speedy on 2019/7/23
 */
public class NoticeInfoViewHolder extends RecyclerView.ViewHolder {
    TextView mTextTitle;
    TextView mTextTime;
    TextView mTextContent;
    RelativeLayout mLayoutDetail;

    public NoticeInfoViewHolder(@NonNull View itemView) {
        super(itemView);
        mTextTitle = itemView.findViewById(R.id.text_title);
        mTextTime = itemView.findViewById(R.id.text_time);
        mTextContent = itemView.findViewById(R.id.text_content);
        mLayoutDetail = itemView.findViewById(R.id.layout_detail);
    }
}
