package com.shangame.fiction.ui.sales.member;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.MemberDialogAdapter;
import com.shangame.fiction.adapter.MemberManageAdapter;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.utils.DateUtils;
import com.shangame.fiction.net.response.BaseResp;
import com.shangame.fiction.net.response.MemberListResp;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UpgradeBean;
import com.shangame.fiction.storage.model.UserInfo;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员管理
 */
@Route(path = "/ss/sales/member/manage")
public class MemberManageActivity extends BaseActivity implements View.OnClickListener, MemberManageContacts.View {

    @Autowired
    int memberCount;
    private List<MemberListResp.DataBean.PageDataBean> mList = new ArrayList<>();
    private MemberManageAdapter mAdapter;
    private MemberDialogAdapter mDialogAdapter;
    private MemberManagePresenter mPresenter;
    private EditText mEditUserId;
    private TextView mTextBeginTime;
    private TextView mTextEndTime;
    private TextView mTextMemberTotal;
    private int pageNum = 1;
    private boolean isEnd = false;
    private int pageSize = 20;
    private String mBeginTime;
    private String mEndTime;

    private int mBeginYear;
    private int mBeginMonth;
    private int mBeginDay;

    private UpgradeBean mBean;
    private List<UpgradeBean> mDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_manage);
        ARouter.getInstance().inject(this);
        initView();
        initPresenter();
        initData();

        Log.e("hhh", "memberCount= " + memberCount);
    }

    private void initView() {
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText("会员管理");

        mEditUserId = findViewById(R.id.edit_user_id);
        mTextMemberTotal = findViewById(R.id.text_member_total);

        String strMemberTotal = "总人数：" + memberCount + "人";
        mTextMemberTotal.setText(strMemberTotal);

        mTextBeginTime = findViewById(R.id.text_begin_time);
        mTextEndTime = findViewById(R.id.text_end_time);

        UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
        int agentGrade = userInfo.agentGrade;

        final RecyclerView recyclerMemberList = findViewById(R.id.recycler_member_list);
        recyclerMemberList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new MemberManageAdapter(R.layout.item_member_manage, mList, agentGrade);
        recyclerMemberList.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_setting) {
                    Log.e("hhh", "onClick");
                    MemberListResp.DataBean.PageDataBean dataBean = mList.get(position);

                    int agentGrade = UserInfoManager.getInstance(mContext).getUserInfo().agentGrade;
                    List<UpgradeBean> list = new ArrayList<>();
                    UpgradeBean silverBean = new UpgradeBean();
                    silverBean.id = 1;
                    silverBean.resId = R.drawable.icon_silver_small;
                    silverBean.title = "银牌合伙人";
                    silverBean.isClick = true;

                    UpgradeBean goldBean = new UpgradeBean();
                    goldBean.id = 2;
                    goldBean.resId = R.drawable.icon_gold_small;
                    goldBean.title = "金牌合伙人";
                    goldBean.isClick = false;
                    if (agentGrade == 1) {
                        list.add(silverBean);
                        list.add(goldBean);
                    } else {
                        list.add(silverBean);
                    }
                    showUpgradeDialog(list, dataBean);
                }
            }
        });

        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        findViewById(R.id.btn_search).setOnClickListener(this);
        mTextBeginTime.setOnClickListener(this);
        mTextEndTime.setOnClickListener(this);

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                recyclerMemberList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isEnd) {
                            //数据全部加载完毕
                            mAdapter.loadMoreEnd();
                        } else {
                            //成功获取更多数据
                            loadData(pageNum);
                        }
                    }
                }, 200);
            }
        }, recyclerMemberList);
    }

    private void initPresenter() {
        mPresenter = new MemberManagePresenter();
        mPresenter.attachView(this);
    }

    private void initData() {
        pageNum = 1;
        UserInfo userInfo = UserInfoManager.getInstance(this).getUserInfo();
        int agentId = 0;
        if (null != userInfo) {
            agentId = userInfo.agentId;
        }
        String selUserId = mEditUserId.getText().toString().trim();
        if (TextUtils.isEmpty(selUserId)) {
            selUserId = "0";
        }
        if (TextUtils.isEmpty(mBeginTime)) {
            mBeginTime = "";
        }
        if (TextUtils.isEmpty(mEndTime)) {
            mEndTime = "";
        }
        Map<String, Object> map = new HashMap<>(6);
        map.put("agentId", agentId);
        map.put("begintime", mBeginTime);
        map.put("endtime", mEndTime);
        map.put("page", pageNum);
        map.put("pagesize", pageSize);
        map.put("seluserid", selUserId);
        mPresenter.getMemberList(map);
    }

    private void showUpgradeDialog(final List<UpgradeBean> list, final MemberListResp.DataBean.PageDataBean dataBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_member_upgrade, null);
        Button btnUpgrade = view.findViewById(R.id.btn_upgrade);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        RecyclerView recyclerItems = view.findViewById(R.id.recycler_items);
        recyclerItems.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mDataList.clear();
        mDataList.addAll(list);

        mBean = mDataList.get(0);

        mDialogAdapter = new MemberDialogAdapter(R.layout.item_member_dialog, mDataList);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.public_line_horizontal));
        recyclerItems.addItemDecoration(dividerItemDecoration);
        recyclerItems.setAdapter(mDialogAdapter);
        final Dialog dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        if (null != window) {
            window.setBackgroundDrawable(new BitmapDrawable());
            window.setContentView(view);
        }
        btnUpgrade.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null == mBean) {
                    Toast.makeText(mContext, "不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
                int upUserId = dataBean.userid;
                UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
                int agentId = 0;
                if (null != userInfo) {
                    agentId = userInfo.agentId;
                }
                if (mBean.id == 1) {
                    mPresenter.setUpGrade(upUserId, agentId, 3);
                } else {
                    mPresenter.setUpGrade(upUserId, agentId, 2);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });

        mDialogAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.image_check) {
                    for (UpgradeBean data : mDataList) {
                        data.isClick = false;
                    }
                    UpgradeBean bean = mDataList.get(position);
                    bean.isClick = true;
                    mDialogAdapter.notifyDataSetChanged();

                    mBean = bean;
                }
            }
        });
    }

    private void loadData(int pageNum) {
        UserInfo userInfo = UserInfoManager.getInstance(this).getUserInfo();
        int agentId = 0;
        if (null != userInfo) {
            agentId = userInfo.agentId;
        }
        if (agentId == 0) {
            Toast.makeText(this, "当前用户邀请码错误", Toast.LENGTH_SHORT).show();
            return;
        }
        String selUserId = mEditUserId.getText().toString().trim();
        if (TextUtils.isEmpty(selUserId)) {
            selUserId = "0";
        }
        if (TextUtils.isEmpty(mBeginTime)) {
            mBeginTime = "";
        }
        if (TextUtils.isEmpty(mEndTime)) {
            mEndTime = "";
        }
        Map<String, Object> map = new HashMap<>(6);
        map.put("agentId", agentId);
        map.put("begintime", mBeginTime);
        map.put("endtime", mEndTime);
        map.put("page", pageNum);
        map.put("pagesize", pageSize);
        map.put("seluserid", selUserId);
        mPresenter.getMemberList(map);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
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

    private void search() {
        loadData(1);
    }

    private void setBeginTime() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(MemberManageActivity.this,
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
                        Log.e("hhh", "startTime = " + mBeginTime);
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
        DatePickerDialog dialog = new DatePickerDialog(MemberManageActivity.this,
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

    @Override
    public void getMemberListSuccess(MemberListResp.DataBean dataBean) {
        Log.e("hhh", "getMemberListSuccess dataBean= " + dataBean);
        String strMemberTotal = "总人数：" + dataBean.records + "人";
        mTextMemberTotal.setText(strMemberTotal);
        if (pageNum == 1) {
            mList.clear();
        }
        if (null == dataBean.pagedata || dataBean.pagedata.size() == 0) {
            getMemberListFailure("请求失败");
            return;
        }
        mList.addAll(dataBean.pagedata);
        if (pageSize > dataBean.pagedata.size()) {
            isEnd = true;
        } else {
            isEnd = false;
            pageNum += 1;
        }
        mAdapter.setNewData(mList);
        mAdapter.loadMoreComplete();
    }

    @Override
    public void getMemberListFailure(String msg) {
        Log.e("hhh", "getMemberListFailure msg= " + msg);
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setUpGradeSuccess(BaseResp resp) {
        Toast.makeText(this, "升级成功", Toast.LENGTH_SHORT).show();
        initData();
    }

    @Override
    public void setUpGradeFailure(String msg) {
        Log.e("hhh", "setUpGradeFailure msg= " + msg);
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }
}
