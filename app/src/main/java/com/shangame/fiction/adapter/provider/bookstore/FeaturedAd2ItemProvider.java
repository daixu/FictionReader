package com.shangame.fiction.adapter.provider.bookstore;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTImage;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.FeaturedAdapter;
import com.shangame.fiction.entity.NormalMultipleEntity;
import com.shangame.fiction.widget.GlideApp;

import java.util.ArrayList;
import java.util.List;

public class FeaturedAd2ItemProvider extends BaseItemProvider<NormalMultipleEntity, BaseViewHolder> {

    @Override
    public int viewType() {
        return FeaturedAdapter.TYPE_AD2;
    }

    @Override
    public int layout() {
        return R.layout.item_featured_ad_view_1;
    }

    @Override
    public void convert(@NonNull BaseViewHolder helper, NormalMultipleEntity data, int position) {
        //可以被点击的view, 也可以把convertView放进来意味item可被点击
        RelativeLayout layoutContent = helper.getView(R.id.layout_content);
        Button creativeButton = helper.getView(R.id.btn_listitem_creative);
        ImageView smallImage = helper.getView(R.id.iv_listitem_image);
        List<View> clickViewList = new ArrayList<>();
        clickViewList.add(layoutContent);
        //触发创意广告的view（点击下载或拨打电话）
        List<View> creativeViewList = new ArrayList<>();
        creativeViewList.add(creativeButton);

        if (null != data.mAds) {
            TTFeedAd ad = data.mAds.get(0);
            //重要! 这个涉及到广告计费，必须正确调用。convertView必须使用ViewGroup。
            ad.registerViewForInteraction(layoutContent, clickViewList, creativeViewList, new TTNativeAd.AdInteractionListener() {
                @Override
                public void onAdClicked(View view, TTNativeAd ad) {
                    if (ad != null) {
                        Log.e("hhh", "广告" + ad.getTitle() + "被点击");
                    }
                }

                @Override
                public void onAdCreativeClick(View view, TTNativeAd ad) {
                    if (ad != null) {
                        Log.e("hhh", "广告" + ad.getTitle() + "被创意按钮被点击");
                    }
                }

                @Override
                public void onAdShow(TTNativeAd ad) {
                    if (ad != null) {
                        Log.e("hhh", "广告" + ad.getTitle() + "展示");
                    }
                }
            });
            helper.setText(R.id.tv_listitem_ad_title, ad.getTitle());
            helper.setText(R.id.tv_listitem_ad_desc, ad.getDescription());
            helper.setText(R.id.tv_listitem_ad_source, ad.getSource() == null ? "广告来源" : data.mAds.get(0).getSource());

            TTImage icon = ad.getIcon();
            if (icon != null && icon.isValid()) {
                GlideApp.with(mContext)
                        .load(icon.getImageUrl())
                        .placeholder(R.drawable.default_cover)
                        .centerCrop()
                        .into(smallImage);
            }

            switch (ad.getInteractionType()) {
                case TTAdConstant.INTERACTION_TYPE_DOWNLOAD:
                    if (mContext instanceof Activity) {
                        ad.setActivityForDownloadApp((Activity) mContext);
                    }
                    creativeButton.setVisibility(View.VISIBLE);
                    break;
                case TTAdConstant.INTERACTION_TYPE_DIAL:
                    creativeButton.setVisibility(View.VISIBLE);
                    creativeButton.setText("立即拨打");
                    break;
                case TTAdConstant.INTERACTION_TYPE_LANDING_PAGE:
                case TTAdConstant.INTERACTION_TYPE_BROWSER:
                    creativeButton.setVisibility(View.VISIBLE);
                    creativeButton.setText("查看详情");
                    break;
                default:
                    creativeButton.setVisibility(View.GONE);
                    Log.e("hhh", "交互类型异常");
            }
        }
    }
}
