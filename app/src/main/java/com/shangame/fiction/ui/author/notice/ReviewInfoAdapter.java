package com.shangame.fiction.ui.author.notice;

import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.BookNoticeInfoResponse;

import java.util.List;

/**
 * Create by Speedy on 2019/8/1
 */
public class ReviewInfoAdapter extends BaseQuickAdapter<BookNoticeInfoResponse.PageDataBean, BaseViewHolder> {
    public ReviewInfoAdapter(int layoutResId, @Nullable List<BookNoticeInfoResponse.PageDataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BookNoticeInfoResponse.PageDataBean item) {
        TextView textTitle = helper.getView(R.id.text_title);
        TextView textTime = helper.getView(R.id.text_time);
        TextView textContent = helper.getView(R.id.text_content);
        textTitle.setText(item.title);
        textTime.setText(item.addtime);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textContent.setText(Html.fromHtml(item.contenttext, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textContent.setText(Html.fromHtml(item.contenttext));
        }
    }
}
