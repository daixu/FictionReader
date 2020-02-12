package com.shangame.fiction.adapter.provider.bookstore;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.FeaturedAdapter;
import com.shangame.fiction.entity.NormalMultipleEntity;
import com.shangame.fiction.ui.bookstore.BookListByTypeActivity;
import com.shangame.fiction.ui.bookstore.BookStoreChannel;
import com.shangame.fiction.ui.bookstore.BookStoreType;
import com.shangame.fiction.ui.bookstore.BookWithTitleAdapter;
import com.shangame.fiction.widget.SpaceItemDecoration;

/**
 * 畅销热搜
 */
public class HotSearchItemProvider extends BaseItemProvider<NormalMultipleEntity, BaseViewHolder> {
    @Override
    public int viewType() {
        return FeaturedAdapter.TYPE_HOT_SEARCH;
    }

    @Override
    public int layout() {
        return R.layout.item_hot_search_view;
    }

    @Override
    public void convert(@NonNull BaseViewHolder helper, NormalMultipleEntity data, int position) {
        RecyclerView recyclerView = helper.getView(R.id.recycler_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        recyclerView.setLayoutManager(gridLayoutManager);

        if (recyclerView.getItemDecorationCount() == 0) {
            recyclerView.addItemDecoration(new SpaceItemDecoration(35));
        }

        BookWithTitleAdapter hotSearchAdapter = new BookWithTitleAdapter(mContext);
        recyclerView.setAdapter(hotSearchAdapter);

        hotSearchAdapter.clear();
        if (null != data.mSearchData) {
            hotSearchAdapter.addAll(data.mSearchData);
            hotSearchAdapter.notifyDataSetChanged();
        }

        helper.getView(R.id.tv_hot_search_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookListByTypeActivity.lunchActivity(mContext, BookStoreType.HotSearachMore, "畅销热搜", BookStoreChannel.CHOICENESS);
            }
        });
    }
}
