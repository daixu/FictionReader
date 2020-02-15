package com.shangame.fiction.ui.bookstore;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.ad.ADConfig;
import com.shangame.fiction.ad.TTAdManagerHolder;
import com.shangame.fiction.core.base.BaseLazyFragment;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.AdBean;
import com.shangame.fiction.net.response.FriendReadResponse;
import com.shangame.fiction.net.response.MaleChannelResponse;
import com.shangame.fiction.net.response.PictureConfigResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.author.home.NetworkImageHolderView;
import com.shangame.fiction.ui.bookdetail.BookDetailActivity;
import com.shangame.fiction.ui.booklib.BookLibraryActivity;
import com.shangame.fiction.ui.booklib.BookLibraryDetailActivity;
import com.shangame.fiction.ui.booklib.BookLibraryType;
import com.shangame.fiction.ui.bookrack.MagicIndicatorAdapter;
import com.shangame.fiction.ui.web.WebViewActivity;
import com.shangame.fiction.widget.FriendReadItemDecoration;
import com.shangame.fiction.widget.RecommendSpaceItemDecoration;
import com.shangame.fiction.widget.SpaceItemDecoration;
import com.shangame.fiction.widget.ad.AdViewPagerBanner;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.List;


/**
 * 女生
 * Create by Speedy on 2018/7/25
 */
public class GirlPageFragment extends BaseLazyFragment implements View.OnClickListener, BookStoreContacts.View {

    private static final String TAG = "GirlPageFragment";
    private final static int LOAD_MORE_PAGE_SIZE = 6;
    private SmartRefreshLayout smartRefreshLayout;
    private AdViewPagerBanner adViewPagerBanner;
    private View bannerShadow;
    private View kindLayout;
    private RecyclerView mustReadListView;
    private RecyclerView editorRecommendRecyclerView;
    private RecyclerView hotSearchRecyclerView;
    private RecyclerView finishRecyclerView;
    private RecyclerView ancientRecyclerView;
    private RecyclerView modernRecyclerView;
    private RecyclerView friendReadRecyclerView;
    private List<View> rankViewList;
    private RecyclerView hotRankRecyclerView;
    private RecyclerView clickRankReadRecyclerView;
    private RecyclerView collectReadRecyclerView;
    private BookStoreRankAdapter hotRankRankAdapter;
    private BookStoreRankAdapter clickRankRankAdapter;
    private BookStoreRankAdapter collectReadRankAdapter;
    private MagicIndicator magicIndicator;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private HighlyRecommendAdapter highlyRecommendAdapter;
    private EditRecommendAdapter editRecommendAdapter;
    private BookWithTitleAdapter hotSearchAdapter;
    private BookWithTitleAdapter bookFinishAdapter;
    private BookWithContentAdapter ancientAdapter;
    private BookWithContentAdapter modernAdapter;
    private FriendReadAdapter friendReadAdapter;
    private DividerItemDecoration dividerItemDecoration;
    private BookStorePresenter bookStorePresenter;
    private int updatePage = 1;
    private int loadMorePage = 1;
    private NestedScrollView nestedScrollView;

    private FrameLayout adContainer1;
    private FrameLayout adContainer2;

    // 头条穿山甲
    private List<TTNativeExpressAd> mTTAdList;

    private boolean hidden;

    private View mLayoutTop;
    private ConvenientBanner mConvenientBanner;
    private List<PictureConfigResponse.PicItem> mList = new ArrayList<>();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        hidden = getUserVisibleHint();
        Log.e("hhh", "hidden= " + hidden);
        if (mConvenientBanner != null) {
            if (isVisibleToUser) {
                mConvenientBanner.startTurning(5000);
            } else {
                mConvenientBanner.stopTurning();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_girl_page, container, false);
        initSmartRefreshLayout(view);
        initBanner(view);
        initMenu(view);
        initHighlyRecommend(view);
        initEditorRecommend(view);
        int verify = AdBean.getInstance().getVerify();
        if (verify == 0) {
            initCsjAd(view);
        }
        initHotSearch(view);
        initBookFinish(view);
        initRank(view);
        initAncient(view);
        initModern(view);
        initFriendRead(view);
        return view;
    }

    private void initSmartRefreshLayout(View view) {
        smartRefreshLayout = view.findViewById(R.id.smartRefreshLayout);
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
    }

    private void initBanner(View view) {
        mLayoutTop = view.findViewById(R.id.layout_top);
        mConvenientBanner = view.findViewById(R.id.convenientBanner);

        mConvenientBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("hhh", "girl onPageSelected position= " + position);
                if (null != mList) {
                    PictureConfigResponse.PicItem picItem = mList.get(position);
                    if (TextUtils.isEmpty(picItem.color) || picItem.color.trim().length() != 7) {
                        mLayoutTop.setBackgroundColor(Color.parseColor("#D0B7BA"));
                        if (null != getParentFragment()) {
                            if (hidden) {
                                ((BookStoreFragment) getParentFragment()).setAppBarColor("#D0B7BA");
                            }
                        }
                    } else {
                        mLayoutTop.setBackgroundColor(Color.parseColor(picItem.color));
                        if (null != getParentFragment()) {
                            if (hidden) {
                                ((BookStoreFragment) getParentFragment()).setAppBarColor(picItem.color);
                            }
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void initMenu(View view) {
        view.findViewById(R.id.menu_classify_girl).setOnClickListener(this);
        view.findViewById(R.id.menu_ancient_words).setOnClickListener(this);
        view.findViewById(R.id.menu_modern_words).setOnClickListener(this);
        view.findViewById(R.id.menu_hot).setOnClickListener(this);
        view.findViewById(R.id.menu_end).setOnClickListener(this);

        view.findViewById(R.id.tvMustReadMMore).setOnClickListener(this);
        view.findViewById(R.id.tvHotSearachMore).setOnClickListener(this);
        view.findViewById(R.id.tvFinishMore).setOnClickListener(this);
        view.findViewById(R.id.tvAncientMore).setOnClickListener(this);
        view.findViewById(R.id.tvModernMore).setOnClickListener(this);

        view.findViewById(R.id.tveEditorRecommendMore).setVisibility(View.GONE);

        kindLayout = view.findViewById(R.id.kindLayout);

        nestedScrollView = view.findViewById(R.id.nestedScrollView);
    }

    private void initHighlyRecommend(View view) {
        mustReadListView = view.findViewById(R.id.mustReadListView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 4;
                } else {
                    return 1;
                }
            }
        });

        mustReadListView.setLayoutManager(gridLayoutManager);
        mustReadListView.addItemDecoration(new RecommendSpaceItemDecoration(35));
        mustReadListView.addItemDecoration(dividerItemDecoration);
        highlyRecommendAdapter = new HighlyRecommendAdapter(mActivity);
        mustReadListView.setAdapter(highlyRecommendAdapter);
    }

    private void initEditorRecommend(View contentView) {
        editorRecommendRecyclerView = contentView.findViewById(R.id.editorRecommendRecyclerView);

        editorRecommendRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        editRecommendAdapter = new EditRecommendAdapter(mActivity);
        editorRecommendRecyclerView.setAdapter(editRecommendAdapter);
    }

    private void initCsjAd(View view) {
        adContainer1 = view.findViewById(R.id.adContainer1);
        adContainer2 = view.findViewById(R.id.adContainer2);

        initCsjAd(adContainer1, ADConfig.CSJAdPositionId.BOOK_STORE_GIRL_ID1);
        initCsjAd(adContainer2, ADConfig.CSJAdPositionId.BOOK_STORE_GIRL_ID2);
    }

    private void initHotSearch(View contentView) {
        hotSearchRecyclerView = contentView.findViewById(R.id.hotSearchRecyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        hotSearchRecyclerView.setLayoutManager(gridLayoutManager);
        hotSearchRecyclerView.addItemDecoration(new SpaceItemDecoration(35));
        hotSearchAdapter = new BookWithTitleAdapter(mActivity);
        hotSearchRecyclerView.setAdapter(hotSearchAdapter);
    }

    private void initBookFinish(View contentView) {
        finishRecyclerView = contentView.findViewById(R.id.finishRecyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        finishRecyclerView.setLayoutManager(gridLayoutManager);
        finishRecyclerView.addItemDecoration(new SpaceItemDecoration(35));
        bookFinishAdapter = new BookWithTitleAdapter(mActivity);
        finishRecyclerView.setAdapter(bookFinishAdapter);
    }

    private void initRank(View contentView) {
        magicIndicator = contentView.findViewById(R.id.magic_indicator);
        viewPager = contentView.findViewById(R.id.viewPager);

        rankViewList = new ArrayList<>(3);

        hotRankRecyclerView = (RecyclerView) getLayoutInflater().inflate(R.layout.book_store_rank_list, null);
        clickRankReadRecyclerView = (RecyclerView) getLayoutInflater().inflate(R.layout.book_store_rank_list, null);
        collectReadRecyclerView = (RecyclerView) getLayoutInflater().inflate(R.layout.book_store_rank_list, null);

        hotRankRankAdapter = new BookStoreRankAdapter(mActivity);
        clickRankRankAdapter = new BookStoreRankAdapter(mActivity);
        collectReadRankAdapter = new BookStoreRankAdapter(mActivity);

        hotRankRankAdapter.setLabelName("推荐");
        clickRankRankAdapter.setLabelName("热度");
        collectReadRankAdapter.setLabelName("收藏");

        hotRankRecyclerView.setAdapter(hotRankRankAdapter);
        clickRankReadRecyclerView.setAdapter(clickRankRankAdapter);
        collectReadRecyclerView.setAdapter(collectReadRankAdapter);

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(mContext, 2);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(mContext, 2);
        GridLayoutManager gridLayoutManager3 = new GridLayoutManager(mContext, 2);

        hotRankRecyclerView.setLayoutManager(gridLayoutManager1);
        clickRankReadRecyclerView.setLayoutManager(gridLayoutManager2);
        collectReadRecyclerView.setLayoutManager(gridLayoutManager3);

        rankViewList.add(hotRankRecyclerView);
        rankViewList.add(clickRankReadRecyclerView);
        rankViewList.add(collectReadRecyclerView);

        final List<String> titleList = new ArrayList<>(2);
        titleList.add("畅销榜");
        titleList.add("点击榜");
        titleList.add("收藏榜");
        MagicIndicatorAdapter magicIndicatorAdapter = new MagicIndicatorAdapter(mContext, viewPager);
        magicIndicatorAdapter.setTitleList(titleList);

        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdapter(magicIndicatorAdapter);

        magicIndicator.setNavigator(commonNavigator);

        pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {

                View view = rankViewList.get(position);
                ViewPager.LayoutParams layoutParams = new ViewPager.LayoutParams();
                layoutParams.width = ViewPager.LayoutParams.MATCH_PARENT;
                layoutParams.height = ViewPager.LayoutParams.WRAP_CONTENT;
                view.setLayoutParams(layoutParams);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) { //
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

        };
        viewPager.setAdapter(pagerAdapter);

        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    private void initAncient(View contentView) {
        ancientRecyclerView = contentView.findViewById(R.id.ancientRecyclerView);

        ancientRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        ancientRecyclerView.addItemDecoration(dividerItemDecoration);
        ancientAdapter = new BookWithContentAdapter(mActivity);
        ancientRecyclerView.setAdapter(ancientAdapter);
    }

    private void initModern(View contentView) {
        modernRecyclerView = contentView.findViewById(R.id.modernRecyclerView);

        modernRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        modernRecyclerView.addItemDecoration(dividerItemDecoration);
        modernAdapter = new BookWithContentAdapter(mActivity);
        modernRecyclerView.setAdapter(modernAdapter);
    }

    private void initFriendRead(View view) {
        friendReadRecyclerView = view.findViewById(R.id.friendReadRecyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position % 6 < 4) {
                    return 1;
                } else {
                    return 4;
                }
            }
        });

        friendReadRecyclerView.setLayoutManager(gridLayoutManager);
        friendReadRecyclerView.addItemDecoration(new FriendReadItemDecoration(35));
        friendReadRecyclerView.addItemDecoration(dividerItemDecoration);
        friendReadAdapter = new FriendReadAdapter(mActivity);
        friendReadRecyclerView.setAdapter(friendReadAdapter);
    }

    private void initPresenter() {
        bookStorePresenter = new BookStorePresenter();
        bookStorePresenter.attachView(this);
    }

    private void loadData() {
        int userId = UserInfoManager.getInstance(mContext).getUserid();
        if (null != bookStorePresenter) {
            bookStorePresenter.getBookData(userId, updatePage, BookStoreChannel.GIRL);
            bookStorePresenter.getPictureConfig(userId, BookStoreChannel.GIRL);
        }
    }

    private void loadMoreData() {
        int userId = UserInfoManager.getInstance(mContext).getUserid();
        bookStorePresenter.getFriendRead(userId, BookStoreChannel.GIRL, loadMorePage, LOAD_MORE_PAGE_SIZE, 0);
    }

    private void initCsjAd(final FrameLayout frameLayout, String codeId) {
        frameLayout.removeAllViews();

        //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        TTAdNative adNative = TTAdManagerHolder.get().createAdNative(getContext());
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(getContext());

        mTTAdList = new ArrayList<>();
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(codeId) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(350, 0) //期望模板广告view的size,单位dp
                .setImageAcceptedSize(640, 320)
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        adNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.i("hhh", "load error 4 : " + code + ", " + message);
                frameLayout.removeAllViews();
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                TTNativeExpressAd mTTAd = ads.get(0);
                mTTAdList.add(mTTAd);
                // mTTAd.setSlideIntervalTime(30 * 1000);
                bindAdListener(mTTAd, frameLayout);
                mTTAd.render();
            }
        });
    }

    private void bindAdListener(TTNativeExpressAd ad, final FrameLayout frameLayout) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                Log.i("hhh", "广告被点击");
            }

            @Override
            public void onAdShow(View view, int type) {
                Log.i("hhh", "广告展示");
            }

            @Override
            public void onRenderFail(View view, String msg, int code) {
                Log.i("hhh", msg + " code:" + code);
            }

            @Override
            public void onRenderSuccess(View view, float width, float height) {
                //返回view的宽高 单位 dp
                Log.i("hhh", "渲染成功");
                frameLayout.removeAllViews();
                frameLayout.addView(view);
            }
        });
        //dislike设置
        bindDislike(ad, frameLayout);
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }
    }

    /**
     * 设置广告的不喜欢, 注意：强烈建议设置该逻辑，如果不设置dislike处理逻辑，则模板广告中的 dislike区域不响应dislike事件。
     *
     * @param ad
     */
    private void bindDislike(TTNativeExpressAd ad, final FrameLayout frameLayout) {
        //使用默认模板中默认dislike弹出样式
        ad.setDislikeCallback(getActivity(), new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onSelected(int position, String value) {
                Log.i("hhh", "点击 " + value);
                //用户选择不喜欢原因后，移除广告展示
                frameLayout.removeAllViews();
            }

            @Override
            public void onCancel() {
                Log.i("hhh", "点击取消");
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.public_line_horizontal));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_classify_girl:
                Intent intent = new Intent(mContext, BookLibraryActivity.class);
                intent.putExtra("BookStoreChannel", BookStoreChannel.GIRL);
                startActivity(intent);
                break;
            case R.id.menu_ancient_words:
                BookLibraryDetailActivity.lunchActivity(mActivity, BookLibraryType.GU_YAN, getString(R.string.menu_ancient_words));
                break;
            case R.id.menu_modern_words:
                BookLibraryDetailActivity.lunchActivity(mActivity, BookLibraryType.XIAN_YAN, getString(R.string.menu_modern_words));
                break;
            case R.id.menu_hot:
                BookListByTypeActivity.lunchActivity(mActivity, "畅销热搜", BookStoreChannel.GIRL, 3);
                break;
            case R.id.menu_end:
                Intent intent1 = new Intent(mActivity, BookEndActivity.class);
                intent1.putExtra("bookStoreChannel", BookStoreChannel.GIRL);
                startActivity(intent1);
                break;
            case R.id.tvMustReadMMore:
                BookListByTypeActivity.lunchActivity(mActivity, BookStoreType.BoutiqueSetMore, "精品必读", BookStoreChannel.GIRL);
                break;
            case R.id.tvHotSearachMore:
                BookListByTypeActivity.lunchActivity(mActivity, BookStoreType.HotSearachMore, "畅销热搜", BookStoreChannel.GIRL);
                break;
            case R.id.tvFinishMore:
                BookListByTypeActivity.lunchActivity(mActivity, BookStoreType.FinishMore, "经典完本", BookStoreChannel.GIRL);
                break;
            case R.id.tvAncientMore:
                BookListByTypeActivity.lunchActivity(mActivity, BookStoreType.AncientMore, "古代言情", BookStoreChannel.GIRL);
                break;
            case R.id.tvModernMore:
                BookListByTypeActivity.lunchActivity(mActivity, BookStoreType.ModernMore, "现代言情", BookStoreChannel.GIRL);
                break;
            default:
                break;
        }
    }

    @Override
    public void getFriendReadSuccess(FriendReadResponse friendReadResponse) {
        smartRefreshLayout.finishLoadMore();
        loadMorePage++;
        friendReadAdapter.addAll(friendReadResponse.pagedata);
        friendReadAdapter.notifyDataSetChanged();
    }

    @Override
    public void getBookDataSuccess(MaleChannelResponse maleChannelResponse) {
        smartRefreshLayout.finishRefresh();
        updatePage++;
        highlyRecommendAdapter.clear();
        editRecommendAdapter.clear();
        hotSearchAdapter.clear();
        bookFinishAdapter.clear();
        ancientAdapter.clear();
        modernAdapter.clear();
        friendReadAdapter.clear();
        hotRankRankAdapter.clear();
        clickRankRankAdapter.clear();
        collectReadRankAdapter.clear();

        highlyRecommendAdapter.addAll(maleChannelResponse.choicedata);
        editRecommendAdapter.setBookInfoEntityList(maleChannelResponse.recdata);
        hotSearchAdapter.addAll(maleChannelResponse.searchdata);
        bookFinishAdapter.addAll(maleChannelResponse.completedata);
        ancientAdapter.addAll(maleChannelResponse.class1data);
        modernAdapter.addAll(maleChannelResponse.class2data);
        hotRankRankAdapter.addAll(maleChannelResponse.subdata);
        clickRankRankAdapter.addAll(maleChannelResponse.clickdata);
        collectReadRankAdapter.addAll(maleChannelResponse.collectdata);

        highlyRecommendAdapter.notifyDataSetChanged();
        editRecommendAdapter.notifyDataSetChanged();
        hotSearchAdapter.notifyDataSetChanged();
        bookFinishAdapter.notifyDataSetChanged();
        ancientAdapter.notifyDataSetChanged();
        modernAdapter.notifyDataSetChanged();
        friendReadAdapter.notifyDataSetChanged();
        hotRankRankAdapter.notifyDataSetChanged();
        clickRankRankAdapter.notifyDataSetChanged();
        collectReadRankAdapter.notifyDataSetChanged();
        pagerAdapter.notifyDataSetChanged();
        kindLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void getPictureConfigSuccess(final PictureConfigResponse pictureConfigResponse) {
        mList.addAll(pictureConfigResponse.picdata);
        mConvenientBanner.setPages(new CBViewHolderCreator() {
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
            PictureConfigResponse.PicItem picItem = pictureConfigResponse.picdata.get(0);
            if (TextUtils.isEmpty(picItem.color) || picItem.color.trim().length() != 7) {
                mLayoutTop.setBackgroundColor(Color.parseColor("#D0B7BA"));
                if (null != getParentFragment()) {
                    ((BookStoreFragment) getParentFragment()).setAppBarColor("#D0B7BA");
                }
            } else {
                mLayoutTop.setBackgroundColor(Color.parseColor(picItem.color));
                if (null != getParentFragment()) {
                    ((BookStoreFragment) getParentFragment()).setAppBarColor(picItem.color);
                }
            }
            mConvenientBanner.startTurning(5000);
        }
    }

    @Override
    public void dismissLoading() {
        super.dismissLoading();
        smartRefreshLayout.finishRefresh();
    }

    @Override
    public void showError(Throwable throwable) {
        super.showError(throwable);
        smartRefreshLayout.finishRefresh();
    }

    public void initData() {
        loadData();
        // loadMoreData();
        mLayoutTop.setBackgroundColor(Color.parseColor("#D0B7BA"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != bookStorePresenter) {
            bookStorePresenter.detachView();
        }
    }

    @Override
    protected void onLazyLoad() {
        initPresenter();
        smartRefreshLayout.autoRefresh();
        loadMoreData();
    }

    public void scrollToTop() {
        nestedScrollView.scrollTo(0, 0);
    }
}
