package com.shangame.fiction.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.SumPriceListResp;

import java.util.List;

public class AccountOverviewAdapter extends BaseQuickAdapter<SumPriceListResp.DataBean.PageDataBean, BaseViewHolder> {
    public AccountOverviewAdapter(int layoutResId, @Nullable List<SumPriceListResp.DataBean.PageDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, SumPriceListResp.DataBean.PageDataBean item) {
        helper.setText(R.id.text_title, item.nickname);
        helper.setText(R.id.text_user_id, item.userid + "");
        helper.setText(R.id.text_money, item.userMoney + "");
    }
}
