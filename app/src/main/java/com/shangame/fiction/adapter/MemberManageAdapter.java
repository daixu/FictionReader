package com.shangame.fiction.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.MemberListResp;

import java.util.List;

public class MemberManageAdapter extends BaseQuickAdapter<MemberListResp.DataBean.PageDataBean, BaseViewHolder> {
    private int mAgentGrade;
    public MemberManageAdapter(int layoutResId, @Nullable List<MemberListResp.DataBean.PageDataBean> data, int agentGrade) {
        super(layoutResId, data);
        this.mAgentGrade = agentGrade;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MemberListResp.DataBean.PageDataBean item) {
        TextView tvSetting = helper.getView(R.id.tv_setting);
        helper.setText(R.id.text_user_id, item.userid + "");
        helper.setText(R.id.text_name, item.nickname);
        helper.setText(R.id.text_money, item.money + "");
        helper.setText(R.id.text_consume_money, item.expend + "");
        helper.setText(R.id.text_recharge_amount, item.ordermoney + "");
        helper.setText(R.id.text_register_time, item.registerDate);
        if (mAgentGrade == 3) {
            tvSetting.setVisibility(View.GONE);
        } else {
            tvSetting.setVisibility(View.VISIBLE);
        }
        helper.addOnClickListener(R.id.tv_setting);
    }
}
