package com.shangame.fiction.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.TaskItem;

import java.util.List;

public class TaskAdapter extends BaseQuickAdapter<TaskItem, BaseViewHolder> {
    private int mType;

    public TaskAdapter(int layoutResId, @Nullable List<TaskItem> data, int type) {
        super(layoutResId, data);
        this.mType = type;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TaskItem item) {
        ImageView imageType = helper.getView(R.id.image_type);
        helper.setText(R.id.tvName, item.taskname);
        helper.setText(R.id.tvContent, item.packname);
        TextView tvState = helper.getView(R.id.tvState);

        switch (mType) {
            case 2:
            case 3:
                imageType.setImageResource(R.drawable.image_small_gold);
                break;
            default:
                imageType.setImageResource(R.drawable.small_red);
                break;
        }

        if (item.receive == 1) {
            tvState.setText("已完成");
            tvState.setEnabled(false);
            tvState.setTextColor(Color.parseColor("#BEBEBE"));
            tvState.setBackgroundResource(R.drawable.half_border_gray_bg);
        } else {
            tvState.setText("去完成");
            tvState.setEnabled(true);
            tvState.setTextColor(ContextCompat.getColorStateList(mContext, R.color.colorPrimary));
            tvState.setBackgroundResource(R.drawable.half_border_theme_color_bg);
        }

        helper.addOnClickListener(R.id.tvState);
    }
}
