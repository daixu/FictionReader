package com.shangame.fiction.ui.sales.partner;

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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.MemberDialogAdapter;
import com.shangame.fiction.adapter.MemberManageAdapter;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.net.response.BaseResp;
import com.shangame.fiction.net.response.MemberListResp;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UpgradeBean;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.sales.member.MemberManageContacts;
import com.shangame.fiction.ui.sales.member.MemberManagePresenter;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员管理Fragment
 */
public class MemberManageFragment extends BaseFragment implements MemberManageContacts.View {

    private List<MemberListResp.DataBean.PageDataBean> mList = new ArrayList<>();
    private MemberManageAdapter mAdapter;
    private MemberDialogAdapter mDialogAdapter;
    private MemberManagePresenter mPresenter;

    private int pageNum = 1;
    private boolean isEnd = false;
    private int pageSize = 20;

    private String mSelUserId;
    private String mBeginTime;
    private String mEndTime;

    private UpgradeBean mBean;
    private List<UpgradeBean> mDataList = new ArrayList<>();

    public MemberManageFragment() {
    }

    public static MemberManageFragment newInstance() {
        return new MemberManageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_member_manage, container, false);
        initView(contentView);
        initPresenter();
        initData();
        return contentView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private void initView(View contentView) {
        UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
        int agentGrade = userInfo.agentGrade;

        final RecyclerView recyclerMemberList = contentView.findViewById(R.id.recycler_member_list);
        recyclerMemberList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new MemberManageAdapter(R.layout.item_member_manage, mList, agentGrade);
        recyclerMemberList.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_setting) {
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

    public void initData() {
        pageNum = 1;
        UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
        int agentId = 0;
        if (null != userInfo) {
            agentId = userInfo.agentId;
        }
        if (TextUtils.isEmpty(mBeginTime)) {
            mBeginTime = "";
        }
        if (TextUtils.isEmpty(mEndTime)) {
            mEndTime = "";
        }
        String selUserId = mSelUserId;
        if (TextUtils.isEmpty(selUserId)) {
            selUserId = "0";
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

    public void loadData(int pageIndex, String selUserId, String beginTime, String endTime) {
        UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
        int agentId = 0;
        if (null != userInfo) {
            agentId = userInfo.agentId;
        }
        pageNum = pageIndex;
        mSelUserId = selUserId;
        mBeginTime = beginTime;
        mEndTime = endTime;
        Map<String, Object> map = new HashMap<>(6);
        map.put("agentId", agentId);
        map.put("begintime", beginTime);
        map.put("endtime", endTime);
        map.put("page", pageNum);
        map.put("pagesize", pageSize);
        map.put("seluserid", selUserId);
        mPresenter.getMemberList(map);
    }

    private void showUpgradeDialog(final List<UpgradeBean> list, final MemberListResp.DataBean.PageDataBean dataBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
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
        UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
        int agentId = 0;
        if (null != userInfo) {
            agentId = userInfo.agentId;
        }
        if (agentId == 0) {
            Toast.makeText(mContext, "当前用户邀请码错误", Toast.LENGTH_SHORT).show();
            return;
        }
        String selUserId = mSelUserId;
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
    public void getMemberListSuccess(MemberListResp.DataBean dataBean) {
        Log.e("hhh", "getMemberListSuccess dataBean= " + dataBean);

        if (pageNum == 1) {
            mList.clear();
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
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setUpGradeSuccess(BaseResp resp) {
        Toast.makeText(mContext, "升级成功", Toast.LENGTH_SHORT).show();
        initData();
    }

    @Override
    public void setUpGradeFailure(String msg) {
        Log.e("hhh", "setUpGradeFailure msg= " + msg);
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }
}
