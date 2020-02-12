package com.shangame.fiction.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.ShareRecListResp;
import com.shangame.fiction.widget.GlideApp;

import java.util.List;

public class ShareBookListAdapter extends BaseQuickAdapter<ShareRecListResp.DataBean.RecDataBean, BaseViewHolder> {

    public ShareBookListAdapter(int layoutResId, @Nullable List<ShareRecListResp.DataBean.RecDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, ShareRecListResp.DataBean.RecDataBean item) {
        ImageView imageBook = helper.getView(R.id.image_book);
        GlideApp.with(mContext)
                .load(item.bookcover)
                .placeholder(R.drawable.default_cover)
                .centerCrop()
                .into(imageBook);

        helper.setText(R.id.text_book_name, item.bookname);
        helper.setText(R.id.text_author, item.author);
    }
}
