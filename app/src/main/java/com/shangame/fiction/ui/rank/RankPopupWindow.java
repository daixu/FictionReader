package com.shangame.fiction.ui.rank;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BasePopupWindow;

/**
 * Create by Speedy on 2018/8/1
 */
public class RankPopupWindow extends BasePopupWindow implements View.OnClickListener{

    private TextView tvRankWeek;
    private TextView tvRankMonth;
    private TextView tvRankAll;

    private OnRankChangeLinster onRankChangeLinster;

    public RankPopupWindow( Activity activity,int dayType) {
        super(activity);
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setContentView(R.layout.popup_window_rank);

        tvRankWeek = findViewById(R.id.tvRankWeek);
        tvRankMonth = findViewById(R.id.tvRankMonth);
        tvRankAll = findViewById(R.id.tvRankAll);

        tvRankWeek.setOnClickListener(this);
        tvRankMonth.setOnClickListener(this);
        tvRankAll.setOnClickListener(this);

        Drawable drawable = mActivity.getResources().getDrawable(R.drawable.checked);
        drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());

        switch (dayType){
            case RankDayType.RANK_WEEK:
                tvRankWeek.setCompoundDrawables(drawable,null,null,null);
                tvRankMonth.setCompoundDrawables(null,null,null,null);
                tvRankAll.setCompoundDrawables(null,null,null,null);
                break;
            case RankDayType.RANK_MONTH:
                tvRankWeek.setCompoundDrawables(null,null,null,null);
                tvRankMonth.setCompoundDrawables(drawable,null,null,null);
                tvRankAll.setCompoundDrawables(null,null,null,null);
                break;
            case RankDayType.RANK_ALL:
                tvRankWeek.setCompoundDrawables(null,null,null,null);
                tvRankMonth.setCompoundDrawables(null,null,null,null);
                tvRankAll.setCompoundDrawables(drawable,null,null,null);
                break;
        }
    }



    @Override
    public void onClick(View view) {
        if(onRankChangeLinster == null){
            return;
        }
        dismiss();
        switch (view.getId()){
            case R.id.tvRankWeek:
                onRankChangeLinster.onRankWeek();
                break;
            case R.id.tvRankMonth:
                onRankChangeLinster.onRankMouth();
                break;
            case R.id.tvRankAll:
                onRankChangeLinster.onRankAll();
                break;
        }
    }

    public void setOnRankChangeLinster(OnRankChangeLinster onRankChangeLinster) {
        this.onRankChangeLinster = onRankChangeLinster;
    }

    public interface OnRankChangeLinster{
        void onRankWeek();
        void onRankMouth();
        void onRankAll();
    }
}
