package com.shangame.fiction.book.drawer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.Log;
import android.view.View;

import com.shangame.fiction.R;
import com.shangame.fiction.book.config.PageConfig;
import com.shangame.fiction.book.helper.FontHelper;
import com.shangame.fiction.book.helper.PaintHelper;
import com.shangame.fiction.book.page.Line;
import com.shangame.fiction.book.page.PageData;
import com.shangame.fiction.core.utils.ScreenUtils;
import com.shangame.fiction.ui.reader.BatteryReceiver;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Create by Speedy on 2018/8/30
 */
public class ChargePageDrawer implements Drawer {

    private static final String TAG = "ChargePageDrawer";
    private static DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    int batteryWidth;
    int batteryHeight;
    private Context mContext;
    private Canvas mCanvas;
    private int mWidth;
    private int mHeight;
    private PageData pageData;
    private Paint headerPaint;
    private Paint chapterPaint;
    private Paint contentPaint;
    private Paint footerPaint;
    private Paint batteryPaint;
    private Paint payPaint;
    private int currentY;
    private int contentTextHeight;
    private int textIndentWidth;
    private int padding;
    private int headerHeight;
    private int footerHeight;
    private PageConfig mPageConfig;
    private Region payRegion;
    private Region payMoreRegion;
    private Region autoPayCheckRegion;

    private Region showMenuRegion;

    private View.OnClickListener onMenuRegionClickListener;

    private View.OnClickListener onPayChapterClickListener;
    private View.OnClickListener onPayMoreChapterClickListener;
    private View.OnClickListener onAutoPayNextChapterListener;

    private boolean autoPayNextChapter = false;

    public ChargePageDrawer(Context context, Canvas canvas) {
        this.mContext = context;
        this.mCanvas = canvas;
        initPageConfig(context);
    }

    private void initPageConfig(Context context) {
        mPageConfig = PageConfig.getInstance(mContext);
        padding = (int) ScreenUtils.spToPx(context, PageConfig.padding);
        headerHeight = (int) ScreenUtils.spToPx(context, PageConfig.headerHeight);
        footerHeight = (int) ScreenUtils.spToPx(context, PageConfig.footerHeight);
        batteryWidth = (int) ScreenUtils.spToPx(context, PageConfig.batteryWidth);
        batteryHeight = (int) ScreenUtils.spToPx(context, PageConfig.batteryHeight);
    }

    public void init() {
        mWidth = mCanvas.getWidth();
        mHeight = mCanvas.getHeight();
    }

    public void updateConfig() {
        initPageConfig(mContext);
        PaintHelper.clear();//刷新笔
    }

    public void initPage(PageData pageData) {
        mWidth = mCanvas.getWidth();
        mHeight = mCanvas.getHeight();

        this.pageData = pageData;

        autoPayNextChapter = pageData.autoPayNextChapter;

        headerPaint = PaintHelper.getHeaderPaint(mContext);
        // int fontSize = (int) ScreenUtils.spToPx(mContext,mPageConfig.fontSize);
        chapterPaint = PaintHelper.getChapterPaint(mPageConfig.fontSize);
        contentPaint = PaintHelper.getContentPaint(mPageConfig.fontSize);
        footerPaint = PaintHelper.getFooterPaint(mContext);
        batteryPaint = PaintHelper.getBatteryPaint(mContext);
        payPaint = PaintHelper.getPayPaint(mContext);

        if (mPageConfig.dayMode == PageConfig.DayMode.NIGHT_MODE) {
            int color = Color.parseColor("#ffffff");
            headerPaint.setColor(color);
            chapterPaint.setColor(color);
            contentPaint.setColor(color);
            footerPaint.setColor(color);
            batteryPaint.setColor(color);
        }

        Rect rect = FontHelper.measureText("小说", contentPaint);
        contentTextHeight = rect.height();
        textIndentWidth = rect.width();

        int radius = ScreenUtils.dpToPxInt(mContext, 40);
        int left = mWidth / 2 - radius;
        int top = mHeight / 2 - radius;
        int right = mWidth / 2 + radius;
        int bottom = mHeight / 2 + radius;

        Rect showMenuRect = new Rect(left, top, right, bottom);
        showMenuRegion = new Region(showMenuRect);

//        Rect openConntentRect = new Rect(left, top, right, bottom);
//        openConntentRegion = new Region(openConntentRect);
    }

    @Override
    public void onDraw() {
        drawBackground();
        if (pageData != null) {
            drawHeader();
            drawContent();
            drawFooter();
        }
        drawChargeInfo();
        // drawAutoBuy();
        drawChargeButton();
        drawPayMoreButton();
    }

    @Override
    public boolean handleClick(float x, float y) {
        if (showMenuRegion.contains((int) x, (int) y)) {
            if (onMenuRegionClickListener != null) {
                onMenuRegionClickListener.onClick(null);
            }
            return true;
        } else if (payRegion.contains((int) x, (int) y)) {
            if (onPayChapterClickListener != null) {
                onPayChapterClickListener.onClick(null);
            }
            return true;
        } /*else if (autoPayCheckRegion.contains((int) x, (int) y)) {
            autoPayNextChapter = !autoPayNextChapter;
            if (onAutoPayNextChapterListener != null) {
                onAutoPayNextChapterListener.onClick(null);
            }
            return true;
        }*/ else if (payMoreRegion.contains((int) x, (int) y)) {
            if (onPayMoreChapterClickListener != null) {
                onPayMoreChapterClickListener.onClick(null);
            }
            return true;
        } else {
            Log.i(TAG, "onTouchEvent: not contains");
            return false;
        }
    }

    private void drawBackground() {
        mCanvas.drawColor(mContext.getResources().getColor(mPageConfig.backgroundColor));
    }

    private void drawHeader() {
        Rect rect = new Rect();
        headerPaint.getTextBounds(pageData.chapterName, 0, pageData.chapterName.length(), rect);
        int x = padding;
        int y = padding + rect.height();
        mCanvas.drawText(pageData.chapterName, x, y, headerPaint);
    }

    private void drawContent() {
        int x = padding;
        int y = headerHeight + padding;
        currentY = y;
        if (pageData.index == 0) {
            Rect rect = FontHelper.measureText(pageData.chapterName, chapterPaint);
            int pageWidth = mWidth - PageConfig.padding - PageConfig.padding;

            // 章节名太长，分两行显示，如果两行还显示不够，那就不是我的错了，不管了，爱咋地咋地，反正我只显示两行
            if (rect.width() < pageWidth) {
                mCanvas.drawText(pageData.chapterName, x, y + contentTextHeight, chapterPaint);
                currentY = currentY + 2 * contentTextHeight + mPageConfig.lineSpace;
            } else {
                //两行显示，适当缩小字体太小
                chapterPaint.setTextSize((float) (chapterPaint.getTextSize() * 0.9));
                int paintSize = chapterPaint.breakText(pageData.chapterName, true, pageWidth - PageConfig.padding, null);
                String chaper1 = pageData.chapterName.substring(0, paintSize);
                mCanvas.drawText(chaper1, x, y + contentTextHeight, chapterPaint);

                currentY = y + rect.height() + mPageConfig.lineSpace;

                String chaper2 = pageData.chapterName.substring(paintSize);
                mCanvas.drawText(chaper2, x, currentY, chapterPaint);

                currentY = y + rect.height() + mPageConfig.lineSpace + contentTextHeight;
            }
            int size = pageData.lineList.size();
            if (size >= 5) {
                size = 5;
            }
            Line line = null;
            for (int i = 0; i < size; i++) {
                line = pageData.lineList.get(i);
                y = currentY + contentTextHeight;
                mCanvas.drawText(line.text, x, y, contentPaint);
                currentY = y + mPageConfig.lineSpace;
            }
        } else {
            int size = pageData.lineList.size();
            if (size >= 5) {
                size = 5;
            }
            Line line = null;
            for (int i = 0; i < size; i++) {
                line = pageData.lineList.get(i);
                y = currentY + contentTextHeight;
                mCanvas.drawText(line.text, x, y, contentPaint);
                currentY = y + mPageConfig.lineSpace;
            }
        }
    }

    private void drawFooter() {
        drawTime();
        drawBatteryView();
//        drawPercent();
    }

    private void drawChargeInfo() {
        int x = padding;
        currentY = mHeight / 2 + ScreenUtils.dpToPxInt(mContext, 60);

        mCanvas.drawText("价格:", x, currentY, payPaint);

        int percentLen = (int) footerPaint.measureText("阅");
        x = x + percentLen * 3;
        payPaint.setColor(Color.parseColor("#DB5151"));
        String chapterprice = pageData.chapterprice + "";
        mCanvas.drawText(chapterprice, x, currentY, payPaint);
        x = x + percentLen + (int) footerPaint.measureText(chapterprice);
        payPaint.setColor(PageConfig.headerTextColor);
        mCanvas.drawText("闪闪币", x, currentY, payPaint);

        x = mWidth / 2;
        mCanvas.drawText("总余额:", x, currentY, payPaint);
        x = x + percentLen * 4 + 10;
        payPaint.setColor(Color.parseColor("#DB5151"));
        String readmoney = pageData.readmoney + "";
        mCanvas.drawText(readmoney, x, currentY, payPaint);
        payPaint.setColor(PageConfig.headerTextColor);
//        x = x+ percentLen + (int)fooderPaint.measureText(readmoney)+10;
//        mCanvas.drawText("闪闪币", x, currentY, payPaint);
    }

    private void drawChargeButton() {
        int btnHeight = currentY;

        currentY = btnHeight + 60;
        final Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.img_subscribe_chapter);
        int textWidth = (mWidth - bitmap.getWidth()) / 2;
        mCanvas.drawBitmap(bitmap, textWidth, btnHeight + 60, payPaint);

        int right = textWidth + bitmap.getWidth();
        int bottom = btnHeight + 60 + bitmap.getHeight();
        Rect rect = new Rect(textWidth, btnHeight + 60, right, bottom);
        payRegion = new Region(rect);
    }

    private void drawPayMoreButton() {
        int btnHeight = currentY;
        final Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.img_batch_subscription);
        int textWidth = (mWidth - bitmap.getWidth()) / 2;
        mCanvas.drawBitmap(bitmap, textWidth, btnHeight + bitmap.getHeight() + 60, payPaint);

        int right = textWidth + bitmap.getWidth();
        int bottom = btnHeight + bitmap.getHeight() + 60 + bitmap.getHeight();
        Rect rect = new Rect(textWidth, btnHeight + bitmap.getHeight() + 60, right, bottom);
        payMoreRegion = new Region(rect);
    }

    private void drawTime() {
        String mTime = dateFormat.format(new Date());
        int x = padding + batteryWidth + 20;
        int y = mHeight - padding;
        mCanvas.drawText(mTime, x, y, footerPaint);
    }

    private void drawBatteryView() {
        int left = padding;
        int bottom = mHeight - padding;
        int top = bottom - batteryHeight;
        int right = left + batteryWidth;

        batteryPaint.setStyle(Paint.Style.STROKE);
        Rect rect = new Rect(left, top, right, bottom);
        mCanvas.drawRect(rect, batteryPaint);

        batteryPaint.setStyle(Paint.Style.FILL);
        Rect rect2 = new Rect(right + 1, top + rect.height() / 2 - 6, right + 5, top + rect.height() / 2 + 6);
        mCanvas.drawRect(rect2, batteryPaint);

        //实际计算电量值为：44-2-2 = 40，2为左右边距
        int lenght = (int) (BatteryReceiver.batterPercent * (batteryWidth - 4));

        batteryPaint.setStyle(Paint.Style.FILL);
        Rect rec2 = new Rect(left + 2, top + 2, left + 2 + lenght, bottom - 2);
        mCanvas.drawRect(rec2, batteryPaint);
    }

    private void drawAutoBuy() {
        String autoBuy = "自动购买VIP章节";
        int len = (int) headerPaint.measureText("闪闪币");

        Rect rect = new Rect();
        headerPaint.getTextBounds(autoBuy, 0, autoBuy.length(), rect);

        int x = (mWidth - len) / 2;
        currentY = currentY + 100;

        mCanvas.drawText(autoBuy, x, currentY, payPaint);
        Bitmap bitmap = null;
        if (autoPayNextChapter) {
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.auto_buy);
        } else {
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.unauto_buy);
        }

        int left = x - 50;
        int top = currentY - bitmap.getHeight() + 6;
        mCanvas.drawBitmap(bitmap, left, top, payPaint);

        int imaPadding = 80;
        Rect autoPayRect = new Rect(left - imaPadding, top - imaPadding, left + imaPadding, top + imaPadding);
        autoPayCheckRegion = new Region(autoPayRect);
    }

    private void drawPercent() {
        int percentLen = (int) footerPaint.measureText("00.00%");
        int x = mWidth - percentLen - padding;
        int y = mHeight - padding;

        mCanvas.drawText(decimalFormat.format(pageData.percent) + "%", x, y, footerPaint);
    }

    public void setOnPayChapterClickListener(View.OnClickListener onPayChapterClickListener) {
        this.onPayChapterClickListener = onPayChapterClickListener;
    }

    public void setOnPayMoreChapterClickListener(View.OnClickListener onPayMoreChapterClickListener) {
        this.onPayMoreChapterClickListener = onPayMoreChapterClickListener;
    }

    public void setOnAutoPayNextChapterListener(View.OnClickListener onAutoPayNextChapterListener) {
        this.onAutoPayNextChapterListener = onAutoPayNextChapterListener;
    }

    public void setOnMenuRegionClickListener(View.OnClickListener onMenuRegionClickListener) {
        this.onMenuRegionClickListener = onMenuRegionClickListener;
    }
}
