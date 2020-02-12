package com.shangame.fiction.book.render;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.view.MotionEvent;

import com.shangame.fiction.book.pageflip.OnPageFlipListener;
import com.shangame.fiction.book.pageflip.PageFlip;

/**
 *
 * Create by Speedy on 2018/8/6
 */
public abstract class PageRender implements OnPageFlipListener {

    private final static String TAG = "PageRender";

    public final static int MSG_ENDED_DRAWING_FRAME = 1;
    public final static int MSG_REFRESH_FRAME = 2;
    public final static int MSG_CHANGE_FRAME = 3;

    final static int DRAW_MOVING_FRAME = 0;
    final static int DRAW_ANIMATING_FRAME = 1;
    final static int DRAW_FULL_PAGE = 2;

    int mPageIndex;
    int mDrawCommand;


    Canvas mCanvas;
    Bitmap mBitmap;

    Context mContext;
    Handler mHandler;
    PageFlip mPageFlip;

    public PageRender(Context context, PageFlip pageFlip,
                      Handler handler) {
        mContext = context;
        mPageFlip = pageFlip;
        mPageIndex = 0;
        mDrawCommand = DRAW_FULL_PAGE;
        mCanvas = new Canvas();
        mPageFlip.setListener(this);
        mHandler = handler;
    }



    public int getPageIndex() {
        return mPageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.mPageIndex = pageIndex;
    }

    /**
     * Release resources
     */
    public void release() {
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
        mPageFlip.setListener(null);
        mCanvas = null;
    }



    /**
     * Handle finger moving event
     *
     * @param x x coordinate of finger moving
     * @param y y coordinate of finger moving
     * @return true if event is handled
     */
    public boolean onFingerMove(float x, float y) {
        mDrawCommand = DRAW_MOVING_FRAME;
        return true;
    }

    /**
     * Handle finger up event
     *
     * @param x x coordinate of finger up
     * @param y y coordinate of inger up
     * @return true if event is handled
     */
    public boolean onFingerUp(float x, float y) {
        if (mPageFlip.animating()) {
            mDrawCommand = DRAW_ANIMATING_FRAME;
            return true;
        }

        return false;
    }


    public abstract int getPageCount();

    /**
     * Render page frame
     */
    public abstract void onDrawFrame();

    /**
     * Handle surface changing event
     *
     * @param width surface width
     * @param height surface height
     */
    public abstract void onSurfaceChanged(int width, int height);

    /**
     * Handle drawing ended event
     *
     * @param what draw command
     * @return true if render is needed
     */
    public abstract boolean onEndedDrawing(int what);

    public abstract boolean handleClick(float x, float y);


}
