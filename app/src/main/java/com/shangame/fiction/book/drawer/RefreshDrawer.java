package com.shangame.fiction.book.drawer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.shangame.fiction.R;
import com.shangame.fiction.book.config.PageConfig;
import com.shangame.fiction.book.helper.FontHelper;
import com.shangame.fiction.book.helper.PaintHelper;
import com.shangame.fiction.core.utils.ScreenUtils;

/**
 * Create by Speedy on 2018/8/30
 */
public class RefreshDrawer implements Drawer {

    private Context mContext;
    private Canvas mCanvas;

    private int mWidth;
    private int mHeight;

    public RefreshDrawer(Context context, Canvas canvas) {
        this.mContext = context;
        this.mCanvas = canvas;
    }

    public void init() {
        mWidth = mCanvas.getWidth();
        mHeight = mCanvas.getHeight();
    }

    @Override
    public void onDraw() {
        drawBackground();
        drawLoading();
    }

    @Override
    public boolean handleClick(float x, float y) {
        return false;
    }

    private void drawBackground() {
        PageConfig pageConfig = PageConfig.getInstance(mContext);
        mCanvas.drawColor(mContext.getResources().getColor(pageConfig.backgroundColor));
    }

    private void drawLoading() {
        int size = (int) ScreenUtils.spToPx(mContext, PageConfig.FontSize.SIZE_3);
        Paint paint = PaintHelper.getContentPaint(size);
        String msg = mContext.getString(R.string.book_loading);

        Rect rect = FontHelper.measureText(msg, paint);

        int x = (mWidth - rect.width()) / 2;
        int y = (mHeight - rect.height()) / 2;

        mCanvas.drawText(msg, x, y, paint);
    }
}
