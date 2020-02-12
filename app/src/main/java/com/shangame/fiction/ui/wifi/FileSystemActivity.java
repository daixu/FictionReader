package com.shangame.fiction.ui.wifi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地导入页面
 */
public class FileSystemActivity extends BaseActivity implements View.OnClickListener {
    private MagicIndicator mMagicIndicator;
    private ViewPager mViewPager;

    private LocalBookFragment mLocalFragment;
    private FileCategoryFragment mCategoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_system);

        initView();
        initListener();

        initMagicIndicator();
    }

    private void initView() {
        mMagicIndicator = findViewById(R.id.magic_indicator);
        mViewPager = findViewById(R.id.view_pager);

        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("本地导入");

        mLocalFragment = LocalBookFragment.newInstance("智能扫描");
        mCategoryFragment = FileCategoryFragment.newInstance("系统目录");

        final List<Fragment> list = new ArrayList<>(2);
        list.add(mLocalFragment);
        list.add(mCategoryFragment);

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0) {
                    return "智能扫描";
                } else {
                    return "系统目录";
                }
            }
        };
        mViewPager.setAdapter(adapter);
    }

    private void initListener() {
        findViewById(R.id.img_back).setOnClickListener(this);
    }

    private void initMagicIndicator() {
        final List<String> titleList = new ArrayList<>(2);
        titleList.add("智能扫描");
        titleList.add("系统目录");
        CommonNavigator navigator = new CommonNavigator(mContext);
        MagicIndicatorAdapter adapter = new MagicIndicatorAdapter(mViewPager);
        adapter.setTitleList(titleList);
        navigator.setAdapter(adapter);
        navigator.setAdjustMode(true);
        mMagicIndicator.setNavigator(navigator);

        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_back) {
            finish();
        }
    }
}
