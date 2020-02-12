package com.shangame.fiction.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;
import com.shangame.fiction.R;
import com.shangame.fiction.ui.sales.withdraw.TimeLineModel;

import java.util.List;

public class WithdrawProgressAdapter extends RecyclerView.Adapter<WithdrawProgressAdapter.TimeLineViewHolder> {
    private Context mContext;
    private List<TimeLineModel> mList;

    public WithdrawProgressAdapter(Context context, @Nullable List<TimeLineModel> data) {
        this.mContext = context;
        this.mList = data;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    @NonNull
    @Override
    public TimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_withdraw_progress, parent, false);
        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeLineViewHolder viewHolder, int position) {
        TimeLineModel item = mList.get(position);
        if (item.status == 0) {
            viewHolder.mTimeline.setMarker(ContextCompat.getDrawable(mContext, R.drawable.ic_marker));
        } else {
            viewHolder.mTimeline.setMarker(ContextCompat.getDrawable(mContext, R.drawable.icon_progress_complete));
        }

        if (!TextUtils.isEmpty(item.date)) {
            viewHolder.mTextDate.setVisibility(View.VISIBLE);
            viewHolder.mTextDate.setText(item.date);
        } else {
            viewHolder.mTextDate.setVisibility(View.GONE);
        }
        viewHolder.mTextMessage.setText(item.message);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class TimeLineViewHolder extends RecyclerView.ViewHolder {
        TimelineView mTimeline;
        TextView mTextDate;
        TextView mTextMessage;

        public TimeLineViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            mTimeline = itemView.findViewById(R.id.timeline);
            mTextDate = itemView.findViewById(R.id.text_date);
            mTextMessage = itemView.findViewById(R.id.text_message);
            mTimeline.initLine(viewType);
        }
    }
}
