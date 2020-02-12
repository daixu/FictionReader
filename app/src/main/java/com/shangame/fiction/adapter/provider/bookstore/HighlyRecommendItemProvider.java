package com.shangame.fiction.adapter.provider.bookstore;

import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
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
import com.shangame.fiction.ui.bookstore.BookWithContentAdapter;

/**
 * 主编力荐
 */
public class HighlyRecommendItemProvider extends BaseItemProvider<NormalMultipleEntity, BaseViewHolder> {
    @Override
    public int viewType() {
        return FeaturedAdapter.TYPE_HIGHLY_RECOMMEND;
    }

    @Override
    public int layout() {
        return R.layout.item_highly_recommend_view;
    }

    @Override
    public void convert(@NonNull BaseViewHolder helper, NormalMultipleEntity data, int position) {
        RecyclerView recyclerView = helper.getView(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(mContext.getResources().getDrawable(R.drawable.public_line_horizontal));
        recyclerView.addItemDecoration(dividerItemDecoration);
        BookWithContentAdapter editorRecommendAdapter = new BookWithContentAdapter(mContext);
        recyclerView.setAdapter(editorRecommendAdapter);

        editorRecommendAdapter.clear();
        if (null != data.mRecData) {
            editorRecommendAdapter.addAll(data.mRecData);
            editorRecommendAdapter.notifyDataSetChanged();
        }

        helper.getView(R.id.tv_highly_recommend_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookListByTypeActivity.lunchActivity(mContext, BookStoreType.EditorRecommendMore, "主编力荐", BookStoreChannel.CHOICENESS);
            }
        });
    }
}
