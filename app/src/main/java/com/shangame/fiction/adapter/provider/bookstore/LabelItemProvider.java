package com.shangame.fiction.adapter.provider.bookstore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.FeaturedAdapter;
import com.shangame.fiction.entity.NormalMultipleEntity;
import com.shangame.fiction.ui.booklib.BookLibraryActivity;
import com.shangame.fiction.ui.bookstore.LabelAdapter;

public class LabelItemProvider extends BaseItemProvider<NormalMultipleEntity, BaseViewHolder> {
    @Override
    public int viewType() {
        return FeaturedAdapter.TYPE_LABEL;
    }

    @Override
    public int layout() {
        return R.layout.item_label_view;
    }

    @Override
    public void convert(@NonNull BaseViewHolder helper, NormalMultipleEntity data, int position) {
        RecyclerView recyclerView = helper.getView(R.id.recycler_view);

        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));

        LabelAdapter labelAdapter = new LabelAdapter(mContext);
        recyclerView.setAdapter(labelAdapter);

        labelAdapter.clear();
        if (null != data.mClassData) {
            labelAdapter.addAll(data.mClassData);
            labelAdapter.notifyDataSetChanged();
        }

        helper.getView(R.id.tv_label_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, BookLibraryActivity.class));
            }
        });
    }
}
