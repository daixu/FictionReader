package com.shangame.fiction.ui.bookstore;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bytedance.sdk.openadsdk.AdSlot;
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
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.base.BaseLazyFragment;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.AdBean;
import com.shangame.fiction.net.response.ChoicenessResponse;
import com.shangame.fiction.net.response.OthersLookResponse;
import com.shangame.fiction.net.response.PictureConfigResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.model.UserInfo;
import com.shangame.fiction.ui.author.home.NetworkImageHolderView;
import com.shangame.fiction.ui.bookdetail.BookDetailActivity;
import com.shangame.fiction.ui.booklib.BookLibraryActivity;
import com.shangame.fiction.ui.login.LoginActivity;
import com.shangame.fiction.ui.task.TaskCenterActivity;
import com.shangame.fiction.ui.web.WebViewActivity;
import com.shangame.fiction.widget.BoutiquepaceItemDecoration;
import com.shangame.fiction.widget.OtherLookItemDecoration;
import com.shangame.fiction.widget.RecommendSpaceItemDecoration;
import com.shangame.fiction.widget.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 精选
 */
public class ChoicenessFragment extends BaseLazyFragment implements View.OnClickListener, ChoicenessContacts.View {

    private static final String TAG = "ChoicenessFragment";

    private static final int OPEN_TASK_CENTER = 66;
    private final static int LOAD_MORE_PAGE_SIZE = 7;
    private SmartRefreshLayout smartRefreshLayout;
    private View kindLayout;
    private List<ChoicenessResponse.CardataBean> carData = new ArrayList<>();
    private RecyclerView highlyRecommendRecyclerView;
    private RecyclerView boutiqueSetRecyclerView;
    private RecyclerView editorRecommendRecyclerView;
    private RecyclerView hotSerialRecyclerView;
    private RecyclerView labelRecyclerView;
    private RecyclerView otherLookRecyclerView;
    private HeavyRecommendAdapter heavyRecommendAdapter;
    private BoutiqueSetAdapter boutiqueSetAdapter;
    private BookWithContentAdapter editorRecommendAdapter;
    private HotSerialAdapter hotSerialAdapter;
    private LabelAdapter labelAdapter;
    private OtherLookAdapter otherLookAdapter;
    private DividerItemDecoration dividerItemDecoration;
    private ChoicenessPresenter choicenessPresenter;
    private int updatePage;
    private int loadMorePage = 1;
    private NestedScrollView nestedScrollView;
    private NestedScrollView.OnScrollChangeListener onScrollChangeListener;

    private FrameLayout adContainer1;
    private FrameLayout adContainer2;

    // 头条穿山甲
    private TTNativeExpressAd mAd;

    private View mLayoutTop;
    private ConvenientBanner mConvenientBanner;

    private List<PictureConfigResponse.PicItem> mList = new ArrayList<>();

    private boolean hidden;

    private ImageView mImageGo;

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
    protected void onLazyLoad() {
        initPresenter();
        smartRefreshLayout.autoRefresh();
        loadMoreData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == OPEN_TASK_CENTER) {
            Intent intent = new Intent(mContext, TaskCenterActivity.class);
            startActivity(intent);
        }
        if (resultCode == Activity.RESULT_OK && requestCode == BaseActivity.LUNCH_LOGIN_ACTIVITY_REQUEST_CODE) {
            UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
            int agentGrade = userInfo.agentGrade;
            if (agentGrade > 0) {
                mImageGo.setImageResource(R.drawable.image_gold_partner);
            } else {
                mImageGo.setImageResource(R.drawable.image_become_partner);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_choiceness, container, false);
        int verify = AdBean.getInstance().getVerify();
        if (verify == 0) {
            initCsjAd(contentView);
        }
        initSmartRefreshLayout(contentView);
        initBanner(contentView);
        initMenu(contentView);
        initHighlyRecommend(contentView);
        initEditorRecommend(contentView);
        initBoutiqueSet(contentView);
        initHotSerial(contentView);
        initBoyGirl(contentView);
        initLabelKind(contentView);
        initOtherLook(contentView);

        return contentView;
    }

    private void initCsjAd(View view) {
        adContainer1 = view.findViewById(R.id.adContainer1);
        adContainer2 = view.findViewById(R.id.adContainer2);

        initCsjAd(adContainer1, ADConfig.CSJAdPositionId.BOOK_STORE_CHOICENESS_ID1);
        initCsjAd(adContainer2, ADConfig.CSJAdPositionId.BOOK_STORE_CHOICENESS_ID2);
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

    private void initBanner(View contentView) {
        mImageGo = contentView.findViewById(R.id.image_go);
        mLayoutTop = contentView.findViewById(R.id.layout_top);
        mConvenientBanner = contentView.findViewById(R.id.convenientBanner);

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
                        mLayoutTop.setBackgroundColor(Color.parseColor("#F46464"));
                        if (null != getParentFragment()) {
                            if (hidden) {
                                ((BookStoreFragment) getParentFragment()).setAppBarColor("#F46464");
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

        mImageGo.setOnClickListener(this);
    }

    private void initMenu(View contentView) {
        contentView.findViewById(R.id.menu_classify).setOnClickListener(this);
        contentView.findViewById(R.id.menu_ranking).setOnClickListener(this);
        contentView.findViewById(R.id.menu_red).setOnClickListener(this);
        contentView.findViewById(R.id.menu_new_book).setOnClickListener(this);
        contentView.findViewById(R.id.menu_end).setOnClickListener(this);

        contentView.findViewById(R.id.tvHighlyRecommendMore).setOnClickListener(this);
        contentView.findViewById(R.id.tvBoutiqueSetMore).setOnClickListener(this);
        contentView.findViewById(R.id.tveEditorRecommendMore).setOnClickListener(this);
        contentView.findViewById(R.id.tvHotSerialMore).setOnClickListener(this);
        contentView.findViewById(R.id.layout_hot_search).setOnClickListener(this);
        contentView.findViewById(R.id.layout_finish).setOnClickListener(this);
        contentView.findViewById(R.id.tvLabelMore).setOnClickListener(this);

        kindLayout = contentView.findViewById(R.id.kindLayout);

        nestedScrollView = contentView.findViewById(R.id.nestedScrollView);
        nestedScrollView.setOnScrollChangeListener(onScrollChangeListener);
    }

    private void initHighlyRecommend(View view) {
        highlyRecommendRecyclerView = view.findViewById(R.id.highlyRecommendListView);

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

        highlyRecommendRecyclerView.setLayoutManager(gridLayoutManager);
        highlyRecommendRecyclerView.addItemDecoration(new RecommendSpaceItemDecoration(35));
        highlyRecommendRecyclerView.addItemDecoration(dividerItemDecoration);
        heavyRecommendAdapter = new HeavyRecommendAdapter(mActivity);
        highlyRecommendRecyclerView.setAdapter(heavyRecommendAdapter);
    }

    private void initEditorRecommend(View contentView) {
        editorRecommendRecyclerView = contentView.findViewById(R.id.editorRecommendRecyclerView);

        editorRecommendRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        editorRecommendRecyclerView.addItemDecoration(dividerItemDecoration);
        editorRecommendAdapter = new BookWithContentAdapter(mActivity);
        editorRecommendRecyclerView.setAdapter(editorRecommendAdapter);
    }

    private void initBoutiqueSet(View contentView) {
        boutiqueSetRecyclerView = contentView.findViewById(R.id.boutiqueSetRecyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position >= 4) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });

        boutiqueSetRecyclerView.setLayoutManager(gridLayoutManager);
        boutiqueSetRecyclerView.addItemDecoration(new BoutiquepaceItemDecoration(35));
        boutiqueSetAdapter = new BoutiqueSetAdapter(mActivity);
        boutiqueSetRecyclerView.setAdapter(boutiqueSetAdapter);
    }

    private void initHotSerial(View contentView) {
        hotSerialRecyclerView = contentView.findViewById(R.id.hotSerialRecyclerView);

        hotSerialRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        hotSerialRecyclerView.addItemDecoration(dividerItemDecoration);
        hotSerialRecyclerView.addItemDecoration(dividerItemDecoration);
        hotSerialAdapter = new HotSerialAdapter(mActivity);
        hotSerialRecyclerView.setAdapter(hotSerialAdapter);
    }

    private void initBoyGirl(View contentView) {
        contentView.findViewById(R.id.girlNovelLayout).setOnClickListener(this);
        contentView.findViewById(R.id.boyNovelLayout).setOnClickListener(this);
    }

    private void initLabelKind(View contentView) {
        labelRecyclerView = contentView.findViewById(R.id.labelRecyclerView);

        labelRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));

        labelAdapter = new LabelAdapter(mActivity);
        labelRecyclerView.setAdapter(labelAdapter);
    }

    private void initOtherLook(View contentView) {
        otherLookRecyclerView = contentView.findViewById(R.id.otherLookRecyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 4);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position % 7 < 3) {
                    return 4;
                } else {
                    return 1;
                }
            }
        });

        otherLookRecyclerView.setLayoutManager(gridLayoutManager);

        otherLookRecyclerView.addItemDecoration(new OtherLookItemDecoration(35));
        otherLookRecyclerView.addItemDecoration(dividerItemDecoration);
        otherLookAdapter = new OtherLookAdapter(mActivity);
        otherLookRecyclerView.setAdapter(otherLookAdapter);
    }

    private void initPresenter() {
        choicenessPresenter = new ChoicenessPresenter();
        choicenessPresenter.attachView(this);
    }

    private void initCsjAd(final FrameLayout frameLayout, String codeId) {
        frameLayout.removeAllViews();

        //step2:创建TTAdNative对象，createAdNative(Context context) banner广告context需要传入Activity对象
        TTAdNative adNative = TTAdManagerHolder.get().createAdNative(getContext());
        //step3:(可选，强烈建议在合适的时机调用):申请部分权限，如read_phone_state,防止获取不了imei时候，下载类广告没有填充的问题。
        TTAdManagerHolder.get().requestPermissionIfNecessary(getContext());

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
                Log.i("hhh", "load error 1 : " + code + ", " + message);
                frameLayout.removeAllViews();
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                mAd = ads.get(0);
                // mTTAd.setSlideIntervalTime(30 * 1000);
                bindAdListener(mAd, frameLayout);
                mAd.render();
            }
        });
    }

    private void loadData() {
        int userId = UserInfoManager.getInstance(mContext).getUserid();
        choicenessPresenter.getChoicenessData(userId, updatePage++);
        choicenessPresenter.getPictureConfig(userId, BookStoreChannel.CHOICENESS);
    }

    private void loadMoreData() {
        int userId = UserInfoManager.getInstance(mContext).getUserid();
        choicenessPresenter.othersLookData(userId, BookStoreChannel.CHOICENESS, loadMorePage, LOAD_MORE_PAGE_SIZE, 0);
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
    public void onDestroy() {
        super.onDestroy();
        if (null != choicenessPresenter) {
            choicenessPresenter.detachView();
        }

        if (mAd != null) {
            mAd.destroy();
        }
    }

    @Override
    public void onClick(View v) {
        long id = v.getId();
        if (id == R.id.menu_classify) {
            startActivity(new Intent(mContext, BookLibraryActivity.class));
        } else if (id == R.id.menu_ranking) {
            ARouter.getInstance()
                    .build("/ss/book/rank")
                    .navigation();
        } else if (id == R.id.menu_red) {
            Intent intent = new Intent(mContext, TaskCenterActivity.class);
            startActivity(intent);
        } else if (id == R.id.menu_new_book) {
            BookListByTypeActivity.lunchActivity(mActivity, "新书", BookStoreChannel.CHOICENESS, 2);
        } else if (id == R.id.menu_end) {
            Intent intent1 = new Intent(mActivity, BookEndActivity.class);
            intent1.putExtra("bookStoreChannel", BookStoreChannel.CHOICENESS);
            startActivity(intent1);
        } else if (id == R.id.boyNovelLayout) {
            BookStoreFragment bookStoreFragment = (BookStoreFragment) getParentFragment();
            if (null != bookStoreFragment) {
                bookStoreFragment.setCurrentItem(2);
            }
        } else if (id == R.id.girlNovelLayout) {
            BookStoreFragment bookStoreFragment = (BookStoreFragment) getParentFragment();
            if (null != bookStoreFragment) {
                bookStoreFragment.setCurrentItem(1);
            }
        } else if (id == R.id.tvHighlyRecommendMore) {
            BookListByTypeActivity.lunchActivity(mActivity, BookStoreType.HighlyRecommend, getString(R.string.highly_recommend), BookStoreChannel.CHOICENESS);
        } else if (id == R.id.tvBoutiqueSetMore) {
            BookListByTypeActivity.lunchActivity(mActivity, BookStoreType.BoutiqueSetMore, "精品专场", BookStoreChannel.CHOICENESS);
        } else if (id == R.id.tveEditorRecommendMore) {
            BookListByTypeActivity.lunchActivity(mActivity, BookStoreType.EditorRecommendMore, "主编力荐", BookStoreChannel.CHOICENESS);
        } else if (id == R.id.tvHotSerialMore) {
            BookListByTypeActivity.lunchActivity(mActivity, BookStoreType.HotSerialMore, "火热连载", BookStoreChannel.CHOICENESS);
        } else if (id == R.id.layout_hot_search) {
            BookListByTypeActivity.lunchActivity(mActivity, BookStoreType.HotSearachMore, "畅销热搜", BookStoreChannel.CHOICENESS);
        } else if (id == R.id.layout_finish) {
            BookListByTypeActivity.lunchActivity(mActivity, BookStoreType.FinishMore, "经典完本", BookStoreChannel.CHOICENESS);
        } else if (id == R.id.tvLabelMore) {
            startActivity(new Intent(mContext, BookLibraryActivity.class));
        } else if (id == R.id.image_go) {
            long userId = UserInfoManager.getInstance(mContext).getUserid();
            if (userId == 0) {
                Intent mIntent = new Intent(mActivity, LoginActivity.class);
                startActivityForResult(mIntent, BaseActivity.LUNCH_LOGIN_ACTIVITY_REQUEST_CODE);
            } else {
                UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
                int agentGrade = userInfo.agentGrade;
                if (agentGrade > 0) {
                    ARouter.getInstance()
                            .build("/ss/sales/partner/manage/home")
                            .navigation();
                } else {
                    ARouter.getInstance()
                            .build("/ss/sales/partner/upgrade/silver")
                            .navigation();
                }
            }
        }
    }

    @Override
    public void getOthersLookDataSuccess(OthersLookResponse othersLookResponse) {
        smartRefreshLayout.finishLoadMore();
        loadMorePage++;
        otherLookAdapter.addAll(othersLookResponse.pagedata);
        otherLookAdapter.notifyDataSetChanged();
    }

    @Override
    public void getChoicenessDataSuccess(ChoicenessResponse choicenessResponse) {
        smartRefreshLayout.finishRefresh();

        heavyRecommendAdapter.clear();
        boutiqueSetAdapter.clear();
        editorRecommendAdapter.clear();
        hotSerialAdapter.clear();
        labelAdapter.clear();
        heavyRecommendAdapter.addAll(choicenessResponse.heavydata);
        heavyRecommendAdapter.notifyDataSetChanged();

        boutiqueSetAdapter.addAll(choicenessResponse.choicedata);
        boutiqueSetAdapter.notifyDataSetChanged();

        editorRecommendAdapter.addAll(choicenessResponse.recdata);
        editorRecommendAdapter.notifyDataSetChanged();

        hotSerialAdapter.addAll(choicenessResponse.hotdata);
        hotSerialAdapter.notifyDataSetChanged();

        labelAdapter.addAll(choicenessResponse.classdata);
        labelAdapter.notifyDataSetChanged();

        carData.addAll(choicenessResponse.cardata);
        kindLayout.setVisibility(View.VISIBLE);

        UserInfo userInfo = UserInfoManager.getInstance(mContext).getUserInfo();
        int agentGrade = userInfo.agentGrade;
        if (agentGrade > 0) {
            mImageGo.setImageResource(R.drawable.image_gold_partner);
        } else {
            mImageGo.setImageResource(R.drawable.image_become_partner);
        }
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
                mLayoutTop.setBackgroundColor(Color.parseColor("#F46464"));
                if (null != getParentFragment()) {
                    ((BookStoreFragment) getParentFragment()).setAppBarColor("#F46464");
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

    private View createCarouselItemView(ChoicenessResponse.CardataBean cardata) {
        View view = getLayoutInflater().inflate(R.layout.choice_carouse_item, null);
        TextView tvClassName = view.findViewById(R.id.tvClassName);
        TextView tvContents = view.findViewById(R.id.tvContents);
        tvClassName.setText(cardata.classname);
        tvContents.setText(cardata.contents);
        return view;
    }

    public void initData() {
        loadData();
        loadMoreData();
        mLayoutTop.setBackgroundColor(Color.parseColor("#F46464"));
    }

    public void scrollToTop() {
        if (null != nestedScrollView) {
            nestedScrollView.scrollTo(0, 0);
        }
    }

    public void setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener onScrollChangeListener) {
        this.onScrollChangeListener = onScrollChangeListener;
    }
}
