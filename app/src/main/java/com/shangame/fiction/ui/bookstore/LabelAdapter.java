package com.shangame.fiction.ui.bookstore;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.core.manager.ImageLoader;
import com.shangame.fiction.net.response.ChoicenessResponse;
import com.shangame.fiction.ui.booklib.BookLibraryDetailActivity;

/**
 * Create by Speedy on 2018/7/27
 */
public class LabelAdapter extends WrapRecyclerViewAdapter<ChoicenessResponse.ClassdataBean, LabelAdapter.MyViewHolder> {

    private Context mActivity;

    public LabelAdapter(Context activity) {
        mActivity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lebal_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ChoicenessResponse.ClassdataBean classDataBean = getItem(position);
        if (null != classDataBean) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BookLibraryDetailActivity.lunchActivity(mActivity, classDataBean.classid, classDataBean.classname);
                }
            });
            ImageLoader.with(mActivity).loadCover(holder.ivCover, classDataBean.classimage);
            holder.tvName.setText(classDataBean.classname);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivCover;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            ivCover = itemView.findViewById(R.id.ivCover);
        }
    }
}
