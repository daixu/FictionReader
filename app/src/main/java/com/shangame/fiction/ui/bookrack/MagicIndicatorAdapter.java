package com.shangame.fiction.ui.bookrack;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.shangame.fiction.R;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Speedy on 2019/3/5
 */
public class MagicIndicatorAdapter extends CommonNavigatorAdapter {

    private List<String> titleList = new ArrayList<>(2);

    private Context mContext;

    private ViewPager mViewPager;

    public MagicIndicatorAdapter(Context mContext, ViewPager mViewPager) {
        this.mContext = mContext;
        this.mViewPager = mViewPager;
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
        colorTransitionPagerTitleView.setNormalColor(mContext.getResources().getColor(R.color.primary_text));
        colorTransitionPagerTitleView.setSelectedColor(mContext.getResources().getColor(R.color.colorPrimary));
        colorTransitionPagerTitleView.setText(titleList.get(index));
        colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(index);
            }
        });
        colorTransitionPagerTitleView.setPadding(50,0,50,0);
        return colorTransitionPagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
        indicator.setColors(mContext.getResources().getColor(R.color.colorPrimary));
        return indicator;
    }

    public void setTitleList(List<String> list) {
        this.titleList.clear();
        this.titleList.addAll(list) ;
    }
}
