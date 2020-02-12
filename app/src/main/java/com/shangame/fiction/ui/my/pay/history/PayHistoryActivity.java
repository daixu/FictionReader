package com.shangame.fiction.ui.my.pay.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 充值记录 Activity
 * Create by Speedy on 2018/7/23
 */
public class PayHistoryActivity extends BaseActivity implements View.OnClickListener {

    public static final int PAY_HISTORY = 0;
    public static final int CONSUME_HISTORY = 1;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private List<Fragment> list;
    private FragmentPagerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_history);
        initView();
    }


    private void initView() {
        TextView tvPublicTitle = (TextView) findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.top_up_and_consume_history);

        findViewById(R.id.ivPublicBack).setOnClickListener(this);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        list = new ArrayList<>(2);
        list.add(PayHistoryFragment.getInstance(PAY_HISTORY));
        list.add(PayHistoryFragment.getInstance(CONSUME_HISTORY));

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
                    return getString(R.string.top_up_history);
                }else{
                    return getString(R.string.consume_history);
                }
            }
        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);

        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

    }




    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivPublicBack:
                finish();
                break;
        }
    }
}
