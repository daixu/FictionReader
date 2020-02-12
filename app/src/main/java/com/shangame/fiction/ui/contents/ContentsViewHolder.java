package com.shangame.fiction.ui.contents;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shangame.fiction.R;

/**
 * Create by Speedy on 2018/8/17
 */
public class ContentsViewHolder extends RecyclerView.ViewHolder {

    ImageView ivLockIcon;
    TextView tvChapterName;
    TextView tvVolumeName;
    TextView tvVolumeSize;


    public ContentsViewHolder(View itemView) {
        super(itemView);
        tvChapterName = itemView.findViewById(R.id.tvChapterName);
        ivLockIcon = itemView.findViewById(R.id.ivLockIcon);
        tvVolumeName = itemView.findViewById(R.id.tvVolumeName);
        tvVolumeSize = itemView.findViewById(R.id.tvVolumeSize);
    }
}
