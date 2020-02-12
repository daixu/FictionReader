package com.shangame.fiction.ui.sales.partner;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.utils.DateUtils;
import com.shangame.fiction.storage.manager.UserInfoManager;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 合伙人管理Activity
 */
@Route(path = "/ss/sales/partner/manage")
public class PartnerManageActivity extends BaseActivity implements View.OnClickListener {
    private final List<String> titleList = new ArrayList<>();
    @Autowired
    int agentCount;
    @Autowired
    int memberCount;
    private MagicIndicator mMagicIndicator;
    private ViewPager mViewPager;
    private PartnerManageFragment mGoldFragment;
    private PartnerManageFragment mSilverFragment;
    private MemberManageFragment memberFragment;
    private TextView mTextBeginTime;
    private TextView mTextEndTime;
    private EditText mEditInvitationCode;
    private String mBeginTime;
    private String mEndTime;
    private int mBeginYear;
    private int mBeginMonth;
    private int mBeginDay;
    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_manage);
        ARouter.getInstance().inject(this);
        initView();
        initViewPager();
        initMagicIndicator();

        Log.e("hhh", "agentCount= " + agentCount + " ,memberCount= " + memberCount);
    }

    private void initView() {
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText("成员管理");

        TextView textMemberTotal = findViewById(R.id.text_member_total);
        String strMemberTotal = "总人数：" + (agentCount + memberCount) + "人";
        textMemberTotal.setText(strMemberTotal);

        mMagicIndicator = findViewById(R.id.magic_indicator);
        mViewPager = findViewById(R.id.view_pager);

        mGoldFragment = PartnerManageFragment.newInstance(1);
        mSilverFragment = PartnerManageFragment.newInstance(2);
        memberFragment = MemberManageFragment.newInstance();

        mTextBeginTime = findViewById(R.id.text_begin_time);
        mTextEndTime = findViewById(R.id.text_end_time);
        mEditInvitationCode = findViewById(R.id.edit_invitation_code);

        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        findViewById(R.id.btn_search).setOnClickListener(this);
        mTextBeginTime.setOnClickListener(this);
        mTextEndTime.setOnClickListener(this);
    }

    private void initViewPager() {
        final List<Fragment> list = new ArrayList<>();
        final int agentGrade = UserInfoManager.getInstance(this).getUserInfo().agentGrade;
        if (agentGrade == 1) {
            titleList.add("金牌");
            titleList.add("银牌");
            titleList.add("会员");
            list.add(mGoldFragment);
            list.add(mSilverFragment);
            list.add(memberFragment);
        } else if (agentGrade == 2) {
            titleList.add("合伙人");
            titleList.add("会员");
            list.add(mSilverFragment);
            list.add(memberFragment);
        } else {
            titleList.add("会员");
            list.add(memberFragment);
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
                search();
            }

            @Override
            public void onPageScrollStateChanged(int stats) {

            }
        });
    }

    private void initMagicIndicator() {
        CommonNavigator navigator = new CommonNavigator(mContext);
        PartnerIndicatorAdapter adapter = new PartnerIndicatorAdapter(mViewPager);
        adapter.setTitleList(titleList);
        navigator.setSkimOver(true);
        navigator.setAdapter(adapter);
        mMagicIndicator.setNavigator(navigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    private void search() {
        int agentGrade = UserInfoManager.getInstance(this).getUserInfo().agentGrade;
        String selUserId = mEditInvitationCode.getText().toString().trim();
        if (TextUtils.isEmpty(selUserId)) {
            selUserId = "0";
        }
        if (TextUtils.isEmpty(mBeginTime)) {
            mBeginTime = "";
        }
        if (TextUtils.isEmpty(mEndTime)) {
            mEndTime = "";
        }
        if (agentGrade == 1) {
            if (mPosition == 0) {
                mGoldFragment.loadData(1, 1, selUserId, mBeginTime, mEndTime);
            } else if (mPosition == 1) {
                mSilverFragment.loadData(2, 1, selUserId, mBeginTime, mEndTime);
            } else {
                memberFragment.loadData(1, selUserId, mBeginTime, mEndTime);
            }
        } else if (agentGrade == 2) {
            if (mPosition == 0) {
                mSilverFragment.loadData(2, 1, selUserId, mBeginTime, mEndTime);
            } else {
                memberFragment.loadData(1, selUserId, mBeginTime, mEndTime);
            }
        } else {
            memberFragment.loadData(1, selUserId, mBeginTime, mEndTime);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivPublicBack:
                finish();
                break;
            case R.id.btn_search:
                search();
                break;
            case R.id.text_begin_time:
                setBeginTime();
                break;
            case R.id.text_end_time:
                setEndTime();
                break;
            default:
                break;
        }
    }

    private void setBeginTime() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(PartnerManageActivity.this,
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
        final Calendar calendar = Calendar.getInstance();
        calendar.set(mBeginYear, mBeginMonth, mBeginDay);
        DatePickerDialog dialog = new DatePickerDialog(PartnerManageActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Log.d("hhh", "onDateSet: year: " + year + ", month: " + month + ", dayOfMonth: " + dayOfMonth);

                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        mTextEndTime.setText(DateUtils.date2String(calendar.getTime(), "yyyy年MM月dd日"));
                        mEndTime = DateUtils.date2String(calendar.getTime(), "yyyy-MM-dd");
                        Log.e("hhh", "endTime = " + mEndTime);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        DatePicker datePicker = dialog.getDatePicker();
        datePicker.setMinDate(calendar.getTimeInMillis());
        dialog.show();
    }
}
