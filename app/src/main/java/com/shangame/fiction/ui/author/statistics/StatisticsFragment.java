package com.shangame.fiction.ui.author.statistics;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.lxj.xpopup.XPopup;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.core.utils.MyMarkerView;
import com.shangame.fiction.net.response.ReportFromResponse;
import com.shangame.fiction.net.response.TimeConfigResponse;
import com.shangame.fiction.ui.author.works.WorksListActivity;
import com.shangame.fiction.ui.popup.StatisticsPopupWindow;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class StatisticsFragment extends BaseFragment implements StatisticsContacts.View, OnChartValueSelectedListener, View.OnClickListener {
    private StatisticsPresenter mPresenter;
    private StatisticsTimeAdapter mAdapter;

    private List<TimeConfigResponse.TimeDataBean> mList = new ArrayList<>();

    private LineChart mLineChart;
    private TextView mTextWorks;
    private TextView mTextWeek;
    private TextView mTextMonth;
    private TextView mTextCount;
    private ImageView mImageType;
    private TextView mTvTitle1;
    private LinearLayout mLayoutTotal;
    private ImageView mImgBack;

    private TextView mTextType;

    private View view;

    private int mBookId = 0;
    private int mTimeType = 0;
    private int mDateType = 0;
    private String mBookName = "";

    private TimeConfigResponse.TimeDataBean mDataBean;

    private static final int WORKS_REQUEST_CODE = 301;

    private List<String> xListData = new ArrayList<>();
    private List<String> yListData = new ArrayList<>();

    public StatisticsFragment() {
        // Required empty public constructor
    }

    public static StatisticsFragment newInstance() {
        StatisticsFragment fragment = new StatisticsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_statistics, container, false);
        initView(contentView);
        initPresenter();
        initData();
        initListener();
        return contentView;
    }

    private void initListener() {
        mImgBack.setOnClickListener(this);
        mTextWorks.setOnClickListener(this);
        mTextWeek.setOnClickListener(this);
        mTextMonth.setOnClickListener(this);
        mTvTitle1.setOnClickListener(this);
        mLayoutTotal.setOnClickListener(this);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mDataBean = mList.get(position);
                mAdapter.notifyDataSetChanged();
                for (TimeConfigResponse.TimeDataBean bean : mList) {
                    bean.isClick = false;
                }
                mDataBean.isClick = !mDataBean.isClick;
                mPresenter.getReportFrom(mTimeType, mBookId, 0, mDateType, mDataBean.years, mDataBean.times);
            }
        });
    }

    private void initPresenter() {
        mPresenter = new StatisticsPresenter();
        mPresenter.attachView(this);
    }

    private void initData() {
        if (mBookId != 0) {
            mPresenter.getTimeConfig(mTimeType, mBookId);
        }
    }

    private void initView(View contentView) {
        mImgBack = contentView.findViewById(R.id.img_back);
        mLineChart = contentView.findViewById(R.id.line_chart);
        mTextWorks = contentView.findViewById(R.id.text_works);
        mTextWeek = contentView.findViewById(R.id.text_week);
        mTextMonth = contentView.findViewById(R.id.text_month);
        mTextCount = contentView.findViewById(R.id.text_count);
        mImageType = contentView.findViewById(R.id.image_type);
        mLineChart.setNoDataText("暂无数据");

        mTextType = contentView.findViewById(R.id.text_type);

        TextView tvTitle = contentView.findViewById(R.id.tv_title);
        mTvTitle1 = contentView.findViewById(R.id.tv_title_1);
        tvTitle.setVisibility(View.GONE);
        mTvTitle1.setVisibility(View.VISIBLE);
        mTvTitle1.setText("点击");

        mLayoutTotal = contentView.findViewById(R.id.layout_total);

        view = contentView.findViewById(R.id.include);

        RecyclerView recyclerTime = contentView.findViewById(R.id.recycler_time);

        recyclerTime.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL
                , false));
        mAdapter = new StatisticsTimeAdapter(R.layout.item_statistic_time, mList);
        recyclerTime.setAdapter(mAdapter);

        initChartView();
    }

    /**
     * 初始化折线图
     */
    private void initChartView() {
        //在点击高亮值时回调
        mLineChart.setOnChartValueSelectedListener(this);

        //设置整个图表的颜色
        mLineChart.setBackgroundResource(R.drawable.bg_line_chart);

        mLineChart.getDescription().setEnabled(false);

        mLineChart.getLegend().setEnabled(false);

        //是否可以缩放、移动、触摸
        mLineChart.setTouchEnabled(true);
        mLineChart.setDragEnabled(true);

        //不能让缩放，不然有bug，所以接口也没实现
        mLineChart.setScaleEnabled(false);
        mLineChart.setPinchZoom(true);

        //设置图表距离上下左右的距离
        mLineChart.setExtraOffsets(10, 10, 10, 0);

        //获取左侧侧坐标轴
        YAxis leftAxis = mLineChart.getAxisLeft();

        //设置是否显示Y轴的值
        leftAxis.setDrawLabels(false);

        //设置所有垂直Y轴的的网格线是否显示
        leftAxis.setDrawGridLines(true);

        leftAxis.setAxisLineColor(ContextCompat.getColor(mContext, R.color.transparent));

        //设置虚线
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        //设置Y极值，我这里没设置最大值，因为项目需要没有设置最大值
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisLineWidth(1);

        //将右边那条线隐藏
        mLineChart.getAxisRight().setEnabled(false);
        //获取X轴
        XAxis xAxis = mLineChart.getXAxis();
        //设置X轴的位置，可上可下
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //将垂直于X轴的网格线隐藏，将X轴显示
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        //设置X轴上label颜色和大小
        xAxis.setTextSize(8f);
        xAxis.setTextColor(Color.GRAY);

        //设置X轴高度
        xAxis.setAxisLineWidth(1);
    }

    @Override
    public void getTimeConfigSuccess(TimeConfigResponse response) {
        mList.clear();
        mList.addAll(response.timedata);
        mAdapter.setNewData(mList);
    }

    @Override
    public void getTimeConfigFailure(String msg) {
    }

    @Override
    public void getReportFromSuccess(ReportFromResponse response) {
        List<ReportFromResponse.TimeDataBean> timeData = response.timedata;
        xListData.clear();
        yListData.clear();
        for (ReportFromResponse.TimeDataBean dataBean : timeData) {
            xListData.add(dataBean.addtime);
            yListData.add(dataBean.number + "");
        }

        if (xListData.size() != 0 || yListData.size() != 0) {
            setChartData(xListData, yListData, 1);
            if (xListData.size() > 10) {
                mLineChart.animateX(1000);
            }
            mLineChart.invalidate();
        }
        mTextCount.setText(response.sumnumber + "");
    }

    @Override
    public void getReportFromEmpty() {
        xListData.clear();
        yListData.clear();
        mLineChart.clear();
    }

    @Override
    public void getReportFromFailure(String msg) {

    }

    /**
     * @param xData
     * @param yData
     * @param flag
     */
    private void setChartData(final List<String> xData, List<String> yData, int flag) {
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setLabelCount(xListData.size(), true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                //对X轴上的值进行Format格式化，转成相应的值
                int intValue = (int) value;
                //筛选出自己需要的值，一般都是这样写没问题，并且一定要加上这个判断，不然会出错
                if (xData.size() > intValue && intValue >= 0) {
                    //这样显示在X轴上值就是 05:30  05:35，不然会是1.0  2.0
                    return xData.get(intValue);
                } else {
                    return "";
                }
            }
        });
        mLineChart.invalidate();
        MyMarkerView mv = new MyMarkerView(mContext, R.layout.custom_marker_view, xData);
        mv.setChartView(mLineChart);
        mLineChart.setMarker(mv);

        final ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < yData.size(); i++) {
            //注意这里的Entry（不一定需要）采用这种方式构造，采用其他的结果是一样的
            values.add(new Entry(i, Float.valueOf(yData.get(i)), xData.get(i)));
        }

        LineDataSet lineDataset;
        if (mLineChart.getData() != null && mLineChart.getData().getDataSetCount() > 0) {
            lineDataset = (LineDataSet) mLineChart.getData().getDataSetByIndex(0);
            lineDataset.setValues(values);
            if (flag == 0) {
                lineDataset.setLabel("");
                lineDataset.setDrawFilled(true);
            } else {
                lineDataset.setLabel("");
                lineDataset.setDrawFilled(false);
            }
            mLineChart.getData().notifyDataChanged();
            mLineChart.notifyDataSetChanged();
        } else {
            if (flag == 0) {
                lineDataset = new LineDataSet(values, "");
                lineDataset.setDrawFilled(true);
            } else {
                lineDataset = new LineDataSet(values, "");
                lineDataset.setDrawFilled(false);
            }

            lineDataset.setColor(ContextCompat.getColor(mContext, R.color.statistics_line_color));
            lineDataset.setCircleColor(ContextCompat.getColor(mContext, R.color.statistics_circle_color));

            //设置是否显示圆点
            lineDataset.setDrawCircles(true);
            lineDataset.setDrawCircleHole(false);

            // lineDataset.setLineWidth(1.5f);
            lineDataset.setCircleRadius(4f);

            //是否显示每个点的Y值
            lineDataset.setDrawValues(false);

            LineData lineData = new LineData(lineDataset);
            mLineChart.setData(lineData);
            if (xData.size() > 10) {
                mLineChart.animateX(1000);
            }
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
    }

    @Override
    public void onNothingSelected() {
    }

    private void selectType() {
        StatisticsPopupWindow popupWindow = new StatisticsPopupWindow(mContext, mDateType);
        popupWindow.setOnStatisticsListener(new StatisticsPopupWindow.OnStatisticsListener() {
            @Override
            public void onClickCount() {
                mDateType = StatisticsType.TYPE_CLICK;
                mTvTitle1.setText("点击");
                mTextType.setText("总点击量");
                mImageType.setImageResource(R.drawable.icon_click_count);

                getReportFrom();
            }

            @Override
            public void onCollectCount() {
                mDateType = StatisticsType.TYPE_COLLECT;
                mTvTitle1.setText("收藏");
                mTextType.setText("总收藏量");
                mImageType.setImageResource(R.drawable.icon_collect_count);

                getReportFrom();
            }

            @Override
            public void onCommentCount() {
                mDateType = StatisticsType.TYPE_COMMENT;
                mTvTitle1.setText("评论");
                mTextType.setText("总评论量");
                mImageType.setImageResource(R.drawable.icon_comment_count);

                getReportFrom();
            }
        });
        new XPopup.Builder(getActivity())
                .atView(view)
                .moveUpToKeyboard(false)
                .asCustom(popupWindow)
                .show();
    }

    private void getReportFrom() {
        if (null != mDataBean) {
            mPresenter.getReportFrom(mTimeType, mBookId, 0, mDateType, mDataBean.years, mDataBean.times);
        }
    }

    private void monthClick() {
        if (mBookId != 0) {
            mTextWeek.setBackgroundResource(R.drawable.rect_white_left_btn);
            mTextMonth.setBackgroundResource(R.drawable.rect_blue_right_btn);

            mTextWeek.setTextColor(ContextCompat.getColor(mContext, R.color.statistics_title_click_color));
            mTextMonth.setTextColor(ContextCompat.getColor(mContext, R.color.white));

            mTimeType = 1;
            mPresenter.getTimeConfig(mTimeType, mBookId);

            mTextWeek.setEnabled(true);
            mTextMonth.setEnabled(false);

            xListData.clear();
            yListData.clear();
            if (null != mLineChart) {
                mLineChart.clear();
            }
        } else {
            Toast.makeText(mContext, "请先选择作品", Toast.LENGTH_SHORT).show();
        }
    }

    private void weekClick() {
        if (mBookId != 0) {
            mTextWeek.setBackgroundResource(R.drawable.rect_blue_left_btn);
            mTextMonth.setBackgroundResource(R.drawable.rect_white_right_btn);

            mTextWeek.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            mTextMonth.setTextColor(ContextCompat.getColor(mContext, R.color.statistics_title_click_color));

            mTimeType = 0;
            mPresenter.getTimeConfig(mTimeType, mBookId);

            mTextWeek.setEnabled(false);
            mTextMonth.setEnabled(true);

            xListData.clear();
            yListData.clear();
            if (null != mLineChart) {
                mLineChart.clear();
            }
        } else {
            Toast.makeText(mContext, "请先选择作品", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back: {
                if (null != getActivity()) {
                    getActivity().finish();
                }
            }
            break;
            case R.id.text_works: {
                Intent intent = new Intent(mContext, WorksListActivity.class);
                startActivityForResult(intent, WORKS_REQUEST_CODE);
            }
            break;
            case R.id.text_week: {
                weekClick();
            }
            break;
            case R.id.text_month: {
                monthClick();
            }
            break;
            case R.id.tv_title_1: {
                selectType();
            }
            break;
            case R.id.layout_total: {
                Intent intent = new Intent(mContext, StatisticsDetailActivity.class);
                intent.putExtra("bookId", mBookId);
                intent.putExtra("bookName", mBookName);
                intent.putExtra("timeType", mTimeType);
                intent.putExtra("dateType", mDateType);
                intent.putExtra("dataBean", mDataBean);
                startActivity(intent);
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == WORKS_REQUEST_CODE) {
            if (null != data) {
                mBookName = data.getStringExtra("bookName");
                mBookId = data.getIntExtra("bookId", 0);
                mTextWorks.setText(mBookName);

                mPresenter.getTimeConfig(mTimeType, mBookId);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
