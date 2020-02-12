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
public class NewBookAddedAdapter extends WrapRecyclerViewAdapter<BookInfoEntity, BookInfoViewHolder> {

    private Activity mActivity;

    private static final int WITH_TITLE = 0;
    private static final int WITH_CONTENT = 1;
    

    public NewBookAddedAdapter(Activity activity) {
        this.mActivity = activity;
    }



    @NonNull
    @Override
    public BookInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == WITH_TITLE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_store_item_with_title,parent,false);
            return new BookInfoViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_store_item_with_content,parent,false);
            return new BookInfoViewHolder(view);
        }
    }



    @Override
    public void onBindViewHolder(@NonNull BookInfoViewHolder holder, int position) {
        final BookInfoEntity bookInfoEntity = getItem(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookDetailActivity.lunchActivity(mActivity, bookInfoEntity.bookid, ApiConstant.ClickType.FROM_CLICK);
            }
        });

        ImageLoader.with(mActivity).loadCover(holder.ivCover, bookInfoEntity.bookcover);

        int viewType = getItemViewType(position);
        if(viewType == WITH_CONTENT){
            holder.tvContent.setText(bookInfoEntity.synopsis);
        }
        holder.tvBookName.setText(bookInfoEntity.bookname);
        holder.tvBookAuthor.setText(bookInfoEntity.author);
    }

    @Override
    public int getItemViewType(int position) {
        if(position < 4){
            return WITH_TITLE;
        }else{
            return WITH_CONTENT;
        }
    }
}
