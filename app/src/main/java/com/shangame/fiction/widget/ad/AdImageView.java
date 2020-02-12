package com.shangame.fiction.widget.ad;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.shangame.fiction.net.response.PictureConfigResponse;

/**
 * Create by Speedy on 2018/7/25
 */
public class AdImageView extends AppCompatImageView {


    private PictureConfigResponse.PicItem adItem;


    public AdImageView(Context context) {
        super(context);
    }

    public AdImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec) {
        Drawable d = getDrawable();
        if (d != null) {
            int width = MeasureSpec.getSize(widthMeasureSpec);//高度根据使得图片的宽度充满屏幕计算而得
            int height = (int) Math.ceil((float) width * (float) d.getIntrinsicHeight() / (float) d.getIntrinsicWidth());
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setAdItem(PictureConfigResponse.PicItem adItem) {
        this.adItem = adItem;
    }

    public PictureConfigResponse.PicItem getAdItem() {
        return adItem;
    }
}
