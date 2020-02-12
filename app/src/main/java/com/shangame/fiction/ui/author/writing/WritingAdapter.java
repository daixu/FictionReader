package com.shangame.fiction.ui.author.writing;

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
 * Create by Speedy on 2019/7/24
 */
public class WritingAdapter extends BaseQuickAdapter<PageDataBean, BaseViewHolder> {

    public WritingAdapter(int layoutResId, @Nullable List<PageDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PageDataBean item) {
        ImageView imgCover = helper.getView(R.id.img_cover);
        TextView textName = helper.getView(R.id.text_name);
        TextView textLabel = helper.getView(R.id.text_label);

        textName.setText(item.bookname);

        RequestOptions options = new RequestOptions()
                .override(120, 160);
        Glide.with(mContext)
                .asBitmap()
                .load(item.bookcover)
                .apply(options)
                .into(imgCover);
        // Glide.with(mContext).load(item.bookcover).into(imgCover);
        textLabel.setText(item.verifyname);

        helper.addOnClickListener(R.id.btn_edit);
        helper.addOnClickListener(R.id.img_setting);
        helper.addOnClickListener(R.id.text_label);
    }
}
