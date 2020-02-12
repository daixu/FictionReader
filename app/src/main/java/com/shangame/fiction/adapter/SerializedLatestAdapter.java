package com.shangame.fiction.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.AlbumModuleResponse;
import com.shangame.fiction.widget.GlideApp;

import java.util.List;

public class SerializedLatestAdapter extends BaseMultiItemQuickAdapter<AlbumModuleResponse.HotDataBean, BaseViewHolder> {

    public SerializedLatestAdapter(List<AlbumModuleResponse.HotDataBean> data) {
        super(data);

        addItemType(1, R.layout.item_end_listen);
        addItemType(2, R.layout.item_must_listen);
    }

    @Override
    protected void convert(BaseViewHolder helper, AlbumModuleResponse.HotDataBean item) {
        switch (helper.getItemViewType()) {
            case 1:
                displayView1(helper, item);
                break;
            case 2:
                displayView2(helper, item);
                break;
            default:
                break;
        }
    }

    private void displayView1(BaseViewHolder helper, AlbumModuleResponse.HotDataBean item) {
        helper.setText(R.id.text_book_name, item.albumName);
        helper.setText(R.id.text_synopsis, item.synopsis);

        StringBuilder type = new StringBuilder();
        if (!TextUtils.isEmpty(item.author)) {
            type.append(item.author);
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
        GlideApp.with(mContext)
                .load(item.bookcover)
                .placeholder(R.drawable.default_cover)
                .centerCrop()
                .into(imageCover);
    }

    private void displayView2(BaseViewHolder helper, AlbumModuleResponse.HotDataBean item) {
        helper.setText(R.id.text_book_name, item.albumName);
        if (!TextUtils.isEmpty(item.author)) {
            helper.setText(R.id.text_book_author, item.author);
        }

        ImageView imageCover = helper.getView(R.id.image_cover);
        GlideApp.with(mContext)
                .load(item.bookcover)
                .placeholder(R.drawable.default_cover)
                .centerCrop()
                .into(imageCover);
    }
}
