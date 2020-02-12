package com.shangame.fiction.ui.bookstore;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.storage.model.NewBookInforankingEntity;
import com.shangame.fiction.ui.bookdetail.BookDetailActivity;

import java.util.List;

/**
 * Create by Speedy on 2019/3/5
 */
public class BookStoreRankAdapter extends WrapRecyclerViewAdapter<NewBookInforankingEntity, BookWithTitleViewHolder> {

    private Activity mActivity;
    private String labelName = "热度: ";

    public BookStoreRankAdapter(Activity activity) {
        mActivity = activity;
    }

    @NonNull
    @Override
    public BookWithTitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_store_item_with_title_land, parent, false);
        return new BookWithTitleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookWithTitleViewHolder holder, int position) {
        final NewBookInforankingEntity newBookInforankingEntity = getItem(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookDetailActivity.lunchActivity(mActivity, newBookInforankingEntity.bookid, ApiConstant.ClickType.FROM_CLICK);
            }
        });

        ImageLoader.with(mActivity).loadCover(holder.ivCover, newBookInforankingEntity.bookcover);

        holder.tvBookName.setText(newBookInforankingEntity.bookname);
        holder.tvBookAuthor.setText(labelName + " : " + newBookInforankingEntity.numbers);
    }

    @Override
    public void addAll(List<NewBookInforankingEntity> list) {
        int halfLength = list.size() / 2;
        NewBookInforankingEntity newBookInforankingEntity;
        for (int i = 0; i < halfLength; i++) {
            newBookInforankingEntity = list.get(i);
            newBookInforankingEntity.bookname = (i + 1) + "." + newBookInforankingEntity.bookname;
            add(newBookInforankingEntity);

            newBookInforankingEntity = list.get(i + halfLength);
            newBookInforankingEntity.bookname = (i + halfLength + 1) + "." + newBookInforankingEntity.bookname;
            add(newBookInforankingEntity);
        }
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }
}
