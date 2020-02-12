package com.shangame.fiction.adapter.provider;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.ChoiceListAdapter;
import com.shangame.fiction.net.response.NewsResp;
import com.shangame.fiction.widget.GlideApp;

/**
 * @description: 居中大图布局(单图文章)
 */
public class CenterPicNewsItemProvider extends BaseNewsItemProvider {

    public CenterPicNewsItemProvider() {
        super();
    }

    @Override
    public int viewType() {
        return ChoiceListAdapter.CENTER_SINGLE_PIC_NEWS;
    }

    @Override
    public int layout() {
        return R.layout.item_center_pic_news;
    }

    @Override
    protected void setData(BaseViewHolder helper, NewsResp.DataBean.PageDataBean news) {
        //中间大图布局
        ImageView imageView = helper.getView(R.id.iv_img);
        GlideApp.with(mContext)
                .load(news.mediaImage.get(0).imageurl)
                .placeholder(R.drawable.default_cover)
                .centerCrop()
                .into(imageView);
    }
}
