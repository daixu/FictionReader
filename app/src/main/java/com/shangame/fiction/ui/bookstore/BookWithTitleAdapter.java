package com.shangame.fiction.ui.bookstore;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.storage.model.BookInfoEntity;
import com.shangame.fiction.ui.bookdetail.BookDetailActivity;

/**
 * Create by Speedy on 2018/7/27
 */
public class BookWithTitleAdapter extends WrapRecyclerViewAdapter<BookInfoEntity, BookWithTitleViewHolder> {

    private Context mActivity;

    public BookWithTitleAdapter(Context activity) {
        mActivity = activity;
    }

    @NonNull
    @Override
    public BookWithTitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_store_item_with_title, parent, false);
        return new BookWithTitleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookWithTitleViewHolder holder, int position) {
        final BookInfoEntity bookInfoEntity = getItem(position);
        if (null != bookInfoEntity) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BookDetailActivity.lunchActivity(mActivity, bookInfoEntity.bookid, ApiConstant.ClickType.FROM_CLICK);
                }
            });

            ImageLoader.with(mActivity).loadCover(holder.ivCover, bookInfoEntity.bookcover);
            holder.tvBookName.setText(bookInfoEntity.bookname);
            holder.tvBookAuthor.setText(bookInfoEntity.author);
        }
    }
}
