package com.shangame.fiction.ui.my.account.coin;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class CoinLoseActivity extends BaseActivity implements View.OnClickListener{

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private List<Fragment> list;
    private FragmentPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_lose);

        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText("已失效赠币");

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        list = new ArrayList<>(2);
        list.add(CoinListFragment.newInstance(CoinState.USED));
        list.add(CoinListFragment.newInstance(CoinState.EXPIRE));

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                if(position == 0){
                    return "已使用";
                }else{
                    return "已过期";
                }
            }
        };
        mViewPager.setAdapter(mAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ivPublicBack){
            finish();
        }
    }
}
