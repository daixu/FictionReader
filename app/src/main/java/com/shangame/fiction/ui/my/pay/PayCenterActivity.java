package com.shangame.fiction.ui.my.pay;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.base.WrapBaseAdapter;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.net.response.CreatWapOrderResponse;
import com.shangame.fiction.net.response.GetPayMenthodsResponse;
import com.shangame.fiction.net.response.GetRechargeConfigResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;

import java.util.Map;

/**
 * 充值中心 Activity
 * Create by Speedy on 2018/7/23
 */
public class PayCenterActivity extends BaseActivity implements View.OnClickListener, PayContracts.View {

    private TextView tvCurrentCoin;
    private MyAdapter myAdapter;
    private PayPresenter payPresenter;
    private GetRechargeConfigResponse.RechargeBean currentRechargeBean;
    private PayPopupWindow payPopupWindow;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showToast(getString(R.string.pay_success));
            long readMoney = intent.getLongExtra("readmoney", 0);
            tvCurrentCoin.setText(String.valueOf(readMoney));
            updateUserInfo(readMoney);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_center);
        initView();
        initPresenter();
        initReceiver();
        loadData();
    }

    private void initView() {
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.top_up_center);

        findViewById(R.id.ivPublicBack).setOnClickListener(this);

        tvCurrentCoin = findViewById(R.id.tvCurrentCoin);

        GridView gridView = findViewById(R.id.gridView);
        myAdapter = new MyAdapter(mActivity);
        gridView.setAdapter(myAdapter);
    }

    private void initPresenter() {
        payPresenter = new PayPresenter(mContext);
        payPresenter.attachView(this);
    }

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter(BroadcastAction.PUSH_PAY_SUCCESS_ACTION);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, intentFilter);
    }

    private void loadData() {
        UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
        payPresenter.getRechargeConfig(userInfo.userid);
        payPresenter.getUserInfo(userInfo.userid);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        payPresenter.detachView();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }

    private void updateUserInfo(final long readMoney) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
                userInfo.money = readMoney;
                UserInfoManager.getInstance(mContext).saveUserInfo(userInfo);
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivPublicBack:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void getPayMethodsSuccess(final GetPayMenthodsResponse getPayMenthodsResponse) {
        payPopupWindow = new PayPopupWindow(mActivity);
        payPopupWindow.setPayItemList(getPayMenthodsResponse.paydata);
        payPopupWindow.setRechargeBean(currentRechargeBean);
        payPopupWindow.setOnPayClickListener(new PayPopupWindow.OnPayClickListener() {
            @Override
            public void onPay(Map<String, Object> map, int payMethod) {
                if (payPopupWindow != null && payPopupWindow.isShowing()) {
                    payPopupWindow.dismiss();
                }
                payPresenter.createWapOrder(map, payMethod);
            }

            @Override
            public void onPay2(String payUrl, Map<String, Object> map, int payMethod) {
                if (payPopupWindow != null && payPopupWindow.isShowing()) {
                    payPopupWindow.dismiss();
                }
                payPresenter.createWapOrder2(payUrl, map, payMethod);
            }
        });
        payPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void getRechargeConfigSuccess(GetRechargeConfigResponse getRechargeConfigResponse) {
        myAdapter.addAll(getRechargeConfigResponse.rechdata);
        currentRechargeBean = myAdapter.getItem(0);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void wapCreateOrderSuccess(CreatWapOrderResponse creatWapOrderResponse, int payMethod) {
        payPresenter.redirectRequest(creatWapOrderResponse.skipurl, payMethod);
    }

    @Override
    public void getUserInfoSuccess(UserInfo userInfo) {
        long money = userInfo.money + userInfo.coin;
        tvCurrentCoin.setText(getString(R.string.current_balance, String.valueOf(money)));
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvMoney;
        TextView tvCoin;
        TextView tvReward;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvMoney = itemView.findViewById(R.id.tvMoney);
            tvCoin = itemView.findViewById(R.id.tvCoin);
            tvReward = itemView.findViewById(R.id.tvReward);
        }
    }

    class MyAdapter extends WrapBaseAdapter<GetRechargeConfigResponse.RechargeBean, MyViewHolder> {

        public MyAdapter(Activity activity) {
            super(activity);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.recharge_item, parent, false);
            MyViewHolder holder = new MyViewHolder(view);

            final GetRechargeConfigResponse.RechargeBean rechargeBean = getItem(position);
            holder.tvMoney.setText(getString(R.string.yuan, rechargeBean.price + ""));
            holder.tvCoin.setText(rechargeBean.remark);

            if (rechargeBean.givenumber > 0) {
                holder.tvReward.setText("+ " + rechargeBean.givenumber + "赠币");
                holder.tvReward.setVisibility(View.VISIBLE);
            } else if (rechargeBean.coinnumber > 0) {
                holder.tvReward.setText("+ " + rechargeBean.coinnumber + "赠币");
                holder.tvReward.setVisibility(View.VISIBLE);
            } else {
                holder.tvReward.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentRechargeBean = rechargeBean;
                    payPresenter.getPayMethods();
                }
            });

            return view;
        }
    }
}
