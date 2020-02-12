package com.shangame.fiction.ui.author.notice;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.WrapRecyclerViewAdapter;
import com.shangame.fiction.net.response.NoticeInfoResponse;

/**
 * Create by Speedy on 2019/7/23
 */
public class NoticeListAdapter extends WrapRecyclerViewAdapter<NoticeInfoResponse.PageDataBean, NoticeInfoViewHolder> {
    private Activity mActivity;
    private int mType;

    public NoticeListAdapter(Activity activity, int type) {
        this.mActivity = activity;
        this.mType = type;
    }

    @NonNull
    @Override
    public NoticeInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice_list, parent, false);
        return new NoticeInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeInfoViewHolder viewHolder, int position) {
        final NoticeInfoResponse.PageDataBean bean = getItem(position);
        if (null != bean) {
            viewHolder.mTextTitle.setText(bean.title);
            viewHolder.mTextTime.setText(bean.addtime);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                viewHolder.mTextContent.setText(Html.fromHtml(bean.contenttext, Html.FROM_HTML_MODE_LEGACY));
            } else {
                viewHolder.mTextContent.setText(Html.fromHtml(bean.contenttext));
            }
        }

        viewHolder.mLayoutDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, NoticeActivity.class);
                intent.putExtra("bean", bean);
                intent.putExtra("type", mType);
                mActivity.startActivity(intent);
            }
        });
    }
}
