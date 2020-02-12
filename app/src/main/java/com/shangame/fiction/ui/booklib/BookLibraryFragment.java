package com.shangame.fiction.ui.booklib;

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

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseFragment;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.net.response.GetBookLibraryTypeResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.bookstore.BookStoreChannel;
import com.shangame.fiction.ui.listen.lib.ListenLibraryDetailActivity;

public class BookLibraryFragment extends BaseFragment implements BookLibraryContracts.View {

    private int channel;
    private SmartRefreshLayout smartRefreshLayout;
    private MyAdapter myAdapter;
    private BookLibraryPresenter bookLibraryPresenter;

    public static BookLibraryFragment newInstance(int channel) {
        BookLibraryFragment fragment = new BookLibraryFragment();
        Bundle args = new Bundle();
        args.putInt("channel", channel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            channel = getArguments().getInt("channel");
        }

        bookLibraryPresenter = new BookLibraryPresenter();
        bookLibraryPresenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_library, container, false);

        smartRefreshLayout = view.findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                int userId = UserInfoManager.getInstance(mContext).getUserid();
                bookLibraryPresenter.getBookLibraryType(userId, channel);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        smartRefreshLayout.autoRefresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bookLibraryPresenter.detachView();
    }

    @Override
    public void getBookLibraryTypeSuccess(GetBookLibraryTypeResponse getBookLibraryTypeResponse) {
        smartRefreshLayout.finishRefresh();
        myAdapter.clear();
        myAdapter.addAll(getBookLibraryTypeResponse.classdata);
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(Throwable throwable) {
        super.showError(throwable);
        smartRefreshLayout.finishRefresh();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvName;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }

    class MyAdapter extends WrapRecyclerViewAdapter<GetBookLibraryTypeResponse.ClassdataBean, MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_library_item, parent, false);
            MyViewHolder rankViewHolder = new MyViewHolder(view);
            return rankViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final GetBookLibraryTypeResponse.ClassdataBean classDataBean = getItem(position);
            if (null != classDataBean) {
                holder.tvName.setText(classDataBean.classname);

                ImageLoader.with(mActivity).loadCover(holder.ivCover, classDataBean.classimage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (channel == BookStoreChannel.LISTEN) {
                            ListenLibraryDetailActivity.lunchActivity(mActivity, classDataBean.classid, classDataBean.classname);
                        } else {
                            BookLibraryDetailActivity.lunchActivity(mActivity, classDataBean.classid, classDataBean.classname);
                        }
                    }
                });
            }
        }
    }
}
