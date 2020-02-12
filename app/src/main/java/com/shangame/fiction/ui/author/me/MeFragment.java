package com.shangame.fiction.ui.author.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.net.response.AuthorInfoResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.author.me.info.AuthorDataActivity;
import com.shangame.fiction.ui.author.me.info.FinanceInfoActivity;
import com.shangame.fiction.ui.author.me.info.SignInfoActivity;
import com.shangame.fiction.widget.GlideApp;

public class MeFragment extends BaseFragment implements View.OnClickListener, MeContacts.View {

    private ImageView mImgBack;
    private RoundedImageView mImageAvatar;
    private TextView mTextName;
    private RelativeLayout mLayoutAuthorData;
    private RelativeLayout mLayoutSignInfo;
    private RelativeLayout mLayoutFinanceInfo;

    private MePresenter mPresenter;

    private AuthorInfoResponse mAuthorInfo;

    public MeFragment() {
        // Required empty public constructor
    }

    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_me, container, false);
        initPresenter();
        initView(contentView);
        initListener();
        return contentView;
    }

    private void initPresenter() {
        mPresenter = new MePresenter();
        mPresenter.attachView(this);
    }

    private void initView(View contentView) {
        mImgBack = contentView.findViewById(R.id.img_back);
        contentView.findViewById(R.id.tv_title).setVisibility(View.GONE);
        contentView.findViewById(R.id.view_title_line).setVisibility(View.GONE);

        mImageAvatar = contentView.findViewById(R.id.image_avatar);
        mTextName = contentView.findViewById(R.id.text_name);
        mLayoutAuthorData = contentView.findViewById(R.id.layout_author_data);
        mLayoutSignInfo = contentView.findViewById(R.id.layout_sign_info);
        mLayoutFinanceInfo = contentView.findViewById(R.id.layout_finance_info);
    }

    private void initListener() {
        mImgBack.setOnClickListener(this);
        mLayoutAuthorData.setOnClickListener(this);
        mLayoutSignInfo.setOnClickListener(this);
        mLayoutFinanceInfo.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private void loadData() {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        mPresenter.getAuthorInfo(userId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back: {
                if (null != getActivity()) {
                    getActivity().finish();
                }
            }
            break;
            case R.id.layout_author_data: {
                Intent intent = new Intent(mContext, AuthorDataActivity.class);
                intent.putExtra("authorInfo", mAuthorInfo);
                startActivity(intent);
            }
            break;
            case R.id.layout_sign_info: {
                Intent intent = new Intent(mContext, SignInfoActivity.class);
                intent.putExtra("authorInfo", mAuthorInfo);
                startActivity(intent);
            }
            break;
            case R.id.layout_finance_info: {
                Intent intent = new Intent(mContext, FinanceInfoActivity.class);
                startActivity(intent);
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void getAuthorInfoSuccess(AuthorInfoResponse response) {
        mAuthorInfo = response;
        mTextName.setText(response.penname);
        // ImageLoader.with(mActivity).loadUserIcon(mImageAvatar, response.headimgurl, R.drawable.default_head);

        GlideApp.with(mContext)
                .load(response.headimgurl)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.drawable.default_head)
                .into(mImageAvatar);
    }

    @Override
    public void getAuthorInfoFailure(String msg) {
    }
}
