package com.shangame.fiction.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.AlbumModuleResponse;
import com.shangame.fiction.widget.GlideApp;

import java.util.List;

public class MustListenAdapter extends BaseQuickAdapter<AlbumModuleResponse.CarDataBean, BaseViewHolder> {
    public MustListenAdapter(int layoutResId, @Nullable List<AlbumModuleResponse.CarDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AlbumModuleResponse.CarDataBean item) {
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
