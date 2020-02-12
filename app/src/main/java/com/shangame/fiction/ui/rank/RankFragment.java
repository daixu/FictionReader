package com.shangame.fiction.ui.rank;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.storage.manager.UserSetting;
import com.shangame.fiction.ui.bookstore.BookStoreChannel;
import com.shangame.fiction.ui.listen.rank.ListenRankFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 榜单 Fragment
 * Create by Speedy on 2018/7/26
 */
public class RankFragment extends BaseFragment implements View.OnClickListener {

    private TextView tvRankType;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private List<Fragment> list;
    private FragmentPagerAdapter mAdapter;

    private int dayType;//默认周榜

    private RankDetailFragment boyRankDetailFragment;
    private RankDetailFragment girlRankDetailFragment;
    private ListenRankFragment listenRankDetailFragment;

    public static RankFragment newInstance() {
        RankFragment fragment = new RankFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dayType = UserSetting.getInstance(mContext).getInt(SharedKey.DAY_TYPE, RankDayType.RANK_WEEK);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_rank, container, false);

        tvRankType = contentView.findViewById(R.id.tvRankType);
        tvRankType.setOnClickListener(this);

        if (dayType == RankDayType.RANK_WEEK) {
            tvRankType.setText(getString(R.string.rank_week));
        } else if (dayType == RankDayType.RANK_MONTH) {
            tvRankType.setText(getString(R.string.rank_month));
        } else if (dayType == RankDayType.RANK_ALL) {
            tvRankType.setText(getString(R.string.rank_all));
        }

        mViewPager = contentView.findViewById(R.id.viewPager);

        boyRankDetailFragment = RankDetailFragment.newInstance(BookStoreChannel.BOY, dayType);
        girlRankDetailFragment = RankDetailFragment.newInstance(BookStoreChannel.GIRL, dayType);
        listenRankDetailFragment = ListenRankFragment.newInstance(dayType);

        list = new ArrayList<>(2);
        list.add(girlRankDetailFragment);
        list.add(boyRankDetailFragment);
        list.add(listenRankDetailFragment);

        mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return list.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0) {
                    return getString(R.string.girl);
                } else if (position == 1) {
                    return getString(R.string.boy);
                } else {
                    return getString(R.string.listen);
                }
            }

            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }
        };
        mViewPager.setAdapter(mAdapter);

        mTabLayout = contentView.findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

        return contentView;
    }

    public void setCurrentItem(int currentItem) {
        if (null != mViewPager) {
            if (currentItem > list.size() - 1) {
                currentItem = 0;
            }
            mViewPager.setCurrentItem(currentItem);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvRankType) {
            RankPopupWindow rankPopupWindow = new RankPopupWindow(mActivity, dayType);
            rankPopupWindow.setOnRankChangeLinster(new RankPopupWindow.OnRankChangeLinster() {
                @Override
                public void onRankWeek() {
                    dayType = RankDayType.RANK_WEEK;
                    tvRankType.setText(getString(R.string.rank_week));
                    UserSetting.getInstance(mContext).putInt(SharedKey.DAY_TYPE, RankDayType.RANK_WEEK);
                    boyRankDetailFragment.setDayType(RankDayType.RANK_WEEK);
                    girlRankDetailFragment.setDayType(RankDayType.RANK_WEEK);
                    listenRankDetailFragment.setDayType(RankDayType.RANK_WEEK);
                }

                @Override
                public void onRankMouth() {
                    dayType = RankDayType.RANK_MONTH;
                    tvRankType.setText(getString(R.string.rank_month));
                    UserSetting.getInstance(mContext).putInt(SharedKey.DAY_TYPE, RankDayType.RANK_MONTH);
                    boyRankDetailFragment.setDayType(RankDayType.RANK_MONTH);
                    girlRankDetailFragment.setDayType(RankDayType.RANK_MONTH);
                    listenRankDetailFragment.setDayType(RankDayType.RANK_MONTH);
                }

                @Override
                public void onRankAll() {
                    dayType = RankDayType.RANK_ALL;
                    tvRankType.setText(getString(R.string.rank_all));
                    UserSetting.getInstance(mContext).putInt(SharedKey.DAY_TYPE, RankDayType.RANK_ALL);
                    boyRankDetailFragment.setDayType(RankDayType.RANK_ALL);
                    girlRankDetailFragment.setDayType(RankDayType.RANK_ALL);
                    listenRankDetailFragment.setDayType(RankDayType.RANK_ALL);
                }
            });
            rankPopupWindow.showAsDropDown(view);
        }
    }
}
