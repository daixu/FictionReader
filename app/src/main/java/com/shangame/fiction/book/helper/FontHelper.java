package com.shangame.fiction.book.helper;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;

import com.shangame.fiction.core.utils.ScreenUtils;

/**
 * Create by Speedy on 2018/8/9
 */
public class FontHelper {

    public static int MAX_FONT_INDEX = 7;


    /**
     * 测量文本的宽、高
     * @param text
     * @param paint
     * @return
     */
    public static final Rect measureText(String text, Paint paint){
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect;
    }


    public static final int getContentFontSize(Context context,int fontSize){
        return (int) ScreenUtils.spToPx(context,fontSize);
    }

}
