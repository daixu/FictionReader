package com.shangame.fiction.adapter;

import android.support.annotation.Nullable;

import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.MultipleItemRvAdapter;
import com.shangame.fiction.adapter.provider.bookstore.BannerItemProvider;
import com.shangame.fiction.adapter.provider.bookstore.BoutiqueItemProvider;
import com.shangame.fiction.adapter.provider.bookstore.BoyGirlItemProvider;
import com.shangame.fiction.adapter.provider.bookstore.CompleteItemProvider;
import com.shangame.fiction.adapter.provider.bookstore.FeaturedAd1ItemProvider;
import com.shangame.fiction.adapter.provider.bookstore.FeaturedAd2ItemProvider;
import com.shangame.fiction.adapter.provider.bookstore.HighlyRecommendItemProvider;
import com.shangame.fiction.adapter.provider.bookstore.HotSearchItemProvider;
import com.shangame.fiction.adapter.provider.bookstore.ImgItemProvider;
import com.shangame.fiction.adapter.provider.bookstore.LabelItemProvider;
import com.shangame.fiction.adapter.provider.bookstore.MenuItemProvider;
import com.shangame.fiction.adapter.provider.bookstore.OtherItemProvider;
import com.shangame.fiction.adapter.provider.bookstore.RecommendItemProvider;
import com.shangame.fiction.adapter.provider.bookstore.SerialItemProvider;
import com.shangame.fiction.entity.NormalMultipleEntity;
import com.shangame.fiction.net.response.ChoicenessResponse;
import com.shangame.fiction.net.response.PictureConfigResponse;
import com.shangame.fiction.storage.model.BookInfoEntity;

import java.util.List;

public class FeaturedAdapter extends MultipleItemRvAdapter<NormalMultipleEntity, BaseViewHolder> {
    public static final int TYPE_BANNER = 100;
    public static final int TYPE_MENU = 200;
    public static final int TYPE_IMG = 300;
    public static final int TYPE_RECOMMEND = 400;
    public static final int TYPE_BOUTIQUE = 500;
    public static final int TYPE_HIGHLY_RECOMMEND = 600;
    public static final int TYPE_SERIAL = 700;
    public static final int TYPE_BOY_GIRL = 800;
    public static final int TYPE_HOT_SEARCH = 900;
    public static final int TYPE_COMPLETE = 1000;
    public static final int TYPE_LABEL = 1100;
    public static final int TYPE_OTHER = 1200;
    public static final int TYPE_AD1 = 1300;
    public static final int TYPE_AD2 = 1400;

    private List<NormalMultipleEntity> mData;

    public FeaturedAdapter(@Nullable List<NormalMultipleEntity> data) {
        super(data);

        this.mData = data;
        finishInitialize();
    }

    @Override
    protected int getViewType(NormalMultipleEntity entity) {
        if (entity.type == NormalMultipleEntity.BANNER) {
            return TYPE_BANNER;
        } else if (entity.type == NormalMultipleEntity.MENU) {
            return TYPE_MENU;
        } else if (entity.type == NormalMultipleEntity.IMG) {
            return TYPE_IMG;
        } else if (entity.type == NormalMultipleEntity.RECOMMEND) {
            return TYPE_RECOMMEND;
        } else if (entity.type == NormalMultipleEntity.BOUTIQUE) {
            return TYPE_BOUTIQUE;
        } else if (entity.type == NormalMultipleEntity.HIGHLY_RECOMMEND) {
            return TYPE_HIGHLY_RECOMMEND;
        } else if (entity.type == NormalMultipleEntity.SERIAL) {
            return TYPE_SERIAL;
        } else if (entity.type == NormalMultipleEntity.BOY_GIRL) {
            return TYPE_BOY_GIRL;
        } else if (entity.type == NormalMultipleEntity.HOT_SEARCH) {
            return TYPE_HOT_SEARCH;
        } else if (entity.type == NormalMultipleEntity.COMPLETE) {
            return TYPE_COMPLETE;
        } else if (entity.type == NormalMultipleEntity.LABEL) {
            return TYPE_LABEL;
        } else if (entity.type == NormalMultipleEntity.OTHER) {
            return TYPE_OTHER;
        } else if (entity.type == NormalMultipleEntity.AD1) {
            return TYPE_AD1;
        } else if (entity.type == NormalMultipleEntity.AD2) {
            return TYPE_AD2;
        }
        return 0;
    }

    @Override
    public void registerItemProvider() {
        mProviderDelegate.registerProvider(new BannerItemProvider());
        mProviderDelegate.registerProvider(new MenuItemProvider());
        mProviderDelegate.registerProvider(new ImgItemProvider());
        mProviderDelegate.registerProvider(new RecommendItemProvider());
        mProviderDelegate.registerProvider(new BoutiqueItemProvider());
        mProviderDelegate.registerProvider(new FeaturedAd1ItemProvider());
        mProviderDelegate.registerProvider(new HighlyRecommendItemProvider());
        mProviderDelegate.registerProvider(new SerialItemProvider());
        mProviderDelegate.registerProvider(new BoyGirlItemProvider());
        mProviderDelegate.registerProvider(new HotSearchItemProvider());
        mProviderDelegate.registerProvider(new CompleteItemProvider());
        mProviderDelegate.registerProvider(new LabelItemProvider());
        mProviderDelegate.registerProvider(new FeaturedAd2ItemProvider());
        mProviderDelegate.registerProvider(new OtherItemProvider());
    }

    public void setBannerList(List<PictureConfigResponse.PicItem> data) {
        mData.get(0).mBannerData = data;
        notifyDataSetChanged();
    }

    public void setFeaturedData(ChoicenessResponse response) {
        mData.get(3).mHeavyData = response.heavydata;
        mData.get(4).mChoiceData = response.choicedata;
        mData.get(6).mRecData = response.recdata;
        mData.get(7).mHotData = response.hotdata;
        mData.get(9).mSearchData = response.searchdata;
        mData.get(10).mCompleteData = response.completedata;
        mData.get(11).mClassData = response.classdata;
        notifyDataSetChanged();
    }

    public void setAd1List(List<TTFeedAd> ads) {
        mData.get(5).mAds = ads;
        notifyDataSetChanged();
    }

    public void setAd2List(List<TTFeedAd> ads) {
        mData.get(12).mAds = ads;
        notifyDataSetChanged();
    }

    public void setOtherList(List<BookInfoEntity> data) {
        mData.get(13).mOtherData.addAll(data);
        notifyDataSetChanged();
    }
}
