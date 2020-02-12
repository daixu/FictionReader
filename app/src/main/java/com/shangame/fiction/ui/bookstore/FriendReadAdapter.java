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
public class FriendReadAdapter extends WrapRecyclerViewAdapter<BookInfoEntity, BookWithContentViewHolder> {

    private int clicktype;
    private Activity mActivity;

    public FriendReadAdapter(Activity activity){
        mActivity = activity;
        clicktype = ApiConstant.ClickType.FROM_CLICK;//默认点击
    }

    public FriendReadAdapter(Activity activity, int clicktype){
        this.clicktype = clicktype;
        mActivity = activity;
    }


    @NonNull
    @Override
    public BookWithContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        if(viewType == 0){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_store_item_with_title,parent,false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout. book_store_item_with_content,parent,false);
        }
        BookWithContentViewHolder bookWithContentViewHolder = new BookWithContentViewHolder(view);
        return bookWithContentViewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if(position % 6 < 4){
            return 0;
        }else{
            return 1;
        }
    }



    @Override
    public void onBindViewHolder(@NonNull BookWithContentViewHolder holder, int position) {


        final BookInfoEntity bookInfoEntity = getItem(position);

        if(bookInfoEntity != null){

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BookDetailActivity.lunchActivity(mActivity, bookInfoEntity.bookid,clicktype);
                }
            });

            int viewType = getItemViewType(position);
            ImageLoader.with(mActivity).loadCover(holder.ivCover, bookInfoEntity.bookcover);
            if(viewType == 0){
                holder.tvBookName.setText(bookInfoEntity.bookname);
                holder.tvBookAuthor.setText(bookInfoEntity.author);
            }else{
                holder.tvBookName.setText(bookInfoEntity.bookname);
                holder.tvContent.setText(bookInfoEntity.synopsis);
                holder.tvBookAuthor.setText(bookInfoEntity.author + "·"+bookInfoEntity.classname+"·"+bookInfoEntity.serstatus+"·"+bookInfoEntity.wordnumbers);
            }
        }
    }
}
