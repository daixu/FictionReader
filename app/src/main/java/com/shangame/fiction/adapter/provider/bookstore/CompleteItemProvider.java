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
 * 经典完本
 */
public class CompleteItemProvider extends BaseItemProvider<NormalMultipleEntity, BaseViewHolder> {
    @Override
    public int viewType() {
        return FeaturedAdapter.TYPE_COMPLETE;
    }

    @Override
    public int layout() {
        return R.layout.item_complete_view;
    }

    @Override
    public void convert(@NonNull BaseViewHolder helper, NormalMultipleEntity data, int position) {
        RecyclerView recyclerView = helper.getView(R.id.recycler_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        recyclerView.setLayoutManager(gridLayoutManager);

        if (recyclerView.getItemDecorationCount() == 0) {
            recyclerView.addItemDecoration(new SpaceItemDecoration(35));
        }

        BookWithTitleAdapter bookFinishAdapter = new BookWithTitleAdapter(mContext);
        recyclerView.setAdapter(bookFinishAdapter);

        bookFinishAdapter.clear();
        if (null != data.mCompleteData) {
            bookFinishAdapter.addAll(data.mCompleteData);
            bookFinishAdapter.notifyDataSetChanged();
        }

        helper.getView(R.id.tv_complete_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookListByTypeActivity.lunchActivity(mContext, BookStoreType.FinishMore, "经典完本", BookStoreChannel.CHOICENESS);
            }
        });
    }
}
