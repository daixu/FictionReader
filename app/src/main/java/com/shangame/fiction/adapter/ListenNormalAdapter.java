package com.shangame.fiction.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.AlbumDataResponse;

import java.util.List;

public class ListenNormalAdapter extends BaseQuickAdapter<AlbumDataResponse.PageDataBean, BaseViewHolder> {
    public ListenNormalAdapter(int layoutResId, @Nullable List<AlbumDataResponse.PageDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AlbumDataResponse.PageDataBean item) {
        helper.setText(R.id.text_book_name, item.albumName);
        helper.setText(R.id.text_synopsis, item.synopsis);

        StringBuilder type = new StringBuilder();
        if (!TextUtils.isEmpty(item.author)) {
            String author = "";
            if (item.author.length() > 4) {
                author = item.author.substring(0, 3) + "...";
            } else {
                author = item.author;
            }
            type.append(author);
            type.append("·");
        }
        if (!TextUtils.isEmpty(item.classname)) {
            type.append(item.classname);
            type.append("·");
        }
        if (!TextUtils.isEmpty(item.serstatus)) {
            type.append(item.serstatus);
            type.append("·");
        }

        type.append(item.chapternumber);
        type.append("集");
        helper.setText(R.id.text_book_type, type);

        ImageView imageCover = helper.getView(R.id.image_cover);
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.default_cover)
                .override(104, 104);
        Glide.with(mContext).load(item.bookcover).apply(options).into(imageCover);
    }
}
