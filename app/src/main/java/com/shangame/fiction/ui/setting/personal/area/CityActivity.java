package com.shangame.fiction.ui.setting.personal.area;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.net.response.CityResponse;
import com.shangame.fiction.net.response.ProvinceResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.setting.personal.PersonalContacts;
import com.shangame.fiction.ui.setting.personal.PersonalPresenter;

import java.util.HashMap;
import java.util.Map;

public class CityActivity extends BaseActivity implements AreaContracts.View, View.OnClickListener, PersonalContacts.View {
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;

    private AreaPresenter areaPresenter;

    private PersonalPresenter personalPresenter;

    private int provinceId;
    private String province;
    private int mType;

    public static void lunchActivity(Activity activity, int provinceId, String province, int requestCode, int type) {
        Intent intent = new Intent(activity, CityActivity.class);
        intent.putExtra("provinceId", provinceId);
        intent.putExtra("province", province);
        intent.putExtra("type", type);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        provinceId = getIntent().getIntExtra("provinceId", 0);
        province = getIntent().getStringExtra("province");
        mType = getIntent().getIntExtra("type", 0);

        TextView tvPublicTitle = (TextView) findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.area);

        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.public_line_horizontal));
        recyclerView.addItemDecoration(dividerItemDecoration);

        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);

        areaPresenter = new AreaPresenter();
        areaPresenter.attachView(this);

        int provinceId = getIntent().getIntExtra("provinceId", 0);
        areaPresenter.getCityList(provinceId);

        personalPresenter = new PersonalPresenter();
        personalPresenter.attachView(this);
    }

    @Override
    public void modifyProfileSuccess(UserInfo userInfo) {
        UserInfoManager.getInstance(mContext).saveUserInfo(userInfo);
        showToast(getString(R.string.modify_success));
        setResult(RESULT_OK);
        finish();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }

    private class MyAdapter extends WrapRecyclerViewAdapter<CityResponse.City, MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.province_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            final CityResponse.City city = getItem(position);
            holder.tvName.setText(city.fullName);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mType == 1) {
                        Intent intent = new Intent();
                        intent.putExtra("fId", city.fId);
                        intent.putExtra("province", province);
                        intent.putExtra("cityName", city.fullName);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Map<String, Object> map = new HashMap<>();
                        map.put("userid", UserInfoManager.getInstance(mContext).getUserid());
                        map.put("city", city.fullName);
                        map.put("province", province);
                        personalPresenter.modifyProfile(map);
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivPublicBack) {
            finish();
        }
    }

    @Override
    public void getProvinceListSuccess(ProvinceResponse provinceResponse) {
    }

    @Override
    public void getCityListSuccess(CityResponse cityResponse) {
        myAdapter.addAll(cityResponse.areaList);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        areaPresenter.detachView();
        personalPresenter.detachView();
    }
}
