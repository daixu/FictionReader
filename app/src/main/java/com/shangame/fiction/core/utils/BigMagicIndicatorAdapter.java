package com.shangame.fiction.core.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.shangame.fiction.R;

import net.lucode.hackware.magicindicator.buildins.ArgbEvaluatorHolder;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

public class BigMagicIndicatorAdapter extends CommonNavigatorAdapter {

    private List<String> titleList = new ArrayList<>(2);

    private Context mContext;

    private ViewPager mViewPager;

    private int mIndicatorColor;

    public BigMagicIndicatorAdapter(Context context, ViewPager viewPager, int indicatorColor) {
        this.mContext = context;
        this.mViewPager = viewPager;
        this.mIndicatorColor = indicatorColor;
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        BigTextTitleView bigTextTitleView = new BigTextTitleView(context);
        bigTextTitleView.setNormalColor(mContext.getResources().getColor(R.color.white));
        bigTextTitleView.setSelectedColor(mContext.getResources().getColor(R.color.white));
        bigTextTitleView.setText(titleList.get(index));
        bigTextTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(index);
            }
        });
        bigTextTitleView.setPadding(50, 0, 50, 0);
        return bigTextTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
        indicator.setLineWidth(UIUtil.dip2px(context, 16));
        indicator.setLineHeight(UIUtil.dip2px(context, 2));
        indicator.setColors(mIndicatorColor);
        return indicator;
    }

    public void setTitleList(List<String> list) {
        this.titleList.clear();
        this.titleList.addAll(list);
    }

    class BigTextTitleView extends SimplePagerTitleView {

        public BigTextTitleView(Context context) {
            super(context);
        }

        @Override
        public void onSelected(int index, int totalCount) {
            setTextSize(17);
            setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }

        @Override
        public void onDeselected(int index, int totalCount) {
            setTextSize(16);
            setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }

        @Override
        public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
            int color = ArgbEvaluatorHolder.eval(leavePercent, mSelectedColor, mNormalColor);
            setTextColor(color);
        }

        @Override
        public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
            int color = ArgbEvaluatorHolder.eval(enterPercent, mNormalColor, mSelectedColor);
            setTextColor(color);
        }
    }
}