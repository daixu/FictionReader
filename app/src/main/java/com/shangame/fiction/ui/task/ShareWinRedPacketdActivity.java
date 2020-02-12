package com.shangame.fiction.ui.task;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.response.GetInviteUrlResponse;
import com.shangame.fiction.net.response.ShareWinRedResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.web.WebViewActivity;
import com.shangame.fiction.widget.GlideApp;

import java.util.ArrayList;
import java.util.List;

/**
 * 邀请好友注册(分享得红包)
 */
public class ShareWinRedPacketdActivity extends BaseActivity implements View.OnClickListener, ShareWinRedContacts.View {

    private SmartRefreshLayout smartRefreshLayout;
    private View scrollView;

    private ImageView ivHeadIcon;
    private TextView tvName;
    private TextView tvReadTime;

    private TextView tvFirst;
    private TextView tvSecond;
    private TextView tvThird;

    private TextView tvRedInfo1;
    private TextView tvRedInfo2;
    private TextView tvRedInfo3;

    private TextView tvInvite1;
    private TextView tvInvite2;
    private TextView tvInvite3;

    private TextView tvShare1;
    private TextView tvShare2;
    private TextView tvShare3;

    private TextView tvRule;
    private ViewPager viewPager;
    private ShareWinRedPresenter shareWinRedPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_win_red_packetd);
        initTitle();
        initSmartRefreshLayout();
        initView();
        initViewPager();
        initPresenter();
        smartRefreshLayout.autoRefresh();
    }

    private void initTitle() {
        findViewById(R.id.ivPublicBack).setOnClickListener(this);
        TextView tvPublicTitle = findViewById(R.id.tvPublicTitle);
        tvPublicTitle.setText("分享得红包");
    }

    private void initSmartRefreshLayout() {
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                long userid = UserInfoManager.getInstance(mContext).getUserid();
                shareWinRedPresenter.getShareRule(userid);
            }
        });
    }

    private void initView() {
        scrollView = findViewById(R.id.scrollView);
        ivHeadIcon = findViewById(R.id.ivHeadIcon);
        tvName = findViewById(R.id.tvName);
        tvReadTime = findViewById(R.id.tvReadTime);

        tvRule = findViewById(R.id.tvRule);
        tvRule.setOnClickListener(this);
    }

    private void initViewPager() {
        viewPager = findViewById(R.id.viewPager);
        View stepView1 = getLayoutInflater().inflate(R.layout.share_step_item, null);
        View stepView2 = getLayoutInflater().inflate(R.layout.share_step_item, null);
        View stepView3 = getLayoutInflater().inflate(R.layout.share_step_item, null);
        ImageView ivStep1 = stepView1.findViewById(R.id.ivStep);
        ImageView ivStep2 = stepView2.findViewById(R.id.ivStep);
        ImageView ivStep3 = stepView3.findViewById(R.id.ivStep);

        ivStep1.setImageResource(R.drawable.share_red_step1);
        ivStep2.setImageResource(R.drawable.share_red_step2);
        ivStep3.setImageResource(R.drawable.share_red_step3);

        tvRedInfo1 = stepView1.findViewById(R.id.tvRedInfo);
        tvRedInfo2 = stepView2.findViewById(R.id.tvRedInfo);
        tvRedInfo3 = stepView3.findViewById(R.id.tvRedInfo);

        tvInvite1 = stepView1.findViewById(R.id.tvInvite);
        tvShare1 = stepView1.findViewById(R.id.tvShare);

        tvInvite2 = stepView2.findViewById(R.id.tvInvite);
        tvShare2 = stepView2.findViewById(R.id.tvShare);

        tvInvite3 = stepView3.findViewById(R.id.tvInvite);
        tvShare3 = stepView3.findViewById(R.id.tvShare);

        tvFirst = stepView1.findViewById(R.id.tvStepRule);
        tvSecond = stepView2.findViewById(R.id.tvStepRule);
        tvThird = stepView3.findViewById(R.id.tvStepRule);

        tvShare1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ShareToFriendActivity.class);
                startActivity(intent);
            }
        });
        tvShare2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ShareToFriendActivity.class);
                startActivity(intent);
            }
        });
        tvShare3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ShareToFriendActivity.class);
                startActivity(intent);
            }
        });

        final List<View> viewList = new ArrayList<>();
        viewList.add(stepView1);
        viewList.add(stepView2);
        viewList.add(stepView3);

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @NonNull
            @Override
            public View instantiateItem(@NonNull ViewGroup container, int position) {
                View view = viewList.get(position);

                if (view.getRootView() != null) {
                    container.removeView(view);
                }
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }
        });
    }

    private void initPresenter() {
        shareWinRedPresenter = new ShareWinRedPresenter();
        shareWinRedPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shareWinRedPresenter.detachView();
    }

    @Override
    public void onClick(View view) {
        long id = view.getId();
        if (id == R.id.ivPublicBack) {
            finish();
        } else if (id == R.id.tvRule) {
            String url = "https://m.anmaa.com/Mine/FriendsRules";
            WebViewActivity.lunchActivity(mActivity, "活动规则", url);
        }
    }

    @Override
    public void getShareRuleSuccess(ShareWinRedResponse shareWinRedResponse) {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.setEnableRefresh(false);

        UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
        // ImageLoader.with(mActivity).loadUserIcon(ivHeadIcon, userInfo.headimgurl);

        GlideApp.with(mActivity)
                .load(userInfo.headimgurl)
                .centerCrop()
                .placeholder(R.drawable.default_head)
                .into(ivHeadIcon);

        tvName.setText(userInfo.nickname);
        tvReadTime.setText("阅读时间：" + shareWinRedResponse.readTime + "分钟");

        ShareWinRedResponse.InviteEntity inviteEntity;
        for (int i = 0; i < shareWinRedResponse.cardata.size(); i++) {
            inviteEntity = shareWinRedResponse.cardata.get(i);
            switch (i) {
                case 0:
                    initFirst(inviteEntity);
                    break;
                case 1:
                    initSecond(inviteEntity);
                    break;
                case 2:
                    initThird(inviteEntity);
                    break;
                default:
                    break;
            }
        }
        scrollView.setVisibility(View.VISIBLE);
    }

    private void initFirst(final ShareWinRedResponse.InviteEntity inviteEntity) {
        Log.i("aa", "initFirst: " + inviteEntity.rule);
        tvFirst.setText(Html.fromHtml(inviteEntity.rule));
        tvRedInfo1.setText("领" + inviteEntity.number + "元红包");
        if (inviteEntity.state == 0) {
            tvShare1.setEnabled(false);
            tvInvite1.setEnabled(false);
            tvShare1.setBackgroundResource(R.drawable.share_red_btn_s);
            tvInvite1.setBackgroundResource(R.drawable.share_red_invite_btn_s);
        } else {
            tvShare1.setEnabled(true);
            tvInvite1.setEnabled(true);
            tvShare1.setBackgroundResource(R.drawable.share_red_btn);
            tvInvite1.setBackgroundResource(R.drawable.share_red_invite_btn);
        }

        tvInvite1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, InviteRecordActivity.class);
                intent.putExtra("inviteid", inviteEntity.inviteid);
                startActivity(intent);
            }
        });
    }

    private void initSecond(final ShareWinRedResponse.InviteEntity inviteEntity) {
        Log.i("aa", "initSecond: " + inviteEntity.rule);
        tvSecond.setText(Html.fromHtml(inviteEntity.rule));
        tvRedInfo2.setText("领" + inviteEntity.number + "元红包");
        if (inviteEntity.state == 0) {
            tvShare2.setEnabled(false);
            tvInvite2.setEnabled(false);
            tvShare2.setBackgroundResource(R.drawable.share_red_btn_s);
            tvInvite2.setBackgroundResource(R.drawable.share_red_invite_btn_s);
        } else {
            tvShare2.setEnabled(true);
            tvInvite2.setEnabled(true);
            tvShare2.setBackgroundResource(R.drawable.share_red_btn);
            tvInvite2.setBackgroundResource(R.drawable.share_red_invite_btn);
        }

        tvInvite2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, InviteRecordActivity.class);
                intent.putExtra("inviteid", inviteEntity.inviteid);
                startActivity(intent);
            }
        });
    }

    private void initThird(final ShareWinRedResponse.InviteEntity inviteEntity) {
        Log.i("aa", "initThird: " + inviteEntity.rule);

        tvThird.setText(Html.fromHtml(inviteEntity.rule));
        tvRedInfo3.setText("领" + inviteEntity.number + "元红包");
        if (inviteEntity.state == 0) {
            tvShare3.setEnabled(false);
            tvInvite3.setEnabled(false);
            tvShare3.setBackgroundResource(R.drawable.share_red_btn_s);
            tvInvite3.setBackgroundResource(R.drawable.share_red_invite_btn_s);
        } else {
            tvShare3.setEnabled(true);
            tvInvite3.setEnabled(true);
            tvShare3.setBackgroundResource(R.drawable.share_red_btn);
            tvInvite3.setBackgroundResource(R.drawable.share_red_invite_btn);
        }

        tvInvite3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, InviteRecordActivity.class);
                intent.putExtra("inviteid", inviteEntity.inviteid);
                startActivity(intent);
            }
        });
    }

    @Override
    public void getInviteUrlSuccess(GetInviteUrlResponse getInviteUrlResponse) {

    }
}
