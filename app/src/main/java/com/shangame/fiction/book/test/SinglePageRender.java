/*
 * Copyright (C) 2016 eschao <esc.chao@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shangame.fiction.book.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.shangame.fiction.ad.FeedAdBean;
import com.shangame.fiction.book.drawer.ADDrawer;
import com.shangame.fiction.book.drawer.AdRefreshListener;
import com.shangame.fiction.book.drawer.ChargePageDrawer;
import com.shangame.fiction.book.drawer.CoverDrawer;
import com.shangame.fiction.book.drawer.PageDrawer;
import com.shangame.fiction.book.drawer.PageRefreshListener;
import com.shangame.fiction.book.drawer.RefreshDrawer;
import com.shangame.fiction.book.loader.ChapterLoader;
import com.shangame.fiction.book.loader.ChapterLoaderObserver;
import com.shangame.fiction.book.page.Line;
import com.shangame.fiction.book.page.PageData;
import com.shangame.fiction.book.pageflip.Page;
import com.shangame.fiction.book.pageflip.PageFlip;
import com.shangame.fiction.book.pageflip.PageFlipState;
import com.shangame.fiction.book.render.RenderAdapter;
import com.shangame.fiction.core.manager.AppSetting;
import com.shangame.fiction.core.utils.RedoUtil;
import com.shangame.fiction.ui.reader.ReadParameter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.shangame.fiction.core.constant.SharedKey.IS_NO_AD;

/**
 * Single page render
 * <p>
 * Every page need 2 texture in single page mode:
 * <ul>
 * <li>First texture: current page content</li>
 * <li>Back texture: back of front content, it is same with first texture
 * </li>
 * <li>Second texture: next page content</li>
 * </ul>
 * </p>
 *
 * @author eschao
 */

public class SinglePageRender extends PageRender implements ChapterLoaderObserver {

    private static final String TAG = "SinglePageRender";

    private PageDrawer pageDrawer;

    private RefreshDrawer refreshDrawer;

    private ChargePageDrawer chargePageDrawer;

    private ADDrawer adDrawer;

    private CoverDrawer coverDrawer;

    private RenderAdapter renderAdapter;

    private ChapterLoader chapterLoader;

    private boolean isLoading;
    private boolean isJumpBeforeChapter;//标记是否为跳转至前一章
    private FrameLayout mFrameLayout;

    private boolean hasChanged = false;//标记当前 Page 页是否内容改变

    /**
     * Constructor
     *
     * @see {@link #SinglePageRender(Context, PageFlip, Handler, int)}
     */
    public SinglePageRender(Context context, PageFlip pageFlip, Handler handler, int pageNo) {
        super(context, pageFlip, handler, pageNo);
        pageDrawer = new PageDrawer(mContext, mCanvas);
        refreshDrawer = new RefreshDrawer(mContext, mCanvas);
        chargePageDrawer = new ChargePageDrawer(mContext, mCanvas);
        adDrawer = new ADDrawer(mContext, mCanvas);
        coverDrawer = new CoverDrawer(mContext, mCanvas);
    }

    public void setLayoutView(FrameLayout frameLayout) {
        mFrameLayout = frameLayout;
    }

    private void drawPage(int pageIndex, int method) {
        if (renderAdapter != null) {
            final PageData pageData = renderAdapter.getItem(pageIndex);
            if (pageData != null) {
                if (pageData.isCoverPage) {
                    coverDrawer.initPage(pageData, new PageRefreshListener() {
                        @Override
                        public void onPageRefresh(PageData oldData) {
                            Log.e("hhh", "112");
                            // refreshPage();
                        }
                    });
                    coverDrawer.onDraw();
                }
//                else if (pageData.isADPage) {
//                    boolean isNoAd = AppSetting.getInstance(mContext).getBoolean(IS_NO_AD, false);
//                    Log.e("hhh", "121 pageData.isADPage isNoAd= " + isNoAd + " ,method= " + method);
//                    if (isNoAd) {
//                        switch (method) {
//                            case 0:
//                                drawPage(++mPageNo, -1);
//                                break;
//                            case 1:
//                                drawPage(--mPageNo, -1);
//                                break;
//                            default:
//                                break;
//                        }
//                    } else {
//                        adDrawer.init(new AdRefreshListener() {
//                            @Override
//                            public void onPageRefresh() {
//                                Log.e("hhh", "137");
//                                // refreshPage();
//                            }
//                        });
//                        adDrawer.setLayoutView(mFrameLayout);
//                        adDrawer.onDraw();
//                    }
//                }
                else if (pageData.isVipPage) {
                    chargePageDrawer.initPage(pageData);
                    chargePageDrawer.onDraw();
                } else {
                    Log.e("hhh", "150");
                    pageDrawer.initPage(pageData, new PageRefreshListener() {
                        @Override
                        public void onPageRefresh(PageData oldData) {
                            Log.e("hhh", "153 pageData.chapterIndex= " + pageData.chapterIndex + " ,oldData.chapterIndex= " + oldData.chapterIndex);
                            // refreshPage();
                        }
                    });
                    pageDrawer.setLayoutView(mFrameLayout);
                    pageDrawer.onDraw();
                }
            } else {
                refreshDrawer.init();
                refreshDrawer.onDraw();
            }
        } else {
            refreshDrawer.init();
            refreshDrawer.onDraw();
        }
    }

    public List<PageData> getChapterPageData(long chapterId) {
        return renderAdapter.getChapterPageData(chapterId);
    }

    public void resetPage() {
        PageData pageData = renderAdapter.getItem(mPageNo);
        if (pageData != null) {
            mPageNo = pageData.index;
            renderAdapter.resetPage(pageData);
        }
    }

    public void setRenderAdapter(RenderAdapter renderAdapter) {
        this.renderAdapter = renderAdapter;
        this.renderAdapter.registerRenderObserver(this);
    }

    public int getProgress() {
        PageData pageData = renderAdapter.getItem(mPageNo);
        if (pageData != null) {
            return (int) pageData.percent;
        } else {
            return 0;
        }
    }

    public void setProgress(int progress) {
        PageData pageData = renderAdapter.getItem(mPageNo);
        if (pageData != null) {
            mPageNo = renderAdapter.getPageByProgress(pageData.chapterId, progress);
            hasChanged = true;
            refreshPage();
        }
    }

    public void refreshPage() {
        Log.e("hhh", "refreshPage");
        Message msg = Message.obtain();
        msg.what = MSG_REFRESH_FRAME;
        msg.arg1 = DRAW_FULL_PAGE;
        mHandler.sendMessage(msg);
    }

    public boolean showNextPage() {
        if (canFlipForward()) {
            mPageNo++;
            refreshPage();
            return true;
        } else {
            return false;
        }
    }

    /**
     * If page can flip forward
     *
     * @return true if it can flip forward
     */
    @Override
    public boolean canFlipForward() {
        boolean hanNext = mPageNo < getPageCount();
        if (getPageCount() - mPageNo < 3) {
            if (chapterLoader != null) {
                //预加载
                if (mPageNo < getPageCount() - 1) {
                    PageData pageData = renderAdapter.getItem(renderAdapter.getPageCount() - 1);
                    if (pageData != null && pageData.nextChapterId != 0) {
                        if (!isLoading && !RedoUtil.isQuickDoubleClick(System.currentTimeMillis())) {
                            isLoading = true;
                            //提前预加载
                            chapterLoader.loadNextChapter(pageData.bookId, pageData.nextChapterId, false, 0, "canFlipForward1");
                        }
                    } else {
                        isLoading = false;
                        return true;
                    }
                } else if (getPageCount() >= 1) {
                    PageData lastPageData = renderAdapter.getItem(mPageNo);
                    if (lastPageData != null) {
                        if (lastPageData.nextChapterId != 0) {
                            isLoading = true;
                            chapterLoader.loadNextChapter(lastPageData.bookId, lastPageData.nextChapterId, true, 0, "canFlipForward2");
                        }  else {
                            isLoading = false;
                            chapterLoader.loadNextChapter(lastPageData.bookId, 0, false, 0, "canFlipForward3");
                        }
                    } else {
                        isLoading = false;
                        chapterLoader.loadNextChapter(0, 0, false, 0, "canFlipForward4");
                    }
                    return false;
                } else {
                    return false;
                }
            }
        }
        return hanNext;
    }

    @Override
    public int getPageCount() {
        return renderAdapter.getPageCount();
    }

    /**
     * Draw frame
     */
    @Override
    public void onDrawFrame() {
        // 1. delete unused textures
        mPageFlip.deleteUnusedTextures();
        Page page = mPageFlip.getFirstPage();

        // 2. handle drawing command triggered from finger moving and animating
        if (mDrawCommand == DRAW_MOVING_FRAME ||
                mDrawCommand == DRAW_ANIMATING_FRAME) {
            // is forward flip
            if (mPageFlip.getFlipState() == PageFlipState.FORWARD_FLIP) {
                // check if second texture of first page is valid, if not,
                // create new one
                if (!page.isSecondTextureSet()) {
                    // drawPage(mPageNo + 1);
                    Log.e("hhh", "293");
                    drawPage(mPageNo + 1, 0);
                    page.setSecondTexture(mBitmap);
                }
            }
            // in backward flip, check first texture of first page is valid
            else if (!page.isFirstTextureSet()) {
                // drawPage(--mPageNo);
                Log.e("hhh", "301");
                drawPage(--mPageNo, 1);
                page.setFirstTexture(mBitmap);
            }

            // draw frame for page flip
            mPageFlip.drawFlipFrame();
        }
        // draw stationary page without flipping
        else if (mDrawCommand == DRAW_FULL_PAGE) {
            Log.e("hhh", "DRAW_FULL_PAGE page.isFirstTextureSet()= " + page.isFirstTextureSet() + " ,hasChanged= " + hasChanged);
            if (hasChanged || !page.isFirstTextureSet()) {
                hasChanged = false;
                boolean isCloseAd = FeedAdBean.getInstance().isCloseAd();
                if (isCloseAd) {
                    Log.e("hhh", "isCloseAd1111 mPageNo= " + mPageNo);
                    final PageData pageData = renderAdapter.getItem(mPageNo);
                    if (null != pageData) {
                        List<PageData> list = renderAdapter.getChapterPageData(pageData.chapterId);
                        List<PageData> pageDataList = new LinkedList<>();
                        for (PageData data : list) {
                            if (!data.isADPage) {
                                pageDataList.add(data);
                            }
                        }
                        Log.e("hhh", pageDataList.size() + "");
                        renderAdapter.replaceNormalPage(pageDataList);
                        mPageNo = pageDataList.size() - 1;
                        drawPage(mPageNo, -1);
                        FeedAdBean.getInstance().setCloseAd(false);
                    }
                } else {
                    Log.e("hhh", "isCloseAd2222");
                    drawPage(mPageNo, -1);
                }
                page.setFirstTexture(mBitmap);
            }

            mPageFlip.drawPageFrame();

            Message msg = Message.obtain();
            msg.what = MSG_CHANGE_FRAME;
            mHandler.sendMessage(msg);
        }

        // 3. send message to main thread to notify drawing is ended so that
        // we can continue to calculate next animation frame if need.
        // Remember: the drawing operation is always in GL thread instead of
        // main thread
        Log.e("hhh", "----351----");
        Message msg = Message.obtain();
        msg.what = MSG_ENDED_DRAWING_FRAME;
        msg.arg1 = mDrawCommand;
        mHandler.sendMessage(msg);
    }

    /**
     * Handle GL surface is changed
     *
     * @param width  surface width
     * @param height surface height
     */
    @Override
    public void onSurfaceChanged(int width, int height) {
        // recycle bitmap resources if need
        if (mBitmap != null) {
            mBitmap.recycle();
        }

        // create bitmap and canvas for page
        //mBackgroundBitmap = background;
        Page page = mPageFlip.getFirstPage();
        mBitmap = Bitmap.createBitmap((int) page.width(), (int) page.height(),
                Bitmap.Config.ARGB_8888);
        mCanvas.setBitmap(mBitmap);
    }

    /**
     * Handle ended drawing event
     * In here, we only tackle the animation drawing event, If we need to
     * continue requesting render, please return true. Remember this function
     * will be called in main thread
     *
     * @param what event type
     * @return ture if need render again
     */
    @Override
    public boolean onEndedDrawing(int what) {
        if (what == DRAW_ANIMATING_FRAME) {
            boolean isAnimating = mPageFlip.animating();
            // continue animating
            if (isAnimating) {
                mDrawCommand = DRAW_ANIMATING_FRAME;
                return true;
            }
            // animation is finished
            else {
                final PageFlipState state = mPageFlip.getFlipState();
                // update page number for backward flip
                if (state == PageFlipState.END_WITH_BACKWARD) {
                    // don't do anything on page number since mPageNo is always
                    // represents the FIRST_TEXTURE no;
                }
                // update page number and switch textures for forward flip
                else if (state == PageFlipState.END_WITH_FORWARD) {
                    mPageFlip.getFirstPage().setFirstTextureWithSecond();
                    mPageNo++;
                    mPageFlip.setFlipState(PageFlipState.END_WITH_FINISH);
                }

                Log.e("hhh", "onEndedDrawing");
                mDrawCommand = DRAW_FULL_PAGE;
                ReadParameter.getInstance().setResume(false);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean handleClick(float x, float y) {
        PageData pageData = getCurrentPageData();
        if (pageData != null) {
            if (pageData.isVipPage) {
                return chargePageDrawer.handleClick(x, y);
            } else if (pageData.isCoverPage) {
                return false;
            }/* else if (pageData.isADPage) {
                return adDrawer.handleClick(x, y);
            }*/ else if (pageDrawer != null && !pageData.hasAdPage) {
                return pageDrawer.handleClick(x, y);
            }
        }
        return false;
    }

    public PageData getCurrentPageData() {
        if (null != renderAdapter) {
            PageData pageData = renderAdapter.getItem(mPageNo);
            if (null != pageData) {
                return pageData;
            } else {
                pageData = new PageData();
                return pageData;
            }
        } else {
            PageData pageData = new PageData();
            return pageData;
        }
        // return renderAdapter.getItem(mPageIndex);
    }

    /**
     * If page can flip backward
     *
     * @return true if it can flip backward
     */
    @Override
    public boolean canFlipBackward() {
        if (mPageNo > 0) {
            mPageFlip.getFirstPage().setSecondTextureWithFirst();
            return true;
        } else {
            if (chapterLoader != null) {
                PageData pageData = renderAdapter.getItem(0);
                if (pageData != null) {
                    if (!isLoading && !RedoUtil.isQuickDoubleClick(System.currentTimeMillis())) {
                        isLoading = true;
                        chapterLoader.loadBeforeChapter(pageData.bookId, pageData.beforeChapterId);
                    }
                } else {
                    isJumpBeforeChapter = false;
                    isLoading = true;
                    chapterLoader.loadBeforeChapter(0, 0);
                }
            }
            return false;
        }
    }

    public boolean showBeforePage() {
        if (canFlipBackward()) {
            mPageNo--;
            refreshPage();
            return true;
        } else {
            return false;
        }
    }

    public void setChapterLoader(ChapterLoader chapterLoader) {
        this.chapterLoader = chapterLoader;
    }

    public void jumpToNextChapter() {
        PageData pageData = renderAdapter.getItem(mPageNo);
        if (pageData != null) {
            int newIndex = mPageNo;
            for (int i = mPageNo; i < getPageCount(); i++) {
                if (pageData.chapterId != renderAdapter.getItem(i).chapterId) {
                    break;
                }
                newIndex++;
            }
            if (newIndex < getPageCount()) {
                mPageNo = newIndex;
                refreshPage();
            } else {
                mPageNo = getPageCount();
                isLoading = true;
                chapterLoader.loadNextChapter(pageData.bookId, pageData.nextChapterId, true, 0, "jumpToNextChapter");
            }
        }
    }

    public void jumpToBeforeChapter() {
        PageData pageData = renderAdapter.getItem(mPageNo);
        if (pageData != null) {
            int newIndex = mPageNo;
            for (int i = mPageNo; i >= 0; i--) {
                PageData temp = renderAdapter.getItem(i);
                if (pageData.chapterId != temp.chapterId && temp.index == 0) {
                    break;
                }
                newIndex--;
            }

            PageData beforePageData = renderAdapter.getItem(newIndex);
            if (beforePageData != null) {
                mPageNo = newIndex;
                refreshPage();
            } else {
                isJumpBeforeChapter = true;
                isLoading = true;
                mPageNo = 0;
                chapterLoader.loadBeforeChapter(pageData.bookId, pageData.beforeChapterId);
            }
        }
    }

    @Override
    public void notifyChapterLoadFinished(int showPageIndex, int type) {
        mPageNo = showPageIndex;
        finishLoading();
        if (mPageNo == getPageCount() - 1) {
            PageData pageData = getCurrentPageData();
            Log.e("hhh", "type510= " + type);
            chapterLoader.loadNextChapter(pageData.bookId, pageData.nextChapterId, false, type, "notifyChapterLoadFinished");//提前预加载
        }
    }

    @Override
    public void notifyNextChapterLoadFinished(int newCount, int oldCount) {
        if (newCount > 100) {
            List<PageData> pageDataList = renderAdapter.getPageDataList();
            PageData pageData = renderAdapter.getItem(mPageNo);
            for (int i = 0; i < pageDataList.size(); i++) {
                PageData temp = pageDataList.get(i);
                if (temp.chapterId == pageData.chapterId) {
                    break;
                } else {
                    pageDataList.remove(temp);
                    mPageNo--;
                }
            }

        }
        // mPageFlipView.onFingerMove(50, 80);
        finishLoading();
    }

    @Override
    public void notifyBeforeChapterLoadFinished(int newCount, int oldCount) {
        if (isJumpBeforeChapter) {
            mPageNo = 0;
        } else {
            mPageNo = newCount - oldCount - 1;//更新索引位置
            if (mPageNo < 0) {
                mPageNo = 0;
            }
        }
        finishLoading();
    }

    @Override
    public void notifyResetPageSuccess() {
        updateConfig();
        Page firstPage = mPageFlip.getFirstPage();
        if (firstPage != null) {
            firstPage.deleteAllTextures();
        }
        refreshPage();
    }

    public void updateConfig() {
        hasChanged = true;
        pageDrawer.updateConfig();
        refreshPage();
    }

    @Override
    public void notifyJumpToBookMarkPage(int index) {
        mPageNo = index;
        refreshPage();
    }

    @Override
    public void notifyReplaceChargePage() {
        mPageNo = 0;
        hasChanged = true;
        refreshPage();
    }

    public void finishLoading() {
        isLoading = false;
        hasChanged = true;
        refreshPage();
    }

    public void setOnPayChapterClickListener(View.OnClickListener onPayChapterClickListener) {
        this.chargePageDrawer.setOnPayChapterClickListener(onPayChapterClickListener);
    }

    public void setOnPayMoreChapterClickListener(View.OnClickListener onPayMoreChapterClickListener) {
        this.chargePageDrawer.setOnPayMoreChapterClickListener(onPayMoreChapterClickListener);
    }

    public void setOnAutoPayNextChapterListener(View.OnClickListener onAutoPayNextChapterListener) {
        this.chargePageDrawer.setOnAutoPayNextChapterListener(onAutoPayNextChapterListener);
    }

    public void setOnMenuRegionClickListener(View.OnClickListener onMenuRegionClickListener) {
        this.pageDrawer.setOnMenuRegionClickListener(onMenuRegionClickListener);
    }

    public void setOnOpenVideoRegionClickListener1(View.OnClickListener onOpenVideoRegionClickListener) {
        this.pageDrawer.setOnOpenVideoRegionClickListener1(onOpenVideoRegionClickListener);
    }

    public void setOnOpenMenuRegionClickListener(View.OnClickListener onOpenMenuRegionClickListener) {
        this.chargePageDrawer.setOnMenuRegionClickListener(onOpenMenuRegionClickListener);
    }

    public void setOnOpenVideoRegionClickListener(View.OnClickListener onOpenVideoRegionClickListener) {
        this.adDrawer.setOnOpenVideoRegionClickListener(onOpenVideoRegionClickListener);
    }
}
