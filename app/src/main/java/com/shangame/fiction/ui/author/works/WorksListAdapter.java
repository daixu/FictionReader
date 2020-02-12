package com.shangame.fiction.ui.author.works;

import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.PageDataBean;

import java.util.List;

/**
 * Create by Speedy on 2019/7/31
 */
public class WorksListAdapter extends BaseQuickAdapter<PageDataBean, BaseViewHolder> {
    public WorksListAdapter(int layoutResId, @Nullable List<PageDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PageDataBean item) {
        TextView textName = helper.getView(R.id.text_name);
        textName.setText(item.bookname);

        ImageView imageCover = helper.getView(R.id.image_cover);
        RequestOptions options = new RequestOptions()
                .override(100, 128);
        Glide.with(mContext)
                .asBitmap()
                .load(item.bookcover)
                .apply(options)
                .into(imageCover);
    }
}
