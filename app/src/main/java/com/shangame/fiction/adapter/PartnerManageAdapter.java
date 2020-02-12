package com.shangame.fiction.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.PartnerListResp;
import com.shangame.fiction.storage.manager.UserInfoManager;

import java.util.List;

public class PartnerManageAdapter extends BaseQuickAdapter<PartnerListResp.DataBean.PageDataBean, BaseViewHolder> {
    private int mType;

    public PartnerManageAdapter(int layoutResId, @Nullable List<PartnerListResp.DataBean.PageDataBean> data, int type) {
        super(layoutResId, data);
        this.mType = type;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PartnerListResp.DataBean.PageDataBean item) {
        TextView tvSetting = helper.getView(R.id.tv_setting);
        helper.setText(R.id.text_invitation_code, item.userid + "");
        helper.setText(R.id.text_name, item.agentName);
        helper.setText(R.id.text_sup_invitation_code, item.parentId + "");
        helper.setText(R.id.text_member_total, item.userCount + "");
        helper.setText(R.id.text_authorization_time, item.agentDate);
        if (mType == 1) {
            tvSetting.setVisibility(View.GONE);
        } else {
            int agentGrade = UserInfoManager.getInstance(mContext).getUserInfo().agentGrade;
            if (agentGrade == 1) {
                tvSetting.setVisibility(View.VISIBLE);
            } else {
                tvSetting.setVisibility(View.GONE);
            }
        }
        helper.addOnClickListener(R.id.tv_setting);
    }
}
