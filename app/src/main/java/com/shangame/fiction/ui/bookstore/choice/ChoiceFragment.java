package com.shangame.fiction.ui.bookstore.choice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.core.utils.BigMagicIndicatorAdapter;
import com.shangame.fiction.ui.bookrack.MagicIndicatorAdapter;
import com.shangame.fiction.ui.search.SearchBookActivity;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * 精选Fragment
 */
public class ChoiceFragment extends BaseFragment {
    private ViewPager mViewPager;
    private List<Fragment> fragmentList;
    private FragmentPagerAdapter mAdapter;

    private ChoiceItemFragment boyFragment;
    private ChoiceItemFragment girlFragment;

    public static ChoiceFragment newInstance(int type) {
        ChoiceFragment fragment = new ChoiceFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_choice, container, false);
        initView(contentView);
        return contentView;
    }

    private void initView(View contentView) {
        contentView.findViewById(R.id.img_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SearchBookActivity.class);
                startActivity(intent);
            }
        });
        initFragment(contentView);
        initMagicIndicator(contentView);
    }

    private void initFragment(View contentView) {
        mViewPager = contentView.findViewById(R.id.viewPager);

        boyFragment = ChoiceItemFragment.newInstance(0);
        girlFragment = ChoiceItemFragment.newInstance(1);

        fragmentList = new ArrayList<>(2);
        fragmentList.add(boyFragment);
        fragmentList.add(girlFragment);

        mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }
        };
        mViewPager.setAdapter(mAdapter);
    }

    private void initMagicIndicator(View contentView) {
        MagicIndicator magicIndicator = contentView.findViewById(R.id.magic_indicator);
        final List<String> titleList = new ArrayList<>(2);
        titleList.add("男生");
        titleList.add("女生");
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        BigMagicIndicatorAdapter magicIndicatorAdapter = new BigMagicIndicatorAdapter(mContext, mViewPager, ContextCompat.getColor(mContext, android.R.color.transparent));
        magicIndicatorAdapter.setTitleList(titleList);
        commonNavigator.setAdapter(magicIndicatorAdapter);
        magicIndicator.setNavigator(commonNavigator);

        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }
}
