package com.shangame.fiction.ui.my.vip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.response.VipInfoResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;

/**
 * VIP 特权 Activity
 * Create by Speedy on 2018/8/2
 */
public class VipPrivilegeActivity extends BaseActivity implements View.OnClickListener ,VipContacts.View{

    private TextView tvVipLevel;
    private TextView tvVipLevelValue;

    private View vipListlayout;
    private RecyclerView recyclerView;
    private VipAdapter vipAdapter;

    private TextView tvVipPrivilegeDetail;
    private VipPresenter vipPresenter;

    private VipInfoResponse vipInfoResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_privilege);
        initView();

        vipPresenter = new VipPresenter();
        vipPresenter.attachView(this);

        int userid = UserInfoManager.getInstance(mContext).getUserid();
        vipPresenter.getVipInfo(userid);
    }

    private void initView() {
        TextView tvPublicTitle = (TextView) findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.vip_privilege);
        findViewById(R.id.ivPublicBack).setOnClickListener(this);

        tvVipLevel = (TextView) findViewById(R.id.tvVipLevel);
        tvVipLevelValue = (TextView) findViewById(R.id.tvVipLevelValue);

        vipListlayout = findViewById(R.id.vipListlayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext,4));
        vipAdapter = new VipAdapter(VipAdapter.GRID_LAYOUT);

        recyclerView.setAdapter(vipAdapter);

        tvVipPrivilegeDetail = (TextView) findViewById(R.id.tvVipPrivilegeDetail);
        tvVipPrivilegeDetail.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.ivPublicBack){
            finish();
        }else if(view.getId() == R.id.tvVipPrivilegeDetail){
            Intent intent = new Intent(mActivity,VipPrivilegeDetailActivity.class);
            if(vipInfoResponse != null){
                intent.putExtra("VipInfoResponse",vipInfoResponse);
            }
            startActivity(intent);
        }
    }

    @Override
    public void getVipInfoSuccess(VipInfoResponse vipInfoResponse) {
        this.vipInfoResponse = vipInfoResponse;
        tvVipLevel.setText(vipInfoResponse.vipname);
        tvVipLevelValue.setText(String.valueOf(vipInfoResponse.vipvalue));

        if(vipInfoResponse.cfgentity!= null && vipInfoResponse.cfgentity.size() > 0 ){
            vipListlayout.setVisibility(View.VISIBLE);
            tvVipPrivilegeDetail.setVisibility(View.VISIBLE);

            vipAdapter.addAll(vipInfoResponse.cfgentity);
            vipAdapter.notifyDataSetChanged();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        vipPresenter.detachView();
    }
}
