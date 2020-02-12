package com.shangame.fiction.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.AlbumChapterFigResponse;

import java.util.List;

public class ChapterOrderAdapter extends BaseQuickAdapter<AlbumChapterFigResponse.SubDataBean, BaseViewHolder> {
    public ChapterOrderAdapter(int layoutResId, @Nullable List<AlbumChapterFigResponse.SubDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AlbumChapterFigResponse.SubDataBean item) {
        TextView textName = helper.getView(R.id.text_name);
        TextView textDiscount = helper.getView(R.id.text_discount);
        LinearLayout layoutContent = helper.getView(R.id.layout_content);
        layoutContent.setSelected(item.isChecked);
        if (item.isChecked) {
            textName.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        } else {
            textName.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_black));
        }
        helper.setText(R.id.text_name, item.subtext);
        if (item.discount == 1 || item.discount == 0) {
            textDiscount.setVisibility(View.GONE);
        } else {
            textDiscount.setVisibility(View.VISIBLE);
            textDiscount.setText(mContext.getString(R.string.discount, String.valueOf(item.discount)));
        }
    }
}
