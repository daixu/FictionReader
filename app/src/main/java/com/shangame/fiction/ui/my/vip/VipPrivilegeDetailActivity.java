package com.shangame.fiction.ui.my.vip;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.response.VipInfoResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;

public class VipPrivilegeDetailActivity extends BaseActivity implements View.OnClickListener ,VipContacts.View{

    private RecyclerView recyclerView;
    private VipAdapter vipAdapter;

    private VipPresenter vipPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_privilege_detail);
        initView();
        initData();
    }

    private void initView() {
        TextView tvPublicTitle = (TextView) findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.vip_privilege_detail);
        findViewById(R.id.ivPublicBack).setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL) ;
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.public_line_horizontal));
        recyclerView.addItemDecoration(dividerItemDecoration);

        vipAdapter = new VipAdapter(VipAdapter.LINEAR_LAYOUT);
        recyclerView.setAdapter(vipAdapter);
    }


    private void initData() {
        VipInfoResponse vipInfoResponse = getIntent().getParcelableExtra("VipInfoResponse");
        if(vipInfoResponse != null){
            getVipInfoSuccess(vipInfoResponse);
        }else{
            vipPresenter = new VipPresenter();
            vipPresenter.attachView(this);
            vipPresenter.getVipInfo(UserInfoManager.getInstance(mContext).getUserid());
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.ivPublicBack){
            finish();
        }
    }

    @Override
    public void getVipInfoSuccess(VipInfoResponse vipInfoResponse) {
        vipAdapter.addAll(vipInfoResponse.cfgentity);
        vipAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(vipPresenter != null){
            vipPresenter.detachView();
        }
    }
}
