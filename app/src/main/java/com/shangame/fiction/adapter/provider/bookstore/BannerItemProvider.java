package com.shangame.fiction.adapter.provider.bookstore;

import android.support.annotation.NonNull;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.FeaturedAdapter;
import com.shangame.fiction.entity.NormalMultipleEntity;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.PictureConfigResponse;
import com.shangame.fiction.ui.bookdetail.BookDetailActivity;
import com.shangame.fiction.ui.web.WebViewActivity;
import com.shangame.fiction.widget.ad.AdViewPagerBanner;

public class BannerItemProvider extends BaseItemProvider<NormalMultipleEntity, BaseViewHolder> {
    @Override
    public int viewType() {
        return FeaturedAdapter.TYPE_BANNER;
    }

    @Override
    public int layout() {
        return R.layout.item_banner_view;
    }

    @Override
    public void convert(@NonNull BaseViewHolder helper, NormalMultipleEntity data, int position) {
        AdViewPagerBanner adViewPagerBanner = helper.getView(R.id.adViewPagerBanner);
        View bannerShadow = helper.getView(R.id.bannerShadow);
        adViewPagerBanner.setPicItemList(mContext, data.mBannerData);
        if (adViewPagerBanner.getPicItemListSize() > 0) {
            bannerShadow.setVisibility(View.VISIBLE);
            adViewPagerBanner.startAutoPlay();
        }

        adViewPagerBanner.setOnItemPageClickListener(new AdViewPagerBanner.OnItemPageClickListener() {
            @Override
            public void onItemPageClick(int position, PictureConfigResponse.PicItem adItem) {
                if (adItem == null) {
                    return;
                }
                if (adItem.bookid == 0) {
                    WebViewActivity.lunchActivity(mContext, "", adItem.linkurl);
                } else {
                    BookDetailActivity.lunchActivity(mContext, adItem.bookid, ApiConstant.ClickType.FROM_CLICK);
                }
            }
        });
    }
}
