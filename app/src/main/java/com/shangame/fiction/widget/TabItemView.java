package com.shangame.fiction.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shangame.fiction.R;

/**
 * Create by Speedy on 2018/7/20
 */
public class TabItemView extends LinearLayout {

    /**
     * 显示红点类型
     */
    public static final int RED_DOT_TPYE = 0;

    /**
     * 显示数字类型
     */
    public static final int SHOW_NUMBER_TPYE = 1;

    private int showType;

    private ImageView ivTabIcon;

    private TextView tvTabText;

    private TextView tvTabMessageCount;
    private TextView tvTabMessageDot;

    private Drawable normalDrawable;
    private Drawable selectedDrawable;

    private String tabText;

    private final int default_text_color = Color.rgb(0x49, 0xC1, 0x20);
    private int normalColor;
    private int selectColor;

    public TabItemView(Context context) {
        this(context, null);
    }

    public TabItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public TabItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TabItemView);
        showType = ta.getInt(R.styleable.TabItemView_show_type, RED_DOT_TPYE);
        normalDrawable = ta.getDrawable(R.styleable.TabItemView_normal_src);
        selectedDrawable = ta.getDrawable(R.styleable.TabItemView_selected_src);
        boolean selected = ta.getBoolean(R.styleable.TabItemView_selected, false);
        tabText = ta.getString(R.styleable.TabItemView_text);

        normalColor = ta.getColor(R.styleable.TabItemView_text_normal_color, default_text_color);
        selectColor = ta.getColor(R.styleable.TabItemView_text_select_color, default_text_color);
        ta.recycle();

        View contentView = LayoutInflater.from(context).inflate(R.layout.tab_item, null);

        ivTabIcon = (ImageView) contentView.findViewById(R.id.ivTabIcon);
        tvTabText = (TextView) contentView.findViewById(R.id.tvTabText);
        tvTabMessageCount = (TextView) contentView.findViewById(R.id.tvTabMessageCount);
        tvTabMessageDot = (TextView) contentView.findViewById(R.id.tvTabMessageDot);

        tvTabText.setText(tabText);

        tvTabMessageCount.setVisibility(View.GONE);
        tvTabMessageDot.setVisibility(View.GONE);

        setSelected(selected);
        addView(contentView);
    }

    @Override
    public void setSelected(boolean selected) {
        if (selected) {
            ivTabIcon.setImageDrawable(selectedDrawable);
            tvTabText.setTextColor(selectColor);
        } else {
            ivTabIcon.setImageDrawable(normalDrawable);
            tvTabText.setTextColor(normalColor);
        }
    }

    public void setMessageNumber(int count) {
        if (count > 0) {
            if (showType == RED_DOT_TPYE) {
                tvTabMessageCount.setVisibility(View.GONE);
                tvTabMessageDot.setVisibility(View.VISIBLE);
            } else {
                tvTabMessageDot.setVisibility(View.GONE);
                tvTabMessageCount.setText(String.valueOf(count));
                tvTabMessageCount.setVisibility(View.VISIBLE);
            }
        } else {
            tvTabMessageCount.setVisibility(View.GONE);
            tvTabMessageDot.setVisibility(View.GONE);
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        final OnClickListener ocl = l;
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ocl.onClick(v);
                setSelected(true);
            }
        });
    }
}
