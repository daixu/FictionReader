package com.shangame.fiction.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.WithdrawListResp;
import com.shangame.fiction.ui.sales.withdraw.TimeLineModel;

import java.util.ArrayList;
import java.util.List;

public class WithdrawAdapter extends BaseQuickAdapter<WithdrawListResp.DataBean.PageDataBean, BaseViewHolder> {
    public WithdrawAdapter(int layoutResId, @Nullable List<WithdrawListResp.DataBean.PageDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, WithdrawListResp.DataBean.PageDataBean item) {
        final LinearLayout layoutWithdraw = helper.getView(R.id.layout_withdraw);
        final TextView tvWithdraw = helper.getView(R.id.tv_withdraw);
        final TextView textWithdrawStatus = helper.getView(R.id.text_withdraw_status);
        final LinearLayout layoutExpand = helper.getView(R.id.layout_expand);
        final ImageView imageExpandStatus = helper.getView(R.id.image_expand_status);

        TextView textDateIncome = helper.getView(R.id.text_date_income);
        String strDateIncome = item.selTime + " 总提现 " + item.cashPrice;
        textDateIncome.setText(strDateIncome);

        helper.setText(R.id.text_withdraw_amount, item.cashPrice + "");
        helper.setText(R.id.text_withdraw_time, item.askforTime + "");

        StringBuilder method = new StringBuilder();
        if (!TextUtils.isEmpty(item.bankname)) {
            method.append(item.bankname);
        }
        if (!TextUtils.isEmpty(item.bankcard)) {
            method.append("( ");
            method.append(item.bankcard);
            method.append(" )");
        }

        helper.setText(R.id.text_withdrawal_method, method);
        helper.setText(R.id.text_withdrawal_number, item.ordeid + "");

        layoutWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutExpand.getVisibility() == View.VISIBLE) {
                    layoutExpand.setVisibility(View.GONE);
                    tvWithdraw.setTextColor(Color.parseColor("#FF383838"));
                    imageExpandStatus.setImageResource(R.drawable.icon_collapse_small);
                } else {
                    layoutExpand.setVisibility(View.VISIBLE);
                    tvWithdraw.setTextColor(Color.parseColor("#FF4A8DE6"));
                    imageExpandStatus.setImageResource(R.drawable.icon_expand);
                }
            }
        });

        List<TimeLineModel> list = new ArrayList<>();
        switch (item.state) {
            case 1:
                list.add(new TimeLineModel("提现", "", 1));
                list.add(new TimeLineModel("申请", "", 0));
                list.add(new TimeLineModel("到账", "", 0));
                textWithdrawStatus.setText("提现");
                break;
            case 2:
                list.add(new TimeLineModel("提现", "", 1));
                list.add(new TimeLineModel("申请", item.askforTime, 1));
                list.add(new TimeLineModel("到账", "", 0));
                textWithdrawStatus.setText("已申请");
                break;
            case 3:
                list.add(new TimeLineModel("提现", "", 1));
                list.add(new TimeLineModel("申请", item.askforTime, 1));
                list.add(new TimeLineModel("到账", item.accTime, 1));
                textWithdrawStatus.setText("已到账");
                break;
            default:
                break;
        }
        RecyclerView recyclerProgress = helper.getView(R.id.recycler_progress);
        WithdrawProgressAdapter adapter = new WithdrawProgressAdapter(mContext, list);
        recyclerProgress.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        recyclerProgress.setAdapter(adapter);

        helper.addOnClickListener(R.id.text_withdraw_status);
    }
}
