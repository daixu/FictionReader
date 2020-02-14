package com.shangame.fiction.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;

import java.util.List;

public class LabelAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public LabelAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, String item) {
        helper.setText(R.id.text_label, item);
    }
}
