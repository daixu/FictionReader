package com.shangame.fiction.book.parser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.shangame.fiction.R;
import com.shangame.fiction.ad.ADConfig;
import com.shangame.fiction.book.config.PageConfig;
import com.shangame.fiction.book.helper.FontHelper;
import com.shangame.fiction.book.helper.PaintHelper;
import com.shangame.fiction.book.page.Line;
import com.shangame.fiction.book.page.PageData;
import com.shangame.fiction.core.manager.AppSetting;
import com.shangame.fiction.core.utils.ScreenUtils;
import com.shangame.fiction.net.api.ApiConstant;
import com.shangame.fiction.net.response.AdBean;
import com.shangame.fiction.storage.model.BookParagraph;
import com.shangame.fiction.storage.model.ChapterInfo;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.shangame.fiction.core.constant.SharedKey.AD_FREE;
import static com.shangame.fiction.core.constant.SharedKey.IS_NO_AD;

/**
 * Create by Speedy on 2018/8/9
 */
public class TextChapterParser implements ChapterParser {

    private static final String TAG = "TextChapterParser";

    private Context mContext;

    private int pageWidth;
    private int pageHeight;

    private PageConfig mPageConfig;

    public TextChapterParser(Context context) {
        mContext = context;
        initPageConfig(context.getApplicationContext());
        pageWidth = (int) (ScreenUtils.getScreenWidth(mContext) - 2 * ScreenUtils.spToPx(context, PageConfig.padding));
        //预留一行字的高度做间距
        pageHeight = (int) (ScreenUtils.getScreenHeight(mContext) - ScreenUtils.spToPx(context, PageConfig.headerHeight) - ScreenUtils.spToPx(context, PageConfig.footerHeight)) - mPageConfig.fontSize - mPageConfig.lineSpace - 90;
    }

    private void initPageConfig(Context context) {
        mPageConfig = PageConfig.getInstance(context);
    }

    public void updateConfig() {
        initPageConfig(mContext);
    }

    @Override
    public void parseChapter(final ChapterInfo chapterInfo, final List<BookParagraph> bookParagraphList, final ChapterParserListener chapterParserListener) {
        //TODO 待优化,后期处理
        Observable.create(new ObservableOnSubscribe<List<PageData>>() {
            @Override
            public void subscribe(ObservableEmitter<List<PageData>> emitter) throws Exception {
                Paint paint = PaintHelper.getContentPaint(mPageConfig.fontSize);
                List<Line> lineList = new LinkedList<>();
                for (int i = 0; i < bookParagraphList.size(); i++) {
                    List<Line> lines = parserParagraph(bookParagraphList.get(i), paint, pageWidth);
                    lineList.addAll(lines);
                }

                List<PageData> pageDataList = parseData(chapterInfo, lineList, paint);
                // List<PageData> pageDataList = parseData1(chapterInfo, lineList, paint);
                emitter.onNext(pageDataList);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<PageData>>() {
                    @Override
                    public void accept(List<PageData> list) throws Exception {
                        if (chapterParserListener != null) {
                            chapterParserListener.parseFinish(list);
                        }
                    }
                }, new Consumer<Throwable>() {

                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "parseChapter: ", throwable);
                    }
                });
    }

    /**
     * 根据屏幕尺寸将段落全部转换适合屏幕宽的行
     *
     * @param bookParagraph
     * @param paint
     * @param pageWidth
     * @return
     */
    @Override
    public List<Line> parserParagraph(BookParagraph bookParagraph, Paint paint, int pageWidth) {
        List<Line> lineList = new LinkedList<>();
        String paragraph = bookParagraph.paragraph;
        //添加段落缩进
        if (!bookParagraph.paragraph.startsWith("   ")) {
            paragraph = "        " + paragraph;
        }
        int index = 0;
        while (paragraph.length() > 0) {
            int paintSize = paint.breakText(paragraph, true, pageWidth, null);
            Line line = new Line();
            line.index = index++;
            line.chapterId = bookParagraph.chapterId;
            line.paragraphId = bookParagraph.pid;

            if (paragraph.startsWith("，")
                    || paragraph.startsWith(",")
                    || paragraph.startsWith("。")
                    || paragraph.startsWith("？")
                    || paragraph.startsWith("?")
                    || paragraph.startsWith("、")
                    || paragraph.startsWith("！")
                    || paragraph.startsWith("。")
                    || paragraph.startsWith("!")
                    || paragraph.startsWith("；")) {
                if (paintSize > 0) {
                    line.text = paragraph.substring(1, paintSize);

                    if (lineList.size() > 0) {
                        Line beforeLine = lineList.get(lineList.size() - 1);
                        beforeLine.text = beforeLine.text + paragraph.substring(0, 1);
                    }
                }
            } else {
                line.text = paragraph.substring(0, paintSize);
            }

            //递归遍历剩下自动
            paragraph = paragraph.substring(paintSize);

            lineList.add(line);
        }
        return lineList;
    }

    private List<PageData> parseData(final ChapterInfo chapterInfo, List<Line> lineList, Paint paint) {
        float size = lineList.size();

        int normalMaxLineCount = computeNormalPageLineCount(paint);
        Log.e("hhh", "normalMaxLineCount= " + normalMaxLineCount);
        int normalPageSize = (int) Math.ceil(size / normalMaxLineCount);
        Log.e("hhh", "normalPageSize= " + normalPageSize);

        int adMaxLineCount = computePageLineCount(paint);
        Log.e("hhh", "adMaxLineCount= " + adMaxLineCount);

        List<PageData> pageDataList = new LinkedList<>();

        int newPageIndex = 0;
        int newCurrentIndex = 0;

        int startIndex = 0;
        int endIndex = 0;

        boolean isSubscription; // 是否订阅

        if (chapterInfo.chargingmode == ApiConstant.ChapterAuth.VIP_READ && chapterInfo.buystatus == ApiConstant.PayStatus.NOT_PAID) {
            isSubscription = false;
        } else {
            isSubscription = true;
        }

        Log.e("hhh", "isSubscription= " + isSubscription);

        do {
            PageData pageData = new PageData();
            pageData.index = newPageIndex++;
            pageData.currentIndex = newCurrentIndex++;
            pageData.bookId = chapterInfo.bookid;
            pageData.bookname = chapterInfo.bookname;
            pageData.nextChapterId = chapterInfo.nextcid;
            pageData.beforeChapterId = chapterInfo.lastcid;
            pageData.chapterId = chapterInfo.cid;
            pageData.chapterIndex = chapterInfo.sort;

            int verify = AdBean.getInstance().getVerify();
            if (verify == 0) {
                boolean isNoAd = AppSetting.getInstance(mContext).getBoolean(IS_NO_AD, false);
                // boolean adFree = AppSetting.getInstance(mContext).getBoolean(AD_FREE, false);
                Log.e("hhh", "chapterInfo.advertopen= " + chapterInfo.advertopen + " ,isNoAd= " + isNoAd /*+ " ,adFree= " + adFree*/);
                if (chapterInfo.advertopen == 1 && (ADConfig.AD_INTERVAL_PAGE > 0) && !isNoAd /*&& !adFree*/) {
                    if (newPageIndex != 0 && (newPageIndex % ADConfig.AD_INTERVAL_PAGE) == 0) {
                        pageData.hasAdPage = true;
                        endIndex = startIndex + adMaxLineCount;
                    } else {
                        endIndex = startIndex + normalMaxLineCount;
                    }
                } else {
                    endIndex = startIndex + normalMaxLineCount;
                }
            } else {
                endIndex = startIndex + normalMaxLineCount;
            }
//            if (newPageIndex != 0 && (newPageIndex % 3) == 0) {
//                pageData.hasAdPage = true;
//                endIndex = startIndex + adMaxLineCount;
//            } else {
//                endIndex = startIndex + normalMaxLineCount;
//            }
            pageData.chapterName = chapterInfo.title;
            pageData.isVipPage = !isSubscription;
            pageData.readmoney = chapterInfo.readmoney;
            pageData.chapterprice = chapterInfo.chapterprice;
            if (newPageIndex == 0) {
                //第一章第一页添加章节在内容页显示，占用2行文字内容
                startIndex = 0;
                endIndex = Math.min(normalMaxLineCount - 1, lineList.size());
            } else {
                endIndex = Math.min(endIndex, lineList.size());

                if (endIndex == lineList.size() - 1) {
                    //如果lineList.size()刚好是maxLineCount整的整数倍，由于添加章节标题占用过1行，所以，最后一页需要多加载一行，
                    endIndex++;
                }
            }

            pageData.lineList = lineList.subList(startIndex, endIndex);

            startIndex = endIndex;
            pageData.chapterPageSize = newPageIndex;

            pageDataList.add(pageData);
        } while (startIndex < size && endIndex < size && isSubscription);

        double averagePercent = 1F / chapterInfo.chaptecount;
        double pagePercent = averagePercent / newPageIndex;
        double chapterPercent = chapterInfo.sort * averagePercent;
        Log.i(TAG, "subscribe: chapterInfo.sort = " + chapterInfo.sort + " chapterInfo.chaptecount = " + chapterInfo.chaptecount);
        Log.i(TAG, "subscribe: chapterPercent = " + chapterPercent + " averagePercent = " + averagePercent);

        for (int i = 0; i < pageDataList.size(); i++) {
            PageData pageData = pageDataList.get(i);
            //计算当前章节进度
            pageData.percent = 100F * (pageData.index + 1) / newPageIndex;
            pageData.bookPercent = 100 * (chapterPercent + pagePercent * (i + 1));
            Log.i(TAG, "subscribe: pageData.bookPercent = " + pageData.bookPercent);
        }

        int verify = AdBean.getInstance().getVerify();
        if (verify == 0) {
            boolean isNoAd = AppSetting.getInstance(mContext).getBoolean(IS_NO_AD, false);
            // boolean adFree = AppSetting.getInstance(mContext).getBoolean(AD_FREE, false);
//            Log.e("hhh",  "adFree= " + adFree);
            if (chapterInfo.advertopen == 1 && !isNoAd /*&& !adFree*/) {
                PageData adPageData = new PageData();
                adPageData.isADPage = true;
                adPageData.index = newPageIndex + 1;
                adPageData.currentIndex = newCurrentIndex + 1;
                adPageData.bookId = chapterInfo.bookid;
                adPageData.bookname = chapterInfo.bookname;
                adPageData.nextChapterId = chapterInfo.nextcid;
                adPageData.beforeChapterId = chapterInfo.lastcid;
                adPageData.chapterId = chapterInfo.cid;
                adPageData.chapterIndex = chapterInfo.sort;
                adPageData.isVipPage = false;
                adPageData.chapterName = chapterInfo.title;
                adPageData.readmoney = 0;
                adPageData.chapterprice = 0;
                adPageData.lineList = new LinkedList<>();

                adPageData.chapterPageSize = newPageIndex;
                pageDataList.add(adPageData);
            }
        }

        return pageDataList;
    }

    private int computeNormalPageLineCount(Paint paint) {
        Rect rect = FontHelper.measureText("小说", paint);
        int itemHeight = rect.height() + mPageConfig.lineSpace;
        int normalMaxLineCount = (int) ((pageHeight - 140) / itemHeight);
        Log.e("hhh", "normalMaxLineCount= " + normalMaxLineCount);
        return normalMaxLineCount;
    }

    /**
     * 计算每一页可以放多行文字
     *
     * @param paint
     * @return
     */
    private int computePageLineCount(Paint paint) {
        Rect rect = FontHelper.measureText("小说", paint);
        int itemHeight = rect.height() + mPageConfig.lineSpace;
        Bitmap defaultBg = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ad_bg);
        Log.e("hhh", "defaultBg.getHeight()= " + defaultBg.getHeight());
        int adMaxLineCount = (int) ((pageHeight - defaultBg.getHeight() - mPageConfig.lineSpace - 90) / itemHeight);
        Log.e("hhh", "adMaxLineCount1= " + adMaxLineCount);
        return adMaxLineCount;
    }

    private List<PageData> parseData1(final ChapterInfo chapterInfo, List<Line> lineList, Paint paint) {
        float size = lineList.size();

        int normalMaxLineCount = computeNormalPageLineCount(paint);
        Log.e("hhh", "normalMaxLineCount= " + normalMaxLineCount);
        int normalPageSize = (int) Math.ceil(size / normalMaxLineCount);
        Log.e("hhh", "normalPageSize= " + normalPageSize);

        List<PageData> pageDataList = new LinkedList<>();

        //计算当前章节用多是页
        int pageSize = (int) Math.ceil(size / normalMaxLineCount);

        int fromIndex = 0;
        int toIndex = normalMaxLineCount;
        int pageIndex = 0;
        int currentIndex = 0;

        // TODO 封面
//        if (chapterInfo.lastcid == 0) {
//            PageData pageData = new PageData();
//            pageData.isCoverPage = true;
//            pageData.index = pageIndex++;
//            pageData.currentIndex = currentIndex++;
//            pageData.bookId = chapterInfo.bookid;
//            pageData.bookname = chapterInfo.bookname;
//            pageData.nextChapterId = chapterInfo.nextcid;
//            pageData.beforeChapterId = chapterInfo.lastcid;
//            pageData.chapterId = chapterInfo.cid;
//            pageData.chapterIndex = chapterInfo.sort;
//            pageData.isVipPage = false;
//            pageData.isADPage = false;
//
//            BookCover bookCover = new BookCover();
//            bookCover.author = chapterInfo.author;
//            bookCover.bookcover = chapterInfo.bookcover;
//            bookCover.booksource = chapterInfo.booksource;
//            bookCover.bookid = chapterInfo.bookid;
//            bookCover.bookname = chapterInfo.bookname;
//            bookCover.synopsis = chapterInfo.synopsis;
//            pageData.bookCover = bookCover;
//
//            pageDataList.add(pageData);
//        }

        double averagePercent = 1F / chapterInfo.chaptecount;
        double pagePercent = averagePercent / pageSize;
        double chapterPercent = chapterInfo.sort * averagePercent;
//      Log.i(TAG, "subscribe: chapterInfo.sort = "+chapterInfo.sort + " chapterInfo.chaptecount = "+chapterInfo.chaptecount );
//      Log.i(TAG, "subscribe: chapterPercent = "+chapterPercent + " averagePercent = "+averagePercent );

        boolean isSubscription; // 是否订阅

        if (chapterInfo.chargingmode == ApiConstant.ChapterAuth.VIP_READ && chapterInfo.buystatus == ApiConstant.PayStatus.NOT_PAID) {
            isSubscription = false;
        } else {
            isSubscription = true;
        }

        Log.e("hhh", "isSubscription= " + isSubscription);
        if (!isSubscription) {
            pageSize = 1;
        }

        for (int i = 0; i < pageSize; i++) {
            PageData pageData = new PageData();
            pageData.index = pageIndex++;
            pageData.currentIndex = currentIndex++;
            pageData.bookId = chapterInfo.bookid;
            pageData.bookname = chapterInfo.bookname;
            pageData.nextChapterId = chapterInfo.nextcid;
            pageData.beforeChapterId = chapterInfo.lastcid;
            pageData.chapterId = chapterInfo.cid;
            pageData.chapterIndex = chapterInfo.sort;
            pageData.chapterPageSize = (int) pageSize;
            if (i != 0 && (i % 3) == 0) {
                pageData.hasAdPage = true;
            }
            //计算当前章节进度
            pageData.percent = 100F * (pageData.index + 1) / pageSize;
            pageData.bookPercent = 100 * (chapterPercent + pagePercent * (i + 1));
            Log.i(TAG, "subscribe: pageData.bookPercent = "+pageData.bookPercent);
            pageData.chapterName = chapterInfo.title;
            pageData.isVipPage = !isSubscription;
            pageData.readmoney = chapterInfo.readmoney;
            pageData.chapterprice = chapterInfo.chapterprice;

            if (i == 0) {
                //第一章第一页添加章节在内容页显示，占用2行文字内容
                fromIndex = 0;
                toIndex = Math.min(normalMaxLineCount - 1, lineList.size());
            } else {
                toIndex = toIndex + normalMaxLineCount;
                toIndex = Math.min(toIndex, lineList.size());

                if (toIndex == lineList.size() - 1) {
                    //如果lineList.size()刚好是maxLineCount整的整数倍，由于添加章节标题占用过1行，所以，最后一页需要多加载一行，
                    toIndex++;
                }
            }

            pageData.lineList = lineList.subList(fromIndex, toIndex);

            fromIndex = toIndex;
            pageDataList.add(pageData);

//            int verify = AdBean.getInstance().getVerify();
//            if (verify == 0) {
//                boolean isNoAd = AppSetting.getInstance(mContext).getBoolean(IS_NO_AD, false);
//                Log.e("hhh", "chapterInfo.advertopen= " + chapterInfo.advertopen + " ,isNoAd= " + isNoAd);
//                if (chapterInfo.advertopen == 1 && (ADConfig.AD_INTERVAL_PAGE > 0) && !isNoAd) {
//                    if (i != 0 && (i % ADConfig.AD_INTERVAL_PAGE) == 0) {
//                        PageData adPageData = new PageData();
//                        adPageData.isADPage = true;
//                        adPageData.index = pageIndex++;
//                        adPageData.bookId = chapterInfo.bookid;
//                        adPageData.bookname = chapterInfo.bookname;
//                        adPageData.chapterId = chapterInfo.cid;
//                        adPageData.chapterIndex = chapterInfo.sort;
//                        adPageData.nextChapterId = chapterInfo.nextcid;
//                        adPageData.beforeChapterId = chapterInfo.lastcid;
//                        adPageData.isVipPage = false;
//                        pageDataList.add(adPageData);
//                    }
//                }
//            }
        }

        return pageDataList;
    }

}
