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

public class EditorRecommendAdapter extends BaseQuickAdapter<BookInfoEntity, BaseViewHolder> {
    public EditorRecommendAdapter(int layoutResId, @Nullable List<BookInfoEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, BookInfoEntity item) {
        ImageView imageCover = helper.getView(R.id.image_cover);
        GlideApp.with(mContext)
                .load(item.bookcover)
                .placeholder(R.drawable.default_cover)
                .centerCrop()
                .into(imageCover);

        helper.setText(R.id.text_book_name, item.bookname);
        helper.setText(R.id.text_class_name, item.classname);
        helper.setText(R.id.text_book_author, item.author);
    }
}
