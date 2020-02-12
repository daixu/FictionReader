package com.shangame.fiction.book.drawer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.shangame.fiction.R;
import com.shangame.fiction.ad.FeedAdBean;
import com.shangame.fiction.book.config.PageConfig;
import com.shangame.fiction.core.utils.BitmapUtils;
import com.shangame.fiction.core.utils.ScreenUtils;
import com.shangame.fiction.storage.manager.FileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Create by Speedy on 2019/5/8
 */
public class ADDrawer implements Drawer {

    private static final String TAG = "ADDrawer";

    private Context mContext;
    private Canvas mCanvas;

    private Paint contentPaint;

    private int mWidth;
    private int mHeight;
    private View mExposureView;

    private boolean loadADSuccess;

    private AdRefreshListener pageRefreshListener;
    private FrameLayout mFrameLayout;

    private Path btnPath;
    private Region mRegion;

    private View.OnClickListener onOpenVideoRegionClickListener;
    private Bitmap adBitmap = null;
    private Map<String, Bitmap> bitmapMap = new HashMap<>();

    public ADDrawer(Context context, Canvas canvas) {
        this.mContext = context;
        this.mCanvas = canvas;
    }

    public void init(AdRefreshListener pageRefreshListener) {
        mWidth = mCanvas.getWidth();
        mHeight = mCanvas.getHeight();
        contentPaint = new Paint();
        contentPaint.setColor(PageConfig.contentTextColor);
        contentPaint.setStrokeWidth(1);
        contentPaint.setAntiAlias(true);
        contentPaint.setTextSize((int) ScreenUtils.spToPx(mContext, 14));

        btnPath = new Path();

        this.pageRefreshListener = pageRefreshListener;
    }

    public void setLayoutView(FrameLayout frameLayout) {
        mFrameLayout = frameLayout;
    }

    @Override
    public void onDraw() {
        PageConfig mPageConfig = PageConfig.getInstance(mContext);
        mCanvas.drawColor(mContext.getResources().getColor(mPageConfig.getBackgroundColor()));

        TTFeedAd ttFeedAd = FeedAdBean.getInstance().getFeedAd();
        if (null != ttFeedAd) {
            drawAd(ttFeedAd);
        }
    }

    private void drawAd(TTFeedAd ttFeedAd) {
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
                loadAdImg(url, defaultCover, ttFeedAd);
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

    private void loadAdImg(final String url, final Bitmap defaultCover, final TTFeedAd ttFeedAd) {
        Log.e("hhh", "loadAdImg");
        Observable.just(url).map(new Function<String, Bitmap>() {
            @Override
            public Bitmap apply(String s) throws Exception {
                return Glide.with(mContext)
                        .asBitmap()
                        .load(url)
                        .submit(mWidth - 80, (mHeight / 4))
                        .get();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        adBitmap = bitmap;
                        defaultCover.recycle();

                        bitmapMap.put(url, adBitmap);

                        if (pageRefreshListener != null) {
                            pageRefreshListener.onPageRefresh();
                        }

                        addListener(ttFeedAd);
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

    @Override
    public boolean handleClick(float x, float y) {
        Log.e("hhh", "x= " + x + " ,y= " + y);
        if (null != mRegion && mRegion.contains((int) x, (int) y)) {
            Log.e("hhh", "mRegion= " + mRegion);
            Log.i("hhh", "handleClick onTouchEvent: not contains");
            if (onOpenVideoRegionClickListener != null) {
                onOpenVideoRegionClickListener.onClick(null);
            }
            return true;
        }
        return true;
    }

    private void loadAdFile(final String url, final Bitmap defaultCover, final TTFeedAd ttFeedAd, final File file) {
        Observable.just(url).map(new Function<String, Bitmap>() {
            @Override
            public Bitmap apply(String s) throws Exception {
                return Glide.with(mContext)
                        .asBitmap()
                        .load(file)
                        .submit(mWidth - 80, (mHeight / 4))
                        .get();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        adBitmap = bitmap;
                        defaultCover.recycle();

                        bitmapMap.put(url, adBitmap);

                        if (pageRefreshListener != null) {
                            pageRefreshListener.onPageRefresh();
                        }

                        addListener(ttFeedAd);
                    }
                });
    }

    public void setOnOpenVideoRegionClickListener(View.OnClickListener onOpenVideoRegionClickListener) {
        this.onOpenVideoRegionClickListener = onOpenVideoRegionClickListener;
    }
}
