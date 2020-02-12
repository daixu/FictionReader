package com.shangame.fiction.adapter.provider;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.shangame.fiction.R;
import com.shangame.fiction.adapter.ChoiceListAdapter;
import com.shangame.fiction.net.response.NewsResp;
import com.shangame.fiction.widget.GlideApp;

/**
 * @author ChayChan
 * @description: 左侧小图布局(小图新闻)
 * @date 2018/3/22  14:36
 */
public class LeftPicNewsItemProvider extends BaseNewsItemProvider {

    public LeftPicNewsItemProvider() {
        super();
    }

    @Override
    public int viewType() {
        return ChoiceListAdapter.RIGHT_PIC_VIDEO_NEWS;
    }

    @Override
    public int layout() {
        return R.layout.item_pic_news;
    }


    @Override
    protected void setData(BaseViewHolder helper, NewsResp.DataBean.PageDataBean news) {
        ImageView imageView = helper.getView(R.id.image_img);
        GlideApp.with(mContext)
                .load(news.mediaImage.get(0).imageurl)
                .placeholder(R.drawable.default_cover)
                .centerCrop()
                .into(imageView);
    }

}
