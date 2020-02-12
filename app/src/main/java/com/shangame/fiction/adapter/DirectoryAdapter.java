package com.shangame.fiction.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.AlbumChapterResponse;
import com.shangame.fiction.ui.listen.palyer.TimeUtils;
import com.shangame.fiction.widget.CircleRotateImageView;

import java.util.List;

public class DirectoryAdapter extends BaseQuickAdapter<AlbumChapterResponse.PageDataBean, BaseViewHolder> {
    private int mType;
    private int chapterId;
    public DirectoryAdapter(int layoutResId, @Nullable List<AlbumChapterResponse.PageDataBean> data, int type) {
        super(layoutResId, data);
        this.mType = type;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    @Override
    protected void convert(BaseViewHolder helper, AlbumChapterResponse.PageDataBean item) {
        TextView textBookName = helper.getView(R.id.text_book_name);
        textBookName.setText(item.title);
        String time = TimeUtils.formatDuration(item.duration * 1000);
        helper.setText(R.id.text_book_duration, time);
        ImageView imageStatus = helper.getView(R.id.image_status);
        CircleRotateImageView imagePlayer = helper.getView(R.id.image_player);
        if (chapterId == item.cid) {
            textBookName.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        } else {
            textBookName.setTextColor(ContextCompat.getColor(mContext, R.color.text_color_black));
        }
        if (mType == 1) {
            if (chapterId == item.cid) {
                imageStatus.setVisibility(View.GONE);
                imagePlayer.setVisibility(View.VISIBLE);
                imagePlayer.startRotateAnimation2();
            } else {
                if (item.chargingmode == 0) {
                    imageStatus.setVisibility(View.GONE);
                    imagePlayer.setVisibility(View.GONE);
                } else {
                    imageStatus.setVisibility(View.VISIBLE);
                    imagePlayer.setVisibility(View.GONE);
                    imageStatus.setImageResource(R.drawable.icon_locking);
                    imagePlayer.cancelRotateAnimation();
                }
            }
        } else {
            imageStatus.setVisibility(View.VISIBLE);
            imagePlayer.setVisibility(View.GONE);
            imagePlayer.cancelRotateAnimation();
            if (item.chargingmode == 0) {
                imageStatus.setImageResource(R.drawable.icon_free);
            } else {
                imageStatus.setImageResource(R.drawable.icon_locking);
            }
        }
    }
}
