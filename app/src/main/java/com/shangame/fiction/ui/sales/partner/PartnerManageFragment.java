package com.shangame.fiction.ui.sales.partner;

import android.app.Dialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.PartnerManageAdapter;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.net.response.BaseResp;
import com.shangame.fiction.net.response.PartnerListResp;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 合伙人管理Fragment
 */
public class PartnerManageFragment extends BaseFragment implements PartnerManageContacts.View {
    private List<PartnerListResp.DataBean.PageDataBean> mList = new ArrayList<>();
    private PartnerManageAdapter mAdapter;

    private PartnerManagePresenter mPresenter;

    private int mType;
    private int pageNum = 1;
    private boolean isEnd = false;
    private int pageSize = 20;
    private String mSelAgentId;
    private String mBeginTime;
    private String mEndTime;

    public PartnerManageFragment() {
        // Required empty public constructor
    }

    public static PartnerManageFragment newInstance(int type) {
        PartnerManageFragment fragment = new PartnerManageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_partner_manage, container, false);

        if (null != getArguments()) {
            mType = getArguments().getInt("type");
        }

        initView(contentView);
        initPresenter();
        initData();
        return contentView;
    }

    private void initView(View contentView) {
        final RecyclerView recyclerPartnerList = contentView.findViewById(R.id.recycler_partner_list);
        recyclerPartnerList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        TextView tvSetting = contentView.findViewById(R.id.tv_setting);
        if (mType == 1) {
            tvSetting.setVisibility(View.GONE);
        } else {
            int agentGrade = UserInfoManager.getInstance(mContext).getUserInfo().agentGrade;
            if (agentGrade == 1) {
                tvSetting.setVisibility(View.VISIBLE);
            } else {
                tvSetting.setVisibility(View.GONE);
            }
        }

        mAdapter = new PartnerManageAdapter(R.layout.item_partner_manage, mList, mType);
        recyclerPartnerList.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_setting) {
                    Log.e("hhh", "onClick");
                    PartnerListResp.DataBean.PageDataBean dataBean = mList.get(position);
                    showUpgradeDialog(dataBean);
                }
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                recyclerPartnerList.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isEnd) {
                            //数据全部加载完毕
                            mAdapter.loadMoreEnd();
                        } else {
                            //成功获取更多数据
                            loadData(mType, pageNum, mSelAgentId, mBeginTime, mEndTime);
                        }
                    }
                }, 200);
            }
        }, recyclerPartnerList);
    }

    private void initPresenter() {
        mPresenter = new PartnerManagePresenter();
        mPresenter.attachView(this);
    }

    private void initData() {
        pageNum = 1;
        String selAgentId = mSelAgentId;
        if (TextUtils.isEmpty(selAgentId)) {
            selAgentId = "0";
        }

        if (TextUtils.isEmpty(mBeginTime)) {
            mBeginTime = "";
        }
        if (TextUtils.isEmpty(mEndTime)) {
            mEndTime = "";
        }

        loadData(mType, pageNum, selAgentId, mBeginTime, mEndTime);
    }

    private void showUpgradeDialog(final PartnerListResp.DataBean.PageDataBean dataBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_partner_upgrade, null);
        Button btnUpgrade = view.findViewById(R.id.btn_upgrade);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
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
                dialog.dismiss();
                int upUserId = dataBean.userid;
                UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
                int agentId = 0;
                if (null != userInfo) {
                    agentId = userInfo.agentId;
                }
                mPresenter.setUpGrade(upUserId, agentId, 2);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }

    public void loadData(int type, int pageIndex, String selAgentId, String beginTime, String endTime) {
        Map<String, Object> map = new HashMap<>(7);
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        mType = type;
        pageNum = pageIndex;
        mSelAgentId = selAgentId;
        mBeginTime = beginTime;
        mEndTime = endTime;
        map.put("userid", userId);
        map.put("agentLeve", type);
        map.put("page", pageIndex);
        map.put("pagesize", pageSize);
        map.put("selagentId", selAgentId);
        map.put("begintime", beginTime);
        map.put("endtime", endTime);
        if (null != mPresenter) {
            mPresenter.getPartnerList(map);
        }
    }

    @Override
    public void getPartnerListSuccess(List<PartnerListResp.DataBean.PageDataBean> dataBean) {
        Log.e("hhh", "getPartnerListSuccess dataBean= " + dataBean);
        if (pageNum == 1) {
            mList.clear();
        }
        mList.addAll(dataBean);
        if (pageSize > dataBean.size()) {
            isEnd = true;
        } else {
            isEnd = false;
            pageNum += 1;
        }
        mAdapter.setNewData(mList);
        mAdapter.loadMoreComplete();
    }

    @Override
    public void getPartnerListFailure(String msg) {
        Log.e("hhh", "getPartnerListFailure msg= " + msg);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mPresenter) {
            mPresenter.detachView();
        }
    }
}
