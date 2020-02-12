package com.shangame.fiction.adapter.provider;

import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.ChoiceListAdapter;
import com.shangame.fiction.net.response.NewsResp;

/**
 * @description: 纯文本新闻
 */
public class TextNewsItemProvider extends BaseNewsItemProvider {

    public TextNewsItemProvider() {
        super();
    }

    @Override
    public int viewType() {
        return ChoiceListAdapter.TEXT_NEWS;
    }

    @Override
    public int layout() {
        return R.layout.item_text_news;
    }

    @Override
    protected void setData(BaseViewHolder helper, NewsResp.DataBean.PageDataBean news) {
        helper.setVisible(R.id.image_top, true);
    }
}
