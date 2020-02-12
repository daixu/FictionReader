
package com.shangame.fiction.core.utils;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.shangame.fiction.R;

import java.util.List;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
public class MyMarkerView extends MarkerView {

    private TextView tvContent, tv_marker_time;
    private List<String> mXList;

    public MyMarkerView(Context context, int layoutResource, List<String> xData) {
        super(context, layoutResource);
        tvContent = findViewById(R.id.tvContent);
        tv_marker_time = findViewById(R.id.tv_marker_time);
        mXList = xData;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;
            tvContent.setText("数量: ".concat(Utils.formatNumber(ce.getHigh(), 0, true)));
        } else {
            //这里
            tvContent.setText("数量: ".concat(Utils.formatNumber(e.getY(), 0, true)));

            String time = mXList.get((int) e.getX());
            tv_marker_time.setText("时间: ".concat(time).concat("号"));
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
