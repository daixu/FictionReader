package com.shangame.fiction.ui.sales.recharge;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.RechargeListAdapter;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.utils.DateUtils;
import com.shangame.fiction.net.response.RechargeListResp;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 充值明细
 */
@Route(path = "/ss/sales/recharge/details")
public class RechargeDetailsActivity extends BaseActivity implements View.OnClickListener, RechargeDetailsContacts.View {
    private List<RechargeListResp.DataBean.PageDataBean> mList = new ArrayList<>();
    private RechargeListAdapter mAdapter;
    private EditText mEditUserId;
    private RechargeDetailsPresenter mPresenter;
    private TextView mTextBeginTime;
    private TextView mTextEndTime;
    private TextView mTextTotalRecharge;
    private TextView mTextTimeIncome;

    private String mBeginTime;
    private String mEndTime;

    private int mBeginYear;
    private int mBeginMonth;
    private int mBeginDay;

    private int pageNum = 1;
    private boolean isEnd = false;
    private int pageSize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_details);
        initView();
        initPresenter();
        initData();
    }

    private void initView() {
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("充值明细");

        mTextBeginTime = findViewById(R.id.text_begin_time);
        mTextEndTime = findViewById(R.id.text_end_time);
        mEditUserId = findViewById(R.id.edit_user_id);

        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.btn_search).setOnClickListener(this);
        mTextBeginTime.setOnClickListener(this);
        mTextEndTime.setOnClickListener(this);

        final RecyclerView recyclerRechargeList = findViewById(R.id.recycler_recharge_list);
        recyclerRechargeList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new RechargeListAdapter(R.layout.item_recharge_list, mList);
        recyclerRechargeList.setAdapter(mAdapter);

        View headerView = LayoutInflater.from(this).inflate(R.layout.view_recharge_details_header, null);
        mTextTotalRecharge = headerView.findViewById(R.id.text_total_recharge);
        mTextTimeIncome = headerView.findViewById(R.id.text_time_income);
        mAdapter.setHeaderView(headerView);

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                recyclerRechargeList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isEnd) {
                            //数据全部加载完毕
                            mAdapter.loadMoreEnd();
                        } else {
                            //成功获取更多数据
                            loadData(pageNum, mBeginTime, mEndTime);
                        }
                    }
                }, 200);
            }
        }, recyclerRechargeList);
    }

    private void initPresenter() {
        mPresenter = new RechargeDetailsPresenter();
        mPresenter.attachView(this);
    }

    private void initData() {
        pageNum = 1;
        loadData(pageNum, "", "");
    }

    private void loadData(int pageIndex, String beginTime, String endTime) {
        Map<String, Object> map = new HashMap<>(6);
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        map.put("userid", userId);
        map.put("begintime", beginTime);
        map.put("endtime", endTime);
        map.put("page", pageIndex);
        map.put("pagesize", pageSize);
        String selUserId = mEditUserId.getText().toString().trim();
        if (TextUtils.isEmpty(selUserId)) {
            selUserId = "0";
        }
        map.put("seluserid", selUserId);
        mPresenter.getRechargeList(map);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_search:
                search();
                break;
            case R.id.text_begin_time:
                setBeginTime();
                break;
            case R.id.text_end_time:
                setEndTime();
                break;
            default:
                break;
        }
    }

    private void search() {
        if (TextUtils.isEmpty(mBeginTime)) {
            Toast.makeText(this, "请选择开始查询时间", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mEndTime)) {
            Toast.makeText(this, "请选择结束查询时间", Toast.LENGTH_SHORT).show();
            return;
        }
        pageNum = 1;
        loadData(pageNum, mBeginTime, mEndTime);
    }

    private void setBeginTime() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(RechargeDetailsActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Log.d("hhh", "onDateSet: year: " + year + ", month: " + month + ", dayOfMonth: " + dayOfMonth);

                        mBeginYear = year;
                        mBeginMonth = month;
                        mBeginDay = dayOfMonth;

                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        mTextBeginTime.setText(DateUtils.date2String(calendar.getTime(), "yyyy年MM月dd日"));
                        mBeginTime = DateUtils.date2String(calendar.getTime(), "yyyy-MM-dd");
                        Log.e("hhh", "startTime = " + mBeginTime);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void setEndTime() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(mBeginYear, mBeginMonth, mBeginDay);
        DatePickerDialog dialog = new DatePickerDialog(RechargeDetailsActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Log.d("hhh", "onDateSet: year: " + year + ", month: " + month + ", dayOfMonth: " + dayOfMonth);

                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        mTextEndTime.setText(DateUtils.date2String(calendar.getTime(), "yyyy年MM月dd日"));
                        mEndTime = DateUtils.date2String(calendar.getTime(), "yyyy-MM-dd");
                        Log.e("hhh", "endTime = " + mEndTime);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        DatePicker datePicker = dialog.getDatePicker();
        datePicker.setMinDate(calendar.getTimeInMillis());
        dialog.show();
    }

    @Override
    public void getRechargeListSuccess(RechargeListResp.DataBean dataBean) {
        Log.e("hhh", "getRechargeListSuccess dataBean= " + dataBean);

        String strTotalRecharge = dataBean.sumPrice + "";
        mTextTotalRecharge.setText(strTotalRecharge);

        StringBuilder stringBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(mBeginTime) && !TextUtils.isEmpty(mEndTime)) {
            stringBuilder.append(mBeginTime);
            stringBuilder.append(" 至 ");
            stringBuilder.append(mEndTime);
        }
        if (TextUtils.isEmpty(mBeginTime) && !TextUtils.isEmpty(mEndTime)) {
            stringBuilder.append("-");
            stringBuilder.append(" 至 ");
            stringBuilder.append(mEndTime);
        }
        if (!TextUtils.isEmpty(mBeginTime) && TextUtils.isEmpty(mEndTime)) {
            stringBuilder.append(mBeginTime);
            stringBuilder.append(" 至 ");
            stringBuilder.append("-");
        }
        stringBuilder.append(" 总收益 ");
        stringBuilder.append(dataBean.selPirce);
        stringBuilder.append("元");

        mTextTimeIncome.setVisibility(View.VISIBLE);
        mTextTimeIncome.setText(stringBuilder);

        if (null == dataBean.pagedata || dataBean.pagedata.size() == 0) {
            return;
        }
        if (pageNum == 1) {
            mList.clear();
        }
        mList.addAll(dataBean.pagedata);
        if (pageSize > dataBean.pagedata.size()) {
            isEnd = true;
        } else {
            isEnd = false;
            pageNum += 1;
        }
        mAdapter.setNewData(mList);
        mAdapter.loadMoreComplete();
    }

    @Override
    public void getRechargeListFailure(String msg) {
        Log.e("hhh", "getRechargeListFailure msg= " + msg);
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }
}
