package com.shangame.fiction.ui.rank;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.storage.manager.UserSetting;
import com.shangame.fiction.ui.bookstore.BookStoreChannel;
import com.shangame.fiction.ui.listen.rank.ListenRankFragment;

import java.util.ArrayList;
import java.util.List;

@Route(path = "/ss/book/rank")
public class RankActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvRankType;

    private ViewPager mViewPager;

    private List<Fragment> mList;

    private int dayType;

    private RankDetailFragment boyRankDetailFragment;
    private RankDetailFragment girlRankDetailFragment;
    private ListenRankFragment listenRankDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        dayType = UserSetting.getInstance(mContext).getInt(SharedKey.DAY_TYPE, RankDayType.RANK_WEEK);
        initView();
    }

    private void initView() {
        tvRankType = findViewById(R.id.tvRankType);
        tvRankType.setOnClickListener(this);
        findViewById(R.id.ivPublicBack).setOnClickListener(this);

        if (dayType == RankDayType.RANK_WEEK) {
            tvRankType.setText(getString(R.string.rank_week));
        } else if (dayType == RankDayType.RANK_MONTH) {
            tvRankType.setText(getString(R.string.rank_month));
        } else if (dayType == RankDayType.RANK_ALL) {
            tvRankType.setText(getString(R.string.rank_all));
        }

        mViewPager = findViewById(R.id.viewPager);

        boyRankDetailFragment = RankDetailFragment.newInstance(BookStoreChannel.BOY, dayType);
        girlRankDetailFragment = RankDetailFragment.newInstance(BookStoreChannel.GIRL, dayType);
        listenRankDetailFragment = ListenRankFragment.newInstance(dayType);

        mList = new ArrayList<>(2);
        mList.add(girlRankDetailFragment);
        mList.add(boyRankDetailFragment);
        mList.add(listenRankDetailFragment);

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mList.size();
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
                return mList.get(position);
            }
        };
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);
    }

    public void setCurrentItem(int currentItem) {
        if (null != mViewPager) {
            if (currentItem > mList.size() - 1) {
                currentItem = 0;
            }
            mViewPager.setCurrentItem(currentItem);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivPublicBack) {
            finish();
        } else if (view.getId() == R.id.tvRankType) {
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
