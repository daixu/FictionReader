package com.shangame.fiction.ui.reader;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BasePopupWindow;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.net.response.ChapterOrderConfigResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.my.pay.PayCenterActivity;

/**
 * Create by Speedy on 2018/8/15
 */
public class ChapterOrderPopupWindow extends BasePopupWindow implements View.OnClickListener, ChapterOrderContracts.View {

    private TextView tvChapterName;
    private TextView tvAccountBalanceCoin;
    private TextView tvNeedCostCoin;
    private TextView tvOriginalCostCoin;
    private Button btnImmediatelyBuy;

    private MyAdapter myAdapter;

    private long bookId;
    private long chapterId;
    private long userId;

    private long currentMoney;//用户剩余闪闪币
    private long coin;//用户剩余赠币

    private OnOrderPayListener onOrderPayListener;

    private ChapterOrderPresenter chapterOrderPresenter;

    private boolean hasPaySuccess;
    private boolean autoPayNextChapter = true;

    public ChapterOrderPopupWindow(Activity activity, long bookId, long chapterId) {
        super(activity);
        initParam(bookId, chapterId);
        initView();
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setFocusable(true);
        setAnimationStyle(R.style.popup_anim_style);
        setBackgroundAlpha(1f);
        chapterOrderPresenter = new ChapterOrderPresenter();
        chapterOrderPresenter.attachView(this);
        chapterOrderPresenter.getChapterOrderConfig(userId, bookId, chapterId);
    }

    private void initParam(long bookId, long chapterId) {
        this.bookId = bookId;
        this.chapterId = chapterId;
        userId = UserInfoManager.getInstance(mActivity).getUserid();
    }

    private void initView() {
        contentView = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.popup_window_chapter_order, null);
        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        setContentView(contentView);

        tvChapterName = findViewById(R.id.tvChapterName);
        tvAccountBalanceCoin = findViewById(R.id.tvAccountBalanceCoin);
        tvNeedCostCoin = findViewById(R.id.tvNeedCostCoin);
        tvOriginalCostCoin = findViewById(R.id.tvOriginalCostCoin);
        btnImmediatelyBuy = findViewById(R.id.btnImmediatelyBuy);

        tvOriginalCostCoin.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        btnImmediatelyBuy.setOnClickListener(this);

        CheckBox checkAutoBuy = findViewById(R.id.check_auto_buy);
        checkAutoBuy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                autoPayNextChapter = isChecked;
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(mActivity.getResources().getDrawable(R.drawable.divider_empty));
        recyclerView.addItemDecoration(dividerItemDecoration);

        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
    }

    public void setCurrentMoney(long money, long coin) {
        this.currentMoney = money;
        this.coin = coin;
        tvAccountBalanceCoin.setText(mActivity.getString(R.string.surplus_coin, String.valueOf(money)) + "    " + coin + "赠币");

        if (currentMoney >= myAdapter.getCheckedItem().disprce) {
            btnImmediatelyBuy.setText(R.string.immediately_buy);
        } else {
            btnImmediatelyBuy.setText(R.string.top_up_and_buy);
        }
    }

    @Override
    public void getChapterOrderConfigSuccess(ChapterOrderConfigResponse chapterOrderConfigResponse) {
        currentMoney = chapterOrderConfigResponse.readmoney;
        coin = chapterOrderConfigResponse.coin;

        tvChapterName.setText(chapterOrderConfigResponse.title);
        tvAccountBalanceCoin.setText(mActivity.getString(R.string.account_balance_coin, String.valueOf(chapterOrderConfigResponse.readmoney)) + "    " + coin + "赠币");

        myAdapter.addAll(chapterOrderConfigResponse.subdata);
        if (myAdapter.getItemCount() > 0) {
            //默认选择第一项
            ChapterOrderConfigResponse.SubdataBean subDataBean = myAdapter.getItem(0);
            if (null != subDataBean) {
                subDataBean.isChecked = true;
                String needCostCoin = mActivity.getString(R.string.need_cost_coin, String.valueOf(subDataBean.disprce));
                SpannableStringBuilder builder = new SpannableStringBuilder(needCostCoin);
                builder.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.colorPrimary)), 3, needCostCoin.length() - 3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                tvNeedCostCoin.setText(builder);
                tvOriginalCostCoin.setText(mActivity.getString(R.string.original_cost_coin, String.valueOf(subDataBean.subprice)));
                myAdapter.notifyDataSetChanged();
            }
        } else {
            showToast("本书暂无收费章节");
            dismiss();
        }
    }

    @Override
    public void bugChapterOrderSuccess() {
        if (onOrderPayListener != null) {
            hasPaySuccess = true;
            onOrderPayListener.onPaySuccess();
            dismiss();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        chapterOrderPresenter.detachView();
        if (!hasPaySuccess) {
            if (onOrderPayListener != null) {
                onOrderPayListener.onCancelPay();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnImmediatelyBuy:
                ChapterOrderConfigResponse.SubdataBean subDataBean = myAdapter.getCheckedItem();
                if (subDataBean != null) {
                    if (currentMoney + coin >= subDataBean.disprce) {
                        ChapterOrderConfigResponse.SubdataBean ss = myAdapter.getCheckedItem();
                        chapterOrderPresenter.bugChapterOrder(userId, bookId, chapterId, ss.subnumber, autoPayNextChapter);
                    } else {
                        Intent intent = new Intent(mActivity, PayCenterActivity.class);
                        mActivity.startActivityForResult(intent, ReadActivity.TOP_UP_FOR_BUY_CHAPTER_REQUEST_CODE);
                    }
                }
                break;
            default:
                break;
        }
    }

    public void setOnOrderPayListener(OnOrderPayListener onOrderPayListener) {
        this.onOrderPayListener = onOrderPayListener;
    }

    public interface OnOrderPayListener {
        void onPaySuccess();

        void onCancelPay();
    }

    class MyAdapter extends WrapRecyclerViewAdapter<ChapterOrderConfigResponse.SubdataBean, ChapterOrderViewHolder> {

        @NonNull
        @Override
        public ChapterOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_config_item, parent, false);
            ChapterOrderViewHolder myViewHolder = new ChapterOrderViewHolder(itemView);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ChapterOrderViewHolder holder, int position) {
            final ChapterOrderConfigResponse.SubdataBean subDataBean = getItem(position);
            if (null != subDataBean) {
                holder.tvOrderTitle.setText(subDataBean.subtext);
                if (subDataBean.discount == 1) {
                    holder.tvOrderDiscount.setVisibility(View.GONE);

                } else {
                    holder.tvOrderDiscount.setVisibility(View.VISIBLE);
                    holder.tvOrderDiscount.setText(mActivity.getString(R.string.discount, String.valueOf(subDataBean.discount)));
                }

                holder.itemView.setSelected(subDataBean.isChecked);
                if (subDataBean.isChecked) {
                    holder.tvOrderTitle.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
                } else {
                    holder.tvOrderTitle.setTextColor(mActivity.getResources().getColor(R.color.text_normal_color));
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChapterOrderConfigResponse.SubdataBean ss;
                        for (int i = 0; i < getItemCount(); i++) {
                            ss = getItem(i);
                            if (null != ss) {
                                ss.isChecked = false;
                            }
                        }
                        subDataBean.isChecked = true;
                        notifyDataSetChanged();

                        String needCostCoin = mActivity.getString(R.string.need_cost_coin, String.valueOf(subDataBean.disprce));
                        SpannableStringBuilder builder = new SpannableStringBuilder(needCostCoin);
                        builder.setSpan(new ForegroundColorSpan(mActivity.getResources().getColor(R.color.colorPrimary)), 3, needCostCoin.length() - 3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                        tvNeedCostCoin.setText(builder);
                        tvOriginalCostCoin.setText(mActivity.getString(R.string.original_cost_coin, String.valueOf(subDataBean.subprice)));

                        if (currentMoney >= subDataBean.disprce) {
                            btnImmediatelyBuy.setText(R.string.immediately_buy);
                        } else {
                            btnImmediatelyBuy.setText(R.string.top_up_and_buy);
                        }
                    }
                });
            }
        }

        public ChapterOrderConfigResponse.SubdataBean getCheckedItem() {
            ChapterOrderConfigResponse.SubdataBean ss = null;
            for (int i = 0; i < getItemCount(); i++) {
                ss = getItem(i);
                if (null != ss && ss.isChecked) {
                    return ss;
                }
            }
            return null;
        }
    }
}
