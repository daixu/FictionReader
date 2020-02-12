package com.shangame.fiction.ui.search;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.storage.model.BookInfoEntity;
import com.shangame.fiction.ui.bookdetail.BookDetailActivity;
import com.shangame.fiction.ui.bookstore.SearchViewHolder;

/**
 * Create by Speedy on 2018/7/27
 */
public class SearchAdapter extends WrapRecyclerViewAdapter<BookInfoEntity, SearchViewHolder> {

    private Activity mActivity;

    public SearchAdapter(Activity activity) {
        mActivity = activity;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        final BookInfoEntity bookInfoEntity = getItem(position);
        if (null != bookInfoEntity) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (bookInfoEntity.booktype == 1) {
                        ARouter.getInstance()
                                .build("/ss/listen/detail")
                                .withInt("albumId", (int) bookInfoEntity.bookid)
                                .navigation();
                    } else {
                        BookDetailActivity.lunchActivity(mActivity, bookInfoEntity.bookid, ApiConstant.ClickType.FROM_SEARCH);
                    }
                }
            });
            if (bookInfoEntity.booktype == 1) {
                holder.ivCover.setImageResource(R.drawable.icon_type_listen);
            } else {
                holder.ivCover.setImageResource(R.drawable.book);
            }
            holder.tvBookName.setText(Html.fromHtml(bookInfoEntity.bookname));
        }
    }
}
