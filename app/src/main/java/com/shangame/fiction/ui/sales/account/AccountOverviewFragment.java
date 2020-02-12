package com.shangame.fiction.ui.sales.account;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.AccountOverviewAdapter;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.net.response.AgentDetailResp;
import com.shangame.fiction.net.response.SumPriceListResp;
import com.shangame.fiction.net.response.WithdrawResp;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountOverviewFragment extends BaseFragment implements AccountOverviewContacts.View {
    private List<SumPriceListResp.DataBean.PageDataBean> mList = new ArrayList<>();
    private AccountOverviewAdapter mAdapter;
    private AccountOverviewPresenter mPresenter;
    private int mType;
    private int pageNum = 1;
    private boolean isEnd = false;
    private int pageSize = 20;
    private String mBeginTime;
    private String mEndTime;
    private TextView mTextTimeIncome;

    public AccountOverviewFragment() {
        // Required empty public constructor
    }

    public static AccountOverviewFragment newInstance(int type) {
        AccountOverviewFragment fragment = new AccountOverviewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_account_overview, container, false);
        if (null != getArguments()) {
            mType = getArguments().getInt("type");
        }
        initView(contentView);
        initPresenter();
        initData();
        return contentView;
    }

    private void initView(View contentView) {
        final RecyclerView recyclerAccountList = contentView.findViewById(R.id.recycler_account_list);
        recyclerAccountList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new AccountOverviewAdapter(R.layout.item_account_overview, mList);
        recyclerAccountList.setAdapter(mAdapter);

        mTextTimeIncome = contentView.findViewById(R.id.text_time_income);

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                recyclerAccountList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isEnd) {
                            //数据全部加载完毕
                            mAdapter.loadMoreEnd();
                        } else {
                            //成功获取更多数据
                            loadData(mType, pageNum, mBeginTime, mEndTime);
                        }
                    }
                }, 200);
            }
        }, recyclerAccountList);
    }

    private void initPresenter() {
        mPresenter = new AccountOverviewPresenter();
        mPresenter.attachView(this);

        Log.e("hhh", "111 mPresenter= " + mPresenter);
    }

    private void initData() {
        pageNum = 1;
        loadData(mType, pageNum, "", "");
    }

    public void loadData(int type, int pageIndex, String beginTime, String endTime) {
        Map<String, Object> map = new HashMap<>(7);
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        mType = type;
        pageNum = pageIndex;
        mBeginTime = beginTime;
        mEndTime = endTime;
        map.put("userid", userId);
        map.put("agentLeve", type);
        map.put("page", pageIndex);
        map.put("pagesize", pageSize);
        map.put("begintime", beginTime);
        map.put("endtime", endTime);

        Log.e("hhh", "111 mPresenter1= " + mPresenter);
        mPresenter.getSumPriceList(map);
    }

    @Override
    public void getSumPriceListSuccess(SumPriceListResp.DataBean dataBean) {
        Log.e("hhh", "getSumPriceListSuccess dataBean= " + dataBean);
        if (null != getActivity()) {
            ((AccountOverviewActivity) getActivity()).setIncomeValue(dataBean.sumPrice + "", dataBean.yesterPrice + "", dataBean.cashPrice + "");
        }

        mTextTimeIncome.setVisibility(View.VISIBLE);

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

        // String strTimeIncome = mBeginTime + " 至 " + mEndTime + " 总收益 " + dataBean.selPirce + "元";
        mTextTimeIncome.setText(stringBuilder);
        if (pageNum == 1) {
            mList.clear();
        }
        if (null == dataBean.pagedata) {
            return;
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
    public void getSumPriceListFailure(String msg) {
        Log.e("hhh", "getSumPriceListFailure msg= " + msg);
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getAgentIdDetailSuccess(AgentDetailResp.DataBean dataBean, int orderId) {

    }

    @Override
    public void getAgentIdDetailFailure(String msg) {

    }

    @Override
    public void withdrawSuccess(WithdrawResp.DataBean dataBean) {
    }

    @Override
    public void withdrawFailure(String msg) {
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
