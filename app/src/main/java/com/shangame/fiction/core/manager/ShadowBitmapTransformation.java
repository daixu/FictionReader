package com.shangame.fiction.core.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.shangame.fiction.R;
import com.shangame.fiction.core.utils.ScreenUtils;

import java.nio.ByteBuffer;
import java.security.MessageDigest;


/**
 * Create by Speedy on 2018/12/26
 */
public class ShadowBitmapTransformation extends BitmapTransformation {

    private static final String ID = ShadowBitmapTransformation.class.getName();
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);

    private Context mContext;

    public ShadowBitmapTransformation(Context context){
        mContext = context;
    };

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        int width = toTransform.getWidth();
        int height = toTransform.getHeight();

        int blurPadding = ScreenUtils.dpToPxInt(mContext,10);

        Bitmap bitmap = pool.get(width+2*blurPadding, height+2*blurPadding, Bitmap.Config.ARGB_4444);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#D4D4D4"));
        paint.setMaskFilter(new BlurMaskFilter(10f, BlurMaskFilter.Blur.OUTER));
//        paint.setShadowLayer(10,20,20,Color.parseColor("#10000000"));

        Rect rect = new Rect(blurPadding,blurPadding,width+blurPadding,height+blurPadding);
        canvas.drawRect(rect,paint);

        Paint paint2 = new Paint();
        canvas.drawBitmap(toTransform, blurPadding, blurPadding, paint2);
        return bitmap;

    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
        {
            byte[] radiusData = ByteBuffer.allocate(4).putInt(hashCode()).array();
            messageDigest.update(radiusData);
        }

    }
}
