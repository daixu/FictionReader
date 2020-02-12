package com.shangame.fiction.ui.contents;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.storage.model.Chapter;

/**
 * Create by Speedy on 2018/8/17
 */
public class ContentsAdapter extends WrapRecyclerViewAdapter<Chapter, ContentsViewHolder> {

    private static final int VOLUME_TYPE = 0;
    private static final int CHAPTER_TYPE = 1;
    private Activity mActivity;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener onItemClickListener;
    private long currentChapterId;
    private int currentPosition;

    public ContentsAdapter(Activity activity, long currentChapterId) {
        this.mActivity = activity;
        this.currentChapterId = currentChapterId;
        mLayoutInflater = LayoutInflater.from(activity);
    }

    @NonNull
    @Override
    public ContentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == CHAPTER_TYPE) {
            view = mLayoutInflater.inflate(R.layout.book_chapter_item, parent, false);
        } else {
            view = mLayoutInflater.inflate(R.layout.book_volume_item, parent, false);
        }
        return new ContentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ContentsViewHolder holder, int position) {
        final Chapter chapterBean = getItem(position);
        int viewType = getItemViewType(position);
        if (viewType == CHAPTER_TYPE) {
            if (null != chapterBean) {
                holder.tvChapterName.setText(chapterBean.title);
                if (chapterBean.cid == currentChapterId) {
                    currentPosition = position;
                    holder.tvChapterName.setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
                } else {
                    holder.tvChapterName.setTextColor(mActivity.getResources().getColor(R.color.primary_text));
                }
                if (chapterBean.chargingmode == 1) {
                    holder.ivLockIcon.setVisibility(View.VISIBLE);
                } else {
                    holder.ivLockIcon.setVisibility(View.GONE);
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(chapterBean);
                        }
                    }
                });
            }
        } else {
            if (null != chapterBean) {
                holder.tvVolumeName.setText(chapterBean.title);
                holder.tvVolumeSize.setText(mActivity.getString(R.string.total_chapter, chapterBean.chapternumber));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        Chapter chapterBean = getItem(position);
        if (null != chapterBean && chapterBean.volume == 0) {
            return VOLUME_TYPE;
        } else {
            return CHAPTER_TYPE;
        }
    }

    public int getCurrentPosition() {
        Chapter chapterBean = null;
        for (int i = 0; i < getItemCount(); i++) {
            chapterBean = getItem(i);
            if (null != chapterBean && chapterBean.cid == currentChapterId) {
                currentPosition = i;
            }
        }
        return currentPosition;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(Chapter chapter);
    }
}
