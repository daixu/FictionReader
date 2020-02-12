package com.shangame.fiction.ui.author.statistics;

import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.ReportFromResponse;

import java.util.List;

/**
 * Create by Speedy on 2019/7/30
 */
public class StatisticsDetailAdapter extends BaseQuickAdapter<ReportFromResponse.TimeDataBean, BaseViewHolder> {
    private int total;

    public StatisticsDetailAdapter(int layoutResId, @Nullable List<ReportFromResponse.TimeDataBean> data) {
        super(layoutResId, data);
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    protected void convert(BaseViewHolder helper, ReportFromResponse.TimeDataBean item) {
        TextView textTime = helper.getView(R.id.text_time);
        TextView textCount = helper.getView(R.id.text_count);

        ProgressBar progressBar = helper.getView(R.id.progress_count);

        textTime.setText(item.addtime);
        textCount.setText(item.number + "");
        if (total > 0) {
            int progress = (int)(((float) item.number / total) * 10000);
            progressBar.setProgress(progress);
        } else {
            progressBar.setProgress(0);
        }
    }
}
