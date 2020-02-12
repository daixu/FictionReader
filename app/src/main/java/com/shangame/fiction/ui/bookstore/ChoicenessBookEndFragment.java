package com.shangame.fiction.ui.bookstore;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.core.manager.Logger;
import com.shangame.fiction.ui.bookrack.MagicIndicatorAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.List;

/**
 * 书城（首页）完本(男生、女生) Fragment
 * Create by Speedy on 2018/7/25
 */
public class ChoicenessBookEndFragment extends BaseFragment implements View.OnClickListener{

    private MagicIndicator magicIndicator;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> fragmentList;



    public static final ChoicenessBookEndFragment newInstance() {
        ChoicenessBookEndFragment fragment = new ChoicenessBookEndFragment();
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_choiceness_book_end, container, false);
        initView(contentView);
        return contentView;
    }

    private void initView(View contentView) {
        contentView.findViewById(R.id.ivBack).setOnClickListener(this);
        initViewPager(contentView);
        initMagicIndicator(contentView);
    }

    private void initViewPager(View contentView) {
        mViewPager = (ViewPager) contentView.findViewById(R.id.viewPager);

        fragmentList = new ArrayList<>();
        fragmentList.add(BookEndFragment.newInstance(BookStoreChannel.GIRL));
        fragmentList.add(BookEndFragment.newInstance(BookStoreChannel.BOY));

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
    }


    private void initMagicIndicator(View contentView) {
        magicIndicator = contentView.findViewById(R.id.magic_indicator);
        final List<String> titleList = new ArrayList<>(2);
        titleList.add("女生");
        titleList.add("男生");
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        MagicIndicatorAdapter magicIndicatorAdapter = new MagicIndicatorAdapter(mContext,mViewPager);
        magicIndicatorAdapter.setTitleList(titleList);
        commonNavigator.setAdapter(magicIndicatorAdapter);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(mViewPager != null){
            try{
                //传递可见性给子Fragment
                Fragment fragment = mAdapter.getItem(mViewPager.getCurrentItem());
                if(fragment != null){
                    fragment.setUserVisibleHint(isVisibleToUser);
                }
            }catch (Exception e){
                Logger.e("BookStoreFragment","",e);
            }

        }

    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ivBack){
            getActivity().finish();
        }
    }
}
