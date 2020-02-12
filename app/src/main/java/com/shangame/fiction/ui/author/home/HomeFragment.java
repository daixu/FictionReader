package com.shangame.fiction.ui.author.home;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.PictureConfigResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.author.notice.NoticeListActivity;
import com.shangame.fiction.ui.bookdetail.BookDetailActivity;
import com.shangame.fiction.ui.web.WebViewActivity;

/**
 * 作者平台-首页
 */
public class HomeFragment extends BaseFragment implements HomeContacts.View, View.OnClickListener {

    private HomePresenter mPresenter;

    private LinearLayout mLayoutAnnouncement;
    private LinearLayout mLayoutCheats;

    private ImageView mImgBack;

    private ConvenientBanner convenientBanner;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_home, container, false);
        initView(contentView);
        initPresenter();
        initData();
        initListener();
        return contentView;
    }

    private void initListener() {
        mLayoutAnnouncement.setOnClickListener(this);
        mLayoutCheats.setOnClickListener(this);
        mImgBack.setOnClickListener(this);
    }

    private void initView(View contentView) {
        contentView.findViewById(R.id.img_back).setOnClickListener(this);
        mImgBack = contentView.findViewById(R.id.img_back);
        convenientBanner = contentView.findViewById(R.id.convenientBanner);
        mLayoutAnnouncement = contentView.findViewById(R.id.layout_announcement);
        mLayoutCheats = contentView.findViewById(R.id.layout_cheats);

        TextView tvTitle = contentView.findViewById(R.id.tv_title);
        tvTitle.setText("首页");
    }

    private void initPresenter() {
        mPresenter = new HomePresenter();
        mPresenter.attachView(this);
    }

    private void initData() {
        int userId = UserInfoManager.getInstance(mContext).getUserid();
        mPresenter.getPicConfig(userId);
    }

    @Override
    public void getPicConfigSuccess(final PictureConfigResponse pictureConfigResponse) {
        convenientBanner.setPages(new CBViewHolderCreator() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, pictureConfigResponse.picdata)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        PictureConfigResponse.PicItem item = pictureConfigResponse.picdata.get(position);
                        if (item.bookid == 0) {
                            if (!TextUtils.isEmpty(item.linkurl)) {
                                WebViewActivity.lunchActivity(mActivity, "", item.linkurl);
                            }
                        } else {
                            BookDetailActivity.lunchActivity(mActivity, item.bookid, ApiConstant.ClickType.FROM_CLICK);
                        }
                    }
                })
                .setCanLoop(pictureConfigResponse.picdata.size() > 1);

        if (pictureConfigResponse.picdata.size() > 1) {
            convenientBanner.startTurning(5000);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        convenientBanner.stopTurning();
    }

    @Override
    public void onResume() {
        super.onResume();
        convenientBanner.startTurning(5000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_announcement: {
                Intent intent = new Intent(mContext, NoticeListActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
            }
            break;
            case R.id.layout_cheats: {
                Intent intent = new Intent(mContext, NoticeListActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
            break;
            case R.id.img_back: {
                if (null != getActivity()) {
                    getActivity().finish();
                }
            }
            break;
            default:
                break;
        }
    }
}
