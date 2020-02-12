package com.shangame.fiction.ui.my.vip;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.net.response.VipInfoResponse;

/**
 * Create by Speedy on 2018/8/23
 */
public class VipAdapter extends WrapRecyclerViewAdapter<VipInfoResponse.VipPrivilegeBean, VipViewHolder> {

    public static final int GRID_LAYOUT = 0;
    public static final int LINEAR_LAYOUT = 1;

    private int viewType = GRID_LAYOUT;

    public VipAdapter(int viewType) {
        this.viewType = viewType;
    }


    @NonNull
    @Override
    public VipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == LINEAR_LAYOUT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vip_linear_item, parent, false);
            return new VipViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vip_grid_item, parent, false);
            return new VipViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @Override
    public void onBindViewHolder(@NonNull VipViewHolder holder, int position) {
        VipInfoResponse.VipPrivilegeBean vipPrivilegeBean = getItem(position);
        if (viewType == GRID_LAYOUT) {
            holder.tvVipName.setText(vipPrivilegeBean.privilegename);
            if (vipPrivilegeBean.state == 0) {
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);//饱和度 0灰色 100过度彩色，50正常
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                holder.ivVipIcon.setColorFilter(filter);

                holder.tvVipName.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.secondary_text));
            }else{
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(50);//饱和度 0灰色 100过度彩色，50正常
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                holder.ivVipIcon.setColorFilter(filter);
                holder.tvVipName.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.primary_text));
            }

        } else {
            holder.tvVipName.setText(vipPrivilegeBean.privilegename);
            holder.tvVipIntro.setText(vipPrivilegeBean.describe);
        }

        switch (vipPrivilegeBean.privilegeid) {
            case 1:
                holder.ivVipIcon.setImageResource(R.drawable.vip1);
                break;
            case 2:
                holder.ivVipIcon.setImageResource(R.drawable.vip2);
                break;
            case 3:
                holder.ivVipIcon.setImageResource(R.drawable.vip3);
                break;
            case 4:
                holder.ivVipIcon.setImageResource(R.drawable.vip4);
                break;
            case 5:
                holder.ivVipIcon.setImageResource(R.drawable.vip5);
                break;
            case 6:
                holder.ivVipIcon.setImageResource(R.drawable.vip6);
                break;
            case 7:
                holder.ivVipIcon.setImageResource(R.drawable.vip7);
                break;
            case 8:
                holder.ivVipIcon.setImageResource(R.drawable.vip8);
                break;
        }
    }
}
