package com.shangame.fiction.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.utils.ScreenUtils;


/**
 * （空白，无数据）提醒控件
 * Created by Speedy on 2017/9/11.
 */

public class RemindFrameLayout extends FrameLayout {

    private LinearLayout remindLayout;
    private ImageView remindImageView;
    private TextView remindTextView;

    public RemindFrameLayout(@NonNull Context context) {
        super(context);
    }

    public RemindFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        remindLayout = new LinearLayout(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        remindLayout.setLayoutParams(layoutParams);
        remindLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.Remind, defStyleAttr, 0);

        int resourceId = ta.getResourceId(R.styleable.Remind_android_src, 0);
        remindImageView = new ImageView(context);
        remindImageView.setImageResource(resourceId);
        remindLayout.addView(remindImageView, lp);

        String remindText = ta.getString(R.styleable.Remind_android_text);
        if (!TextUtils.isEmpty(remindText)) {
            float remindTextSize = ta.getDimensionPixelSize(R.styleable.Remind_android_textSize, 14);
            remindTextView = new TextView(context);
            remindTextView.setText(remindText);
            remindTextView.setPadding(0, 16, 0, 0);
            remindTextView.setTextSize(remindTextSize);
            remindTextView.setTextColor(getResources().getColor(R.color.secondary_text));
            lp.topMargin = ScreenUtils.dpToPxInt(context, 10);
            remindLayout.addView(remindTextView, lp);
        }
        remindLayout.setVisibility(View.GONE);
        addView(remindLayout);
        ta.recycle();
    }

    public RemindFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public void showRemindView() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view == remindLayout) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }

    public void showContentView() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view == remindLayout) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setRemindImage(int resId) {
        remindImageView.setImageResource(resId);
    }

    public void setRemindText(String text) {
        remindTextView.setText(text);
    }


}
