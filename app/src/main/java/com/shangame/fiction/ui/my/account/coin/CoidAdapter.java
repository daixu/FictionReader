package com.shangame.fiction.ui.my.account.coin;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.core.utils.DateUtils;
import com.shangame.fiction.core.utils.TimeUtils;
import com.shangame.fiction.net.response.CoinSummaryResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CoidAdapter extends WrapRecyclerViewAdapter<CoinSummaryResponse.CoinItem, CoinViewHolder> {


    private int state;

    public CoidAdapter(int state) {
        this.state = state;
    }

    @NonNull
    @Override
    public CoinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        if(state == CoinState.VALID){
            view = layoutInflater.inflate(R.layout.coid_item, parent, false);
        }else{
            view = layoutInflater.inflate(R.layout.coid_item_due, parent, false);
        }
        return new CoinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinViewHolder holder, int position) {

        CoinSummaryResponse.CoinItem coinItem = getItem(position);
        holder.tvCoinCount.setText(coinItem.counts + "");
        holder.tv1.setText(coinItem.remark);
        holder.tv2.setText("有效日期至：" + DateUtils.getStandardFormatTime(new Date(coinItem.deadline*1000L)));
        holder.tv3.setText("共" + coinItem.counts + "赠币");

        if(state != CoinState.VALID){
            if(coinItem.state == CoinState.USED){
                holder.ivFlag.setImageResource(R.drawable.used);
            }else{
                holder.ivFlag.setImageResource(R.drawable.due);
            }
        }


    }

}