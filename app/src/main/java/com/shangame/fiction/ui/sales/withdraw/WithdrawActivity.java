package com.shangame.fiction.ui.sales.withdraw;

import android.app.Dialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lxj.xpopup.XPopup;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.WithdrawAdapter;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.utils.DateUtils;
import com.shangame.fiction.net.response.AgentDetailResp;
import com.shangame.fiction.net.response.BaseResp;
import com.shangame.fiction.net.response.WithdrawListResp;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.widget.wheel.dialog.DateTimeWheelDialog;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 可提现金额
 */
@Route(path = "/ss/sales/withdraw")
public class WithdrawActivity extends BaseActivity implements View.OnClickListener, WithdrawContacts.View {
    private List<WithdrawListResp.DataBean.PageDataBean> mList = new ArrayList<>();
    private WithdrawAdapter mAdapter;
    private TextView mTextDate;
    //    private TextView mTextSelectDate;
    private TextView mTextWithdrawAmount;
    private TextView mTextCanWithdraw;

    private String mQueryMonth;

    private int pageNum = 1;
    private boolean isEnd = false;
    private int pageSize = 20;

    private WithdrawPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        initView();
        initPresenter();
        initData();
    }

    private void initView() {
        TextView tvTitle = findViewById(R.id.tv_title);
        TextView textOption = findViewById(R.id.text_option);

        tvTitle.setText("可提现金额");
        textOption.setVisibility(View.VISIBLE);
        textOption.setText("规则");

        mTextDate = findViewById(R.id.text_date);

        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.text_option).setOnClickListener(this);
        mTextDate.setOnClickListener(this);
        final RecyclerView recyclerWithdraw = findViewById(R.id.recycler_withdraw);
        mAdapter = new WithdrawAdapter(R.layout.item_withdraw, mList);
        recyclerWithdraw.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        recyclerWithdraw.setAdapter(mAdapter);

        View headerView = LayoutInflater.from(this).inflate(R.layout.view_withdraw_header, null);
        headerView.findViewById(R.id.tv_withdraw_option).setOnClickListener(this);

        TextView textDate = headerView.findViewById(R.id.text_current_date);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        String strMonth;
        if (month < 10) {
            strMonth = "0" + month;
        } else {
            strMonth = "" + month;
        }

        String strDate = strMonth + "/" + year;
        SpannableString spannableString = new SpannableString(strDate);
        spannableString.setSpan(new AbsoluteSizeSpan(18, true), 0, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textDate.setText(spannableString);

        // mTextSelectDate = headerView.findViewById(R.id.text_current_date);
        mTextWithdrawAmount = headerView.findViewById(R.id.text_withdraw_amount);
        mTextCanWithdraw = headerView.findViewById(R.id.text_can_withdraw);
        mAdapter.setHeaderView(headerView);

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                recyclerWithdraw.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isEnd) {
                            //数据全部加载完毕
                            mAdapter.loadMoreEnd();
                        } else {
                            //成功获取更多数据
                            loadData(pageNum, mQueryMonth);
                        }
                    }
                }, 200);
            }
        }, recyclerWithdraw);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.text_withdraw_status) {
                    WithdrawListResp.DataBean.PageDataBean dataBean = mList.get(position);
                    if (dataBean.state == 1) {
                        UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
                        int agentId = 0;
                        if (null != userInfo) {
                            agentId = userInfo.agentId;
                        }
                        mPresenter.getAgentIdDetail(agentId, dataBean.ordeid);
                    }
                }
            }
        });
    }

    private void initPresenter() {
        mPresenter = new WithdrawPresenter();
        mPresenter.attachView(this);
    }

    private void initData() {
        pageNum = 1;
        loadData(pageNum, "");
    }

    private void loadData(int pageIndex, String queryMonth) {
        UserInfo userInfo = UserInfoManager.getInstance(this).getUserInfo();
        int agentId = 0;
        if (null != userInfo) {
            agentId = userInfo.agentId;
        }
        Map<String, Object> map = new HashMap<>(6);
        map.put("agentId", agentId);
        map.put("page", pageIndex);
        map.put("pagesize", pageSize);
        map.put("begintime", queryMonth);
        mPresenter.getWithdrawList(map);
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
            case R.id.text_option:
                Log.e("hhh", "规则");
                showWithdrawRuleDialog();
                break;
            case R.id.tv_withdraw_option:
                Log.e("hhh", "提现");
                UserInfo userInfo = UserInfoManager.getInstance(this).getUserInfo();
                int agentId = 0;
                if (null != userInfo) {
                    agentId = userInfo.agentId;
                }
                mPresenter.getAgentIdDetail(agentId, 0);
                break;
            case R.id.text_date:
                DateTimeWheelDialog dialog = createDialog();
                dialog.show();
                break;
            default:
                break;
        }
    }

    private void showWithdrawRuleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_withdraw_rule, null);
        ImageView btnCancel = view.findViewById(R.id.image_close);

        final Dialog dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        if (null != window) {
            window.setBackgroundDrawable(new BitmapDrawable());
            window.setContentView(view);
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }

    private DateTimeWheelDialog createDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2019);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        Date startDate = calendar.getTime();
        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2030);
        calendar.set(Calendar.MONTH, 11);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date endDate = calendar.getTime();

        DateTimeWheelDialog dialog = new DateTimeWheelDialog(this);
        dialog.setItemVerticalSpace(80);
        dialog.show();
        dialog.setTitle("选择时间");
        int config = DateTimeWheelDialog.SHOW_YEAR_MONTH;
        dialog.configShowUI(config);
        dialog.setCancelButton("取消", null);
        dialog.setOKButton("确定", new DateTimeWheelDialog.OnClickCallBack() {
            @Override
            public boolean callBack(View v, @NonNull Date selectedDate) {
                mTextDate.setText(DateUtils.date2String(selectedDate, "yyyy年MM月"));
                mQueryMonth = DateUtils.date2String(selectedDate, "yyyy-MM");

                loadData(1, mQueryMonth);
                return false;
            }
        });
        dialog.setDateArea(startDate, endDate, true);
        dialog.updateSelectedDate(new Date(System.currentTimeMillis()));
        return dialog;
    }

    @Override
    public void getWithdrawListSuccess(WithdrawListResp.DataBean dataBean) {
        Log.e("hhh", "getMemberListSuccess dataBean= " + dataBean);

        String strWithdrawAmount = dataBean.ytxPrice + "";
        mTextWithdrawAmount.setText(strWithdrawAmount);

        String strCanWithdraw = dataBean.ktxPrice + "";
        mTextCanWithdraw.setText(strCanWithdraw);

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
    public void getWithdrawListFailure(String msg) {
        Log.e("hhh", "getWithdrawListFailure msg= " + msg);
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void withdrawSuccess(BaseResp resp) {
        Toast.makeText(this, "提现申请成功", Toast.LENGTH_SHORT).show();
        initData();
    }

    @Override
    public void withdrawFailure(String msg) {
        Log.e("hhh", "withdrawFailure msg= " + msg);
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getAgentIdDetailSuccess(AgentDetailResp.DataBean dataBean, int orderId) {
        Log.e("hhh", "getAgentIdDetailSuccess dataBean= " + dataBean);
        UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
        int agentId = 0;
        if (null != userInfo) {
            agentId = userInfo.agentId;
        }
        mPresenter.withdraw(agentId, orderId);
    }

    @Override
    public void getAgentIdDetailFailure(String msg) {
        Log.e("hhh", "getAgentIdDetailFailure msg= " + msg);
        new XPopup.Builder(this)
                .asCustom(new FinanceDataWindow(this, null))
                .show();
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }
}
