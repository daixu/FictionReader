package com.shangame.fiction.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.MultipleItemRvAdapter;
import com.shangame.fiction.adapter.provider.CenterPicNewsItemProvider;
import com.shangame.fiction.adapter.provider.LeftPicNewsItemProvider;
import com.shangame.fiction.adapter.provider.TextNewsItemProvider;
import com.shangame.fiction.adapter.provider.ThreePicNewsItemProvider;
import com.shangame.fiction.net.response.NewsResp;

import java.util.List;

public class ChoiceListAdapter extends MultipleItemRvAdapter<NewsResp.DataBean.PageDataBean, BaseViewHolder> {

    /**
     * 纯文字布局(文章、广告)
     */
    public static final int TEXT_NEWS = 100;
    /**
     * 居中大图布局(单图文章)
     */
    public static final int CENTER_SINGLE_PIC_NEWS = 200;
    /**
     * 左侧小图布局(小图新闻)
     */
    public static final int RIGHT_PIC_VIDEO_NEWS = 300;
    /**
     * 三张图片布局(文章)
     */
    public static final int THREE_PICS_NEWS = 400;

    public ChoiceListAdapter(@Nullable List<NewsResp.DataBean.PageDataBean> data) {
        super(data);
        finishInitialize();
    }

    @Override
    protected int getViewType(NewsResp.DataBean.PageDataBean news) {
        if (news.showType == 1) {
            //纯文字新闻
            return TEXT_NEWS;
        } else {
            if (news.showType == 3) {
                //左侧图片
                return RIGHT_PIC_VIDEO_NEWS;
            }

            if (news.showType == 4) {
                //三图
                return THREE_PICS_NEWS;
            }

            //中间大图
            return CENTER_SINGLE_PIC_NEWS;
        }
    }

    @Override
    public void registerItemProvider() {
        //注册itemProvider
        mProviderDelegate.registerProvider(new TextNewsItemProvider());
        mProviderDelegate.registerProvider(new CenterPicNewsItemProvider());
        mProviderDelegate.registerProvider(new LeftPicNewsItemProvider());
        mProviderDelegate.registerProvider(new ThreePicNewsItemProvider());
    }
}
