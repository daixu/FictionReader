package com.shangame.fiction.ui.signin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.net.response.SignInInfoResponse;
import com.shangame.fiction.net.response.SignInResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.bookdetail.gift.FlowPopopWindow;

/**
 * 签到
 * Create by Speedy on 2018/7/23
 */
public class SignInActivity extends BaseActivity implements View.OnClickListener, SigninContract.View {

    private TextView tvSumdays;
    private TextView tvDays;
    private Button btnSignIn;

    private RecyclerView mRecyclerView;
    private MyAdapter myAdapter;

    private SignInPresenter signInPresenter;

    private boolean hadSignIn;

    private long userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initView();
        initPresenter();
    }

    private void initView() {
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText(R.string.sign_in);

        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this);

        tvSumdays = findViewById(R.id.tvSumdays);
        tvDays = findViewById(R.id.tvDays);

        mRecyclerView = findViewById(R.id.signInRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider_empty));

        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        myAdapter = new MyAdapter();
        mRecyclerView.setAdapter(myAdapter);
    }

    private void initPresenter() {
        signInPresenter = new SignInPresenter();
        signInPresenter.attachView(this);
        userid = UserInfoManager.getInstance(mContext).getUserid();
        signInPresenter.getSignInInfo(userid);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        signInPresenter.detachView();
    }

    @Override
    public void signInSuccess(SignInResponse signInResponse) {
        hadSignIn = true;
        btnSignIn.setText(R.string.sign_in_ed);
        signInPresenter.getSignInInfo(userid);
        FlowPopopWindow flowPopopWindow = new FlowPopopWindow(mActivity, signInResponse.msg);
        flowPopopWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    @Override
    public void getSigninInfoSuccess(SignInInfoResponse signInInfoResponse) {
        tvSumdays.setText(String.valueOf(signInInfoResponse.sumdays));
        tvDays.setText(getString(R.string.week_sign_in_count, signInInfoResponse.days));

        if (signInInfoResponse.todaystate == 1) {
            hadSignIn = true;
            btnSignIn.setText(R.string.sign_in_ed);
        } else {
            btnSignIn.setText(R.string.sign_in_immediately);
        }
        myAdapter.clear();
        myAdapter.addAll(signInInfoResponse.signindata);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivPublicBack:
                finish();
                break;
            case R.id.btnSignIn:
                if (hadSignIn) {
                    showToast(getString(R.string.sign_in_ed));
                } else {
                    int userid = UserInfoManager.getInstance(mContext).getUserid();
                    signInPresenter.signIn(userid);
                }
                break;
            default:
                break;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        View signLayout;
        TextView tvSignDay;
        ImageView ivSignType;
        TextView tvSignState;

        public MyViewHolder(View itemView) {
            super(itemView);
            signLayout = itemView.findViewById(R.id.signLayout);
            tvSignDay = itemView.findViewById(R.id.tvSignDay);
            ivSignType = itemView.findViewById(R.id.ivSignType);
            tvSignState = itemView.findViewById(R.id.tvSignState);
        }
    }

    class MyAdapter extends WrapRecyclerViewAdapter<SignInInfoResponse.SignInDataBean, MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.sign_in_item, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            SignInInfoResponse.SignInDataBean signInDataBean = getItem(position);
            holder.tvSignDay.setText(signInDataBean.day);
            holder.tvSignState.setText(signInDataBean.state);

            if (signInDataBean.signstate == 1) {
                holder.ivSignType.setImageResource(R.drawable.sign_in_ok);
                holder.signLayout.setBackgroundResource(R.drawable.sign_ok_bg);
            } else {
                holder.ivSignType.setImageResource(R.drawable.sign_normal);
                holder.signLayout.setBackgroundResource(R.drawable.sign_normal_bg);
            }
        }
    }
}
