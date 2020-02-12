package com.shangame.fiction.ui.sales.partner;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Speedy on 2019/7/26
 */
public class PartnerIndicatorAdapter extends CommonNavigatorAdapter {
    private List<String> mDataList = new ArrayList<>();

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    private ViewPager mViewPager;

    public PartnerIndicatorAdapter(ViewPager mViewPager) {
        this.mViewPager = mViewPager;
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        SimplePagerTitleView clipPagerTitleView = new SimplePagerTitleView(context);
        clipPagerTitleView.setText(mDataList.get(index));
        clipPagerTitleView.setTextColor(Color.parseColor("#1A1F24"));
        clipPagerTitleView.setNormalColor(Color.parseColor("#1A1F24"));
        clipPagerTitleView.setSelectedColor(Color.parseColor("#4792F8"));
        clipPagerTitleView.setTextSize(15);
        clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(index);
            }
        });
        return clipPagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
        indicator.setLineWidth(UIUtil.dip2px(context, 20));
        indicator.setYOffset(UIUtil.dip2px(context, 3));
        indicator.setRoundRadius(UIUtil.dip2px(context, 3));
        indicator.setStartInterpolator(new AccelerateInterpolator());
        indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
        indicator.setColors(Color.parseColor("#4792F8"));
        return indicator;
    }

    public void setTitleList(List<String> list) {
        this.mDataList.clear();
        this.mDataList.addAll(list) ;
    }

}
