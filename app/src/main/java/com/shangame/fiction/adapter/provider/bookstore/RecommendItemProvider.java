package com.shangame.fiction.adapter.provider.bookstore;

import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
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
import com.shangame.fiction.ui.bookstore.HighlyRecommendAdapter;
import com.shangame.fiction.widget.RecommendSpaceItemDecoration;

/**
 * 重磅推荐
 */
public class RecommendItemProvider extends BaseItemProvider<NormalMultipleEntity, BaseViewHolder> {
    @Override
    public int viewType() {
        return FeaturedAdapter.TYPE_RECOMMEND;
    }

    @Override
    public int layout() {
        return R.layout.item_recommend_view;
    }

    @Override
    public void convert(@NonNull BaseViewHolder helper, NormalMultipleEntity data, int position) {
        RecyclerView recyclerView = helper.getView(R.id.recycler_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 4;
                } else {
                    return 1;
                }
            }
        });

        recyclerView.setLayoutManager(gridLayoutManager);
        if (recyclerView.getItemDecorationCount() == 0) {
            recyclerView.addItemDecoration(new RecommendSpaceItemDecoration(35));
        }
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(mContext.getResources().getDrawable(R.drawable.public_line_horizontal));
        recyclerView.addItemDecoration(dividerItemDecoration);
        HighlyRecommendAdapter highlyRecommendAdapter = new HighlyRecommendAdapter(mContext);
        recyclerView.setAdapter(highlyRecommendAdapter);

        highlyRecommendAdapter.clear();
        if (null != data.mHeavyData) {
            highlyRecommendAdapter.addAll(data.mHeavyData);
            highlyRecommendAdapter.notifyDataSetChanged();
        }

        helper.getView(R.id.tv_recommend_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookListByTypeActivity.lunchActivity(mContext, BookStoreType.HighlyRecommend, mContext.getString(R.string.highly_recommend), BookStoreChannel.CHOICENESS);
            }
        });
    }
}
