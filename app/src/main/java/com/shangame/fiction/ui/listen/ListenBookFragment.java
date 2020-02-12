package com.shangame.fiction.ui.listen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.BoyListenAdapter;
import com.shangame.fiction.adapter.DiscountAreaAdapter;
import com.shangame.fiction.adapter.EndListenAdapter;
import com.shangame.fiction.adapter.GirlListenAdapter;
import com.shangame.fiction.adapter.MustListenAdapter;
import com.shangame.fiction.adapter.OtherListenAdapter;
import com.shangame.fiction.adapter.SerializedLatestAdapter;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.net.response.AlbumDataResponse;
import com.shangame.fiction.net.response.AlbumModuleResponse;
import com.shangame.fiction.net.response.PictureConfigResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.booklib.BookLibraryActivity;
import com.shangame.fiction.ui.bookstore.BookStoreChannel;
import com.shangame.fiction.ui.listen.menu.ListenMenuActivity;
import com.shangame.fiction.ui.listen.more.ListenMoreActivity;
import com.shangame.fiction.ui.search.SearchBookActivity;
import com.shangame.fiction.ui.web.WebViewActivity;
import com.shangame.fiction.widget.ad.AdViewPagerBanner;

import java.util.ArrayList;
import java.util.List;

/**
 * 听书Fragment
 *
 * @author hhh
 */
public class ListenBookFragment extends BaseFragment implements View.OnClickListener, ListenBookContacts.View {

    private SmartRefreshLayout mSmartRefreshLayout;
    private NestedScrollView mNestedScrollView;
    private ListenBookPresenter mPresenter;
    private AdViewPagerBanner adViewPagerBanner;
    private View bannerShadow;
    private View kindLayout;

    private MustListenAdapter mustListenAdapter;
    private DiscountAreaAdapter mDiscountAdapter;
    private EndListenAdapter mEndListenAdapter;
    private BoyListenAdapter mBoyListenAdapter;
    private GirlListenAdapter mGirlListenAdapter;
    private SerializedLatestAdapter mSerializedLatestAdapter;
    private OtherListenAdapter mOtherListenAdapter;
    private List<AlbumModuleResponse.CarDataBean> mustListenList = new ArrayList<>();
    private List<AlbumModuleResponse.DisDataBean> mDisDataList = new ArrayList<>();
    private List<AlbumModuleResponse.StateDataBean> mStateDataList = new ArrayList<>();
    private List<AlbumModuleResponse.BoyDataBean> mBoyDataList = new ArrayList<>();
    private List<AlbumModuleResponse.GirlDataBean> mGirlDataList = new ArrayList<>();
    private List<AlbumModuleResponse.HotDataBean> mHotDataList = new ArrayList<>();
    private List<AlbumDataResponse.PageDataBean> mPageDataList = new ArrayList<>();

    private int updatePage;
    private int pageIndex = 1;
    private int type;

    public ListenBookFragment() {
        // Required empty public constructor
    }

    public static ListenBookFragment newInstance(int type) {
        ListenBookFragment fragment = new ListenBookFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    public void scrollToTop() {
        mNestedScrollView.scrollTo(0, 0);
    }

    @Override
    public void getAlbumModuleSuccess(AlbumModuleResponse response) {
        kindLayout.setVisibility(View.VISIBLE);
        mSmartRefreshLayout.finishRefresh();
        if (null != response) {
            if (null != response.cardata) {
                mustListenList.clear();
                mustListenList.addAll(response.cardata);
                mustListenAdapter.setNewData(mustListenList);
            }

            if (null != response.disdata) {
                mDisDataList.clear();
                List<AlbumModuleResponse.DisDataBean> list = new ArrayList<>();
                for (int i = 0; i < response.disdata.size(); i++) {
                    AlbumModuleResponse.DisDataBean dataBean = response.disdata.get(i);
                    if (i == 0) {
                        dataBean.type = 1;
                    } else {
                        dataBean.type = 2;
                    }
                    list.add(dataBean);
                }
                mDisDataList.addAll(list);
                mDiscountAdapter.setNewData(mDisDataList);
            }

            if (null != response.statedata) {
                mStateDataList.clear();
                mStateDataList.addAll(response.statedata);
                mEndListenAdapter.setNewData(mStateDataList);
            }

            if (null != response.boydata) {
                mBoyDataList.clear();
                mBoyDataList.addAll(response.boydata);
                mBoyListenAdapter.setNewData(mBoyDataList);
            }

            if (null != response.grildata) {
                mGirlDataList.clear();
                mGirlDataList.addAll(response.grildata);
                mGirlListenAdapter.setNewData(mGirlDataList);
            }

            if (null != response.hotdata) {
                mHotDataList.clear();
                List<AlbumModuleResponse.HotDataBean> list = new ArrayList<>();
                for (int i = 0; i < response.hotdata.size(); i++) {
                    AlbumModuleResponse.HotDataBean dataBean = response.hotdata.get(i);
                    if (i == 0) {
                        dataBean.type = 1;
                    } else {
                        dataBean.type = 2;
                    }
                    list.add(dataBean);
                }
                mHotDataList.addAll(list);
                mSerializedLatestAdapter.setNewData(mHotDataList);
            }
        }
    }

    @Override
    public void getAlbumModuleFailure(String msg) {
        mSmartRefreshLayout.finishRefresh();
        mSmartRefreshLayout.finishLoadMore();
    }

    @Override
    public void getAlbumDataSuccess(AlbumDataResponse response) {
        mSmartRefreshLayout.finishRefresh();
        if (null != response) {
            if (pageIndex == 1) {
                mPageDataList.clear();
            }
            mSmartRefreshLayout.finishLoadMore();
            pageIndex++;
            mPageDataList.addAll(response.pagedata);
            mOtherListenAdapter.setNewData(mPageDataList);
        }
    }

    @Override
    public void getAlbumDataFailure(String msg) {
        mSmartRefreshLayout.finishRefresh();
        mSmartRefreshLayout.finishLoadMore();
    }

    @Override
    public void getPictureConfigSuccess(PictureConfigResponse response) {
        adViewPagerBanner.setPicItemList(this, response.picdata);
        if (adViewPagerBanner.getPicItemListSize() > 0) {
            bannerShadow.setVisibility(View.VISIBLE);
            adViewPagerBanner.startAutoPlay();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (adViewPagerBanner != null) {
            if (isVisibleToUser) {
                adViewPagerBanner.startAutoPlay();
            } else {
                adViewPagerBanner.stopAutoPlay();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_listen_book, container, false);
        initView(contentView);
        initListener(contentView);
        initBanner(contentView);
        return contentView;
    }

    private void initView(View contentView) {
        mSmartRefreshLayout = contentView.findViewById(R.id.smart_refresh_layout);
        mNestedScrollView = contentView.findViewById(R.id.nested_scroll_view);
        kindLayout = contentView.findViewById(R.id.kindLayout);

        RelativeLayout layoutTitle = contentView.findViewById(R.id.layout_title);
        View fakeStatusBar = contentView.findViewById(R.id.fake_status_bar);
        if (type == 1) {
            layoutTitle.setVisibility(View.VISIBLE);
            fakeStatusBar.setVisibility(View.VISIBLE);
        } else {
            layoutTitle.setVisibility(View.GONE);
            fakeStatusBar.setVisibility(View.GONE);
        }

        RecyclerView recyclerMustListen = contentView.findViewById(R.id.recycler_must_listen);
        recyclerMustListen.setLayoutManager(new GridLayoutManager(mContext, 3));
        mustListenAdapter = new MustListenAdapter(R.layout.item_must_listen, mustListenList);
        recyclerMustListen.setAdapter(mustListenAdapter);

        RecyclerView recyclerDiscountArea = contentView.findViewById(R.id.recycler_discount_area);
        recyclerDiscountArea.setLayoutManager(new GridLayoutManager(mContext, 3));
        mDiscountAdapter = new DiscountAreaAdapter(mDisDataList);
        mDiscountAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                if (mDiscountAdapter.getItemViewType(position) == 1) {
                    return 3;
                }
                return 1;
            }
        });
        recyclerDiscountArea.setAdapter(mDiscountAdapter);

        RecyclerView recyclerEndListen = contentView.findViewById(R.id.recycler_end_listen);
        recyclerEndListen.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mEndListenAdapter = new EndListenAdapter(R.layout.item_end_listen, mStateDataList);
        recyclerEndListen.setAdapter(mEndListenAdapter);

        RecyclerView recyclerBoyBoutique = contentView.findViewById(R.id.recycler_boy_boutique);
        recyclerBoyBoutique.setLayoutManager(new GridLayoutManager(mContext, 3));
        mBoyListenAdapter = new BoyListenAdapter(R.layout.item_must_listen, mBoyDataList);
        recyclerBoyBoutique.setAdapter(mBoyListenAdapter);

        RecyclerView recyclerGirlBoutique = contentView.findViewById(R.id.recycler_girl_boutique);
        recyclerGirlBoutique.setLayoutManager(new GridLayoutManager(mContext, 3));
        mGirlListenAdapter = new GirlListenAdapter(R.layout.item_must_listen, mGirlDataList);
        recyclerGirlBoutique.setAdapter(mGirlListenAdapter);

        RecyclerView recyclerSerializedLatest = contentView.findViewById(R.id.recycler_serialized_latest);
        recyclerSerializedLatest.setLayoutManager(new GridLayoutManager(mContext, 3));
        mSerializedLatestAdapter = new SerializedLatestAdapter(mHotDataList);

        mSerializedLatestAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                if (mSerializedLatestAdapter.getItemViewType(position) == 1) {
                    return 3;
                }
                return 1;
            }
        });
        recyclerSerializedLatest.setAdapter(mSerializedLatestAdapter);

        RecyclerView recyclerOtherListen = contentView.findViewById(R.id.recycler_other_listen);
        recyclerOtherListen.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mOtherListenAdapter = new OtherListenAdapter(R.layout.item_end_listen, mPageDataList);
        recyclerOtherListen.setAdapter(mOtherListenAdapter);
    }

    private void initListener(View contentView) {
        contentView.findViewById(R.id.tv_serialized_latest_more).setOnClickListener(this);
        contentView.findViewById(R.id.tv_girl_boutique_more).setOnClickListener(this);
        contentView.findViewById(R.id.tv_boy_boutique_more).setOnClickListener(this);
        contentView.findViewById(R.id.tv_end_listen_more).setOnClickListener(this);
        contentView.findViewById(R.id.tv_must_listen_more).setOnClickListener(this);
        contentView.findViewById(R.id.tv_discount_area_more).setOnClickListener(this);

        contentView.findViewById(R.id.menu_classify_listen).setOnClickListener(this);
        contentView.findViewById(R.id.menu_rank_listen).setOnClickListener(this);
        contentView.findViewById(R.id.menu_complete_listen).setOnClickListener(this);
        contentView.findViewById(R.id.menu_end_listen).setOnClickListener(this);
        contentView.findViewById(R.id.menu_free_listen).setOnClickListener(this);
        contentView.findViewById(R.id.img_search).setOnClickListener(this);

        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        });

        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadMoreData();
            }
        });
        mustListenAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mustListenAdapterItemClick(position);
            }
        });

        mDiscountAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                disDataAdapterItemClick(position);
            }
        });

        mEndListenAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                endListenAdapterItemClick(position);
            }
        });

        mBoyListenAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                boyListenAdapterItemClick(position);
            }
        });

        mGirlListenAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                girlListenAdapterItemClick(position);
            }
        });

        mSerializedLatestAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                serializedLatestAdapterItemClick(position);
            }
        });

        mOtherListenAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                otherListenAdapterItemClick(position);
            }
        });
    }

    private void initBanner(View view) {
        bannerShadow = view.findViewById(R.id.bannerShadow);
        adViewPagerBanner = view.findViewById(R.id.adViewPagerBanner);
        adViewPagerBanner.setOnItemPageClickListener(new AdViewPagerBanner.OnItemPageClickListener() {
            @Override
            public void onItemPageClick(int position, PictureConfigResponse.PicItem adItem) {
                if (adItem == null) {
                    return;
                }
                if (adItem.bookid == 0) {
                    WebViewActivity.lunchActivity(mActivity, "", adItem.linkurl);
                } else {
                    ARouter.getInstance()
                            .build("/ss/listen/detail")
                            .withInt("albumId", adItem.bookid)
                            .navigation();
                }
            }
        });
    }

    private void initData() {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        mPresenter.getAlbumModule(userId, updatePage++);
        mPresenter.getPictureConfig(userId, BookStoreChannel.LISTEN);
    }

    private void loadMoreData() {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        mPresenter.getAlbumData(userId, pageIndex, 7, 0);
    }

    private void mustListenAdapterItemClick(int position) {
        if (null != mustListenList && mustListenList.size() > position) {
            AlbumModuleResponse.CarDataBean bean = mustListenList.get(position);
            if (null != bean) {
                jumpToDetail(bean.albumid);
            }
        }
    }

    private void disDataAdapterItemClick(int position) {
        if (null != mDisDataList && mDisDataList.size() > position) {
            AlbumModuleResponse.DisDataBean bean = mDisDataList.get(position);
            if (null != bean) {
                jumpToDetail(bean.albumid);
            }
        }
    }

    private void endListenAdapterItemClick(int position) {
        if (null != mStateDataList && mStateDataList.size() > position) {
            AlbumModuleResponse.StateDataBean bean = mStateDataList.get(position);
            if (null != bean) {
                jumpToDetail(bean.albumid);
            }
        }
    }

    private void boyListenAdapterItemClick(int position) {
        if (null != mBoyDataList && mBoyDataList.size() > position) {
            AlbumModuleResponse.BoyDataBean bean = mBoyDataList.get(position);
            if (null != bean) {
                jumpToDetail(bean.albumid);
            }
        }
    }

    private void girlListenAdapterItemClick(int position) {
        if (null != mGirlDataList && mGirlDataList.size() > position) {
            AlbumModuleResponse.GirlDataBean bean = mGirlDataList.get(position);
            if (null != bean) {
                jumpToDetail(bean.albumid);
            }
        }
    }

    private void serializedLatestAdapterItemClick(int position) {
        if (null != mHotDataList && mHotDataList.size() > position) {
            AlbumModuleResponse.HotDataBean bean = mHotDataList.get(position);
            if (null != bean) {
                jumpToDetail(bean.albumid);
            }
        }
    }

    private void otherListenAdapterItemClick(int position) {
        if (null != mPageDataList && mPageDataList.size() > position) {
            AlbumDataResponse.PageDataBean bean = mPageDataList.get(position);
            if (null != bean) {
                jumpToDetail(bean.albumid);
            }
        }
    }

    private void jumpToDetail(int albumId) {
        ARouter.getInstance()
                .build("/ss/listen/detail")
                .withInt("albumId", albumId)
                .navigation();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt("type");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPresenter();
        initData();
        loadMoreData();
    }

    private void initPresenter() {
        mPresenter = new ListenBookPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_classify_listen: {
                Intent intent = new Intent(mContext, BookLibraryActivity.class);
                intent.putExtra("BookStoreChannel", BookStoreChannel.LISTEN);
                startActivity(intent);
            }
            break;
            case R.id.menu_rank_listen: {
                ARouter.getInstance()
                        .build("/ss/book/rank")
                        .navigation();
            }
            break;
            case R.id.menu_complete_listen: {
                Intent intent = new Intent(mContext, ListenMenuActivity.class);
                intent.putExtra("status", 1);
                startActivity(intent);
            }
            break;
            case R.id.menu_end_listen: {
                Intent intent = new Intent(mContext, ListenMenuActivity.class);
                intent.putExtra("status", 2);
                startActivity(intent);
            }
            break;
            case R.id.menu_free_listen: {
                Intent intent = new Intent(mContext, ListenMenuActivity.class);
                intent.putExtra("status", 3);
                startActivity(intent);
            }
            break;
            case R.id.tv_must_listen_more: {
                jumpToMore(1);
            }
            break;
            case R.id.tv_discount_area_more: {
                jumpToMore(2);
            }
            break;
            case R.id.tv_end_listen_more: {
                jumpToMore(6);
            }
            break;
            case R.id.tv_boy_boutique_more: {
                jumpToMore(3);
            }
            break;
            case R.id.tv_girl_boutique_more: {
                jumpToMore(4);
            }
            break;
            case R.id.tv_serialized_latest_more: {
                jumpToMore(5);
            }
            break;
            case R.id.img_search: {
                Intent intent = new Intent(mContext, SearchBookActivity.class);
                startActivity(intent);
            }
            break;
            default:
                break;
        }
    }

    private void jumpToMore(int moduleId) {
        Intent intent = new Intent(mContext, ListenMoreActivity.class);
        intent.putExtra("moduleId", moduleId);
        startActivity(intent);
    }
}
