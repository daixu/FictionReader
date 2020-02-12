package com.shangame.fiction.ui.sales.account;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.lxj.xpopup.XPopup;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.utils.DateUtils;
import com.shangame.fiction.net.response.AgentDetailResp;
import com.shangame.fiction.net.response.SumPriceListResp;
import com.shangame.fiction.net.response.WithdrawResp;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.sales.withdraw.FinanceDataWindow;
import com.trello.rxlifecycle2.LifecycleTransformer;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 账户总览
 */
@Route(path = "/ss/sales/account/overview")
public class AccountOverviewActivity extends BaseActivity implements View.OnClickListener, AccountOverviewContacts.View {
    private final List<String> titleList = new ArrayList<>();
    private MagicIndicator mMagicIndicator;
    private ViewPager mViewPager;
    private AccountOverviewFragment mGoldFragment;
    private AccountOverviewFragment mSilverFragment;
    private AccountOverviewFragment memberFragment;

    private TextView mTextBeginTime;
    private TextView mTextEndTime;
    private int mPosition;

    private AccountOverviewPresenter mPresenter;

    private TextView mTextTotalIncome;
    private TextView mTextYesterdayIncome;
    private TextView mTextWithdraw;

    private String mBeginTime;
    private String mEndTime;

    private int mBeginYear;
    private int mBeginMonth;
    private int mBeginDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_overview);
        ARouter.getInstance().inject(this);
        initView();
        initViewPager();
        initPresenter();
        initMagicIndicator();
        initListener();
    }

    private void initView() {
        TextView tvTitle = findViewById(R.id.tv_title);
        TextView textOption = findViewById(R.id.text_option);

        tvTitle.setText("账户总览");
        textOption.setVisibility(View.VISIBLE);
        textOption.setText("规则");

        mMagicIndicator = findViewById(R.id.magic_indicator);
        mViewPager = findViewById(R.id.view_pager);

        mGoldFragment = AccountOverviewFragment.newInstance(1);
        mSilverFragment = AccountOverviewFragment.newInstance(2);
        memberFragment = AccountOverviewFragment.newInstance(0);

        mTextBeginTime = findViewById(R.id.text_begin_time);
        mTextEndTime = findViewById(R.id.text_end_time);

        mTextTotalIncome = findViewById(R.id.text_total_income);
        mTextYesterdayIncome = findViewById(R.id.text_yesterday_income);
        mTextWithdraw = findViewById(R.id.text_withdraw);
    }

    private void initViewPager() {
        final List<Fragment> list = new ArrayList<>();
        int agentGrade = UserInfoManager.getInstance(this).getUserInfo().agentGrade;
        switch (agentGrade) {
            case 1:
                titleList.add("金牌合伙人");
                titleList.add("银牌合伙人");
                titleList.add("会员");
                list.add(mGoldFragment);
                list.add(mSilverFragment);
                list.add(memberFragment);
                break;
            case 2:
                titleList.add("银牌合伙人");
                titleList.add("会员");
                list.add(mSilverFragment);
                list.add(memberFragment);
                break;
            case 3:
                titleList.add("会员");
                list.add(memberFragment);
                break;
            default:
                break;
        }

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titleList.get(position);
            }

            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }
        };
        mViewPager.setAdapter(adapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                Log.e("hhh", "position= " + mPosition);
            }

            @Override
            public void onPageScrollStateChanged(int stats) {

            }
        });
    }

    private void initPresenter() {
        mPresenter = new AccountOverviewPresenter();
        mPresenter.attachView(this);
    }

    private void initMagicIndicator() {
        CommonNavigator navigator = new CommonNavigator(mContext);
        navigator.setSkimOver(true);
        navigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titleList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView clipPagerTitleView = new SimplePagerTitleView(context);
                clipPagerTitleView.setText(titleList.get(index));
                clipPagerTitleView.setTextColor(Color.parseColor("#1A1F24"));
                clipPagerTitleView.setNormalColor(Color.parseColor("#1A1F24"));
                clipPagerTitleView.setSelectedColor(Color.parseColor("#4792F8"));
                clipPagerTitleView.setTextSize(15);
                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineWidth(UIUtil.dip2px(context, 20));
                indicator.setYOffset(UIUtil.dip2px(context, 3));
                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(Color.parseColor("#4792F8"));
                return indicator;
            }
        });
        mMagicIndicator.setNavigator(navigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    private void initListener() {
        mTextBeginTime.setOnClickListener(this);
        mTextEndTime.setOnClickListener(this);
        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.text_option).setOnClickListener(this);
        findViewById(R.id.layout_withdraw).setOnClickListener(this);
        findViewById(R.id.btn_withdraws_record).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mPresenter) {
            mPresenter.detachView();
        }
    }

    public void setIncomeValue(String totalIncome, String yesterdayIncome, String withdraw) {
        mTextTotalIncome.setText(totalIncome);
        mTextYesterdayIncome.setText(yesterdayIncome);
        mTextWithdraw.setText(withdraw);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.text_option:
                Log.e("hhh", "规则");
                showWithdrawRuleDialog();
                break;
            case R.id.text_begin_time:
                setBeginTime();
                break;
            case R.id.text_end_time:
                setEndTime();
                break;
            case R.id.layout_withdraw:
                withdraw();
                break;
            case R.id.btn_withdraws_record:
                ARouter.getInstance()
                        .build("/ss/sales/withdraw")
                        .navigation();
                break;
            default:
                break;
        }
    }

    private void showWithdrawRuleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_withdraw_rule, null);
        ImageView btnCancel = view.findViewById(R.id.image_close);

        final Dialog dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        if (null != window) {
            window.setBackgroundDrawable(new BitmapDrawable());
            window.setContentView(view);
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }

    private void setBeginTime() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(AccountOverviewActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Log.d("hhh", "onDateSet: year: " + year + ", month: " + month + ", dayOfMonth: " + dayOfMonth);

                        mBeginYear = year;
                        mBeginMonth = month;
                        mBeginDay = dayOfMonth;

                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        mTextBeginTime.setText(DateUtils.date2String(calendar.getTime(), "yyyy年MM月dd日"));
                        mBeginTime = DateUtils.date2String(calendar.getTime(), "yyyy-MM-dd");
                        Log.e("hhh", "beginTime = " + mBeginTime);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void setEndTime() {
        if (TextUtils.isEmpty(mBeginTime)) {
            Toast.makeText(AccountOverviewActivity.this, "请选择开始时间", Toast.LENGTH_SHORT).show();
            return;
        }
        final Calendar calendar = Calendar.getInstance();
        calendar.set(mBeginYear, mBeginMonth, mBeginDay);
        DatePickerDialog dialog = new DatePickerDialog(AccountOverviewActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Log.d("hhh", "onDateSet: year: " + year + ", month: " + month + ", dayOfMonth: " + dayOfMonth);

                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String endTime = "至" + DateUtils.date2String(calendar.getTime(), "yyyy年MM月dd日");
                        mTextEndTime.setText(endTime);
                        mEndTime = DateUtils.date2String(calendar.getTime(), "yyyy-MM-dd");
                        Log.e("hhh", "endTime = " + mEndTime);

                        int agentGrade = UserInfoManager.getInstance(AccountOverviewActivity.this).getUserInfo().agentGrade;
                        switch (agentGrade) {
                            case 1:
                                if (mPosition == 0) {
                                    mGoldFragment.loadData(1, 1, mBeginTime, mEndTime);
                                } else if (mPosition == 1) {
                                    mSilverFragment.loadData(2, 1, mBeginTime, mEndTime);
                                } else {
                                    memberFragment.loadData(0, 1, mBeginTime, mEndTime);
                                }
                                break;
                            case 2:
                                if (mPosition == 0) {
                                    mSilverFragment.loadData(2, 1, mBeginTime, mEndTime);
                                } else {
                                    memberFragment.loadData(0, 1, mBeginTime, mEndTime);
                                }
                                break;
                            case 3:
                                memberFragment.loadData(0, 1, mBeginTime, mEndTime);
                                break;
                            default:
                                break;
                        }
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        DatePicker datePicker = dialog.getDatePicker();
        datePicker.setMinDate(calendar.getTimeInMillis());
        dialog.show();
    }

    private void withdraw() {
        UserInfo userInfo = UserInfoManager.getInstance(this).getUserInfo();
        int agentId = 0;
        if (null != userInfo) {
            agentId = userInfo.agentId;
        }
        mPresenter.getAgentIdDetail(agentId, 0);
    }

    @Override
    public void getSumPriceListSuccess(SumPriceListResp.DataBean dataBean) {

    }

    @Override
    public void getSumPriceListFailure(String msg) {

    }

    @Override
    public void getAgentIdDetailSuccess(AgentDetailResp.DataBean dataBean, int orderId) {
        Log.e("hhh", "getAgentIdDetailSuccess dataBean= " + dataBean);
        UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
        int agentId = 0;
        if (null != userInfo) {
            agentId = userInfo.agentId;
        }
        mPresenter.withdraw(agentId, orderId);
    }

    @Override
    public void getAgentIdDetailFailure(String msg) {
        Log.e("hhh", "getAgentIdDetailFailure msg= " + msg);
        new XPopup.Builder(this)
                .asCustom(new FinanceDataWindow(this, null))
                .show();
    }

    @Override
    public void withdrawSuccess(WithdrawResp.DataBean dataBean) {
        Toast.makeText(this, "申请成功", Toast.LENGTH_SHORT).show();
        mTextWithdraw.setText("0");
    }

    @Override
    public void withdrawFailure(String msg) {
        Log.e("hhh", "withdrawFailure msg= " + msg);
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }
}
