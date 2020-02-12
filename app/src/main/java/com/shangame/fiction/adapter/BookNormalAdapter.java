package com.shangame.fiction.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.storage.model.BookInfoEntity;
import com.shangame.fiction.widget.GlideApp;

import java.util.List;

public class BookNormalAdapter extends BaseQuickAdapter<BookInfoEntity, BaseViewHolder> {
    public BookNormalAdapter(int layoutResId, @Nullable List<BookInfoEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BookInfoEntity item) {
        ImageView imageCover = helper.getView(R.id.ivCover);
        GlideApp.with(mContext)
                .load(item.bookcover)
                .placeholder(R.drawable.default_cover)
                .centerCrop()
                .into(imageCover);
        helper.setText(R.id.tvBookName, item.bookname);
        helper.setText(R.id.tvContent, item.synopsis);
        helper.setText(R.id.tvBookAuthor, item.author + "·" + item.classname + "·" + item.serstatus + "·" + item.wordnumbers);
    }
}
