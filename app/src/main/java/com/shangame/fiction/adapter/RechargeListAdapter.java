package com.shangame.fiction.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.RechargeListResp;

import java.util.List;

public class RechargeListAdapter extends BaseQuickAdapter<RechargeListResp.DataBean.PageDataBean, BaseViewHolder> {
    public RechargeListAdapter(int layoutResId, @Nullable List<RechargeListResp.DataBean.PageDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, RechargeListResp.DataBean.PageDataBean item) {
        helper.setText(R.id.text_title, item.nickname);
        helper.setText(R.id.text_user_id, item.userid + "");
        helper.setText(R.id.text_money, item.orderMoney + "");
        helper.setText(R.id.text_time, item.creatorTime);
    }
}
