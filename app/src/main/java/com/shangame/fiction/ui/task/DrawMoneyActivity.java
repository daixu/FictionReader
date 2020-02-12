package com.shangame.fiction.ui.task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.utils.RedoUtil;
import com.shangame.fiction.net.response.BindWeChatResponse;
import com.shangame.fiction.net.response.CashConfigResponse;
import com.shangame.fiction.net.response.RedListResponse;
import com.shangame.fiction.net.response.WeChatCashResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.wxapi.WeChatConstants;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class DrawMoneyActivity extends BaseActivity implements RedPacketContacts.View, View.OnClickListener, BindWeChatContacts.View {

    private RedPacketPresenter redPacketPresenter;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView mRecyclerView;
    private MyAdapter myAdapter;
    private View dataLayout;
    private CashConfigResponse.CashItem currentCashItem;
    private TextView tvBindState;
    private boolean hasBindWeChat;
    private IWXAPI api;
    private BindWeChatPresenter bindWechatPresenter;
    private TextView tvMoney;
    private Button btnDraw;

    private double cashMoney;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BroadcastAction.WECHAT_BIND_ACTION.equals(action)) {
                String code = intent.getStringExtra("code");
                long userid = UserInfoManager.getInstance(mContext).getUserid();
                bindWechatPresenter.bindWeChat(userid, code, WeChatConstants.APP_ID);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_money);
        initView();
        initSmartRefreshLayout();
        initRecyclerView();
        initPresenter();
        initReceiver();
        smartRefreshLayout.autoRefresh();
    }

    private void initView() {
        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText("我的红包");

        dataLayout = findViewById(R.id.dataLayout);
        tvBindState = findViewById(R.id.tvBindState);
        findViewById(R.id.bindWechatLayout).setOnClickListener(this);
        btnDraw = findViewById(R.id.btnDraw);
        btnDraw.setOnClickListener(this);

        tvMoney = findViewById(R.id.tvMoney);
    }

    private void initSmartRefreshLayout() {
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadConfig();
            }
        });
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));

        myAdapter = new MyAdapter();
        mRecyclerView.setAdapter(myAdapter);
    }

    private void initPresenter() {
        redPacketPresenter = new RedPacketPresenter();
        redPacketPresenter.attachView(this);

        bindWechatPresenter = new BindWeChatPresenter();
        bindWechatPresenter.attachView(this);
    }

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.WECHAT_BIND_ACTION);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, intentFilter);
    }

    private void loadConfig() {
        long userid = UserInfoManager.getInstance(mContext).getUserid();
        redPacketPresenter.getCashConfig(userid);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        redPacketPresenter.detachView();
        bindWechatPresenter.detachView();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }

    @Override
    public void showError(Throwable throwable) {
        super.showError(throwable);
        btnDraw.setEnabled(true);
    }

    @Override
    public void getRedListSuccess(RedListResponse redListResponse) {

    }

    @Override
    public void getCashconfigSuccess(CashConfigResponse cashconfigResponse) {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.setEnableRefresh(false);

        cashMoney = cashconfigResponse.cashmoney;
        tvMoney.setText(cashconfigResponse.cashmoney + "");

        if (TextUtils.isEmpty(cashconfigResponse.nickname)) {
            tvBindState.setText("去绑定");
            hasBindWeChat = false;
        } else {
            tvBindState.setText("已绑定：" + cashconfigResponse.nickname);
            hasBindWeChat = true;
        }

        myAdapter.addAll(cashconfigResponse.data);
        currentCashItem = myAdapter.getItem(0);
        if (currentCashItem != null) {
            currentCashItem.isChecked = true;
        }
        myAdapter.notifyDataSetChanged();

        dataLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void weChatCashSuccess(WeChatCashResponse weChatCashResponse, String msg) {
        showToast(msg);
        cashMoney = weChatCashResponse.cashmoney;
        tvMoney.setText(cashMoney + "");
        btnDraw.setEnabled(true);
    }

    public void toWeChatLogin() {
        api = WXAPIFactory.createWXAPI(this, WeChatConstants.APP_ID, true);
        api.registerApp(WeChatConstants.APP_ID);

        if (!api.isWXAppInstalled()) {
            Toast.makeText(mActivity, R.string.not_wechat_app, Toast.LENGTH_SHORT).show();
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = WeChatConstants.State.BIND;
        api.sendReq(req);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivPublicBack) {
            finish();
        } else if (v.getId() == R.id.btnDraw) {
            if (!hasBindWeChat) {
                BindWeChatPopupWindow bindWechatPopupWindow = new BindWeChatPopupWindow(this);
                bindWechatPopupWindow.show();
            } else {
                if (cashMoney > currentCashItem.price) {
                    if (!RedoUtil.isQuickDoubleClick(System.currentTimeMillis())) {
                        btnDraw.setEnabled(false);
                        long userid = UserInfoManager.getInstance(mContext).getUserid();
                        redPacketPresenter.weChatCash(userid, currentCashItem.cashid, WeChatConstants.APP_ID);
                    }
                } else {
                    showToast("可提现余额不足");
                }
            }
        } else if (v.getId() == R.id.bindWechatLayout) {
            if (hasBindWeChat) {
                ChnageBindWechatPopupWindow chnageBindWechatPopupWindow = new ChnageBindWechatPopupWindow(this);
                chnageBindWechatPopupWindow.show();
            } else {
                BindWeChatPopupWindow bindWechatPopupWindow = new BindWeChatPopupWindow(this);
                bindWechatPopupWindow.show();
            }
        }
    }

    @Override
    public void bindWeChatSuccess(BindWeChatResponse inviteRecordResponse) {
        showToast("绑定微信成功");
        tvBindState.setText("已绑定");
        hasBindWeChat = true;
    }

    class MyAdapter extends WrapRecyclerViewAdapter<CashConfigResponse.CashItem, MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = getLayoutInflater().inflate(R.layout.cash_item, viewGroup, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            final CashConfigResponse.CashItem cashItem = getItem(i);
            myViewHolder.tvMoney.setText(cashItem.price + "元");
            if (cashItem.isChecked) {
                myViewHolder.itemView.setBackgroundResource(R.drawable.border_theme_color_bg);
            } else {
                myViewHolder.itemView.setBackgroundResource(R.drawable.corner_gray_bg);
            }
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearState();
                    currentCashItem = cashItem;
                    cashItem.isChecked = true;
                    notifyDataSetChanged();
                }
            });
        }

        public void clearState() {
            for (CashConfigResponse.CashItem cashItem : getData()) {
                cashItem.isChecked = false;
            }
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvMoney;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMoney = itemView.findViewById(R.id.tvMoney);
        }
    }
}
