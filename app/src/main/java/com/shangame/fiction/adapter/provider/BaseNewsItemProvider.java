package com.shangame.fiction.adapter.provider;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.provider.BaseItemProvider;
import com.shangame.fiction.R;
import com.shangame.fiction.net.response.NewsResp;

/**
 * @description: 将新闻中设置数据公共部分抽取
 */

public abstract class BaseNewsItemProvider extends BaseItemProvider<NewsResp.DataBean.PageDataBean, BaseViewHolder> {

    public BaseNewsItemProvider() {
    }

    @Override
    public void convert(BaseViewHolder helper, NewsResp.DataBean.PageDataBean news, int i) {
        if (TextUtils.isEmpty(news.title)) {
            //如果没有标题，则直接跳过
            return;
        }

        //设置标题、底部作者、评论数、发表时间
        helper.setText(R.id.text_title, news.title)
                .setText(R.id.text_author, news.author)
                .setText(R.id.text_comment_num, news.readcount + "评论");

        //根据情况显示置顶、广告和热点的标签
        setData(helper, news);
    }

    protected abstract void setData(BaseViewHolder helper, NewsResp.DataBean.PageDataBean news);
}
