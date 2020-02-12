package com.shangame.fiction.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shangame.fiction.R;

/**
 * Create by Speedy on 2019/3/28
 */
public class WeekDayItem extends LinearLayout {

    private Context mContext;

    public TextView tvGift;
    public ImageView ivLine1;
    public ImageView ivDot;
    public ImageView ivLine2;
    public TextView tvDay;

    public WeekDayItem(Context context) {
        super(context);
        initView(context);
    }

    public WeekDayItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.week_day_item,this,false);

        tvGift = view.findViewById(R.id.tvGift);
        ivLine1 = view.findViewById(R.id.ivLine1);
        ivDot = view.findViewById(R.id.ivDot);
        ivLine2 = view.findViewById(R.id.ivLine2);
        tvDay = view.findViewById(R.id.tvDay);

        addView(view);
    }












}
