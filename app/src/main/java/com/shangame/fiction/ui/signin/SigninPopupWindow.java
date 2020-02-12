package com.shangame.fiction.ui.signin;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BasePopupWindow;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.net.response.SignInInfoResponse;
import com.shangame.fiction.net.response.SignInResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.task.TaskRewardPopupWindow;
import com.shangame.fiction.widget.WeekDayItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Speedy on 2019/1/2
 */
public class SigninPopupWindow extends BasePopupWindow implements View.OnClickListener, SigninContract.View {

    private SignInPresenter signInPresenter;

    private long userid;

    private boolean hadSignIn;

    private TextView tvDayCount;
    private Button btnSignIn;

    private WeekDayItem weekDayItem1;
    private WeekDayItem weekDayItem2;
    private WeekDayItem weekDayItem3;
    private WeekDayItem weekDayItem4;
    private WeekDayItem weekDayItem5;
    private WeekDayItem weekDayItem6;
    private WeekDayItem weekDayItem7;

    private List<WeekDayItem> weekDayItemList;

    public SigninPopupWindow(Activity activity) {
        super(activity);
        initView();
        initPresenter();
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
    }

    private void initView() {
        View contentView = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.popup_window_sign_in, null);
        tvDayCount = contentView.findViewById(R.id.tvDayCount);
        btnSignIn = contentView.findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this);
        contentView.findViewById(R.id.ivX).setOnClickListener(this);

        weekDayItem1 = contentView.findViewById(R.id.weekDay1);
        weekDayItem2 = contentView.findViewById(R.id.weekDay2);
        weekDayItem3 = contentView.findViewById(R.id.weekDay3);
        weekDayItem4 = contentView.findViewById(R.id.weekDay4);
        weekDayItem5 = contentView.findViewById(R.id.weekDay5);
        weekDayItem6 = contentView.findViewById(R.id.weekDay6);
        weekDayItem7 = contentView.findViewById(R.id.weekDay7);

        weekDayItemList = new ArrayList<>(7);
        weekDayItemList.add(weekDayItem1);
        weekDayItemList.add(weekDayItem2);
        weekDayItemList.add(weekDayItem3);
        weekDayItemList.add(weekDayItem4);
        weekDayItemList.add(weekDayItem5);
        weekDayItemList.add(weekDayItem6);
        weekDayItemList.add(weekDayItem7);

        setContentView(contentView);
    }

    private void initPresenter() {
        signInPresenter = new SignInPresenter();
        signInPresenter.attachView(this);
        userid = UserInfoManager.getInstance(mActivity).getUserid();
        signInPresenter.getSignInInfo(userid);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivX) {
            dismiss();
        } else if (v.getId() == R.id.btnSignIn) {
            int userid = UserInfoManager.getInstance(mActivity).getUserid();
            signInPresenter.signIn(userid);
        }
    }

    @Override
    public void signInSuccess(SignInResponse signInResponse) {
        Intent intent = new Intent(BroadcastAction.UPDATE_SIAN_INFO);
        intent.putExtra("signInfo", signInResponse.newviptext);
        LocalBroadcastManager.getInstance(mActivity).sendBroadcast(intent);
        UserInfoManager userInfoManager = UserInfoManager.getInstance(mActivity);
        UserInfo userInfo = userInfoManager.getUserInfo();
        userInfo.coin = signInResponse.coin;
        userInfoManager.saveUserInfo(userInfo);

        if (signInResponse.coin > 0) {
            showToast("签到成功");
            TaskRewardPopupWindow taskRewardPopupWindow = new TaskRewardPopupWindow(mActivity);
            taskRewardPopupWindow.show("", signInResponse.msg + "");
            dismiss();
        } else {
            hadSignIn = true;
            btnSignIn.setText(R.string.sign_in_ed);
            btnSignIn.setEnabled(false);
            signInPresenter.getSignInInfo(userid);
        }
    }

    @Override
    public void getSigninInfoSuccess(SignInInfoResponse signInInfoResponse) {
        tvDayCount.setText(String.valueOf(signInInfoResponse.sumdays));

        if (signInInfoResponse.todaystate == 1) {
            hadSignIn = true;
            btnSignIn.setText(R.string.sign_in_ed);
            btnSignIn.setEnabled(false);
            btnSignIn.setBackgroundResource(R.drawable.helf_round_theme_bg);
        } else {
            btnSignIn.setText(R.string.sign_in_immediately);
            btnSignIn.setEnabled(true);
        }

        WeekDayItem weekDayItem;
        SignInInfoResponse.SignInDataBean signInDataBean;
        for (int i = 0; i < signInInfoResponse.signindata.size(); i++) {
            weekDayItem = weekDayItemList.get(i);
            signInDataBean = signInInfoResponse.signindata.get(i);

            weekDayItem.tvDay.setText(signInDataBean.day);
            weekDayItem.tvGift.setText(signInDataBean.reward);

            if (i == 0) {
                weekDayItem.ivLine1.setVisibility(View.GONE);
            } else if (i == 6) {
                weekDayItem.ivLine2.setVisibility(View.GONE);
            }
            weekDayItem.tvGift.setTextColor(mActivity.getResources().getColor(R.color.primary_text));
            weekDayItem.tvDay.setTextColor(mActivity.getResources().getColor(R.color.primary_text));
            if (signInDataBean.signstate == 1) {

                if (signInInfoResponse.days == i + 1) {
                    weekDayItem.ivLine1.setImageResource(R.drawable.sign_line);
                    weekDayItem.ivDot.setImageResource(R.drawable.dot_red);
                    weekDayItem.ivLine2.setImageResource(R.drawable.sign_line_gray2);
                } else {
                    weekDayItem.ivLine1.setImageResource(R.drawable.sign_line);
                    weekDayItem.ivDot.setImageResource(R.drawable.dot_red);
                    weekDayItem.ivLine2.setImageResource(R.drawable.sign_line);
                }
            } else {
                weekDayItem.ivLine1.setImageResource(R.drawable.sign_line_gray2);
                weekDayItem.ivDot.setImageResource(R.drawable.dot_gray2);
                weekDayItem.ivLine2.setImageResource(R.drawable.sign_line_gray2);
            }
        }

//        if(signInInfoResponse.todaystate == 1){
//            btnSignState.setText("已连续签到"+signInInfoResponse.days+"天");
//            btnSignState.setBackgroundResource(R.drawable.task_2);
//            btnSignState.setEnabled(false);
//        }else{
//            btnSignState.setText("签到");
//            btnSignState.setBackgroundResource(R.drawable.task_1);
//            btnSignState.setEnabled(true);
//
//            signInPresenter.signIn(userid);//自动签到
//        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvGift;
        ImageView ivDot;
        ImageView ivLine1;
        ImageView ivLine2;
        TextView tvState;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvGift = itemView.findViewById(R.id.tvGift);
            ivDot = itemView.findViewById(R.id.ivDot);
            ivLine1 = itemView.findViewById(R.id.ivLine1);
            ivLine2 = itemView.findViewById(R.id.ivLine2);
            tvState = itemView.findViewById(R.id.tvState);
        }
    }

    private class MyAdapter extends WrapRecyclerViewAdapter<SignInInfoResponse.SignInDataBean, MyViewHolder> {

        private int todaydays;

        public void setTodaydays(int todaydays) {
            this.todaydays = todaydays;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.signin_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            final SignInInfoResponse.SignInDataBean signInDataBean = getItem(position);

            holder.tvGift.setText(signInDataBean.reward);
            holder.tvState.setText(signInDataBean.day);
            if (signInDataBean.signstate == 1) {
                holder.ivDot.setImageResource(R.drawable.dot_red);
                holder.ivLine1.setImageResource(R.drawable.sign_line);
                holder.ivLine2.setImageResource(R.drawable.sign_line);
            } else {
                holder.ivDot.setImageResource(R.drawable.dot_gray);
                holder.ivLine1.setImageResource(R.drawable.sign_line_gray);
                holder.ivLine2.setImageResource(R.drawable.sign_line_gray);
            }

            if (position == 6) {
                holder.ivLine2.setVisibility(View.GONE);
            } else {
                holder.ivLine2.setVisibility(View.VISIBLE);
            }

            if (position == 0) {
                holder.ivLine1.setVisibility(View.GONE);
            }
            if (position < todaydays) {
                holder.tvState.setText(signInDataBean.state);
            } else if (position == todaydays) {
                holder.ivLine1.setImageResource(R.drawable.sign_line);
                holder.ivLine2.setImageResource(R.drawable.sign_line_gray);
                holder.tvState.setText("今天");
                holder.tvState.setTextColor(mActivity.getResources().getColor(R.color.white));
                holder.tvState.setBackgroundResource(R.drawable.helf_round_theme_bg);
            } else {
                holder.tvState.setText(signInDataBean.day);
            }
        }
    }
}
