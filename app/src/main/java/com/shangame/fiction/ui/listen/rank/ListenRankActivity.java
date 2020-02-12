package com.shangame.fiction.ui.listen.rank;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.ui.rank.RankDayType;

import java.util.ArrayList;
import java.util.List;

public class ListenRankActivity extends BaseActivity implements View.OnClickListener {

    private List<Fragment> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_detail);

        String rankName = getIntent().getStringExtra("rankName");

        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(rankName);

        ViewPager viewPager = findViewById(R.id.viewPager);

        mList = new ArrayList<>(2);
        mList.add(ListenRankFragment.newInstance(RankDayType.RANK_WEEK));
        mList.add(ListenRankFragment.newInstance(RankDayType.RANK_MONTH));
        mList.add(ListenRankFragment.newInstance(RankDayType.RANK_ALL));

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "周榜";
                    case 1:
                        return "月榜";
                    case 2:
                        return "总榜";
                    default:
                        return "";
                }
            }

            @Override
            public Fragment getItem(int position) {
                return mList.get(position);
            }
        };
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivPublicBack) {
            finish();
        }
    }
}
