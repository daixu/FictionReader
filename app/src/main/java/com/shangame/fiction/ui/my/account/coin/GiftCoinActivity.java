package com.shangame.fiction.ui.my.account.coin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.CoinSummaryResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.web.WebViewActivity;

public class GiftCoinActivity extends BaseActivity implements View.OnClickListener, GiftCoinContracts.View {

    private TextView tvCurrentCoin;
    private RecyclerView validRecyclerView;
    private CoidAdapter validCoidAdapter;

    private RecyclerView loseRecyclerView;
    private CoidAdapter loseCoidAdapter;

    private GiftCoinPresenter giftCoinPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_coin);
        initView();
        initPresenter();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        giftCoinPresenter.detachView();
    }

    private void initView() {
        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.my_coin);

        tvCurrentCoin = findViewById(R.id.tvCurrentCoin);
        findViewById(R.id.validLayout).setOnClickListener(this);
        findViewById(R.id.loseLayout).setOnClickListener(this);
        findViewById(R.id.tvDetail).setOnClickListener(this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.public_line_horizontal));

        validRecyclerView = findViewById(R.id.validRecyclerView);
        validRecyclerView.addItemDecoration(dividerItemDecoration);
        validRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        validCoidAdapter = new CoidAdapter(CoinState.VALID);
        validRecyclerView.setAdapter(validCoidAdapter);

        loseRecyclerView = findViewById(R.id.loseRecyclerView);
        loseRecyclerView.addItemDecoration(dividerItemDecoration);
        loseRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        loseCoidAdapter = new CoidAdapter(CoinState.EXPIRE);
        loseRecyclerView.setAdapter(loseCoidAdapter);
    }

    private void initPresenter() {
        giftCoinPresenter = new GiftCoinPresenter();
        giftCoinPresenter.attachView(this);
    }

    private void loadData() {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        giftCoinPresenter.getCoinSummary(userId);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.validLayout) {
            Intent intent = new Intent(mContext, CoinDetailListActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.loseLayout) {
            Intent intent = new Intent(mContext, CoinLoseActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.tvDetail) {
            String url = "https://m.anmaa.com//Mine/Zbgz?channel=" + ApiConstant.Channel.ANDROID;
            WebViewActivity.lunchActivity(mActivity, "赠币使用说明", url);
        } else if (v.getId() == R.id.ivPublicBack) {
            finish();
        }
    }

    @Override
    public void getCoinSummarySuccess(CoinSummaryResponse coinSummaryResponse) {
        tvCurrentCoin.setText(String.valueOf(coinSummaryResponse.coin));

        validCoidAdapter.addAll(coinSummaryResponse.validdata);
        validCoidAdapter.notifyDataSetChanged();

        loseCoidAdapter.addAll(coinSummaryResponse.losedata);
        loseCoidAdapter.notifyDataSetChanged();
    }
}
