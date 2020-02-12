package com.shangame.fiction.ui.setting.personal.area;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

public class ProvinceActivity extends BaseActivity implements View.OnClickListener, AreaContracts.View {
    private static final int PICK_CITY_CODE = 371;
    private TextView tvCheckCity;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private AreaPresenter areaPresenter;

    private int mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_province);

        mType = getIntent().getIntExtra("type", 0);
        String cityName = getIntent().getStringExtra("cityName");

        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.area);

        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        tvCheckCity = findViewById(R.id.tvCheckCity);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.public_line_horizontal));
        recyclerView.addItemDecoration(dividerItemDecoration);

        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);

        areaPresenter = new AreaPresenter();
        areaPresenter.attachView(this);

        UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
        if (mType == 1) {
            if (!TextUtils.isEmpty(cityName)) {
                tvCheckCity.setText(cityName);
            }
        } else {
            tvCheckCity.setText(userInfo.city);
        }
        areaPresenter.getProvinceList();
    }

    @Override
    public void getProvinceListSuccess(ProvinceResponse provinceResponse) {
        myAdapter.addAll(provinceResponse.areaList);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void getCityListSuccess(CityResponse provinceResponse) {
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }

    private class MyAdapter extends WrapRecyclerViewAdapter<ProvinceResponse.Province, MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.province_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            final ProvinceResponse.Province province = getItem(position);
            holder.tvName.setText(province.fullName);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CityActivity.lunchActivity(mActivity, province.fId, province.fullName, PICK_CITY_CODE, mType);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CITY_CODE && resultCode == RESULT_OK) {
            if (mType == 1) {
                if (null != data) {
                    int fId = data.getIntExtra("fId", -1);
                    String province = data.getStringExtra("province");
                    String cityName = data.getStringExtra("cityName");
                    Intent intent = new Intent();
                    intent.putExtra("fId", fId);
                    intent.putExtra("province", province);
                    intent.putExtra("cityName", cityName);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            } else {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivPublicBack) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        areaPresenter.detachView();
    }
}
