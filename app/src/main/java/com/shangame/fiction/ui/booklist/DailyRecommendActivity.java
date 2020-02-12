package com.shangame.fiction.ui.booklist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.response.AlbumChapterDetailResponse;
import com.shangame.fiction.net.response.BookRackResponse;
import com.shangame.fiction.net.response.RecommendBookResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.bookrack.BookRackContacts;
import com.shangame.fiction.ui.bookrack.BookRackPresenter;

import java.util.ArrayList;
import java.util.List;

public class DailyRecommendActivity extends BaseActivity implements BookRackContacts.View, View.OnClickListener {

    private ViewPager mViewPager;

    private List<DailyRecommendFragment> list = new ArrayList<>();

    private FragmentPagerAdapter mAdapter;

    private SmartRefreshLayout smartRefreshLayout;

    private BookRackPresenter bookRackPresenter;

    private long userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_recommend);
        initTitle();
        initSmartRefreshLayout();
        initViewPager();
        initPresenter();
        smartRefreshLayout.autoRefresh();
    }

    private void initTitle() {
        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText("每日推荐");
    }

    private void initSmartRefreshLayout() {
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });
    }

    private void initViewPager() {
        mViewPager = findViewById(R.id.viewPager);
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

        };
        mViewPager.setAdapter(mAdapter);
    }

    private void initPresenter() {
        userid = UserInfoManager.getInstance(mContext).getUserid();

        bookRackPresenter = new BookRackPresenter();
        bookRackPresenter.attachView(this);
    }

    private void loadData() {
        bookRackPresenter.getRecommendBook(userid, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bookRackPresenter.detachView();
    }

    @Override
    public void getBookListSuccess(BookRackResponse bookRackResponse) {

    }

    @Override
    public void getRecommendBookSuccess(RecommendBookResponse recommendBookResponse) {
        smartRefreshLayout.finishRefresh();
        if (recommendBookResponse.recdata != null && recommendBookResponse.recdata.size() > 0) {
            for (RecommendBookResponse.RecdataBean recdataBean : recommendBookResponse.recdata) {
                DailyRecommendFragment dailyRecommendFragment = DailyRecommendFragment.newInstance(recdataBean);
                list.add(dailyRecommendFragment);
            }
            mAdapter.notifyDataSetChanged();
        }
        mViewPager.setVisibility(View.VISIBLE);
    }

    @Override
    public void getAlbumChapterDetailSuccess(AlbumChapterDetailResponse response, int albumId, int cid) {

    }

    @Override
    public void getAlbumChapterDetailFailure(String msg) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivPublicBack) {
            finish();
        }
    }
}
