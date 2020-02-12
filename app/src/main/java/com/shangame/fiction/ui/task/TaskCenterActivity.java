package com.shangame.fiction.ui.task;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.shangame.fiction.R;
import com.shangame.fiction.ad.ADConfig;
import com.shangame.fiction.ad.TTAdManagerHolder;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.net.response.AdBean;
import com.shangame.fiction.net.response.TaskListResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.bookrack.MagicIndicatorAdapter;
import com.shangame.fiction.ui.web.WebViewActivity;
import com.shangame.fiction.widget.SmartViewSwitcher;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

public class TaskCenterActivity extends BaseActivity implements View.OnClickListener, TaskCenterContacts.View {

//    private static final int FORM_RED_PACKET  = 727;
//    private static final int FORM_RED_PACKET  = 727;

    private View taskHeaderLayout;

    private double cashMoney;
    private TextView tvCashMoney;

    private SmartViewSwitcher smartViewSwitcher;

    private ViewPager mViewPager;
    private List<Fragment> list;
    private TaskCenterPresenter taskCenterPresenter;

    private FrameLayout adContainer1;
    // 头条穿山甲
    private List<TTNativeExpressAd> mTTAdList;

    private List<TaskListResponse.Cardata> mData = new ArrayList<>();
    private SmartViewSwitcher.ViewSwitcherAdapter viewSwitcherAdapter = new SmartViewSwitcher.ViewSwitcherAdapter() {

        @Override
        public void onBindView(int position, View view) {
            TaskListResponse.Cardata bean = mData.get(position);
            ImageView ivHeadIcon = view.findViewById(R.id.ivHeadIcon);
            TextView tvInfo = view.findViewById(R.id.tvInfo);
            ImageLoader.with(mActivity).loadCover(ivHeadIcon, bean.headimgurl);
            tvInfo.setText(Html.fromHtml(bean.nickname + "提现红包<font color='red'> " + bean.price + "</font> 元"));
        }

        @Override
        public Object getItemData(int position) {
            return mData.get(position);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(this);
        setStatusBarColor(R.color.task_title_color);
        setContentView(R.layout.activity_task_center);
        initHeader();
        initPresenter();
        int verify = AdBean.getInstance().getVerify();
        if (verify == 0) {
            initCsjAd();
        }
        loadData();
    }

    private void initHeader() {
        taskHeaderLayout = findViewById(R.id.taskHeaderLayout);

        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        findViewById(R.id.tvRule).setOnClickListener(this);
        findViewById(R.id.tvWithDraw).setOnClickListener(this);
        findViewById(R.id.tvDetail).setOnClickListener(this);

        tvCashMoney = findViewById(R.id.tvCashmoney);

        smartViewSwitcher = findViewById(R.id.smartViewSwitcher);

        smartViewSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                View view = getLayoutInflater().inflate(R.layout.red_roll_item, null);
                return view;
            }
        });
        smartViewSwitcher.setAdapter(viewSwitcherAdapter);
    }

    private void initPresenter() {
        taskCenterPresenter = new TaskCenterPresenter();
        taskCenterPresenter.attachView(this);
    }

    private void initCsjAd() {
        adContainer1 = findViewById(R.id.adContainer);

        initCsjAd(adContainer1, ADConfig.CSJAdPositionId.TASK_CENTER_ID);
    }

    private void loadData() {
        showLoading();

        long userId = UserInfoManager.getInstance(mContext).getUserid();
        taskCenterPresenter.getTaskList(userId);
    }

    private void initCsjAd(final FrameLayout frameLayout, String codeId) {
        frameLayout.removeAllViews();
        //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        TTAdNative adNative = TTAdManagerHolder.get().createAdNative(this);
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(this);

        mTTAdList = new ArrayList<>();
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(350, 0) //期望模板广告view的size,单位dp
                .setImageAcceptedSize(640, 320)
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        adNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.i("hhh", "load error 1 : " + code + ", " + message);
                frameLayout.removeAllViews();
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                TTNativeExpressAd mTTAd = ads.get(0);
                mTTAdList.add(mTTAd);
                // mTTAd.setSlideIntervalTime(30 * 1000);
                bindAdListener(mTTAd, frameLayout);
                mTTAd.render();
            }
        });
    }

    private void bindAdListener(TTNativeExpressAd ad, final FrameLayout frameLayout) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                Log.i("hhh", "广告被点击");
            }

            @Override
            public void onAdShow(View view, int type) {
                Log.i("hhh", "广告展示");
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Log.i("hhh", msg + " code:" + code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                //返回view的宽高 单位 dp
                Log.i("hhh", "渲染成功");
                frameLayout.removeAllViews();
                frameLayout.addView(view);
            }
        });
        //dislike设置
        bindDislike(ad, frameLayout);
    }

    /**
     * 设置广告的不喜欢, 注意：强烈建议设置该逻辑，如果不设置dislike处理逻辑，则模板广告中的 dislike区域不响应dislike事件。
     *
     * @param ad
     */
    private void bindDislike(TTNativeExpressAd ad, final FrameLayout frameLayout) {
        //使用默认模板中默认dislike弹出样式
        ad.setDislikeCallback(this, new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onSelected(int position, String value) {
                Log.i("hhh", "点击 " + value);
                //用户选择不喜欢原因后，移除广告展示
                frameLayout.removeAllViews();
            }

            @Override
            public void onCancel() {
                Log.i("hhh", "点击取消");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        smartViewSwitcher.stopLooping();
        taskCenterPresenter.detachView();
    }

    @Override
    public void onClick(View view) {
        long id = view.getId();
        if (R.id.ivPublicBack == id) {
            finish();
        } else if (R.id.tvRule == id) {
            String url = "https://m.anmaa.com/Mine/Rules";
            WebViewActivity.lunchActivity(mActivity, "规则", url);
        } else if (R.id.tvDetail == id) {

            long userid = UserInfoManager.getInstance(mContext).getUserid();
            if (userid == 0) {
                super.lunchLoginActivity();
            } else {
                Intent intent = new Intent(mActivity, RedPacketActivity.class);
                intent.putExtra("cashMoney", cashMoney);
                startActivity(intent);
            }
        } else if (R.id.tvWithDraw == id) {
            long userId = UserInfoManager.getInstance(mContext).getUserid();
            if (userId == 0) {
                super.lunchLoginActivity();
            } else {
                Intent intent = new Intent(mActivity, DrawMoneyActivity.class);
                intent.putExtra("cashMoney", cashMoney);
                startActivity(intent);
            }
        }
    }

    @Override
    public void getTaskListSuccess(TaskListResponse taskListResponse) {
        dismissLoading();
        cashMoney = taskListResponse.cashmoney;
        tvCashMoney.setText("￥" + taskListResponse.cashmoney);

        initViewPager(taskListResponse);
        initMagicIndicator();

        initSmartViewSwitcher(taskListResponse);

        taskHeaderLayout.setVisibility(View.VISIBLE);
        // mViewPager.setVisibility(View.VISIBLE);
    }

    private void initViewPager(TaskListResponse taskListResponse) {
        mViewPager = findViewById(R.id.viewPager);

        list = new ArrayList<>(3);
        list.add(TaskFragment.newInstance(1, taskListResponse.reddata));
        list.add(TaskFragment.newInstance(2, taskListResponse.daydata));
        list.add(TaskFragment.newInstance(3, taskListResponse.newhanddata));

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

        };
        mViewPager.setAdapter(adapter);
    }

    private void initMagicIndicator() {
        MagicIndicator magicIndicator = findViewById(R.id.magic_indicator);
        final List<String> titleList = new ArrayList<>(2);
        titleList.add("红包福利");
        titleList.add("日常任务");
        titleList.add("新手任务");
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        MagicIndicatorAdapter magicIndicatorAdapter = new MagicIndicatorAdapter(mContext, mViewPager) {
            @Override
            public IPagerTitleView getTitleView(Context context, int index) {
                ColorTransitionPagerTitleView iPagerTitleView = (ColorTransitionPagerTitleView) super.getTitleView(context, index);
                iPagerTitleView.setNormalColor(mContext.getResources().getColor(R.color.primary_text));
                iPagerTitleView.setSelectedColor(mContext.getResources().getColor(R.color.colorPrimary));
                return iPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = (LinePagerIndicator) super.getIndicator(context);
                linePagerIndicator.setColors(mContext.getResources().getColor(R.color.colorPrimary));
                return linePagerIndicator;
            }
        };
        magicIndicatorAdapter.setTitleList(titleList);
        commonNavigator.setAdapter(magicIndicatorAdapter);
        magicIndicator.setNavigator(commonNavigator);

        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    private void initSmartViewSwitcher(TaskListResponse taskListResponse) {
        mData.addAll(taskListResponse.cardata);
        smartViewSwitcher.notifyDataChange();
        smartViewSwitcher.setVisibility(View.VISIBLE);
        smartViewSwitcher.startLooping();
    }
}
