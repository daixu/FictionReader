package com.shangame.fiction.ui.my.pay;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BasePopupWindow;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.GetPayMenthodsResponse;
import com.shangame.fiction.net.response.GetRechargeConfigResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by Speedy on 2018/9/12
 */
public class PayPopupWindow extends BasePopupWindow {

    private TextView tvPayAmount;
    private TextView tvPay;
    private RecyclerView recyclerView;

    private GetPayMenthodsResponse.PayItem checkPayItem;

    private MyAdapter myAdapter;

    private OnPayClickListener onPayClickListener;

    private GetRechargeConfigResponse.RechargeBean rechargeBean;

    public PayPopupWindow(Activity activity) {
        super(activity);
        initView();
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        super.setBackgroundAlpha(0.8f);
        setAnimationStyle(R.style.popup_anim_style);
    }

    private void initView() {
        contentView = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.popup_window_pay, null);
        setContentView(contentView);
        tvPayAmount = contentView.findViewById(R.id.tvPayAmount);
        tvPay = contentView.findViewById(R.id.tvPay);
        recyclerView = contentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);

        tvPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPayClickListener != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("userid", UserInfoManager.getInstance(mActivity).getUserid());
                    map.put("channel", ApiConstant.Channel.ANDROID);
                    map.put("propid", rechargeBean.propid);
                    map.put("payprot", checkPayItem.payprot);
//                    map.put("createip","");
//                    onPayClickListener.onPay(map,chedkPayItem.payprot);
                    onPayClickListener.onPay2(checkPayItem.payurl, map, checkPayItem.payprot);
                }
            }
        });
    }

    public void setRechargeBean(GetRechargeConfigResponse.RechargeBean rechargeBean) {
        this.rechargeBean = rechargeBean;
        tvPayAmount.setText(mActivity.getString(R.string.pay_money_amount, rechargeBean.price + ""));
    }

    public void setPayItemList(List<GetPayMenthodsResponse.PayItem> payItemList) {
        checkPayItem = payItemList.get(0);
        checkPayItem.isChecked = true;

        myAdapter.addAll(payItemList);
        myAdapter.notifyDataSetChanged();
    }

    public void setOnPayClickListener(OnPayClickListener onPayClickListener) {
        this.onPayClickListener = onPayClickListener;
    }

    public interface OnPayClickListener {
        void onPay(Map<String, Object> map, int payMethod);

        void onPay2(String payUrl, Map<String, Object> map, int payMethod);
    }

    class MyHolder extends RecyclerView.ViewHolder {

        ImageView ivPayIcon;
        TextView tvPayName;
        ImageView cbCheck;

        public MyHolder(View itemView) {
            super(itemView);
            ivPayIcon = itemView.findViewById(R.id.ivPayIcon);
            tvPayName = itemView.findViewById(R.id.tvPayName);
            cbCheck = itemView.findViewById(R.id.cbCheck);
        }
    }

    class MyAdapter extends WrapRecyclerViewAdapter<GetPayMenthodsResponse.PayItem, MyHolder> {

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pay_method_item, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
            final GetPayMenthodsResponse.PayItem payItem = getItem(position);
            holder.tvPayName.setText(payItem.payname);
            if (payItem.isChecked) {
                holder.cbCheck.setImageResource(R.drawable.pay_checked);
            } else {
                holder.cbCheck.setImageResource(R.drawable.pay_uncheck);
            }

            switch (payItem.payprot) {
                case ApiConstant.PayMethod.WECHAT_SDK:
                case ApiConstant.PayMethod.BEI_WAP_WECHAT:
                    holder.ivPayIcon.setImageResource(R.drawable.wechat_pay);
                    break;
                case ApiConstant.PayMethod.BEI_WAP_ALIPAY:
                    holder.ivPayIcon.setImageResource(R.drawable.alipay);
                    break;
                    default:
                        break;
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!payItem.isChecked) {
                        for (int i = 0; i < getItemCount(); i++) {
                            getItem(i).isChecked = false;
                        }
                        payItem.isChecked = true;
                        checkPayItem = payItem;
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }
}
