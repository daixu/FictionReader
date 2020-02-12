package com.shangame.fiction.ui.rank;

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

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.RankResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.bookdetail.BookDetailActivity;
import com.shangame.fiction.ui.bookstore.BookStoreChannel;

/**
 * 榜单 Fragment
 * Create by Speedy on 2018/7/26
 */
public class RankDetailFragment extends BaseFragment implements RankContacts.View {

    private SmartRefreshLayout smartRefreshLayout;

    private RadioGroup radioGroup;

    private RecyclerView recyclerView;
    private RankAdapter rankAdapter;

    private RankPresenter rankPresenter;

    private int dayType;//默认周榜
    private int malechannel;

    private RankResponse rankResponse;

    public static RankDetailFragment newInstance(int malechannel, int daytype) {
        RankDetailFragment fragment = new RankDetailFragment();
        Bundle args = new Bundle();
        args.putInt("malechannel", malechannel);
        args.putInt("dayType", daytype);
        fragment.setArguments(args);
        return fragment;
    }

    public void setDayType(int dayType) {
        this.dayType = dayType;
        smartRefreshLayout.autoRefresh();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            malechannel = getArguments().getInt("malechannel", BookStoreChannel.GIRL);
            dayType = getArguments().getInt("dayType", RankDayType.RANK_WEEK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_rank_detail, container, false);
        rankPresenter = new RankPresenter();
        rankPresenter.attachView(this);
        initSmartRefreshLayout(contentView);
        initRadioGroup(contentView);
        initRecyclerView(contentView);
        smartRefreshLayout.autoRefresh();
        return contentView;
    }

    private void initSmartRefreshLayout(View contentView) {
        smartRefreshLayout = contentView.findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadRankList();
            }
        });
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
                recyclerView.scrollToPosition(0);
            }
        });
    }

    private void initRecyclerView(View contentView) {
        recyclerView = contentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rankAdapter = new RankAdapter();
        recyclerView.setAdapter(rankAdapter);
    }

    private void loadRankList() {
        int userid = UserInfoManager.getInstance(mContext).getUserid();
        rankPresenter.getRankList(userid, malechannel, dayType);
    }

    @Override
    public void getRankListSuccess(RankResponse rankResponse) {
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


        public RankViewHolder(View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            ivRankTop = itemView.findViewById(R.id.ivRankTop);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvBookAuthor = itemView.findViewById(R.id.tvBookAuthor);
        }
    }

    class RankAdapter extends WrapRecyclerViewAdapter<RankResponse.RankBookBean, RankViewHolder> {


        @NonNull
        @Override
        public RankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item, parent, false);
            RankViewHolder rankViewHolder = new RankViewHolder(view);
            return rankViewHolder;
        }


        @Override
        public void onBindViewHolder(@NonNull RankViewHolder holder, int position) {

            final RankResponse.RankBookBean rankBookBean = getItem(position);
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
                    holder.ivRankTop.setText(String.valueOf(position + 1) + ". ");
                    holder.ivRankTop.setTextColor(getResources().getColor(R.color.primary_text));
            }

            holder.tvBookName.setText(rankBookBean.bookname);
            holder.tvContent.setText(rankBookBean.synopsis);
            holder.tvBookAuthor.setText(getString(R.string.author_zhu, rankBookBean.author));

            ImageLoader.with(mActivity).loadCover(holder.ivCover, rankBookBean.bookcover);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BookDetailActivity.lunchActivity(mActivity, rankBookBean.bookid, ApiConstant.ClickType.FROM_CLICK);

                }
            });
        }
    }


}
