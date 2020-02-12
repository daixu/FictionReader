package com.shangame.fiction.ui.listen.reward;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.RewardAdapter;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.net.response.GetGiftListConfigResponse;
import com.shangame.fiction.net.response.GiveGiftResponse;
import com.shangame.fiction.net.response.ReceivedGiftResponse;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.bookdetail.gift.ReceivedGiftActivity;
import com.shangame.fiction.ui.my.pay.PayCenterActivity;
import com.shangame.fiction.ui.popup.ReadRedPacketPopupWindow;
import com.shangame.fiction.ui.task.TaskId;
import com.shangame.fiction.ui.task.TaskRewardPopupWindow;
import com.shangame.fiction.widget.GiftCarouselSwitcher;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 听书打赏界面
 *
 * @author hhh
 */
public class RewardActivity extends BaseActivity implements RewardContracts.View, View.OnClickListener {
    private static final int TOP_UP_FOR_GIFT_REQUEST_CODE = 503;
    private RewardPresenter mPresenter;
    private RewardAdapter mAdapter;
    private List<GetGiftListConfigResponse.GiftBean> mList = new ArrayList<>();
    private double currentMoney;
    /**
     * 标记是否需要先充值
     */
    private boolean needTopUpFirst;
    private TextView mTvPayTour;
    private TextView mTvTotalCoin;
    private TextView mTvCurrentMoney;
    private TextView mTvRewardTotal;
    private int albumId;
    private GetGiftListConfigResponse.GiftBean mGiftBean;

    private GiftCarouselSwitcher giftCarouselSwitcher;

    private ThreadFactory nameThreadFactory = new ThreadFactoryBuilder().setNameFormat("").build();
    private ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MICROSECONDS, new LinkedBlockingQueue<Runnable>(1024), nameThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        initView();
        initPresenter();
        initData();
    }

    private void initView() {
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.pay_tour);
        mTvPayTour = findViewById(R.id.tv_pay_tour);
        mTvTotalCoin = findViewById(R.id.tv_total_coin);
        mTvCurrentMoney = findViewById(R.id.tv_current_money);

        RecyclerView recyclerReward = findViewById(R.id.recycler_reward);
        recyclerReward.setLayoutManager(new GridLayoutManager(mContext, 3));
        mAdapter = new RewardAdapter(R.layout.gift_item, mList);

        View view = getLayoutInflater().inflate(R.layout.view_reward_header, (ViewGroup) recyclerReward.getParent(), false);
        mAdapter.addHeaderView(view);
        recyclerReward.setAdapter(mAdapter);

        mTvRewardTotal = view.findViewById(R.id.tv_reward_total);
        giftCarouselSwitcher = view.findViewById(R.id.text_switcher);
        giftCarouselSwitcher.setOnClickListener(this);

        giftCarouselSwitcher.setInAnimation(this, R.anim.slide_top_in);
        giftCarouselSwitcher.setOutAnimation(this, R.anim.slide_bootom_out);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mGiftBean = mList.get(position);
                if (currentMoney < mGiftBean.price) {
                    needTopUpFirst = true;
                    mTvPayTour.setText(R.string.top_up_and_pay_tour);
                } else {
                    needTopUpFirst = false;
                    mTvPayTour.setText(R.string.pay_tour);
                }
                for (GetGiftListConfigResponse.GiftBean bean : mList) {
                    bean.isChecked = false;
                }
                mGiftBean.isChecked = true;
                mAdapter.notifyDataSetChanged();
                mTvTotalCoin.setText(getString(R.string.total_coin, String.valueOf(mGiftBean.price)));
            }
        });

        mTvPayTour.setOnClickListener(this);
        findViewById(R.id.ivPublicBack).setOnClickListener(this);
    }

    private void initPresenter() {
        mPresenter = new RewardPresenter();
        mPresenter.attachView(this);
    }

    private void initData() {
        albumId = getIntent().getIntExtra("albumId", 0);
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        mPresenter.getGiftConfig(userId);
        mPresenter.getReceivedGift(albumId, 1, 10);
    }

    @Override
    public void getGiftConfigSuccess(GetGiftListConfigResponse response) {
        if (null != response) {
            mList.clear();
            if (null != response.data) {
                mList.addAll(response.data);
                mGiftBean = mList.get(0);
                mGiftBean.isChecked = true;
                mAdapter.setNewData(mList);

                currentMoney = response.readmoney;
                mTvCurrentMoney.setText(getString(R.string.surplus_coin, String.valueOf(currentMoney)));
                mTvTotalCoin.setText(mActivity.getString(R.string.total_coin, String.valueOf(mGiftBean.price)));

                if (currentMoney < mGiftBean.price) {
                    needTopUpFirst = true;
                    mTvPayTour.setText(R.string.top_up_and_pay_tour);
                } else {
                    needTopUpFirst = false;
                    mTvPayTour.setText(R.string.pay_tour);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mPresenter) {
            mPresenter.detachView();
        }
        if (null != giftCarouselSwitcher) {
            giftCarouselSwitcher.stopLooping();
        }
    }

    @Override
    public void rewardGiftSuccess(GiveGiftResponse response) {
        showToast(getString(R.string.give_gift_success));
        updateUserInfo(response.readmoney);
        if (response.receive == 0) {
            long userId = UserInfoManager.getInstance(mContext).getUserid();
            mPresenter.getTaskAward(userId, TaskId.PLAY_TOUR, false);
        }
    }

    private void updateUserInfo(final long readMoney) {
        singleThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                UserInfoManager userInfoManager = UserInfoManager.getInstance(mContext);
                UserInfo userInfo = userInfoManager.getUserInfo();
                userInfo.money = readMoney;
                userInfoManager.saveUserInfo(userInfo);
            }
        });
    }

    @Override
    public void getReceivedGiftSuccess(ReceivedGiftResponse response) {
        if (response.pagedata == null || response.pagedata.size() < 1) {
            giftCarouselSwitcher.setVisibility(View.GONE);

            StringBuilder total = new StringBuilder();
            total.append("总打赏量 0");

            SpannableStringBuilder builder = new SpannableStringBuilder(total);
            builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.colorPrimary)), total.indexOf("量") + 1, total.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            mTvRewardTotal.setText(builder);

        } else {
            StringBuilder total = new StringBuilder();
            total.append("总打赏量");
            total.append(" ");
            total.append(response.records);

            SpannableStringBuilder builder = new SpannableStringBuilder(total);
            builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.colorPrimary)), total.indexOf("量") + 1, total.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            mTvRewardTotal.setText(builder);

            giftCarouselSwitcher.setGiftList(mActivity, response.pagedata);
            giftCarouselSwitcher.startLooping();
        }
    }

    @Override
    public void getTaskAwardSuccess(TaskAwardResponse response, int taskId) {
        Log.i("hhh", "getTaskAwardSuccess goldType= " + response.goldtype);
        if (response.goldtype == 1) {
            ReadRedPacketPopupWindow redPacketPopupWindow = new ReadRedPacketPopupWindow(mActivity, response);
            redPacketPopupWindow.show();
        } else {
            if (response.number > 0) {
                UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
                userInfo.money = (long) response.number;
                UserInfoManager.getInstance(mContext).saveUserInfo(userInfo);

                Intent intent = new Intent(BroadcastAction.UPDATE_TASK_LIST);
                intent.putExtra(SharedKey.TASK_ID, taskId);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

                TaskRewardPopupWindow taskRewardPopupWindow = new TaskRewardPopupWindow(mActivity);
                taskRewardPopupWindow.show(response.desc, response.number + "");
            } else if (response.regtype == 0) {
                showToast("已经领取");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivPublicBack: {
                finish();
            }
            break;
            case R.id.tv_pay_tour: {
                if (needTopUpFirst) {
                    Intent intent = new Intent(mActivity, PayCenterActivity.class);
                    startActivityForResult(intent, TOP_UP_FOR_GIFT_REQUEST_CODE);
                } else {
                    int userId = UserInfoManager.getInstance(mActivity).getUserid();
                    if (null != mGiftBean) {
                        mPresenter.rewardGift(userId, mGiftBean.propid, 1, albumId);
                    } else {
                        Toast.makeText(mContext, "请选择打赏金额", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case R.id.text_switcher: {
                Intent intent = new Intent(mActivity, ReceivedGiftActivity.class);
                intent.putExtra("bookid", (long) albumId);
                startActivity(intent);
            }
            break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Tencent.onActivityResultData(requestCode, resultCode, data, null);

        if (requestCode == TOP_UP_FOR_GIFT_REQUEST_CODE) {
            UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
            double money = userInfo.money + userInfo.coin;
            setCurrentMoney(money);
        }
    }

    public void setCurrentMoney(double money) {
        this.currentMoney = money;
        mTvCurrentMoney.setText(getString(R.string.surplus_coin, String.valueOf(money)));

        if (mGiftBean != null) {
            if (currentMoney < mGiftBean.price) {
                needTopUpFirst = true;
                mTvPayTour.setText(R.string.top_up_and_pay_tour);
            } else {
                needTopUpFirst = false;
                mTvPayTour.setText(R.string.pay_tour);
            }
        }
    }

    private class ThreadFactoryBuilder implements ThreadFactory {

        private String mNameFormat;

        private ThreadFactoryBuilder() {
        }

        private ThreadFactoryBuilder setNameFormat(String nameFormat) {
            this.mNameFormat = nameFormat;
            return this;
        }

        public ThreadFactoryBuilder build() {
            return this;
        }

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            return new Thread(runnable, mNameFormat + "-Thread-");
        }
    }
}
