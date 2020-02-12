package com.shangame.fiction.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.storage.model.PosterBean;

import java.util.List;

public class SharePosterAdapter extends BaseQuickAdapter<PosterBean, BaseViewHolder> {

    public SharePosterAdapter(int layoutResId, @Nullable List<PosterBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PosterBean item) {
        ConstraintLayout layoutContent1 = helper.getView(R.id.layout_content_1);
        ConstraintLayout layoutContent2 = helper.getView(R.id.layout_content_2);
        int type = item.type;
        if (type == 1) {
            layoutContent1.setVisibility(View.VISIBLE);
            layoutContent2.setVisibility(View.GONE);
            ImageView imagePoster1 = helper.getView(R.id.image_poster_1);
            imagePoster1.setImageResource(item.resId);
        } else {
            layoutContent2.setVisibility(View.VISIBLE);
            layoutContent1.setVisibility(View.GONE);
            ImageView imagePoster2 = helper.getView(R.id.image_poster_2);
            imagePoster2.setImageResource(item.resId);
        }
    }
}
