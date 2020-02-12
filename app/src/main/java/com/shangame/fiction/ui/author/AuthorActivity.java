package com.shangame.fiction.ui.author;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.ui.author.home.HomeFragment;
import com.shangame.fiction.ui.author.me.MeFragment;
import com.shangame.fiction.ui.author.statistics.StatisticsFragment;
import com.shangame.fiction.ui.author.writing.WritingFragment;
import com.shangame.fiction.widget.TabItemView;

/**
 * 作者平台main
 */
public class AuthorActivity extends BaseActivity implements View.OnClickListener {

    private TabItemView tabHome;
    private TabItemView tabWriting;
    private TabItemView tabStatistics;
    private TabItemView tabMe;

    private HomeFragment homeFragment;
    private WritingFragment writingFragment;
    private StatisticsFragment statisticsFragment;
    private MeFragment meFragment;

    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);

        initView();
        initFragment(savedInstanceState);
        initCurrentItem();
    }

    private void initCurrentItem() {
        if (getIntent().hasExtra("index")) {
            int index = getIntent().getIntExtra("index", 1);
            switch (index) {
                case 0:
                    setCurrentItem(AuthorItemType.HOME);
                    break;
                case 1:
                    setCurrentItem(AuthorItemType.WRITING);
                    break;
                case 2:
                    setCurrentItem(AuthorItemType.STATISTICS);
                    break;
                case 3:
                    setCurrentItem(AuthorItemType.ME);
                    break;
                default:
                    break;
            }
        } else {
            setCurrentItem(AuthorItemType.HOME);
        }
    }

    private void initView() {
        tabHome = findViewById(R.id.tab_home);
        tabWriting = findViewById(R.id.tab_writing);
        tabStatistics = findViewById(R.id.tab_statistics);
        tabMe = findViewById(R.id.tab_me);

        tabHome.setOnClickListener(this);
        tabWriting.setOnClickListener(this);
        tabStatistics.setOnClickListener(this);
        tabMe.setOnClickListener(this);
    }

    private void initFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            homeFragment = HomeFragment.newInstance();
            writingFragment = WritingFragment.newInstance();
            statisticsFragment = StatisticsFragment.newInstance();
            meFragment = MeFragment.newInstance();

            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.container, homeFragment, "homeFragment");
            fragmentTransaction.add(R.id.container, writingFragment, "writingFragment");
            fragmentTransaction.add(R.id.container, statisticsFragment, "statisticsFragment");
            fragmentTransaction.add(R.id.container, meFragment, "MeFragment");
            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.show(writingFragment);
            fragmentTransaction.hide(statisticsFragment);
            fragmentTransaction.hide(meFragment);
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            homeFragment = (HomeFragment) getSupportFragmentManager().getFragment(savedInstanceState, "homeFragment");
            writingFragment = (WritingFragment) getSupportFragmentManager().getFragment(savedInstanceState, "writingFragment");
            statisticsFragment = (StatisticsFragment) getSupportFragmentManager().getFragment(savedInstanceState, "statisticsFragment");
            meFragment = (MeFragment) getSupportFragmentManager().getFragment(savedInstanceState, "MeFragment");

            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.show(writingFragment);
            fragmentTransaction.hide(statisticsFragment);
            fragmentTransaction.hide(meFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void onClick(View view) {
        updateTabItemState(view.getId());
        switch (view.getId()) {
            case R.id.tab_home:
                setCurrentItem(AuthorItemType.HOME);
                break;
            case R.id.tab_writing:
                setCurrentItem(AuthorItemType.WRITING);
                break;
            case R.id.tab_statistics:
                setCurrentItem(AuthorItemType.STATISTICS);
                break;
            case R.id.tab_me:
                setCurrentItem(AuthorItemType.ME);
                break;
            default:
                break;
        }
    }

    public void setCurrentItem(AuthorItemType itemType) {
        switch (itemType) {
            case HOME:
                updateTabItemState(R.id.tab_home);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.show(homeFragment);
                fragmentTransaction.hide(writingFragment);
                fragmentTransaction.hide(statisticsFragment);
                fragmentTransaction.hide(meFragment);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case WRITING:
                updateTabItemState(R.id.tab_writing);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(homeFragment);
                fragmentTransaction.show(writingFragment);
                fragmentTransaction.hide(statisticsFragment);
                fragmentTransaction.hide(meFragment);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case STATISTICS:
                updateTabItemState(R.id.tab_statistics);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(homeFragment);
                fragmentTransaction.hide(writingFragment);
                fragmentTransaction.show(statisticsFragment);
                fragmentTransaction.hide(meFragment);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            case ME:
                updateTabItemState(R.id.tab_me);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(homeFragment);
                fragmentTransaction.hide(writingFragment);
                fragmentTransaction.hide(statisticsFragment);
                fragmentTransaction.show(meFragment);
                fragmentTransaction.commitAllowingStateLoss();
                break;
            default:
                break;
        }
    }

    private void updateTabItemState(int selectTabId) {
        switch (selectTabId) {
            case R.id.tab_home:
                tabHome.setSelected(true);
                tabWriting.setSelected(false);
                tabStatistics.setSelected(false);
                tabMe.setSelected(false);
                break;
            case R.id.tab_writing:
                tabHome.setSelected(false);
                tabWriting.setSelected(true);
                tabStatistics.setSelected(false);
                tabMe.setSelected(false);
                break;
            case R.id.tab_statistics:
                tabHome.setSelected(false);
                tabWriting.setSelected(false);
                tabStatistics.setSelected(true);
                tabMe.setSelected(false);
                break;
            case R.id.tab_me:
                tabHome.setSelected(false);
                tabWriting.setSelected(false);
                tabStatistics.setSelected(false);
                tabMe.setSelected(true);
                break;
            default:
                break;
        }
    }
}
