package com.shangame.fiction.ui.author.statistics;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.response.ReportFromResponse;
import com.shangame.fiction.net.response.TimeConfigResponse;
import com.shangame.fiction.ui.author.works.WorksListActivity;

import java.util.ArrayList;
import java.util.List;

public class StatisticsDetailActivity extends BaseActivity implements View.OnClickListener, StatisticsContacts.View {
    private ImageView mImgBack;
    private TextView mTextWorks;
    private TextView mTextWeek;
    private TextView mTextMonth;

    private static final int WORKS_REQUEST_CODE = 101;

    private int mBookId;
    private String mBookName;
    private int mTimeType = 0;
    private int mDateType = 0;
    private int mYears;
    private int mTimes;
    private TimeConfigResponse.TimeDataBean mDataBean;
    private List<TimeConfigResponse.TimeDataBean> mDataList = new ArrayList<>();

    private StatisticsPresenter mPresenter;

    private List<ReportFromResponse.TimeDataBean> mList = new ArrayList<>();
    private StatisticsDetailAdapter mAdapter;
    private StatisticsTimeAdapter mTimeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_detail);

        mBookId = getIntent().getIntExtra("bookId", 0);
        mBookName = getIntent().getStringExtra("bookName");
        mTimeType = getIntent().getIntExtra("timeType", 0);
        mDateType = getIntent().getIntExtra("dateType", 0);
        mDataBean = getIntent().getParcelableExtra("dataBean");

        if (null != mDataBean) {
            mDataBean.isClick = true;
            mDataList.clear();
            mDataList.add(mDataBean);
            mYears = mDataBean.years;
            mTimes = mDataBean.times;
        }

        initView();
        initListener();
        initPresenter();
        initData();
    }

    private void initData() {
        if (mBookId != 0) {
            mPresenter.getReportFrom(mTimeType, mBookId, 1, mDateType, mYears, mTimes);
        }
    }

    private void initPresenter() {
        mPresenter = new StatisticsPresenter();
        mPresenter.attachView(this);
    }

    private void initListener() {
        mImgBack.setOnClickListener(this);
        mTextWorks.setOnClickListener(this);
        mTextWeek.setOnClickListener(this);
        mTextMonth.setOnClickListener(this);

        mTimeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mDataBean = mDataList.get(position);
                mTimeAdapter.notifyDataSetChanged();
                for (TimeConfigResponse.TimeDataBean bean : mDataList) {
                    bean.isClick = false;
                }
                mDataBean.isClick = !mDataBean.isClick;
                mPresenter.getReportFrom(mTimeType, mBookId, 1, mDateType, mDataBean.years, mDataBean.times);
            }
        });
    }

    private void initView() {
        TextView tvTitle = findViewById(R.id.tv_title);
        TextView textType = findViewById(R.id.text_type);

        mImgBack = findViewById(R.id.img_back);
        mTextWorks = findViewById(R.id.text_works);
        mTextWeek = findViewById(R.id.text_week);
        mTextMonth = findViewById(R.id.text_month);

        RecyclerView recyclerDetail = findViewById(R.id.recycler_detail);
        recyclerDetail.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL
                , false));
        mAdapter = new StatisticsDetailAdapter(R.layout.item_statistic_detail, mList);
        recyclerDetail.setAdapter(mAdapter);

        RecyclerView recyclerTime = findViewById(R.id.recycler_time);
        recyclerTime.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL
                , false));
        mTimeAdapter = new StatisticsTimeAdapter(R.layout.item_statistic_time, mDataList);
        recyclerTime.setAdapter(mTimeAdapter);

        if (!TextUtils.isEmpty(mBookName)) {
            mTextWorks.setText(mBookName);
        }

        if (mTimeType == 0) {
            mTextWeek.setBackgroundResource(R.drawable.rect_blue_left_btn);
            mTextMonth.setBackgroundResource(R.drawable.rect_white_right_btn);

            mTextWeek.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            mTextMonth.setTextColor(ContextCompat.getColor(mContext, R.color.statistics_title_click_color));

            mTextWeek.setEnabled(false);
            mTextMonth.setEnabled(true);
        } else {
            mTextWeek.setBackgroundResource(R.drawable.rect_white_left_btn);
            mTextMonth.setBackgroundResource(R.drawable.rect_blue_right_btn);

            mTextWeek.setTextColor(ContextCompat.getColor(mContext, R.color.statistics_title_click_color));
            mTextMonth.setTextColor(ContextCompat.getColor(mContext, R.color.white));

            mTextWeek.setEnabled(true);
            mTextMonth.setEnabled(false);
        }
        switch (mDateType) {
            case 0:
                textType.setText("点击量明细");
                tvTitle.setText("点击");
                break;
            case 1:
                textType.setText("收藏量明细");
                tvTitle.setText("收藏");
                break;
            case 2:
                textType.setText("评论量明细");
                tvTitle.setText("评论");
                break;
            default:
                break;
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
        } else {
            Toast.makeText(mContext, "请先选择作品", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getTimeConfigSuccess(TimeConfigResponse response) {
        mDataList.clear();
        mDataList.addAll(response.timedata);
        mTimeAdapter.setNewData(mDataList);
    }

    @Override
    public void getTimeConfigFailure(String msg) {
        Log.e("hhh", "getTimeConfigFailure");
    }

    @Override
    public void getReportFromSuccess(ReportFromResponse response) {
        mList.clear();
        mList.addAll(response.timedata);
        mAdapter.setTotal(response.sumnumber);
        mAdapter.setNewData(mList);
    }

    @Override
    public void getReportFromEmpty() {
        Log.e("hhh", "getReportFromEmpty");
    }

    @Override
    public void getReportFromFailure(String msg) {
        Log.e("hhh", "getReportFromFailure");
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back: {
                finish();
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
            case R.id.text_works: {
                Intent intent = new Intent(mContext, WorksListActivity.class);
                startActivityForResult(intent, WORKS_REQUEST_CODE);
            }
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
