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
import android.widget.FrameLayout;

import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.shangame.fiction.R;
import com.shangame.fiction.ad.FeedAdBean;
import com.shangame.fiction.book.config.PageConfig;
import com.shangame.fiction.book.helper.FontHelper;
import com.shangame.fiction.book.helper.PaintHelper;
import com.shangame.fiction.book.page.Line;
import com.shangame.fiction.book.page.PageData;
import com.shangame.fiction.core.utils.BitmapUtils;
import com.shangame.fiction.core.utils.ScreenUtils;
import com.shangame.fiction.storage.manager.FileManager;
import com.shangame.fiction.ui.reader.BatteryReceiver;
import com.shangame.fiction.widget.GlideApp;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by Speedy on 2018/8/30
 */
public class PageDrawer implements Drawer {

    private static DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private int batteryWidth;
    private int batteryHeight;
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
    private Paint adPaint;
    private int currentY;
    private int contentTextHeight;
    private int textIndentWidth;
    private int padding;
    private int headerHeight;
    private int footerHeight;
    private PageConfig mPageConfig;

    private Region showMenuRegion;

    private Bitmap adBitmap = null;
    private Map<String, Bitmap> bitmapMap = new HashMap<>();

    private PageRefreshListener pageRefreshListener;
    private FrameLayout mFrameLayout;

    private Region mRegion;

    private View.OnClickListener onMenuRegionClickListener;
    private View.OnClickListener onOpenVideoRegionClickListener1;

    public PageDrawer(Context context, Canvas canvas) {
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

    public void updateConfig() {
        initPageConfig(mContext);
        PaintHelper.clear();//刷新笔
    }

    public void initPage(PageData pageData, PageRefreshListener pageRefreshListener) {
        mWidth = mCanvas.getWidth();
        mHeight = mCanvas.getHeight();

        int radius = ScreenUtils.dpToPxInt(mContext, 100);
        int left = mWidth / 2 - radius;
        int top = mHeight / 2 - radius;
        int right = mWidth / 2 + radius;
        int bottom = mHeight / 2 + radius;

        Rect showMenuRect = new Rect(left, top, right, bottom);
        showMenuRegion = new Region(showMenuRect);

        this.pageData = pageData;

        headerPaint = PaintHelper.getHeaderPaint(mContext);
        int fontSize = mPageConfig.fontSize;
        chapterPaint = PaintHelper.getChapterPaint(fontSize);
        contentPaint = PaintHelper.getContentPaint(fontSize);
        footerPaint = PaintHelper.getFooterPaint(mContext);
        batteryPaint = PaintHelper.getBatteryPaint(mContext);
        adPaint = PaintHelper.getAdPaint(mContext);

        if (mPageConfig.dayMode == PageConfig.DayMode.NIGHT_MODE) {
            int color = Color.parseColor("#ffffff");
            headerPaint.setColor(color);
            chapterPaint.setColor(color);
            contentPaint.setColor(color);
            footerPaint.setColor(color);
            batteryPaint.setColor(color);
            adPaint.setColor(color);
        }

        Rect rect = FontHelper.measureText("小说", contentPaint);
        contentTextHeight = rect.height();
        textIndentWidth = rect.width();

        this.pageRefreshListener = pageRefreshListener;
    }

    public void setLayoutView(FrameLayout frameLayout) {
        mFrameLayout = frameLayout;
    }

    @Override
    public void onDraw() {
        drawBackground();
        if (pageData != null) {
            if (pageData.isADPage) {
                TTFeedAd ttFeedAd = FeedAdBean.getInstance().getFeedAd();
                if (null != ttFeedAd) {
                    drawAdPage(ttFeedAd);
                }
            } else {
                drawHeader();
                drawContent();
                drawFooter();
            }
        }
    }

    @Override
    public boolean handleClick(float x, float y) {
        if (pageData.isADPage) {
            if (null != mRegion && mRegion.contains((int) x, (int) y)) {
                Log.e("hhh", "mRegion= " + mRegion);
                Log.i("hhh", "handleClick onTouchEvent: not contains");
                if (onOpenVideoRegionClickListener1 != null) {
                    onOpenVideoRegionClickListener1.onClick(null);
                }
                return true;
            }
        } else {
            if (showMenuRegion != null && showMenuRegion.contains((int) x, (int) y)) {
                if (onMenuRegionClickListener != null) {
                    onMenuRegionClickListener.onClick(null);
                    return true;
                }
            }
        }
        return false;
    }

    private void drawBackground() {
        mCanvas.drawColor(mContext.getResources().getColor(mPageConfig.backgroundColor));
    }

    private void drawAdPage(TTFeedAd ttFeedAd) {
        //绘制广告图片
        final String url = ttFeedAd.getImageList().get(0).getImageUrl();
        Log.e("hhh", "url= " + url);
        Bitmap bm = bitmapMap.get(url);
        if (bm == null) {
            final Bitmap defaultCover = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.book_ad_default_bg);
            adBitmap = defaultCover;

            String fileName = url.substring(url.lastIndexOf("/") + 1);
            String path = FileManager.getInstance(mContext).getCacheDir() + File.separator + fileName;
            final File file = new File(path);
            if (file.exists()) {
                Log.e("hhh", "file.exists() " + path);
                // loadAdFile(url, defaultCover, ttFeedAd, file);
                loadAdForFile(url, defaultCover, ttFeedAd, path);
            } else {
                loadAdImg(url, defaultCover, ttFeedAd, pageData);
            }
        }

        // 画广告背景
        final Bitmap defaultBg = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ad_bg);
        int bgHeight = (mHeight / 4) - 80;
        mCanvas.drawBitmap(defaultBg, 0, bgHeight, contentPaint);

        // 画广告标题文字
        int titleHeight = mHeight / 4;
        mCanvas.drawText(ttFeedAd.getTitle(), 30, titleHeight, contentPaint);
        int width = adBitmap.getWidth();
        int height = adBitmap.getHeight();

        // 画免广告按钮
        int btnHeight = bgHeight + defaultBg.getHeight();
        final Bitmap noAdBtn = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_no_ad);
        int textWidth = (mWidth - noAdBtn.getWidth()) / 2;
        mCanvas.drawBitmap(noAdBtn, textWidth, btnHeight + 100, contentPaint);
        // mCanvas.drawText("看视频免15分钟广告", textWidth, y + 350, contentPaint);

        int right = textWidth + noAdBtn.getWidth();
        int bottom = btnHeight + 100 + noAdBtn.getHeight();
        Rect rect = new Rect(textWidth, btnHeight + 100, right, bottom);
        mRegion = new Region(rect);

        // mCanvas.drawRect(rect, contentPaint);

        // 画广告详情文字
        String desc = ttFeedAd.getDescription();
        if (desc.length() > 20) {
            desc = desc.substring(0, 15) + "...";
        }
        mCanvas.drawText(desc, 30, btnHeight - 30, contentPaint);

        // 画广告图片
        mCanvas.drawBitmap(adBitmap, 40, titleHeight + 30, contentPaint);
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
                String chapter1 = pageData.chapterName.substring(0, paintSize);
                mCanvas.drawText(chapter1, x, y, chapterPaint);

                currentY = y + rect.height() + (mPageConfig.lineSpace / 2);

                String chapter2 = pageData.chapterName.substring(paintSize);
                mCanvas.drawText(chapter2, x, currentY, chapterPaint);

                currentY = y + rect.height() + (mPageConfig.lineSpace / 2) + contentTextHeight;
            }
            for (Line line : pageData.lineList) {
                y = currentY + contentTextHeight;
                mCanvas.drawText(line.text, x, y, contentPaint);
                currentY = y + mPageConfig.lineSpace;
            }
        } else {
            if (null != pageData.lineList) {
                if (pageData.lineList.size() > 3) {
                    for (int i = 0; i < 3; i++) {
                        Line line = pageData.lineList.get(i);
                        y = currentY + contentTextHeight;
                        mCanvas.drawText(line.text, x, y, contentPaint);
                        currentY = y + mPageConfig.lineSpace;
                    }

                    if (pageData.hasAdPage) {
                        TTFeedAd ttFeedAd = FeedAdBean.getInstance().getFeedAd();
                        if (null != ttFeedAd) {
                            drawAd(ttFeedAd, pageData);
                        }
                    }

                    for (int i = 3; i < pageData.lineList.size(); i++) {
                        Line line = pageData.lineList.get(i);
                        y = currentY + contentTextHeight;
                        mCanvas.drawText(line.text, x, y, contentPaint);
                        currentY = y + mPageConfig.lineSpace;
                    }
                } else {
                    for (Line line : pageData.lineList) {
                        y = currentY + contentTextHeight;
                        mCanvas.drawText(line.text, x, y, contentPaint);
                        currentY = y + mPageConfig.lineSpace;
                    }
                }
            }
        }
    }

    private void drawFooter() {
        drawTime();
        drawBatteryView();
        drawPercent();
    }

    private void drawAd(TTFeedAd ttFeedAd, PageData pageData) {
        //绘制广告图片
        final String url = ttFeedAd.getImageList().get(0).getImageUrl();
        Log.e("hhh", "url= " + url);
        Bitmap bm = bitmapMap.get(url);
        if (bm == null) {
            final Bitmap defaultCover = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.book_ad_default_bg);
            adBitmap = defaultCover;

            String fileName = url.substring(url.lastIndexOf("/") + 1);
            String path = FileManager.getInstance(mContext).getCacheDir() + File.separator + fileName;
            final File file = new File(path);
            if (file.exists()) {
                Log.e("hhh", "file.exists() " + path);
                // loadAdFile(url, defaultCover, ttFeedAd, file);
                loadAdForFile(url, defaultCover, ttFeedAd, path);
            } else {
                loadAdImg(url, defaultCover, ttFeedAd, pageData);
            }
        }

        // 画广告背景
        final Bitmap defaultBg = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ad_bg);
        int bgHeight = currentY;
        mCanvas.drawBitmap(defaultBg, 0, bgHeight, adPaint);

        // 画广告标题文字
        int titleHeight = currentY + 80;
        mCanvas.drawText(ttFeedAd.getTitle(), 30, titleHeight, adPaint);
        int width = adBitmap.getWidth();
        int height = adBitmap.getHeight();

        // 画广告详情文字
        String desc = ttFeedAd.getDescription();
        if (desc.length() > 20) {
            desc = desc.substring(0, 15) + "...";
        }
        mCanvas.drawText(desc, 30, bgHeight + defaultBg.getHeight() - 30, adPaint);

        currentY = currentY + defaultBg.getHeight() + mPageConfig.lineSpace;

        // 画广告图片
        mCanvas.drawBitmap(adBitmap, 40, titleHeight + 30, adPaint);
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
        int length = (int) (BatteryReceiver.batterPercent * (batteryWidth - 4));

        batteryPaint.setStyle(Paint.Style.FILL);
        Rect rec2 = new Rect(left + 2, top + 2, left + 2 + length, bottom - 2);
        mCanvas.drawRect(rec2, batteryPaint);
    }

    private void drawPercent() {
        int percentLen = (int) footerPaint.measureText("00.00%");
        int x = mWidth - percentLen - padding;
        int y = mHeight - padding;

        if (pageData.bookPercent > 100) {
            mCanvas.drawText("100%", x, y, footerPaint);
        } else {
            mCanvas.drawText(decimalFormat.format(pageData.bookPercent) + "%", x, y, footerPaint);
        }
    }

    private void loadAdForFile(final String url, final Bitmap defaultCover, final TTFeedAd ttFeedAd, final String path) {
        Log.e("hhh", "loadAdForFile");
        adBitmap = BitmapUtils.decodeBitmap(path, mWidth - 80, (mHeight / 4));
        bitmapMap.put(url, adBitmap);

        Observable.just(url).map(new Function<String, String>() {
            @Override
            public String apply(String url) throws Exception {
                Log.e("hhh", "url= " + url);
                return "url" + url;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        defaultCover.recycle();
                        addListener(ttFeedAd);
                    }
                });
    }

    private void loadAdImg(final String url, final Bitmap defaultCover, final TTFeedAd ttFeedAd, final PageData pageData) {
        Log.e("hhh", "loadAdImg");
        Observable.just(url).map(new Function<String, Bitmap>() {
            @Override
            public Bitmap apply(String s) throws Exception {
                Bitmap bitmap = null;
                try {
                    bitmap = GlideApp.with(mContext)
                            .asBitmap()
                            .load(url)
                            .submit(mWidth - 80, (mHeight / 4))
                            .get();
                } catch (Exception e) {
                    Log.e("hhh", e.getMessage());
                }
                return bitmap;
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        if (null != bitmap) {
                            adBitmap = bitmap;
                            defaultCover.recycle();

                            bitmapMap.put(url, adBitmap);
                        }
                        if (pageRefreshListener != null) {
                            pageRefreshListener.onPageRefresh(pageData);
                        }
                        addListener(ttFeedAd);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("hhh", "loadAdImg ", throwable);
                    }
                });
    }

    private void addListener(TTFeedAd ttFeedAd) {
        //可以被点击的view, 也可以把convertView放进来意味item可被点击
        List<View> clickViewList = new ArrayList<>();
        clickViewList.add(mFrameLayout);
        //触发创意广告的view（点击下载或拨打电话）
        List<View> creativeViewList = new ArrayList<>();
        creativeViewList.add(mFrameLayout);
        ttFeedAd.registerViewForInteraction(mFrameLayout, clickViewList, creativeViewList, new TTNativeAd.AdInteractionListener() {
            @Override
            public void onAdClicked(View view, TTNativeAd ad) {
                if (ad != null) {
                    Log.e("hhh", "广告" + ad.getTitle() + "被点击");
                }
            }

            @Override
            public void onAdCreativeClick(View view, TTNativeAd ad) {
                if (ad != null) {
                    Log.e("hhh", "广告" + ad.getTitle() + "被创意按钮被点击");
                }
            }

            @Override
            public void onAdShow(TTNativeAd ad) {
                if (ad != null) {
                    Log.e("hhh", "广告" + ad.getTitle() + "展示");
                }
            }
        });
    }

    private int computeMaxLineCount() {
        int contentHeight = mCanvas.getHeight() - headerHeight - footerHeight;
        Rect rect = FontHelper.measureText("小说", contentPaint);
        int itemHeight = rect.height() + (int) ScreenUtils.spToPx(mContext, mPageConfig.lineSpace);
        int maxLineCount = (int) ((contentHeight / itemHeight) - 0.5);
        return maxLineCount;
    }

    public void setOnMenuRegionClickListener(View.OnClickListener onMenuRegionClickListener) {
        this.onMenuRegionClickListener = onMenuRegionClickListener;
    }

    public void setOnOpenVideoRegionClickListener1(View.OnClickListener onOpenVideoRegionClickListener1) {
        this.onOpenVideoRegionClickListener1 = onOpenVideoRegionClickListener1;
    }
}
