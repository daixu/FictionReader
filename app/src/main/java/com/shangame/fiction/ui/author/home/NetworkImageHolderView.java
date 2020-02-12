package com.shangame.fiction.ui.author.home;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.shangame.fiction.net.response.PictureConfigResponse;

/**
 * Create by Speedy on 2019/7/31
 */
public class NetworkImageHolderView implements Holder<PictureConfigResponse.PicItem> {
    private ImageView imageView;

    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, PictureConfigResponse.PicItem data) {
        Glide.with(context).load(data.imgurl).into(imageView);
    }
}
