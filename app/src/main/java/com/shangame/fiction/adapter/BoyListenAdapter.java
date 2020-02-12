package com.shangame.fiction.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.AlbumModuleResponse;
import com.shangame.fiction.widget.GlideApp;

import java.util.List;

public class BoyListenAdapter extends BaseQuickAdapter<AlbumModuleResponse.BoyDataBean, BaseViewHolder> {
    public BoyListenAdapter(int layoutResId, @Nullable List<AlbumModuleResponse.BoyDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AlbumModuleResponse.BoyDataBean item) {
        ImageView imageCover = helper.getView(R.id.image_cover);
        GlideApp.with(mContext)
                .load(item.bookcover)
                .placeholder(R.drawable.default_cover)
                .centerCrop()
                .into(imageCover);

        helper.setText(R.id.text_book_name, item.albumName);
        helper.setText(R.id.text_book_author, item.author);
    }
}
