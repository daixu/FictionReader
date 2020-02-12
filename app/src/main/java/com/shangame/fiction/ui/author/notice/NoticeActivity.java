package com.shangame.fiction.ui.author.notice;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.net.response.BookNoticeInfoResponse;
import com.shangame.fiction.net.response.NoticeInfoResponse;

public class NoticeActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTextTitle;
    private TextView mTextTime;
    private TextView mTextContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        TextView tvTitle = findViewById(R.id.tv_title);
        mTextTitle = findViewById(R.id.text_title);
        mTextTime = findViewById(R.id.text_time);
        mTextContent = findViewById(R.id.text_content);

        int type = getIntent().getIntExtra("type", 0);

        if (type == 0) {
            tvTitle.setText("公告详情");
        } else if (type == 1){
            tvTitle.setText("新手作者秘籍详情");
        } else {
            tvTitle.setText("消息详情");
        }

        NoticeInfoResponse.PageDataBean bean = getIntent().getParcelableExtra("bean");
        if (null != bean) {
            initView(bean);
        }

        BookNoticeInfoResponse.PageDataBean dataBean = getIntent().getParcelableExtra("dataBean");
        if (null != dataBean) {
            initReview(dataBean);
        }
        findViewById(R.id.img_back).setOnClickListener(this);
    }

    private void initReview(BookNoticeInfoResponse.PageDataBean bean) {
        Log.e("hhh", "bean.title= " + bean.title);
        Log.e("hhh", "bean.addtime= " + bean.addtime);
        Log.e("hhh", "bean.contenttext= " + bean.contenttext);
        if (!TextUtils.isEmpty(bean.title)) {
            mTextTitle.setText(bean.title);
        }
        if (!TextUtils.isEmpty(bean.addtime)) {
            mTextTime.setText(bean.addtime);
        }
        if (!TextUtils.isEmpty(bean.contenttext)) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                mTextContent.setText(Html.fromHtml(bean.contenttext, Html.FROM_HTML_MODE_LEGACY));
            } else {
                mTextContent.setText(Html.fromHtml(bean.contenttext));
            }
        }
    }

    private void initView(NoticeInfoResponse.PageDataBean bean) {
        Log.e("hhh", "bean.title= " + bean.title);
        Log.e("hhh", "bean.addtime= " + bean.addtime);
        Log.e("hhh", "bean.contenttext= " + bean.contenttext);
        if (!TextUtils.isEmpty(bean.title)) {
            mTextTitle.setText(bean.title);
        }
        if (!TextUtils.isEmpty(bean.addtime)) {
            mTextTime.setText(bean.addtime);
        }
        if (!TextUtils.isEmpty(bean.contenttext)) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                mTextContent.setText(Html.fromHtml(bean.contenttext, Html.FROM_HTML_MODE_LEGACY));
            } else {
                mTextContent.setText(Html.fromHtml(bean.contenttext));
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_back) {
            finish();
        }
    }
}
