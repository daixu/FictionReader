package com.shangame.fiction.ui.bookrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lxj.xpopup.XPopup;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.storage.model.LocalBookBean;
import com.shangame.fiction.ui.booklist.BatchManagerActivity;
import com.shangame.fiction.ui.search.SearchBookActivity;
import com.shangame.fiction.ui.wifi.FileSystemActivity;
import com.shangame.fiction.ui.wifi.WifiPopupWindow;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.List;

/**
 * 书架Fragment
 * Create by Speedy on 2018/7/20
 */
public class BookRackFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "BookRackFragment";

    private static final int START_FILER_ACITIVTY_REQUEST_CODE = 660;

    private MagicIndicator magicIndicator;
    private ViewPager mViewPager;

    private List<Fragment> list;
    private FragmentPagerAdapter mAdapter;

    private BookListFragment bookListFragment;
    private BrowseHistoryFragment browseHistoryFragment;


    public static BookRackFragment newInstance() {
        BookRackFragment fragment = new BookRackFragment();
        return fragment;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e("hhh", "BookRackFragment onHiddenChanged hidden= " + hidden);

        if (null != bookListFragment) {
            bookListFragment.refreshData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_bookrack, container, false);

        contentView.findViewById(R.id.ivFilter).setOnClickListener(this);
        contentView.findViewById(R.id.ivSearch).setOnClickListener(this);
        contentView.findViewById(R.id.ivDian).setOnClickListener(this);

        mViewPager = contentView.findViewById(R.id.viewPager);

        bookListFragment = new BookListFragment();
        browseHistoryFragment = new BrowseHistoryFragment();

        list = new ArrayList<>(2);
        list.add(bookListFragment);
        list.add(browseHistoryFragment);

        mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
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
                if (position == 0) {
                    return getString(R.string.bookrack);
                } else {
                    return getString(R.string.browse_history);
                }
            }
        };
        mViewPager.setAdapter(mAdapter);
        initMagicIndicator(contentView);
        return contentView;
    }

    private void initMagicIndicator(View contentView) {
        magicIndicator = contentView.findViewById(R.id.magic_indicator);
        final List<String> titleList = new ArrayList<>(2);
        titleList.add("书架");
        titleList.add("浏览记录");
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        MagicIndicatorAdapter magicIndicatorAdapter = new MagicIndicatorAdapter(mContext, mViewPager);
        magicIndicatorAdapter.setTitleList(titleList);
        commonNavigator.setAdapter(magicIndicatorAdapter);
        magicIndicator.setNavigator(commonNavigator);

        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.ivFilter:
                Intent intent = new Intent(mContext, FilteBookActivity.class);
                startActivityForResult(intent, START_FILER_ACITIVTY_REQUEST_CODE);
                break;
            case R.id.ivSearch:
                intent = new Intent(mContext, SearchBookActivity.class);
                startActivity(intent);
                break;
            case R.id.ivDian:
                final RackPopupWindow rackPopupWindow = new RackPopupWindow(mActivity);
                rackPopupWindow.setOnRackChangeListener(new RackPopupWindow.OnRackChangeListener() {

                    @Override
                    public void onListMode() {
                        int index = mViewPager.getCurrentItem();
                        if (index == 0) {
                            Fragment fragment = mAdapter.getItem(index);
                            if (fragment instanceof BookListFragment) {
                                BookListFragment bookListFragment = (BookListFragment) fragment;
                                bookListFragment.changeMode();
                            }
                        }
                    }

                    @Override
                    public void onManager() {
                        List<LocalBookBean> bookBeanList = bookListFragment.getBookList();
                        ArrayList<LocalBookBean> list = new ArrayList<>(bookBeanList.size());
                        list.addAll(bookBeanList);
                        Intent intent = new Intent(mActivity, BatchManagerActivity.class);
                        intent.putParcelableArrayListExtra("BookList", list);
                        startActivity(intent);
//                        BatchManagerPopupWindow batchManagerPopupWindow = new BatchManagerPopupWindow(mActivity);
//                        batchManagerPopupWindow.show(bookListFragment.getBookList());
                    }

                    @Override
                    public void onLocalImport() {
                        Intent intent = new Intent(mContext, FileSystemActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onWifiBook() {
                        new XPopup.Builder(getContext())
                                .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
                                .asCustom(new WifiPopupWindow(mContext)/*.enableDrag(false)*/)
                                .show();
//                        Intent intent = new Intent(mContext, WifiBookActivity.class);
//                        startActivity(intent);
                    }
                });
                rackPopupWindow.show(view);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == START_FILER_ACITIVTY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                int statusid = data.getIntExtra("statusid", 0);
                int readecid = data.getIntExtra("readecid", 0);
                int numcid = data.getIntExtra("numcid", 0);
                int malecid = data.getIntExtra("malecid", 0);

                mViewPager.setCurrentItem(0);
                bookListFragment.setFilteParam(statusid, readecid, numcid, malecid);
                bookListFragment.refresh();
            }
        }
    }
}
