package com.shangame.fiction.book.drawer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.bumptech.glide.Glide;
import com.shangame.fiction.R;
import com.shangame.fiction.book.config.PageConfig;
import com.shangame.fiction.book.helper.FontHelper;
import com.shangame.fiction.book.page.PageData;
import com.shangame.fiction.core.utils.ScreenUtils;
import com.shangame.fiction.storage.model.BookCover;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Create by Speedy on 2019/5/21
 */
public class CoverDrawer implements Drawer {

    private BookCover bookCover;

    private Context mContext;
    private Canvas mCanvas;

    private Paint mPaint;

    private int mWidth;
    private int mHeight;
    private int currentY;

    private PageConfig mPageConfig;
    private int padding;
    private Bitmap coverBitmap = null;

    private PageRefreshListener pageRefreshListener;

    public CoverDrawer(Context context, Canvas canvas) {
        this.mContext = context;
        this.mCanvas = canvas;
    }

    public void initPage(PageData pageData, PageRefreshListener pageRefreshListener) {
        mWidth = mCanvas.getWidth();
        mHeight = mCanvas.getHeight();
        mPageConfig = PageConfig.getInstance(mContext);
        this.bookCover = pageData.bookCover;
        this.pageRefreshListener = pageRefreshListener;
        padding = (int) ScreenUtils.spToPx(mContext, PageConfig.padding);
    }

    @Override
    public void onDraw() {
        mCanvas.drawColor(mContext.getResources().getColor(mPageConfig.backgroundColor));

        mPaint = new Paint();
        mPaint.setStrokeWidth(1);
        mPaint.setAntiAlias(true);

        //绘制封面
        if (coverBitmap == null) {
            final Bitmap defaultCover = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_cover);
            coverBitmap = defaultCover;
            Observable.just(bookCover.bookcover).map(new Function<String, Bitmap>() {
                @Override
                public Bitmap apply(String s) throws Exception {
                    return Glide.with(mContext)
                            .asBitmap()
                            .load(bookCover.bookcover)
                            .into(defaultCover.getWidth(), defaultCover.getHeight())
                            .get();
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<Bitmap>() {
                        @Override
                        public void accept(Bitmap bitmap) throws Exception {
                            coverBitmap = bitmap;
                            defaultCover.recycle();
                            if (pageRefreshListener != null) {
                                pageRefreshListener.onPageRefresh(null);
                            }
                        }
                    });
        }

        int coverX = (mWidth - coverBitmap.getWidth()) / 2;
        int coverY = (int) ScreenUtils.spToPx(mContext, 80);
        mCanvas.drawBitmap(coverBitmap, coverX, coverY, mPaint);

        currentY = coverY + coverBitmap.getHeight() + padding;

        // 绘制标题
        mPaint.setColor(Color.parseColor("#111111"));
        mPaint.setTextSize((int) ScreenUtils.spToPx(mContext, PageConfig.FontSize.SIZE_5));
        Rect rect = FontHelper.measureText(bookCover.bookname, mPaint);
        currentY = currentY + padding + padding;
        int x1 = (mWidth - rect.width()) / 2;
        mCanvas.drawText(bookCover.bookname, x1, currentY, mPaint);

        currentY = currentY + rect.height();

        // 绘制作者名
        mPaint.setColor(Color.parseColor("#494949"));
        mPaint.setTextSize((int) ScreenUtils.spToPx(mContext, PageConfig.FontSize.SIZE_3));
        Rect rect2 = FontHelper.measureText(bookCover.author, mPaint);
        currentY = currentY + padding;
        int x2 = (mWidth - rect2.width()) / 2;
        mCanvas.drawText(bookCover.author, x2, currentY, mPaint);

        currentY = currentY + rect2.height();

        // 绘制标语
        mPaint.setTextSize((int) ScreenUtils.spToPx(mContext, PageConfig.FontSize.SIZE_3));
        if (bookCover.synopsis.length() > 15) {
            bookCover.synopsis = bookCover.synopsis.substring(0, 15);
        }
        Rect rect3 = FontHelper.measureText(bookCover.synopsis, mPaint);
        currentY = currentY + padding + padding;
        int x3 = (mWidth - rect3.width()) / 2;
        mCanvas.drawText(bookCover.synopsis, x3, currentY, mPaint);

        // 绘制版权申明
        mPaint.setTextSize((int) ScreenUtils.spToPx(mContext, PageConfig.FontSize.SIZE_1));
        mPaint.setColor(Color.parseColor("#777479"));
        String copyright2 = "版权所有 · 侵权必究";
        Rect rect4 = FontHelper.measureText(copyright2, mPaint);
        currentY = (int) (mHeight - ScreenUtils.spToPx(mContext, 30) - rect4.height());
        int x4 = (mWidth - rect4.width()) / 2;
        mCanvas.drawText(copyright2, x4, currentY, mPaint);

        String copyright1 = bookCover.booksource;
        Rect rect5 = FontHelper.measureText(copyright1, mPaint);
        currentY = currentY - padding - rect5.height();
        int x5 = (mWidth - rect5.width()) / 2;
        mCanvas.drawText(copyright1, x5, currentY, mPaint);
    }

    @Override
    public boolean handleClick(float x, float y) {
        return false;
    }
}
