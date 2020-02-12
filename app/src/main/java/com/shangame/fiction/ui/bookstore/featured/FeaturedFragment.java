package com.shangame.fiction.ui.bookstore.featured;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.ad.TTAdManagerHolder;
import com.shangame.fiction.adapter.FeaturedAdapter;
import com.shangame.fiction.core.base.BaseLazyFragment;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.entity.NormalMultipleEntity;
import com.shangame.fiction.net.response.ChoicenessResponse;
import com.shangame.fiction.net.response.OthersLookResponse;
import com.shangame.fiction.net.response.PictureConfigResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.BookInfoEntity;
import com.shangame.fiction.ui.bookstore.BookStoreChannel;
import com.shangame.fiction.ui.bookstore.BookStoreFragment;

import java.util.ArrayList;
import java.util.List;

import static com.shangame.fiction.ad.ADConfig.CSJAdPositionId.READ_PAGE_ID;

public class FeaturedFragment extends BaseLazyFragment implements FeaturedContacts.View {
    private final static int LOAD_MORE_PAGE_SIZE = 7;
    private FeaturedAdapter mAdapter;
    private FeaturedPresenter mPresenter;
    private int updatePage;
    private int loadMorePage = 1;
    private RecyclerView mRecyclerView;

    private SmartRefreshLayout smartRefreshLayout;

    private TTAdNative mTTAdNative;

    public FeaturedFragment() {
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BroadcastAction.JUMP_BOY_FRAGMENT.equals(action)) {
                setCurrentItem(3);
            } else if (BroadcastAction.JUMP_GIRL_FRAGMENT.equals(action)) {
                setCurrentItem(2);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_featured, container, false);
        initView(contentView);
        initFilter();
        return contentView;
    }

    private void initFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.JUMP_BOY_FRAGMENT);
        intentFilter.addAction(BroadcastAction.JUMP_GIRL_FRAGMENT);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, intentFilter);
    }

    private void initView(View contentView) {
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        mTTAdNative = ttAdManager.createAdNative(mContext);
        //申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(mContext);

        mRecyclerView = contentView.findViewById(R.id.recycler_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        smartRefreshLayout = contentView.findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }
        });

        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadMoreData();
            }
        });

        final List<NormalMultipleEntity> data = new ArrayList<>();
        data.add(new NormalMultipleEntity(NormalMultipleEntity.BANNER));
        data.add(new NormalMultipleEntity(NormalMultipleEntity.MENU));
        data.add(new NormalMultipleEntity(NormalMultipleEntity.IMG));
        data.add(new NormalMultipleEntity(NormalMultipleEntity.RECOMMEND));
        data.add(new NormalMultipleEntity(NormalMultipleEntity.BOUTIQUE));
        data.add(new NormalMultipleEntity(NormalMultipleEntity.AD1));
        data.add(new NormalMultipleEntity(NormalMultipleEntity.HIGHLY_RECOMMEND));
        data.add(new NormalMultipleEntity(NormalMultipleEntity.SERIAL));
        data.add(new NormalMultipleEntity(NormalMultipleEntity.BOY_GIRL));
        data.add(new NormalMultipleEntity(NormalMultipleEntity.HOT_SEARCH));
        data.add(new NormalMultipleEntity(NormalMultipleEntity.COMPLETE));
        data.add(new NormalMultipleEntity(NormalMultipleEntity.LABEL));
        data.add(new NormalMultipleEntity(NormalMultipleEntity.AD2));
        data.add(new NormalMultipleEntity(NormalMultipleEntity.OTHER));

        mAdapter = new FeaturedAdapter(data);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void scrollToTop() {
        Log.e("hhh", "scrollToTop");
        if (null != mRecyclerView) {
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    /**
     * 加载feed广告
     */
    private void loadListAd() {
        //feed广告请求类型参数
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(READ_PAGE_ID)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(640, 320)
                .setAdCount(3)
                .build();
        //调用feed广告异步请求接口
        mTTAdNative.loadFeedAd(adSlot, new TTAdNative.FeedAdListener() {
            @Override
            public void onError(int code, String message) {

            }

            @Override
            public void onFeedAdLoad(List<TTFeedAd> ads) {
                mAdapter.setAd1List(ads);
                mAdapter.setAd2List(ads);
            }
        });
    }

    @Override
    protected void onLazyLoad() {
        initPresenter();
        smartRefreshLayout.autoRefresh();
        loadMoreData();
        loadListAd();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != mPresenter) {
            mPresenter.detachView();
        }
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }

    public void setCurrentItem(int position) {
        BookStoreFragment bookStoreFragment = (BookStoreFragment) getParentFragment();
        if (null != bookStoreFragment) {
            bookStoreFragment.setCurrentItem(position);
        }
    }

    private void initPresenter() {
        mPresenter = new FeaturedPresenter();
        mPresenter.attachView(this);
    }

    private void loadData() {
        int userId = UserInfoManager.getInstance(mContext).getUserid();
        mPresenter.getFeaturedData(userId, updatePage++);
        mPresenter.getPictureConfig(userId, BookStoreChannel.CHOICENESS);

        loadListAd();
    }

    private void loadMoreData() {
        int userId = UserInfoManager.getInstance(mContext).getUserid();
        mPresenter.othersLookData(userId, BookStoreChannel.CHOICENESS, loadMorePage, LOAD_MORE_PAGE_SIZE, 0);
    }

    @Override
    public void getOthersLookDataSuccess(OthersLookResponse response) {
        if (mRecyclerView.getVisibility() == View.GONE) {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        smartRefreshLayout.finishLoadMore();
        List<BookInfoEntity> list = response.pagedata;
        loadMorePage++;
        mAdapter.setOtherList(list);
    }

    @Override
    public void getFeaturedDataSuccess(ChoicenessResponse response) {
        if (mRecyclerView.getVisibility() == View.GONE) {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        smartRefreshLayout.finishRefresh();
        mAdapter.setFeaturedData(response);
    }

    @Override
    public void getPictureConfigSuccess(PictureConfigResponse response) {
        if (mRecyclerView.getVisibility() == View.GONE) {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        List<PictureConfigResponse.PicItem> list = response.picdata;
        mAdapter.setBannerList(list);
    }
}
