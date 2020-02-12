package com.shangame.fiction.ui.author.works.enter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;

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
public class MagicIndicatorAdapter extends CommonNavigatorAdapter {
    private List<String> mDataList = new ArrayList<>(2);

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    private ViewPager mViewPager;

    public MagicIndicatorAdapter(ViewPager mViewPager) {
        this.mViewPager = mViewPager;
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
        simplePagerTitleView.setText(mDataList.get(index));
        simplePagerTitleView.setNormalColor(Color.parseColor("#1A1F24"));
        simplePagerTitleView.setSelectedColor(Color.parseColor("#5094F2"));
        simplePagerTitleView.setTextSize(15);
        simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(index);
            }
        });
        return simplePagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
        indicator.setLineWidth(UIUtil.dip2px(context, 20));
        indicator.setYOffset(UIUtil.dip2px(context, 3));
        indicator.setColors(Color.parseColor("#5094F2"));
        return indicator;
    }

    public void setTitleList(List<String> list) {
        this.mDataList.clear();
        this.mDataList.addAll(list) ;
    }

}
