package com.shangame.fiction.widget.ad;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.net.response.PictureConfigResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 轮播广告Banner
 * Create by Speedy on 2018/7/25
 */
public class AdViewPagerBanner extends ViewPager {

    //自动播放间隔时间（5秒）
    private static final long PALY_PERIOD = 5;

    private MyPagerAdapter myPagerAdapter;

    private CompositeDisposable mCompositeDisposable;

    private OnItemPageClickListener onItemPageClickListener;

    public AdViewPagerBanner(@NonNull Context context) {
        super(context);
        init();
    }

    public void init() {
        myPagerAdapter = new MyPagerAdapter();
        setAdapter(myPagerAdapter);
        mCompositeDisposable = new CompositeDisposable();
    }

    public AdViewPagerBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setOnItemPageClickListener(OnItemPageClickListener onItemPageClickListener) {
        this.onItemPageClickListener = onItemPageClickListener;
    }

    /**
     * 重新onMeasure，解决ViewPager设置wrap_content无效问题
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        //下面遍历所有child的高度
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) //采用最大的view的高度。
                height = h;
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setPicItemList(Activity activity, List<PictureConfigResponse.PicItem> list) {
        if (list != null) {
            for (PictureConfigResponse.PicItem adItem : list) {
                AdImageView adImageView = createAdImageView(adItem);
                ImageLoader.with(activity).loadPicture(adImageView, adItem.imgurl);
                myPagerAdapter.addAdImageView(adImageView);
            }
            myPagerAdapter.notifyDataSetChanged();
        }
    }

    private AdImageView createAdImageView(PictureConfigResponse.PicItem adItem) {
        AdImageView adImageView = new AdImageView(getContext());
        ViewPager.LayoutParams layoutParams = new ViewPager.LayoutParams();
        layoutParams.width = ViewPager.LayoutParams.MATCH_PARENT;
        layoutParams.height = LayoutParams.WRAP_CONTENT;
        adImageView.setLayoutParams(layoutParams);
        adImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        adImageView.setAdItem(adItem);
        return adImageView;
    }

    public void setPicItemList(Fragment fragment, List<PictureConfigResponse.PicItem> list) {
        if (list != null) {
            for (PictureConfigResponse.PicItem adItem : list) {
                AdImageView adImageView = createAdImageView(adItem);
                ImageLoader.with(fragment).loadPicture(adImageView, adItem.imgurl);
                myPagerAdapter.addAdImageView(adImageView);
            }
            myPagerAdapter.notifyDataSetChanged();
        }
    }

    public void setPicItemList(Context context, List<PictureConfigResponse.PicItem> list) {
        if (list != null) {
            for (PictureConfigResponse.PicItem adItem : list) {
                AdImageView adImageView = createAdImageView(adItem);
                ImageLoader.with(context).loadPicture(adImageView, adItem.imgurl);
                myPagerAdapter.addAdImageView(adImageView);
            }
            myPagerAdapter.notifyDataSetChanged();
        }
    }

    public int getPicItemListSize() {
        return myPagerAdapter.getDataSize();
    }

    /**
     * 启动自动轮播
     */
    public void startAutoPlay() {
        mCompositeDisposable.clear();
        Disposable disposable = Observable.interval(PALY_PERIOD, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        int position = getCurrentItem() + 1;
                        setCurrentItem(position);
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    /**
     * 停止自动轮播
     */
    public void stopAutoPlay() {
        mCompositeDisposable.clear();
    }

    public interface OnItemPageClickListener {
        void onItemPageClick(int position, PictureConfigResponse.PicItem adItem);
    }

    class MyPagerAdapter extends PagerAdapter {


        private List<AdImageView> adImageViewList = new ArrayList<>();

        public void addAdImageView(AdImageView adImageView) {
            adImageViewList.add(adImageView);
        }


        @Override
        public int getCount() {
            if (adImageViewList.size() == 0) {
                return 0;
            }
            return Integer.MAX_VALUE;
        }

        @Override
        public AdImageView instantiateItem(ViewGroup container, final int position) {
            final AdImageView bannerView = getItem(position);
            if (bannerView.getRootView() != null) {
                container.removeView(bannerView);
            }
            bannerView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemPageClickListener != null) {
                        onItemPageClickListener.onItemPageClick(position, bannerView.getAdItem());
                    }
                }
            });

            container.addView(bannerView);
            return bannerView;
        }

        public AdImageView getItem(int position) {
            int index = position % getDataSize();
            return adImageViewList.get(index);
        }

        public int getDataSize() {
            return adImageViewList.size();
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            //无数据时不显示
            if (adImageViewList.size() == 0) {
                setVisibility(View.GONE);
            } else {
                setVisibility(View.VISIBLE);
            }
        }
    }
}
