package com.shangame.fiction.ui.bookstore;

import android.app.Activity;
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
 * 书城重磅推荐
 */
public class HeavyRecommendAdapter extends WrapRecyclerViewAdapter<BookInfoEntity, BookWithContentViewHolder> {

    private int clickType;
    private Context mActivity;

    public HeavyRecommendAdapter(Context activity) {
        mActivity = activity;
        clickType = ApiConstant.ClickType.FROM_CLICK;//默认点击
    }

    public HeavyRecommendAdapter(Activity activity, int clickType) {
        this.clickType = clickType;
        mActivity = activity;
    }

    @NonNull
    @Override
    public BookWithContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choiceness_recommend_horizontal, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_store_item_with_title, parent, false);
        }
        return new BookWithContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookWithContentViewHolder holder, int position) {
        final BookInfoEntity bookInfoEntity = getItem(position);
        if (bookInfoEntity != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BookDetailActivity.lunchActivity(mActivity, bookInfoEntity.bookid, clickType);
                }
            });

            int viewType = getItemViewType(position);
            ImageLoader.with(mActivity).loadCover(holder.ivCover, bookInfoEntity.bookcover);
            if (viewType == 0) {
                holder.tvBookName.setText(bookInfoEntity.bookname);
                holder.tvContent.setText(bookInfoEntity.synopsis);
                String content = bookInfoEntity.author;
                holder.tvBookAuthor.setText(content);
                holder.textClassName.setText(bookInfoEntity.classname);
                holder.textStatus.setText(bookInfoEntity.serstatus);
                holder.textWorkCount.setText(bookInfoEntity.wordnumbers);
            } else {
                holder.tvBookName.setText(bookInfoEntity.bookname);
                holder.tvBookAuthor.setText(bookInfoEntity.author);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }
}
