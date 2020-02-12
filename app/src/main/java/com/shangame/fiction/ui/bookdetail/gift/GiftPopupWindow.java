package com.shangame.fiction.ui.bookdetail.gift;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
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
import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.net.response.GetGiftListConfigResponse;

import java.util.List;

/**
 * 打赏礼物
 * Create by Speedy on 2018/7/31
 */
public class GiftPopupWindow extends BasePopupWindow implements View.OnClickListener {

    private LinearLayout contentView;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;

    private TextView tvTotalCoin;
    private TextView tvCurrentMoney;
    private TextView tvPayTour;

    private double currentMoney;//剩余闪闪币


    private GetGiftListConfigResponse.GiftBean currentGift;

    private boolean needTopUpFirst;//标记是否需要先充值

    private OnPayTourListener onPayTourListener;


    public GiftPopupWindow(Activity activity) {
        super(activity);
        initView();
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setAnimationStyle(R.style.popup_anim_style);
        setBackgroundAlpha(1f);
    }

    private void initView() {
        contentView = (LinearLayout) LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.popup_window_gift, null);
        setContentView(contentView);

        recyclerView = contentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mActivity.getApplicationContext(), 3));
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);

        tvTotalCoin = contentView.findViewById(R.id.tvTotalCoin);
        tvCurrentMoney = contentView.findViewById(R.id.tvCurrentMoney);
        tvPayTour = contentView.findViewById(R.id.tvPayTour);

        tvPayTour.setOnClickListener(this);
    }

    public void setGiftList(List<GetGiftListConfigResponse.GiftBean> list) {
        myAdapter.addAll(list);
        myAdapter.notifyDataSetChanged();

        currentGift = myAdapter.getItem(0);
        currentGift.isChecked = true;
        tvTotalCoin.setText(mActivity.getString(R.string.total_coin, String.valueOf(currentGift.price)));
    }

    public void setCurrentMoney(double money) {
        this.currentMoney = money;
        tvCurrentMoney.setText(mActivity.getString(R.string.surplus_coin, String.valueOf(money)));

        if (currentGift != null) {
            if (currentMoney < currentGift.price) {
                needTopUpFirst = true;
                tvPayTour.setText(R.string.top_up_and_pay_tour);
            } else {
                needTopUpFirst = false;
                tvPayTour.setText(R.string.pay_tour);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvPayTour) {
            if (onPayTourListener != null) {
                if (needTopUpFirst) {
                    onPayTourListener.showTopUpActivity();
                } else {
                    dismiss();
                    onPayTourListener.onPayTour(currentGift);
                }
            }
        }
    }

    public void setOnPayTourListener(OnPayTourListener onPayTourListener) {
        this.onPayTourListener = onPayTourListener;
    }

    public interface OnPayTourListener {
        void onPayTour(GetGiftListConfigResponse.GiftBean giftBean);

        void showTopUpActivity();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        ImageView giftIcon;
        TextView giftName;
        TextView giftCoin;
        View borderLayout;

        public MyHolder(View itemView) {
            super(itemView);
            giftIcon = itemView.findViewById(R.id.giftIcon);
            giftName = itemView.findViewById(R.id.giftName);
            giftCoin = itemView.findViewById(R.id.giftCoin);
            borderLayout = itemView.findViewById(R.id.borderLayout);
        }
    }

    class MyAdapter extends WrapRecyclerViewAdapter<GetGiftListConfigResponse.GiftBean, MyHolder> {

        @NonNull
        @Override
        public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gift_item, null);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyHolder holder, int position) {
            final GetGiftListConfigResponse.GiftBean giftItem = getItem(position);
            ImageLoader.with(mActivity).loadPicture(holder.giftIcon, giftItem.propimage, 80, 80);

            holder.giftName.setText(giftItem.propname);
            holder.giftCoin.setText(giftItem.price + "闪闪币");

            if (giftItem.isChecked) {
                holder.borderLayout.setSelected(true);
            } else {
                holder.borderLayout.setSelected(false);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentGift = giftItem;
                    if (currentMoney < giftItem.price) {
                        needTopUpFirst = true;
                        tvPayTour.setText(R.string.top_up_and_pay_tour);
                    } else {
                        needTopUpFirst = false;
                        tvPayTour.setText(R.string.pay_tour);
                    }
                    final int count = getItemCount();
                    for (int i = 0; i < count; i++) {
                        GetGiftListConfigResponse.GiftBean gi = getItem(i);
                        if (null != gi) {
                            gi.isChecked = false;
                        }
                    }
                    giftItem.isChecked = true;
                    notifyDataSetChanged();
                    tvTotalCoin.setText(mActivity.getString(R.string.total_coin, String.valueOf(giftItem.price)));
                }
            });
        }
    }

}
