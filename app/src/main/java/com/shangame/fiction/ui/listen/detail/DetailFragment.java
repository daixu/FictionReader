package com.shangame.fiction.ui.listen.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.HotRecommendAdapter;
import com.shangame.fiction.adapter.SimilarRecommendAdapter;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.net.response.AlubmDetailResponse;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends BaseFragment implements View.OnClickListener {
    private TextView mTextContent;
    private ImageView mImgMore;
    private SimilarRecommendAdapter mSimilarRecommendAdapter;
    private HotRecommendAdapter mHotRecommendAdapter;
    private List<AlubmDetailResponse.LikeDataBean> mSimilarRecommendList = new ArrayList<>();
    private List<AlubmDetailResponse.ClickBookBean> mHotRecommendList = new ArrayList<>();

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(String arg1) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("arg1", arg1);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_detail, container, false);
        initView(contentView);
        initListener();
        return contentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView(View contentView) {
        mTextContent = contentView.findViewById(R.id.text_content);
        mImgMore = contentView.findViewById(R.id.img_more);

        RecyclerView recyclerSimilarRecommend = contentView.findViewById(R.id.recycler_similar_recommend);
        recyclerSimilarRecommend.setLayoutManager(new GridLayoutManager(mContext, 3));
        mSimilarRecommendAdapter = new SimilarRecommendAdapter(mSimilarRecommendList);
        mSimilarRecommendAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                if (mSimilarRecommendAdapter.getItemViewType(position) == 1) {
                    return 3;
                }
                return 1;
            }
        });
        recyclerSimilarRecommend.setAdapter(mSimilarRecommendAdapter);
        RecyclerView recyclerHotRecommend = contentView.findViewById(R.id.recycler_hot_recommend);
        recyclerHotRecommend.setLayoutManager(new GridLayoutManager(mContext, 3));
        mHotRecommendAdapter = new HotRecommendAdapter(R.layout.item_must_listen, mHotRecommendList);
        recyclerHotRecommend.setAdapter(mHotRecommendAdapter);
    }

    private void initListener() {
        mImgMore.setOnClickListener(this);
        mSimilarRecommendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AlubmDetailResponse.LikeDataBean bean = mSimilarRecommendList.get(position);
                if (null != bean) {
                    ARouter.getInstance()
                            .build("/ss/listen/detail")
                            .withInt("albumId", bean.albumid)
                            .navigation();
                }
            }
        });
        mHotRecommendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AlubmDetailResponse.ClickBookBean bean = mHotRecommendList.get(position);
                if (null != bean) {
                    ARouter.getInstance()
                            .build("/ss/listen/detail")
                            .withInt("albumId", bean.albumid)
                            .navigation();
                }
            }
        });
    }

    public void setData(AlubmDetailResponse detailResponse) {
        if (null != detailResponse) {
            AlubmDetailResponse.DetailsDataBean bean = detailResponse.detailsdata;
            if (null != bean) {
                mTextContent.setText(bean.synopsis);
            }

            if (null != detailResponse.likedata) {
                mSimilarRecommendList.clear();
                List<AlubmDetailResponse.LikeDataBean> list = new ArrayList<>();
                for (int i = 0; i < detailResponse.likedata.size(); i++) {
                    AlubmDetailResponse.LikeDataBean dataBean = detailResponse.likedata.get(i);
                    if (i == 0) {
                        dataBean.type = 1;
                    } else {
                        dataBean.type = 2;
                    }
                    list.add(dataBean);
                }
                mSimilarRecommendList.addAll(list);
                mSimilarRecommendAdapter.setNewData(mSimilarRecommendList);
            }

            if (null != detailResponse.clickbook) {
                mHotRecommendList.clear();
                mHotRecommendList.addAll(detailResponse.clickbook);
                mHotRecommendAdapter.setNewData(mHotRecommendList);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_more: {
                if (mTextContent.getMaxLines() == 4) {
                    mTextContent.setMaxLines(20);
                    mImgMore.setImageResource(R.drawable.up_icon);
                } else {
                    mTextContent.setMaxLines(4);
                    mImgMore.setImageResource(R.drawable.down_icon);
                }
            }
            break;
            default:
                break;
        }
    }
}
