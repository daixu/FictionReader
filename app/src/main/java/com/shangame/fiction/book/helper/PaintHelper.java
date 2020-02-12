package com.shangame.fiction.book.helper;

import android.content.Context;
import android.graphics.Paint;

import com.shangame.fiction.book.config.PageConfig;
import com.shangame.fiction.core.utils.ScreenUtils;

/**
 * Create by Speedy on 2018/8/6
 */
public final class PaintHelper {

    private static Paint headerPaint;
    private static Paint contentPaint;
    private static Paint footerPaint;
    private static Paint batteryPaint;
    private static Paint adPaint;
    private static Paint payPaint;
//    private  static Paint chapterPaint;

    public static Paint getHeaderPaint(Context context) {
        if (headerPaint == null) {
            headerPaint = new Paint();
            headerPaint.setColor(PageConfig.headerTextColor);
            headerPaint.setStrokeWidth(1);
            headerPaint.setAntiAlias(true);

            int size = (int) ScreenUtils.spToPx(context, PageConfig.FontSize.SIZE_1);
            headerPaint.setTextSize(size);
        }
        return headerPaint;
    }

    public static Paint getPayPaint(Context context) {
        payPaint = new Paint();
        payPaint.setColor(PageConfig.headerTextColor);
        payPaint.setStrokeWidth(1);
        payPaint.setAntiAlias(true);

        int size = (int) ScreenUtils.spToPx(context, PageConfig.FontSize.SIZE_2);
        payPaint.setTextSize(size);
        return headerPaint;
    }

    public static Paint getFooterPaint(Context context) {
        if (footerPaint == null) {
            footerPaint = new Paint();
            footerPaint.setColor(PageConfig.footerTextColor);
            footerPaint.setStrokeWidth(1);
            footerPaint.setAntiAlias(true);

            int size = (int) ScreenUtils.spToPx(context, PageConfig.FontSize.SIZE_1);
            footerPaint.setTextSize(size);
        }
        return footerPaint;
    }

    public static Paint getBatteryPaint(Context context) {
        if (batteryPaint == null) {
            batteryPaint = new Paint();
            batteryPaint.setColor(PageConfig.footerTextColor);
            batteryPaint.setStrokeWidth(1);
            batteryPaint.setAntiAlias(true);

            int size = (int) ScreenUtils.spToPx(context, PageConfig.FontSize.SIZE_1);
            batteryPaint.setTextSize(size);
        }
        return batteryPaint;
    }

    public static Paint getAdPaint(Context context) {
        if (adPaint == null) {
            adPaint = new Paint();
            adPaint.setFilterBitmap(true);

            adPaint.setColor(PageConfig.footerTextColor);
            adPaint.setStrokeWidth(1);
            adPaint.setAntiAlias(true);

            adPaint.setTextSize((int) ScreenUtils.spToPx(context, 14));
        }
        return adPaint;
    }

    public static Paint getContentPaint(int contentFontSize) {
        if (contentPaint == null) {
            contentPaint = new Paint();
            contentPaint.setFilterBitmap(true);

            contentPaint.setColor(PageConfig.contentTextColor);
            contentPaint.setStrokeWidth(1);
            contentPaint.setAntiAlias(true);
            contentPaint.setTextSize(contentFontSize);
        }
        return contentPaint;
    }

    public static Paint getChapterPaint(int contentFontSize) {
        //不缓存，
        Paint chapterPaint = new Paint();
        chapterPaint.setFilterBitmap(true);
        chapterPaint.setColor(PageConfig.chapterTextColor);
        chapterPaint.setStrokeWidth(1);
        chapterPaint.setAntiAlias(true);
        chapterPaint.setFakeBoldText(true);
        int size = (int) (contentFontSize * 1.5);
        chapterPaint.setTextSize(size);
        return chapterPaint;
    }

    public static void clear() {
        contentPaint = null;
//        chapterPaint = null;
        headerPaint = null;
        footerPaint = null;
        batteryPaint = null;
        adPaint = null;
    }
}
