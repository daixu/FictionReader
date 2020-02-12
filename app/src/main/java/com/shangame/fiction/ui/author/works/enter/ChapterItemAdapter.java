package com.shangame.fiction.ui.author.works.enter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.ChapterResponse;

import java.util.List;

/**
 * Create by Speedy on 2019/7/25
 */
public class ChapterItemAdapter extends BaseQuickAdapter<ChapterResponse.PageDataBean, BaseViewHolder> {
    private int mType;

    public ChapterItemAdapter(int layoutResId, @Nullable List<ChapterResponse.PageDataBean> data, int type) {
        super(layoutResId, data);
        this.mType = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, ChapterResponse.PageDataBean item) {
        TextView textTitle = helper.getView(R.id.text_title);
        ImageView imgTime = helper.getView(R.id.img_time);
        ImageView imgVip = helper.getView(R.id.img_vip);
        TextView textCount = helper.getView(R.id.text_count);
        TextView textTime = helper.getView(R.id.text_time);

        textTitle.setText(item.title);

        String count;
        // 0 草稿箱 1 已发布
        if (mType == 1) {
            count = "正文卷" + " | " + item.wordnumber + "字";
        } else {
            count = item.wordnumber + "字";
        }
        textCount.setText(count);

        textTime.setText(item.lastmodifytime);
        if (item.timetype == 0) {
            imgTime.setVisibility(View.GONE);
        } else {
            imgTime.setVisibility(View.VISIBLE);
        }

        if (item.chargingmode == 0) {
            imgVip.setVisibility(View.GONE);
        } else {
            imgVip.setVisibility(View.VISIBLE);
        }

        helper.addOnClickListener(R.id.text_delete);
        helper.addOnClickListener(R.id.content);
    }
}
