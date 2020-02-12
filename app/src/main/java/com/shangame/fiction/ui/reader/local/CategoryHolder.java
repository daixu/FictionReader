package com.shangame.fiction.ui.reader.local;

import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.ui.reader.local.page.TxtChapter;

public class CategoryHolder extends ViewHolderImpl<TxtChapter> {

    private TextView mTvChapter;

    @Override
    public void initView() {
        mTvChapter = findById(R.id.category_tv_chapter);
    }

    @Override
    public void onBind(TxtChapter value, int pos) {
        mTvChapter.setSelected(false);
        mTvChapter.setTextColor(ContextCompat.getColor(getContext(), R.color.text_default));
        mTvChapter.setText(value.getTitle());
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_category;
    }

    public void setSelectedChapter() {
        mTvChapter.setTextColor(ContextCompat.getColor(getContext(), R.color.light_red));
        mTvChapter.setSelected(true);
    }
}