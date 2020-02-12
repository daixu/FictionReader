package com.shangame.fiction.ui.listen.order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lxj.xpopup.core.BottomPopupView;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.ChapterOrderAdapter;
import com.shangame.fiction.net.response.AlbumChapterFigResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.login.LoginActivity;
import com.shangame.fiction.ui.my.pay.PayCenterActivity;
import com.shangame.fiction.widget.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class ChapterOrderPopWindow extends BottomPopupView implements ChapterOrderContracts.View, View.OnClickListener {

    public static final int LUNCH_LOGIN_ACTIVITY_REQUEST_CODE = 435;
    private static final int TOP_UP_FOR_BUY_CHAPTER_REQUEST_CODE_1 = 507;
    private static final int TOP_UP_FOR_BUY_CHAPTER_REQUEST_CODE = 508;
    private long albumId;
    private long chapterId;
    //用户剩余闪闪币
    private double currentMoney;
    //用户剩余赠币
    private long coin;
    private Context mContext;
    private Activity mActivity;
    private TextView mTextChapterName;
    private TextView mTextAccountBalance;
    private TextView mTextTotal;
    private TextView mTextPrice;
    private Button mBtnBuy;
    private Button mBtnBuy1;
    private CheckBox mCheckAutoBuy;
    private RelativeLayout mLayoutAutoPay;
    private RelativeLayout mLayoutPrice;
    private View mViewLinePrice;
    private ChapterOrderAdapter mAdapter;
    private List<AlbumChapterFigResponse.SubDataBean> mList = new ArrayList<>();
    private ChapterOrderPresenter mPresenter;
    private AlbumChapterFigResponse.SubDataBean mBean;
    private boolean isAutoPay = false;
    private OnOrderPayListener onOrderPayListener;
    private boolean hasPaySuccess;

    public ChapterOrderPopWindow(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public ChapterOrderPopWindow(@NonNull Context context, Activity activity, long albumId, long chapterId) {
        super(context);
        this.mActivity = activity;
        this.mContext = context;
        this.albumId = albumId;
        this.chapterId = chapterId;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_window_chapter_order_1;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        initView();
    }

    private void initView() {
        mTextChapterName = findViewById(R.id.text_chapter_name);
        mTextAccountBalance = findViewById(R.id.text_account_balance);
        mTextTotal = findViewById(R.id.text_total);
        mTextPrice = findViewById(R.id.text_price);
        mLayoutAutoPay = findViewById(R.id.layout_auto_pay);
        mLayoutPrice = findViewById(R.id.layout_price);
        mViewLinePrice = findViewById(R.id.view_line_price);

        mBtnBuy = findViewById(R.id.btn_buy);
        mBtnBuy1 = findViewById(R.id.btn_buy_1);
        mBtnBuy.setOnClickListener(this);
        mBtnBuy1.setOnClickListener(this);

        mCheckAutoBuy = findViewById(R.id.check_auto_buy);
        mCheckAutoBuy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isAutoPay = isChecked;
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        recyclerView.addItemDecoration(new SpaceItemDecoration(20));
        mAdapter = new ChapterOrderAdapter(R.layout.item_chapter_order, mList);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mBean = mList.get(position);
                for (int i = 0; i < mList.size(); i++) {
                    AlbumChapterFigResponse.SubDataBean subDataBean = mList.get(i);
                    subDataBean.isChecked = false;
                }
                mBean.isChecked = true;
                mAdapter.notifyDataSetChanged();

                double price;
                if (mBean.disprce > 0) {
                    price = mBean.disprce;
                } else {
                    price = mBean.subprice;
                }

                String totalCoin = mContext.getString(R.string.total_coin, String.valueOf(price));
                SpannableStringBuilder builder = new SpannableStringBuilder(totalCoin);
                builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.colorPrimary)),
                        3, totalCoin.length() - 3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                mTextTotal.setText(builder);
                mTextPrice.setText(price + "闪闪币");

                if (currentMoney >= price) {
                    mBtnBuy.setText(R.string.immediately_buy);
                    mBtnBuy1.setText(R.string.immediately_buy);
                } else {
                    mBtnBuy.setText(R.string.top_up_and_buy);
                    mBtnBuy1.setText(R.string.top_up_and_buy);
                }
            }
        });
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        mPresenter.detachView();

        if (!hasPaySuccess) {
            if (onOrderPayListener != null) {
                onOrderPayListener.onCancelPay();
            }
        }
    }

    @Override
    protected void onShow() {
        super.onShow();
        initPresenter();
        loadData();
    }

    private void initPresenter() {
        mPresenter = new ChapterOrderPresenter();
        mPresenter.attachView(this);
    }

    private void loadData() {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        mPresenter.getChapterOrderConfig(userId, albumId, chapterId);
    }

    public void setCurrentMoney(long money, long coin) {
        this.currentMoney = money;
        this.coin = coin;
        mTextAccountBalance.setText(currentMoney + "闪闪币");

        double price;
        if (mBean.disprce > 0) {
            price = mBean.disprce;
        } else {
            price = mBean.subprice;
        }

        if (currentMoney >= price) {
            mBtnBuy.setText(R.string.immediately_buy);
            mBtnBuy1.setText(R.string.immediately_buy);
        } else {
            mBtnBuy.setText(R.string.top_up_and_buy);
            mBtnBuy1.setText(R.string.top_up_and_buy);
        }
    }

    @Override
    public void getChapterOrderConfigSuccess(AlbumChapterFigResponse response) {
        if (null != response) {
            mList.clear();
            mList.addAll(response.subdata);
            mAdapter.setNewData(mList);

            currentMoney = response.readmoney;

            mTextChapterName.setText(response.title);
            mTextAccountBalance.setText(currentMoney + "闪闪币");

            if (response.IsVIP == 1) {
                mLayoutAutoPay.setVisibility(View.GONE);
                mTextTotal.setVisibility(View.GONE);
                mBtnBuy.setVisibility(View.GONE);
                mBtnBuy1.setVisibility(View.VISIBLE);
                mLayoutPrice.setVisibility(View.VISIBLE);
                mViewLinePrice.setVisibility(View.VISIBLE);
            } else {
                mLayoutAutoPay.setVisibility(View.VISIBLE);
                mTextTotal.setVisibility(View.VISIBLE);
                mBtnBuy.setVisibility(View.VISIBLE);
                mBtnBuy1.setVisibility(View.GONE);
                mLayoutPrice.setVisibility(View.GONE);
                mViewLinePrice.setVisibility(View.GONE);
                if (response.autorenew == 1) {
                    mCheckAutoBuy.setChecked(true);
                } else {
                    mCheckAutoBuy.setChecked(false);
                }
            }

            if (mList.size() > 0) {
                mBean = mList.get(0);
                mBean.isChecked = true;

                double price;
                if (mBean.disprce > 0) {
                    price = mBean.disprce;
                } else {
                    price = mBean.subprice;
                }

                String totalCoin = mContext.getString(R.string.total_coin, String.valueOf(price));
                SpannableStringBuilder builder = new SpannableStringBuilder(totalCoin);
                builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.colorPrimary)),
                        3, totalCoin.length() - 3, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                mTextTotal.setText(builder);
                mTextPrice.setText(price + "闪闪币");

                if (currentMoney >= price) {
                    mBtnBuy.setText(R.string.immediately_buy);
                    mBtnBuy1.setText(R.string.immediately_buy);
                } else {
                    mBtnBuy.setText(R.string.top_up_and_buy);
                    mBtnBuy1.setText(R.string.top_up_and_buy);
                }
            } else {
                Toast.makeText(mContext, "本书暂无收费章节", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        }
    }

    @Override
    public void setAlbumSubChapterSuccess() {
        if (onOrderPayListener != null) {
            hasPaySuccess = true;
            onOrderPayListener.onPaySuccess();
            dismiss();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showNotNetworkView() {

    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showError(Throwable throwable) {

    }

    @Override
    public void lunchLoginActivity() {
        Intent mIntent = new Intent(mContext, LoginActivity.class);
        mActivity.startActivityForResult(mIntent, LUNCH_LOGIN_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_buy:
            case R.id.btn_buy_1: {
                buy();
            }
            break;
            default:
                break;
        }
    }

    private void buy() {
        AlbumChapterFigResponse.SubDataBean subDataBean = mBean;
        if (mBean != null) {
            double price;
            if (mBean.disprce > 0) {
                price = mBean.disprce;
            } else {
                price = mBean.subprice;
            }
            if (currentMoney + coin >= price) {
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                mPresenter.setAlbumSubChapter(userId, albumId, chapterId, subDataBean.subnumber, isAutoPay);
            } else {
                Intent intent = new Intent(mContext, PayCenterActivity.class);
                if (chapterId == 0) {
                    mActivity.startActivityForResult(intent, TOP_UP_FOR_BUY_CHAPTER_REQUEST_CODE_1);
                } else {
                    mActivity.startActivityForResult(intent, TOP_UP_FOR_BUY_CHAPTER_REQUEST_CODE);
                }
            }
        }
    }

    public void setOnOrderPayListener(OnOrderPayListener onOrderPayListener) {
        this.onOrderPayListener = onOrderPayListener;
    }

    public interface OnOrderPayListener {
        void onPaySuccess();

        void onCancelPay();
    }
}
