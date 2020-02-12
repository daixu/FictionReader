package com.shangame.fiction.ui.my.account.coin;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shangame.fiction.R;

public class CoinViewHolder extends RecyclerView.ViewHolder{
        public TextView tvCoinCount;
        public TextView tv1;
        public TextView tv2;
        public TextView tv3;
        public ImageView ivFlag;

        public CoinViewHolder(View itemView) {
            super(itemView);
            tvCoinCount = itemView.findViewById(R.id.tvCoinCount);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
            tv3 = itemView.findViewById(R.id.tv3);
            ivFlag = itemView.findViewById(R.id.ivFlag);
        }
    }