package com.shangame.fiction.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.ui.listen.timing.TimingBean;

import java.util.List;

public class TimingAdapter extends BaseQuickAdapter<TimingBean, BaseViewHolder> {
    private TimingBean mBean;

    public TimingAdapter(int layoutResId, @Nullable List<TimingBean> data) {
        super(layoutResId, data);
    }

    public void setTimingBean(TimingBean bean) {
        this.mBean = bean;
    }

    @Override
    protected void convert(BaseViewHolder helper, TimingBean item) {
        TextView textTitle = helper.getView(R.id.text_title);
        textTitle.setText(item.time);
        if (null != mBean && mBean.value == item.value) {
            textTitle.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_dis_price));
        } else {
            textTitle.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_black));
        }
    }
}
