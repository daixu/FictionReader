package com.shangame.fiction.ui.sales.partner;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.lxj.xpopup.XPopup;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.utils.StatusBarUtil;
import com.shangame.fiction.net.response.AgentDetailResp;
import com.shangame.fiction.net.response.AgentIdInfoResp;
import com.shangame.fiction.net.response.WithdrawResp;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.sales.withdraw.FinanceDataWindow;
import com.shangame.fiction.widget.GlideApp;
import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * 合伙人管理首页
 */
@Route(path = "/ss/sales/partner/manage/home")
public class PartnerManageHomeActivity extends BaseActivity implements View.OnClickListener, PartnerManageHomeContacts.View {
    private PartnerManageHomePresenter mPresenter;
    private FrameLayout mLayoutContent;

    private ConstraintLayout mLayoutPartnerType;
    private ConstraintLayout mLayoutShare;
    private RoundedImageView mImageAvatar;
    private TextView mTextName;
    private TextView mTextUserId;
    private ImageView mImageUserType;
    private TextView mTextInvitationCode;
    private TextView mTextMemberCount;
    private TextView mTextTotalIncome;
    private TextView mTextYesterdayIncome;
    private ImageView mImageUpgrade;
    private ImageView mImgRewardRedPackage;
    private ImageView mImagePromotion;

    private AgentIdInfoResp.DataBean mDataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_manage_home);
        ARouter.getInstance().inject(this);
        setStatusBar();
        initView();
        initPresenter();
        initListener();
    }

    private void setStatusBar() {
        View viewNeedOffset = findViewById(R.id.view_need_offset);
        StatusBarUtil.setTranslucentForImageView(this, 0, viewNeedOffset);
    }

    private void initView() {
        mLayoutContent = findViewById(R.id.layout_content);
        mLayoutShare = findViewById(R.id.layout_share);

        mLayoutPartnerType = findViewById(R.id.layout_partner_type);
        mImageAvatar = findViewById(R.id.image_avatar);
        mTextName = findViewById(R.id.text_name);
        mTextUserId = findViewById(R.id.text_user_id);
        mImageUserType = findViewById(R.id.image_user_type);
        mTextInvitationCode = findViewById(R.id.text_invitation_code);
        mTextMemberCount = findViewById(R.id.text_member_count);
        mTextTotalIncome = findViewById(R.id.text_total_income);
        mTextYesterdayIncome = findViewById(R.id.text_yesterday_income);
        mImageUpgrade = findViewById(R.id.image_upgrade);
        mImagePromotion = findViewById(R.id.image_promotion);

        mImgRewardRedPackage = findViewById(R.id.img_reward_red_package);
        Glide.with(this)
                .asGif()
                .load(R.drawable.image_reward_red_package)
                .into(mImgRewardRedPackage);
    }

    private void initPresenter() {
        mPresenter = new PartnerManageHomePresenter();
        mPresenter.attachView(this);
    }

    private void initListener() {
        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.layout_member_manage).setOnClickListener(this);
        findViewById(R.id.image_modify_info).setOnClickListener(this);
        findViewById(R.id.tv_account_overview).setOnClickListener(this);
        findViewById(R.id.image_share).setOnClickListener(this);
        findViewById(R.id.layout_customer_service).setOnClickListener(this);
        mLayoutShare.setOnClickListener(this);
        findViewById(R.id.text_invitation_code).setOnClickListener(this);
        findViewById(R.id.layout_account_overview).setOnClickListener(this);
        mImgRewardRedPackage.setOnClickListener(this);
        mImageUpgrade.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private void initData() {
        long userId = UserInfoManager.getInstance(this).getUserid();
        mPresenter.getAgentIdInfo(userId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.layout_member_manage:
                if (null != mDataBean) {
                    ARouter.getInstance()
                            .build("/ss/sales/partner/manage")
                            .withInt("agentCount", mDataBean.agentCount)
                            .withInt("memberCount", mDataBean.userCount)
                            .navigation();
                }
                break;
            case R.id.image_modify_info:
                getAgentIdDetail();
                break;
            case R.id.image_share:
            case R.id.layout_share:
                ARouter.getInstance()
                        .build("/ss/share/poster")
                        .navigation();
                break;
            case R.id.text_invitation_code:
                if (null != mDataBean) {
                    String copyStr = mDataBean.agentId + "";
                    copy(copyStr);
                }
                break;
            case R.id.image_upgrade:
                upgrade();
                break;
            case R.id.layout_account_overview:
                ARouter.getInstance()
                        .build("/ss/sales/account/overview")
                        .navigation();
                break;
            case R.id.img_reward_red_package:
                double orderPrice = 0;
                if (null != mDataBean) {
                    orderPrice = mDataBean.orderPrice;
                }
                ARouter.getInstance()
                        .build("/ss/sales/reward/red/envelope")
                        .withDouble("orderPrice", orderPrice)
                        .navigation();
                break;
            case R.id.layout_customer_service:
                ARouter.getInstance()
                        .build("/ss/customer/service")
                        .navigation();
                break;
            default:
                break;
        }
    }

    private void getAgentIdDetail() {
        UserInfo userInfo = UserInfoManager.getInstance(this).getUserInfo();
        int agentId = 0;
        if (null != userInfo) {
            agentId = userInfo.agentId;
        }
        mPresenter.getAgentIdDetail(agentId);
    }

    private void copy(String copyStr) {
        //获取剪贴板管理器
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", copyStr);
        // 将ClipData内容放到系统剪贴板里。
        if (null != cm) {
            cm.setPrimaryClip(mClipData);
        }
    }

    private void upgrade() {
        UserInfo userInfo = UserInfoManager.getInstance(this).getUserInfo();
        int agentGrade = 0;
        if (null != userInfo) {
            agentGrade = userInfo.agentGrade;
        }
        if (agentGrade == 3) {
            int buyDisplay = 0;
            if (null != mDataBean) {
                buyDisplay = mDataBean.butdisplay;
            }
            ARouter.getInstance()
                    .build("/ss/sales/partner/upgrade/gold")
                    .withInt("buyDisplay", buyDisplay)
                    .navigation();
        } else if (agentGrade == 2) {
            ARouter.getInstance()
                    .build("/ss/sales/partner/upgrade/diamond")
                    .navigation();
        }
    }

    @Override
    public void getAgentIdInfoSuccess(AgentIdInfoResp.DataBean dataBean) {
        Log.e("hhh", "getAgentIdInfoSuccess dataBean= " + dataBean);
        mDataBean = dataBean;
        mLayoutContent.setVisibility(View.VISIBLE);

        GlideApp.with(this)
                .load(dataBean.headimgurl)
                .placeholder(R.drawable.default_head)
                .centerCrop()
                .into(mImageAvatar);

        mTextName.setText(dataBean.agentName);
        String strUserId = "ID：" + dataBean.userid;
        mTextUserId.setText(strUserId);
        String strInvitationCode = "邀请码: " + dataBean.agentId;
        mTextInvitationCode.setText(strInvitationCode);
        String strMemberCount = (dataBean.userCount + dataBean.agentCount) + "人";
        mTextMemberCount.setText(strMemberCount);

        String strTotalIncome = dataBean.sumPrice + "";
        mTextTotalIncome.setText(strTotalIncome);
        String strYesterdayIncome = dataBean.yesterPrice + "";
        mTextYesterdayIncome.setText(strYesterdayIncome);

        UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
        if (null != userInfo) {
            userInfo.agentId = dataBean.agentId;
        }
        UserInfoManager.getInstance(mContext).saveUserInfo(userInfo);

        setView(dataBean);
    }

    private void setView(AgentIdInfoResp.DataBean dataBean) {
        switch (dataBean.agentGrade) {
            case 1:
                mTextName.setTextColor(Color.parseColor("#EACAA0"));
                mTextInvitationCode.setTextColor(Color.parseColor("#D8D8D8"));
                mImageUserType.setImageResource(R.drawable.icon_diamond_partner);
                mLayoutPartnerType.setBackgroundResource(R.drawable.image_diamond_medal_bg);
                mTextUserId.setBackgroundResource(R.drawable.bg_rounded_diamond_small);

                mImageUpgrade.setVisibility(View.GONE);

                mImagePromotion.setImageResource(R.drawable.icon_partner_1);
                break;
            case 2:
                mTextName.setTextColor(Color.parseColor("#9C7D5E"));
                mTextInvitationCode.setTextColor(Color.parseColor("#9B7D5D"));
                mImageUserType.setImageResource(R.drawable.icon_gold_partner);
                mLayoutPartnerType.setBackgroundResource(R.drawable.image_gold_medal_bg);
                mTextUserId.setBackgroundResource(R.drawable.bg_rounded_gold_small);

                mImageUpgrade.setVisibility(View.VISIBLE);
                mImageUpgrade.setImageResource(R.drawable.image_upgrade_diamond);

                mImagePromotion.setImageResource(R.drawable.icon_partner_2);
                break;
            case 3:
                mTextName.setTextColor(Color.parseColor("#FFFFFF"));
                mTextInvitationCode.setTextColor(Color.parseColor("#EAEAEA"));
                mImageUserType.setImageResource(R.drawable.icon_silver_partner);
                mLayoutPartnerType.setBackgroundResource(R.drawable.image_silver_medal_bg);
                mTextUserId.setBackgroundResource(R.drawable.bg_rounded_silver_small);

                mImageUpgrade.setVisibility(View.VISIBLE);
                mImageUpgrade.setImageResource(R.drawable.image_upgrade_gold);

                mImagePromotion.setImageResource(R.drawable.icon_partner_3);
                break;
            default:
                break;
        }
    }

    @Override
    public void getAgentIdInfoFailure(String msg) {
        Log.e("hhh", "getAgentIdInfoFailure msg= " + msg);
        mLayoutContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void withdrawSuccess(WithdrawResp.DataBean dataBean) {
        Toast.makeText(this, "申请成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void withdrawFailure(String msg) {
        Log.e("hhh", "withdrawFailure msg= " + msg);
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getAgentIdDetailSuccess(AgentDetailResp.DataBean dataBean) {
        Log.e("hhh", "getAgentIdDetailSuccess dataBean= " + dataBean);
        new XPopup.Builder(this)
                .moveUpToKeyboard(false)
                .asCustom(new FinanceDataWindow(this, dataBean))
                .show();
    }

    @Override
    public void getAgentIdDetailFailure(String msg) {
        Log.e("hhh", "getAgentIdDetailFailure msg= " + msg);
        new XPopup.Builder(this)
                .moveUpToKeyboard(false)
                .asCustom(new FinanceDataWindow(this, null))
                .show();
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.<T>bindToLifecycle();
    }
}
