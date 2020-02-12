package com.shangame.fiction.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.net.response.GetGiftListConfigResponse;

import java.util.List;

public class RewardAdapter extends BaseQuickAdapter<GetGiftListConfigResponse.GiftBean, BaseViewHolder> {
    public RewardAdapter(int layoutResId, @Nullable List<GetGiftListConfigResponse.GiftBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetGiftListConfigResponse.GiftBean item) {
        ImageView giftIcon = helper.getView(R.id.giftIcon);
        ImageLoader.with(mContext).loadPicture(giftIcon, item.propimage, 80, 80);
        LinearLayout borderLayout = helper.getView(R.id.borderLayout);
        helper.setText(R.id.giftName, item.propname);
        helper.setText(R.id.giftCoin, item.price + "闪闪币");
        if (item.isChecked) {
            borderLayout.setSelected(true);
        } else {
            borderLayout.setSelected(false);
        }
    }
}
