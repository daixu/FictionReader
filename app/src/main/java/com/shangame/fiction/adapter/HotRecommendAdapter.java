package com.shangame.fiction.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.AlubmDetailResponse;

import java.util.List;

public class HotRecommendAdapter extends BaseQuickAdapter<AlubmDetailResponse.ClickBookBean, BaseViewHolder> {
    public HotRecommendAdapter(int layoutResId, @Nullable List<AlubmDetailResponse.ClickBookBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AlubmDetailResponse.ClickBookBean item) {
        ImageView imageCover = helper.getView(R.id.image_cover);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.default_cover)
                .override(104, 104);
        Glide.with(mContext).load(item.bookcover).apply(options).into(imageCover);

        helper.setText(R.id.text_book_name, item.albumName);
        helper.setText(R.id.text_book_author, item.author);
    }
}
