package com.shangame.fiction.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.AlbumSelectionResponse;

import java.util.List;

public class DirectorySelectionAdapter extends BaseQuickAdapter<AlbumSelectionResponse.SelectModeBean, BaseViewHolder> {
    private String remark = "";
    public DirectorySelectionAdapter(int layoutResId, @Nullable List<AlbumSelectionResponse.SelectModeBean> data) {
        super(layoutResId, data);
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    protected void convert(BaseViewHolder helper, AlbumSelectionResponse.SelectModeBean item) {
        TextView textName = helper.getView(R.id.text_name);
        if (remark.equals(item.remark)) {
            textName.setBackgroundResource(R.drawable.border_blue_rounded);
        } else {
            textName.setBackgroundResource(R.drawable.border_gray_rounded);
        }
        helper.setText(R.id.text_name, item.remark);
    }
}
