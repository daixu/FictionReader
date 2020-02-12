package com.shangame.fiction.adapter.provider;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.ChoiceListAdapter;
import com.shangame.fiction.net.response.NewsResp;
import com.shangame.fiction.widget.GlideApp;

/**
 * @description: 三张图片布局(文章、广告)
 */
public class ThreePicNewsItemProvider extends BaseNewsItemProvider {

    public ThreePicNewsItemProvider() {
        super();
    }

    @Override
    public int viewType() {
        return ChoiceListAdapter.THREE_PICS_NEWS;
    }

    @Override
    public int layout() {
        return R.layout.item_three_pics_news;
    }

    @Override
    protected void setData(BaseViewHolder helper, NewsResp.DataBean.PageDataBean news) {
        //三张图片的新闻
        ImageView imageView1 = helper.getView(R.id.image_img1);
        ImageView imageView2 = helper.getView(R.id.image_img2);
        ImageView imageView3 = helper.getView(R.id.image_img3);
        GlideApp.with(mContext)
                .load(news.mediaImage.get(0).imageurl)
                .placeholder(R.drawable.default_cover)
                .centerCrop()
                .into(imageView1);

        GlideApp.with(mContext)
                .load(news.mediaImage.get(1).imageurl)
                .placeholder(R.drawable.default_cover)
                .centerCrop()
                .into(imageView2);

        GlideApp.with(mContext)
                .load(news.mediaImage.get(2).imageurl)
                .placeholder(R.drawable.default_cover)
                .centerCrop()
                .into(imageView3);
    }

}
