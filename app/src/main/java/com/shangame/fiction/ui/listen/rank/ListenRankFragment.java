package com.shangame.fiction.ui.listen.rank;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.net.response.AlbumRankingResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.rank.RankDayType;

/**
 * 榜单 Fragment
 * Create by Speedy on 2018/7/26
 *
 * @author hhh
 */
public class ListenRankFragment extends BaseFragment implements ListenRankContacts.View {

    private SmartRefreshLayout smartRefreshLayout;
    private RankAdapter rankAdapter;
    private ListenRankPresenter mPresenter;

    private RadioGroup radioGroup;
    private AlbumRankingResponse rankResponse;
    private RecyclerView mRecyclerView;

    /**
     * 默认周榜
     */
    private int dayType;

    public static ListenRankFragment newInstance(int dayType) {
        ListenRankFragment fragment = new ListenRankFragment();
        Bundle args = new Bundle();
        args.putInt("dayType", dayType);
        fragment.setArguments(args);
        return fragment;
    }

    public void setDayType(int dayType) {
        this.dayType = dayType;
        if (null != smartRefreshLayout) {
            smartRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dayType = getArguments().getInt("dayType", RankDayType.RANK_WEEK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_rank_detail, container, false);
        mPresenter = new ListenRankPresenter();
        mPresenter.attachView(this);
        initSmartRefreshLayout(contentView);
        initRadioGroup(contentView);
        initRecyclerView(contentView);
        smartRefreshLayout.autoRefresh();
        return contentView;
    }

    private void initSmartRefreshLayout(View contentView) {
        smartRefreshLayout = contentView.findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadRankList();
            }
        });
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.autoRefresh();
    }

    private void initRadioGroup(View contentView) {
        radioGroup = contentView.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                if (rankResponse == null) {
                    return;
                }
                rankAdapter.clear();
                switch (checkId) {
                    case R.id.tvRankHotSell:
                        rankAdapter.addAll(rankResponse.subdata);
                        rankAdapter.notifyDataSetChanged();
                        break;
                    case R.id.tvRankClick:
                        rankAdapter.addAll(rankResponse.clickdata);
                        rankAdapter.notifyDataSetChanged();
                        break;
                    case R.id.tvRankCollect:
                        rankAdapter.addAll(rankResponse.collectdata);
                        rankAdapter.notifyDataSetChanged();
                        break;
                    case R.id.tvRankGift:
                        rankAdapter.addAll(rankResponse.giftdata);
                        rankAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
                mRecyclerView.scrollToPosition(0);
            }
        });
    }

    private void initRecyclerView(View contentView) {
        mRecyclerView = contentView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rankAdapter = new RankAdapter();
        mRecyclerView.setAdapter(rankAdapter);
    }

    private void loadRankList() {
        int userId = UserInfoManager.getInstance(mContext).getUserid();
        mPresenter.getAlbumRank(userId, dayType);
    }

    @Override
    public void getAlbumRankSuccess(AlbumRankingResponse rankResponse) {
        smartRefreshLayout.finishRefresh();
        this.rankResponse = rankResponse;
        rankAdapter.clear();
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.tvRankHotSell:
                rankAdapter.addAll(rankResponse.subdata);
                rankAdapter.notifyDataSetChanged();
                break;
            case R.id.tvRankClick:
                rankAdapter.addAll(rankResponse.clickdata);
                rankAdapter.notifyDataSetChanged();
                break;
            case R.id.tvRankCollect:
                rankAdapter.addAll(rankResponse.collectdata);
                rankAdapter.notifyDataSetChanged();
                break;
            case R.id.tvRankGift:
                rankAdapter.addAll(rankResponse.giftdata);
                rankAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    @Override
    public void showNotNetworkView() {
        super.showNotNetworkView();
        smartRefreshLayout.finishRefresh();
    }

    @Override
    public void showError(Throwable throwable) {
        super.showError(throwable);
        smartRefreshLayout.finishRefresh();
    }

    class RankViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView ivRankTop;
        TextView tvBookName;
        TextView tvContent;
        TextView tvBookAuthor;

        RankViewHolder(View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            ivRankTop = itemView.findViewById(R.id.ivRankTop);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvBookAuthor = itemView.findViewById(R.id.tvBookAuthor);
        }
    }

    class RankAdapter extends WrapRecyclerViewAdapter<AlbumRankingResponse.DataBean, RankViewHolder> {

        @NonNull
        @Override
        public RankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_detail_item, parent, false);
            return new RankViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RankViewHolder holder, int position) {
            final AlbumRankingResponse.DataBean dataBean = getItem(position);
            switch (position) {
                case 0:
                    holder.ivRankTop.setText(getString(R.string.rank_top_1));
                    holder.ivRankTop.setTextColor(getResources().getColor(R.color.rank_top_1));
                    break;
                case 1:
                    holder.ivRankTop.setText(getString(R.string.rank_top_2));
                    holder.ivRankTop.setTextColor(getResources().getColor(R.color.rank_top_2));
                    break;
                case 2:
                    holder.ivRankTop.setText(getString(R.string.rank_top_3));
                    holder.ivRankTop.setTextColor(getResources().getColor(R.color.rank_top_3));
                    break;
                default:
                    String top = (position + 1) + ". ";
                    holder.ivRankTop.setText(top);
                    holder.ivRankTop.setTextColor(getResources().getColor(R.color.primary_text));
            }
            if (null != dataBean) {
                holder.tvBookName.setText(dataBean.albumName);
                holder.tvContent.setText(dataBean.synopsis);
                holder.tvBookAuthor.setText(getString(R.string.author_zhu, dataBean.author));

                ImageLoader.with(mActivity).loadCover(holder.ivCover, dataBean.bookcover);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ARouter.getInstance()
                                .build("/ss/listen/detail")
                                .withInt("albumId", dataBean.albumid)
                                .navigation();
                    }
                });
            }
        }
    }
}
