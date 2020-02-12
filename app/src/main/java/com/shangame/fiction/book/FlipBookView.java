package com.shangame.fiction.book;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.shangame.fiction.book.loader.ChapterLoader;
import com.shangame.fiction.book.page.PageData;
import com.shangame.fiction.book.pageflip.PageFlip;
import com.shangame.fiction.book.pageflip.PageFlipException;
import com.shangame.fiction.book.render.FlipPageRender;
import com.shangame.fiction.book.render.PageRender;
import com.shangame.fiction.book.render.RenderAdapter;
import com.shangame.fiction.core.manager.Logger;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Create by Speedy on 2018/8/6
 */
public class FlipBookView extends GLSurfaceView implements GLSurfaceView.Renderer {

    private final static String TAG = "FlipBookView";

    Handler mHandler;
    PageFlip mPageFlip;
    FlipPageRender mPageRender;
    ReentrantLock mDrawLock;

    int mAnimateDuration = 1000;//1000毫秒
    boolean isAutoPage = true;
    int pixelsOfMesh = 10;

    private RenderAdapter renderAdapter;

    private ChapterLoader chapterLoader;

    private View.OnClickListener onPayChapterClickListener;
    private View.OnClickListener onPayMoreChapterClickListener;
    private View.OnClickListener onAutoPayNextChapterListener;

    private View.OnClickListener onMenuRegionClickListener;
    private View.OnClickListener onOpenMenuRegionClickListener;
    private OnChangePageListener onChangePageListener;
    private View.OnClickListener onOpenVideoRegionClickListener;

    private float downTouchX;
    private float downTouchY;

    private FrameLayout mFrameLayout;

    public FlipBookView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        initHandler();
        mPageFlip = new PageFlip(context);
        mPageFlip.setSemiPerimeterRatio(0.8f)
                .setShadowWidthOfFoldEdges(5, 60, 0.3f)
                .setShadowWidthOfFoldBase(5, 80, 0.4f)
                .setPixelsOfMesh(pixelsOfMesh)
                .enableAutoPage(isAutoPage);

        mDrawLock = new ReentrantLock();
        mPageRender = new FlipPageRender(context, mPageFlip, mHandler);
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case PageRender.MSG_ENDED_DRAWING_FRAME:
                        try {
                            mDrawLock.lock();
                            // notify page render to handle ended drawing
                            // message
                            if (mPageRender != null &&
                                    mPageRender.onEndedDrawing(msg.arg1)) {
                                requestRender();
                            }
                        } finally {
                            mDrawLock.unlock();
                        }
                        break;
                    case PageRender.MSG_REFRESH_FRAME:
                        try {
                            mDrawLock.lock();
                            if (mPageRender != null) {
                                requestRender();
                            }
                        } finally {
                            mDrawLock.unlock();
                        }
                        break;
                    case PageRender.MSG_CHANGE_FRAME:
                        PageData pageData = mPageRender.getCurrentPageData();
                        onChangePageListener.onChangePage(pageData);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public FlipBookView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setLayoutView(FrameLayout frameLayout) {
        mFrameLayout = frameLayout;
    }

    public void updateConfig() {
        mPageRender.updateConfig();
    }

    public void resetPage() {
        mPageRender.resetPage();
    }

    public void refreshPage() {
        mPageRender.refreshPage();
    }

    public PageData getCurrentPageData() {
        return mPageRender.getCurrentPageData();
    }

    public List<PageData> getChapterPageData(long chapterId) {
        return mPageRender.getChapterPageData(chapterId);
    }

    public int getProgress() {
        return mPageRender.getProgress();
    }

    public void setProgress(int progress) {
        mPageRender.setProgress(progress);
    }

    public void setRenderAdapter(RenderAdapter renderAdapter) {
        this.renderAdapter = renderAdapter;
        mPageRender.setRenderAdapter(renderAdapter);
    }

    public void setChapterLoader(ChapterLoader chapterLoader) {
        this.chapterLoader = chapterLoader;
        mPageRender.setChapterLoader(chapterLoader);
    }

    public void finishLoading() {
        mPageRender.finishLoading();
    }

    public void jumpToNextChapter() {
        mPageRender.jumpToNextChapter();
    }

    public void jumpToBeforeChapter() {
        mPageRender.jumpToBeforeChapter();
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        try {
            mPageFlip.onSurfaceCreated();
        } catch (PageFlipException e) {
            Logger.e(TAG, "Failed to run FlipPageRender:onSurfaceCreated");
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        try {
            mPageFlip.onSurfaceChanged(width, height);

            int pageIndex = mPageRender.getPageIndex();
            mPageRender.release();

            mPageRender = new FlipPageRender(getContext(), mPageFlip, mHandler);
            mPageRender.setPageIndex(pageIndex);
            mPageRender.onSurfaceChanged(width, height);
            mPageRender.setRenderAdapter(renderAdapter);
            mPageRender.setChapterLoader(chapterLoader);

            mPageRender.setLayoutView(mFrameLayout);

            mPageRender.setOnPayChapterClickListener(onPayChapterClickListener);
            mPageRender.setOnPayMoreChapterClickListener(onPayMoreChapterClickListener);
            mPageRender.setOnAutoPayNextChapterListener(onAutoPayNextChapterListener);
            mPageRender.setOnMenuRegionClickListener(onMenuRegionClickListener);
            mPageRender.setOnOpenMenuRegionClickListener(onOpenMenuRegionClickListener);
            mPageRender.setOnOpenVideoRegionClickListener(onOpenVideoRegionClickListener);
        } catch (Exception e) {
            Logger.e(TAG, "Failed to run FlipPageRender:onSurfaceCreated");
        }
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        try {
            mDrawLock.lock();
            if (mPageRender != null) {
                mPageRender.onDrawFrame();
            }
        } finally {
            mDrawLock.unlock();
        }
    }


    /**
     * Handle finger down event
     *
     * @param x finger x coordinate
     * @param y finger y coordinate
     */
    public void onFingerDown(float x, float y) {
        downTouchX = x;
        downTouchY = y;
        if (!mPageFlip.isAnimating() &&
                mPageFlip.getFirstPage() != null) {
            mPageFlip.onFingerDown(x, y);
        }
    }

    /**
     * Handle finger moving event
     *
     * @param x finger x coordinate
     * @param y finger y coordinate
     */
    public void onFingerMove(float x, float y) {
        if (mPageFlip.isAnimating()) {
            // nothing to do during animating
        } else if (mPageFlip.canAnimate(x, y)) {
            // if the point is out of current page, try to start animating
            onFingerUp(x, y);
        }
        // move page by finger
        else if (mPageFlip.onFingerMove(x, y)) {
            try {
                mDrawLock.lock();
                if (mPageRender != null &&
                        mPageRender.onFingerMove(x, y)) {
                    requestRender();
                }
            } finally {
                mDrawLock.unlock();
            }
        }
    }

    /**
     * Handle finger up event and start animating if need
     *
     * @param x finger x coordinate
     * @param y finger y coordinate
     */
    public void onFingerUp(float x, float y) {
        downTouchX = 0;
        downTouchY = 0;
        if (!mPageFlip.isAnimating()) {
//            Log.i(TAG, "onFingerUp: 111");
            try {
                mPageFlip.onFingerUp(x, y, mAnimateDuration);
                mDrawLock.lock();
                if (mPageRender != null &&
                        mPageRender.onFingerUp(x, y)) {
                    requestRender();
                }
            } catch (Exception e) {
                Logger.e(TAG, "onFingerUp: Exception", e);
                mPageFlip.abortAnimating();
            } finally {
                mDrawLock.unlock();
            }
        } else {
            Log.i(TAG, "mPageFlip.isAnimating()");
        }
    }

    public boolean handleClick(float x, float y) {
        boolean boolX1 = false;
        boolean boolY1 = false;
        int minToleranceRange = 0;
        int maxToleranceRange = 5;
        boolean downX = downTouchX - x >= minToleranceRange && downTouchX - x <= maxToleranceRange;
        boolean downX2 = x - downTouchX >= minToleranceRange && x - downTouchX < maxToleranceRange;
        boolean downY = downTouchY - y >= minToleranceRange && downTouchY - y <= maxToleranceRange;
        boolean downY2 = y - downTouchY >= minToleranceRange && y - downTouchY < maxToleranceRange;
        if (downX || downX2) {
            boolX1 = true;
        }
        if (downY || downY2) {
            boolY1 = true;
        }

        if (!mPageFlip.isAnimating() && boolX1 && boolY1) {
            return mPageRender.handleClick(x, y);
        } else {
            return false;
        }
    }

    public boolean showNextPage() {
        return mPageRender.showNextPage();
    }

    public boolean showBeforePage() {
        return mPageRender.showBeforePage();
    }

    public void setOnPayChapterClickListener(View.OnClickListener onPayChapterClickListener) {
        this.onPayChapterClickListener = onPayChapterClickListener;
        this.mPageRender.setOnPayChapterClickListener(onPayChapterClickListener);
    }

    public void setOnPayMoreChapterClickListener(View.OnClickListener onPayMoreChapterClickListener) {
        this.onPayMoreChapterClickListener = onPayMoreChapterClickListener;
        this.mPageRender.setOnPayMoreChapterClickListener(onPayMoreChapterClickListener);
    }

    public void setOnAutoPayNextChapterListener(View.OnClickListener onAutoPayNextChapterListener) {
        this.onAutoPayNextChapterListener = onAutoPayNextChapterListener;
        this.mPageRender.setOnAutoPayNextChapterListener(onAutoPayNextChapterListener);
    }

    public void setOnMenuRegionClickListener(View.OnClickListener onMenuRegionClickListener) {
        this.onMenuRegionClickListener = onMenuRegionClickListener;
        this.mPageRender.setOnMenuRegionClickListener(onMenuRegionClickListener);
    }

    public void setOnOpenMenuRegionClickListener(View.OnClickListener onOpenMenuRegionClickListener) {
        this.onOpenMenuRegionClickListener = onOpenMenuRegionClickListener;
        this.mPageRender.setOnOpenMenuRegionClickListener(onOpenMenuRegionClickListener);
    }

    public void setOnOpenVideoRegionClickListener(View.OnClickListener onOpenVideoRegionClickListener) {
        this.onOpenVideoRegionClickListener = onOpenVideoRegionClickListener;
        this.mPageRender.setOnOpenVideoRegionClickListener(onOpenVideoRegionClickListener);
    }

    public void setOnChangePageListener(OnChangePageListener onChangePageListener) {
        this.onChangePageListener = onChangePageListener;
    }

    public interface OnChangePageListener {
        void onChangePage(PageData pageData);
    }
}
