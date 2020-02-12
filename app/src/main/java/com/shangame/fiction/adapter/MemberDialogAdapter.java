package com.shangame.fiction.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.storage.model.UpgradeBean;

import java.util.List;

public class MemberDialogAdapter extends BaseQuickAdapter<UpgradeBean, BaseViewHolder> {

    public MemberDialogAdapter(int layoutResId, @Nullable List<UpgradeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UpgradeBean item) {
        ImageView imageCheck = helper.getView(R.id.image_check);
        ImageView imageType = helper.getView(R.id.image_type);
        imageCheck.setSelected(item.isClick);
        imageType.setImageResource(item.resId);
        helper.setText(R.id.text_name, item.title);

        helper.addOnClickListener(R.id.image_check);
    }
}
