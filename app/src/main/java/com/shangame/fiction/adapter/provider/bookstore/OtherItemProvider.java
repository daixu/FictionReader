package com.shangame.fiction.adapter.provider.bookstore;

import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.FeaturedAdapter;
import com.shangame.fiction.entity.NormalMultipleEntity;
import com.shangame.fiction.ui.bookstore.OtherLookAdapter;
import com.shangame.fiction.widget.OtherLookItemDecoration;

public class OtherItemProvider extends BaseItemProvider<NormalMultipleEntity, BaseViewHolder> {
    @Override
    public int viewType() {
        return FeaturedAdapter.TYPE_OTHER;
    }

    @Override
    public int layout() {
        return R.layout.item_other_view;
    }

    @Override
    public void convert(@NonNull BaseViewHolder helper, NormalMultipleEntity data, int position) {
        RecyclerView recyclerView = helper.getView(R.id.recycler_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position % 7 < 3) {
                    return 4;
                } else {
                    return 1;
                }
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        if (recyclerView.getItemDecorationCount() == 0) {
            recyclerView.addItemDecoration(new OtherLookItemDecoration(35));
        }
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(mContext.getResources().getDrawable(R.drawable.public_line_horizontal));
        recyclerView.addItemDecoration(dividerItemDecoration);
        OtherLookAdapter otherLookAdapter = new OtherLookAdapter(mContext);
        recyclerView.setAdapter(otherLookAdapter);

        if (null != data.mOtherData) {
            otherLookAdapter.addAll(data.mOtherData);
            otherLookAdapter.notifyDataSetChanged();
        }
    }
}
