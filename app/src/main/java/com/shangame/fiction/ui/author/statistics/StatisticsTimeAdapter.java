package com.shangame.fiction.ui.author.statistics;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.TimeConfigResponse;

import java.util.List;

/**
 * Create by Speedy on 2019/7/30
 */
public class StatisticsTimeAdapter extends BaseQuickAdapter<TimeConfigResponse.TimeDataBean, BaseViewHolder> {
    public StatisticsTimeAdapter(int layoutResId, @Nullable List<TimeConfigResponse.TimeDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TimeConfigResponse.TimeDataBean item) {
        TextView textTime = helper.getView(R.id.text_time);
        textTime.setText(item.timetext);
        View viewLine = helper.getView(R.id.view_line);

        if (item.isClick) {
            textTime.setTextColor(ContextCompat.getColor(mContext, R.color.statistics_text_click_color));
            viewLine.setVisibility(View.VISIBLE);
        } else {
            textTime.setTextColor(ContextCompat.getColor(mContext, R.color.statistics_text_normal_color));
            viewLine.setVisibility(View.GONE);
        }
    }
}
