package com.shangame.fiction.ui.search;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.storage.db.BookReadProgressDao;
import com.shangame.fiction.storage.manager.DbManager;
import com.shangame.fiction.storage.model.BookInfoEntity;
import com.shangame.fiction.storage.model.BookReadProgress;
import com.shangame.fiction.ui.bookdetail.BookDetailActivity;
import com.shangame.fiction.ui.bookstore.SearchViewHolder;
import com.shangame.fiction.ui.reader.ReadActivity;

import java.util.List;

/**
 * Create by Speedy on 2018/7/27
 */
public class SearchHintAdapter extends WrapRecyclerViewAdapter<BookInfoEntity, SearchViewHolder> {

    private boolean isShowAll;
    private Activity mActivity;

    public SearchHintAdapter(Activity activity) {
        mActivity = activity;
    }

    public boolean isShowAll() {
        return isShowAll;
    }

    public void setShowAll(boolean showAll) {
        isShowAll = showAll;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_hint_item, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        final BookInfoEntity bookInfoEntity = getItem(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookDetailActivity.lunchActivity(mActivity, bookInfoEntity.bookid, ApiConstant.ClickType.FROM_CLICK);
            }
        });

        ImageLoader.with(mActivity).loadCover(holder.ivCover, bookInfoEntity.bookcover);
        holder.tvBookName.setText(Html.fromHtml(bookInfoEntity.bookname));
        holder.tvRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookReadProgressDao bookReadProgressDao = DbManager.getDaoSession(mActivity).getBookReadProgressDao();
                List<BookReadProgress> bookReadProgressList = bookReadProgressDao.queryBuilder().where(BookReadProgressDao.Properties.BookId.eq(bookInfoEntity.bookid)).build().list();

                if (bookReadProgressList != null && bookReadProgressList.size() > 0) {
                    BookReadProgress bookReadProgress = bookReadProgressList.get(0);
                    int pageIndex = bookReadProgress.pageIndex;
                    ReadActivity.lunchActivity(mActivity, bookReadProgress.bookId, bookReadProgress.chapterId, pageIndex, 1);
                } else {
                    ReadActivity.lunchActivity(mActivity, bookInfoEntity.bookid, bookInfoEntity.chapterid);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (isShowAll) {
            return super.getItemCount();
        } else {
            if (super.getItemCount() >= 1) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
