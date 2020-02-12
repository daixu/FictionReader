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
import com.shangame.fiction.storage.model.BookInfoEntity;
import com.shangame.fiction.ui.bookdetail.BookDetailActivity;

/**
 * Create by Speedy on 2018/7/27
 */
public class BookWithHotDemandAdapter extends WrapRecyclerViewAdapter<BookInfoEntity, BookWithHotDemandViewHolder> {

    private Activity mActivity;

    public BookWithHotDemandAdapter(Activity activity) {
        mActivity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return 0;
        }else{
            return 1;
        }
    }


    @NonNull
    @Override
    public BookWithHotDemandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        if(viewType == 0){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_store_item_hot_demand_book_no1,parent,false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_store_item_hot_demand_book,parent,false);
        }
        BookWithHotDemandViewHolder  bookWithHotDemandViewHolder = new BookWithHotDemandViewHolder(view);
        return bookWithHotDemandViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull BookWithHotDemandViewHolder holder, int position) {

        final BookInfoEntity bookInfoEntity = getItem(position);

        if(position == 1){
            holder.ivNo.setImageResource(R.drawable.no2);
            holder.ivNo.setVisibility(View.VISIBLE);
        }else if(position == 2){
            holder.ivNo.setImageResource(R.drawable.no3);
            holder.ivNo.setVisibility(View.VISIBLE);
        }else if(position > 2){
            holder.ivNo.setVisibility(View.GONE);
        }

        holder.tvBookName.setText(bookInfoEntity.bookname);
        holder.tvBookAuthor.setText( mActivity.getString(R.string.author_zhu, bookInfoEntity.author));

        ImageLoader.with(mActivity).loadCover(holder.ivCover, bookInfoEntity.bookcover);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookDetailActivity.lunchActivity(mActivity, bookInfoEntity.bookid,ApiConstant.ClickType.FROM_CLICK);

            }
        });
    }

    @Override
    public int getItemCount() {
        if(super.getItemCount() > 5){
            return 5;
        }
        return super.getItemCount();
    }
}
