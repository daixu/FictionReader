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
import com.shangame.fiction.ui.bookstore.BoutiqueSetAdapter;
import com.shangame.fiction.widget.BoutiquepaceItemDecoration;

/**
 * 精品专场
 */
public class BoutiqueItemProvider extends BaseItemProvider<NormalMultipleEntity, BaseViewHolder> {
    @Override
    public int viewType() {
        return FeaturedAdapter.TYPE_BOUTIQUE;
    }

    @Override
    public int layout() {
        return R.layout.item_boutique_view;
    }

    @Override
    public void convert(@NonNull BaseViewHolder helper, NormalMultipleEntity data, int position) {
        RecyclerView recyclerView = helper.getView(R.id.recycler_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position >= 4) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });

        recyclerView.setLayoutManager(gridLayoutManager);
        if (recyclerView.getItemDecorationCount() == 0) {
            recyclerView.addItemDecoration(new BoutiquepaceItemDecoration(35));
        }
        BoutiqueSetAdapter boutiqueSetAdapter = new BoutiqueSetAdapter(mContext);
        recyclerView.setAdapter(boutiqueSetAdapter);

        boutiqueSetAdapter.clear();
        if (null != data.mChoiceData) {
            boutiqueSetAdapter.addAll(data.mChoiceData);
            boutiqueSetAdapter.notifyDataSetChanged();
        }

        helper.getView(R.id.tv_boutique_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookListByTypeActivity.lunchActivity(mContext, BookStoreType.BoutiqueSetMore, "精品专场", BookStoreChannel.CHOICENESS);
            }
        });
    }
}
