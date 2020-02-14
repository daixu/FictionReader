package com.shangame.fiction.adapter.provider;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.ChoiceListAdapter;
import com.shangame.fiction.adapter.LabelAdapter;
import com.shangame.fiction.net.response.NewsResp;
import com.shangame.fiction.widget.GlideApp;

import java.util.Arrays;
import java.util.List;

/**
 * @description: 大标题布局
 */
public class BigTitleNewsItemProvider extends BaseNewsItemProvider {

    public BigTitleNewsItemProvider() {
        super();
    }

    @Override
    public int viewType() {
        return ChoiceListAdapter.BIG_TITLE_NEWS;
    }

    @Override
    public int layout() {
        return R.layout.item_big_title_news;
    }

    @Override
    protected void setData(BaseViewHolder helper, NewsResp.DataBean.PageDataBean news) {
        ImageView imageView = helper.getView(R.id.image_img);
        GlideApp.with(mContext)
                .load(news.bookcover)
                .placeholder(R.drawable.default_cover)
                .centerCrop()
                .into(imageView);

        helper.setText(R.id.text_book_name, news.bookname);

        RecyclerView recyclerLabel = helper.getView(R.id.recycler_label);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(mContext);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setJustifyContent(JustifyContent.FLEX_END);
        recyclerLabel.setLayoutManager(layoutManager);

        String label = news.keyword;
        if (!TextUtils.isEmpty(label)) {
            List<String> labelList = Arrays.asList(label.split(","));
            LabelAdapter labelAdapter = new LabelAdapter(R.layout.item_label, labelList);
            recyclerLabel.setAdapter(labelAdapter);
        }
    }

}
