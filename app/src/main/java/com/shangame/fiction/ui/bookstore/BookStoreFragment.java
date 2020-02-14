package com.shangame.fiction.ui.bookstore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.core.manager.AppSetting;
import com.shangame.fiction.core.manager.Logger;
import com.shangame.fiction.core.utils.BigMagicIndicatorAdapter;
import com.shangame.fiction.ui.bookrack.MagicIndicatorAdapter;
import com.shangame.fiction.ui.bookstore.choice.ChoiceFragment;
import com.shangame.fiction.ui.listen.ListenBookFragment;
import com.shangame.fiction.ui.popup.FreeReadPopupWindow;
import com.shangame.fiction.ui.search.SearchBookActivity;
import com.shangame.fiction.ui.task.TaskCenterActivity;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.List;

/**
 * 书城（首页）
 * Create by Speedy on 2018/7/25
 */
public class BookStoreFragment extends BaseFragment implements View.OnClickListener {

    private static final int OPEN_TASK_CENTER = 66;

    private static final String TAG = "BookStoreFragment";

    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> fragmentList;

//    private ChoiceFragment mChoiceFragment;
    private ChoicenessFragment choicenessFragment;
    private GirlPageFragment girlPageFragment;
    private BoyPageFragment boyPageFragment;
    private ListenBookFragment mListenBookFragment;

    public static BookStoreFragment newInstance() {
        BookStoreFragment fragment = new BookStoreFragment();
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mViewPager != null) {
            try {
                //传递可见性给子Fragment
                Fragment fragment = mAdapter.getItem(mViewPager.getCurrentItem());
                if (fragment != null) {
                    fragment.setUserVisibleHint(isVisibleToUser);
                }
            } catch (Exception e) {
                Logger.e("BookStoreFragment", "", e);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == OPEN_TASK_CENTER) {
            Intent intent = new Intent(mContext, TaskCenterActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_book_store, container, false);
        initView(contentView);
        return contentView;
    }

    private void initView(View contentView) {
        contentView.findViewById(R.id.ivSearch).setOnClickListener(this);
        initViewPager(contentView);
        initMagicIndicator(contentView);

        ImageView ivRed = contentView.findViewById(R.id.ivRed);
        ivRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TaskCenterActivity.class);
                startActivity(intent);
            }
        });
        Glide.with(this).asGif().load(R.drawable.red_anim).into(ivRed);
    }

    private void initViewPager(View contentView) {
        mViewPager = contentView.findViewById(R.id.viewPager);

//        mChoiceFragment = new ChoiceFragment();
        choicenessFragment = new ChoicenessFragment();
        girlPageFragment = new GirlPageFragment();
        boyPageFragment = new BoyPageFragment();
        mListenBookFragment = new ListenBookFragment();

        fragmentList = new ArrayList<>();
//        fragmentList.add(mChoiceFragment);
        fragmentList.add(choicenessFragment);
        fragmentList.add(girlPageFragment);
        fragmentList.add(boyPageFragment);
        fragmentList.add(mListenBookFragment);

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
        mViewPager.setOffscreenPageLimit(1);
    }

    private void initMagicIndicator(View contentView) {
        MagicIndicator magicIndicator = contentView.findViewById(R.id.magic_indicator);
        final List<String> titleList = new ArrayList<>(3);
//        titleList.add("精选");
        titleList.add("书城");
        titleList.add("女生");
        titleList.add("男生");
        titleList.add("听书");
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        BigMagicIndicatorAdapter magicIndicatorAdapter = new BigMagicIndicatorAdapter(mContext, mViewPager, ContextCompat.getColor(mContext, android.R.color.transparent));
        magicIndicatorAdapter.setTitleList(titleList);
        commonNavigator.setAdapter(magicIndicatorAdapter);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null != getActivity()) {
            showGuide(getActivity());
        }
    }

    public void showGuide(final Activity activity) {
        final AppSetting appSetting = AppSetting.getInstance(activity.getApplicationContext());
        boolean hasShowGuide = appSetting.getBoolean(TAG, false);
        if (!hasShowGuide) {
            appSetting.putBoolean(TAG, true);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    FreeReadPopupWindow freeReadPopupWindow = new FreeReadPopupWindow(getActivity());
                    freeReadPopupWindow.show();

                }
            }, 100);
        }
    }

    public void setCurrentItem(int position) {
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivSearch) {
            Intent intent = new Intent(mContext, SearchBookActivity.class);
            startActivity(intent);
        }
    }

    public void scrollToTop() {
        int position = mViewPager.getCurrentItem();
        switch (position) {
            case 0:
                // mChoiceFragment.scrollToTop();
                choicenessFragment.scrollToTop();
                break;
            case 1:
                girlPageFragment.scrollToTop();
                break;
            case 2:
                boyPageFragment.scrollToTop();
                break;
            case 3:
                mListenBookFragment.scrollToTop();
                break;
            default:
                break;
        }
    }
}
