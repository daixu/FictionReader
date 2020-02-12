package com.shangame.fiction.ui.reader;

import android.content.Context;
import android.util.Log;

import com.shangame.fiction.book.page.Line;
import com.shangame.fiction.book.page.PageData;
import com.shangame.fiction.book.parser.ChapterParserListener;
import com.shangame.fiction.book.parser.TextChapterParser;
import com.shangame.fiction.book.render.RenderAdapter;
import com.shangame.fiction.net.response.ChapterDetailResponse;
import com.shangame.fiction.storage.model.BookMark;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Create by Speedy on 2018/8/31
 */
public class PageRenderAdapter extends RenderAdapter {

    private Context mContext;
    private List<PageData> pageDataList = new LinkedList<PageData>();

    private Object mLocked = new Object();

    public PageRenderAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getPageCount() {
        return pageDataList.size();
    }

    @Override
    public PageData getItem(int index) {
        if (pageDataList.size() > index && index >= 0) {
            return pageDataList.get(index);
        } else {
            return null;
        }
    }

    @Override
    public List<PageData> getChapterPageData(long chapterId) {
        List<PageData> list = new ArrayList<>();
        for (PageData pageData : pageDataList) {
            if (pageData.chapterId == chapterId) {
                list.add(pageData);
            }
        }
        return list;
    }

    @Override
    public List<PageData> getPageDataList() {
        return pageDataList;
    }

    @Override
    public void addChapterPage(List<PageData> list, int showPageIndex, int type) {
        synchronized (mLocked) {
            pageDataList.clear();
            if (list != null && list.size() > 0) {
                if (!pageDataList.containsAll(list)) {
                    pageDataList.addAll(list);
                }
            }
        }
        Log.e("hhh", "type72= " + type);
        notifyChapterLoadFinished(showPageIndex, type);
    }

    /**
     * 替换收费章节
     */
    public void replaceChargePage(List<PageData> list) {
        pageDataList.clear();
        pageDataList.addAll(list);
        notifyReplaceChargePage();
    }

    @Override
    public void replaceNormalPage(List<PageData> list) {
        pageDataList.clear();
        pageDataList.addAll(list);
        Log.e("hhh", "pageDataList= " + pageDataList.size());
    }

    @Override
    public void addNextChapterPage(List<PageData> list) {
        if (list != null && list.size() > 0) {
            int oldCount = pageDataList.size();
            synchronized (mLocked) {
                if (!pageDataList.containsAll(list)) {
                    pageDataList.addAll(list);
                }
            }
            int newCount = pageDataList.size();
            notifyNextChapterLoadFinished(newCount, oldCount);
        }
    }

    @Override
    public void addBeforeChapterPage(List<PageData> list) {
        if (list != null && list.size() > 0) {
            int oldCount = pageDataList.size();
            synchronized (mLocked) {
                if (!pageDataList.containsAll(list)) {
                    pageDataList.addAll(0, list);
                }
            }
            int newCount = pageDataList.size();
            notifyBeforeChapterLoadFinished(newCount, oldCount);
        }
    }

    @Override
    public int getPageByProgress(long chapterId, int progress) {
        PageData pageData;
        final int count = getPageCount();
        int index = 0;
        for (int i = 0; i < count; i++) {
            index = i;
            pageData = getItem(i);
            if (pageData.chapterId == chapterId) {
                index = i;
                if (pageData.percent > progress) {
                    return index;
                }
            }
        }
        return index;
    }

    @Override
    public void jumpToBookMarkPage(List<PageData> list, BookMark bookMark) {
        synchronized (mLocked) {
            pageDataList.clear();
            if (list != null && list.size() > 0) {
                synchronized (mLocked) {
                    if (!pageDataList.containsAll(list)) {
                        pageDataList.addAll(list);
                    }
                }
            }
        }

        PageData pageData;
        for (int i = 0; i < pageDataList.size(); i++) {
            pageData = pageDataList.get(i);
            if (pageData.chapterId == bookMark.chapterid) {
                Line line;
                for (int j = 0; j < pageData.lineList.size(); j++) {
                    line = pageData.lineList.get(j);
                    if (line.paragraphId == bookMark.pid) {
                        if (line.text.equals(bookMark.content)) {
                            notifyJumpToBookMarkPage(i);
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void resetPage(PageData pageData) {
        BookLoadPresenter bookLoadPresenter = new BookLoadPresenter(mContext);
        ChapterDetailResponse chapterDetailResponse = bookLoadPresenter.queryCacheChapter(pageData.bookId, pageData.chapterId);
        if (chapterDetailResponse != null) {
            TextChapterParser textChapterParser = new TextChapterParser(mContext);
            textChapterParser.parseChapter(chapterDetailResponse.chaptermode, chapterDetailResponse.textdata, new ChapterParserListener() {

                @Override
                public void parseFinish(List<PageData> list) {
                    synchronized (mLocked) {
                        pageDataList.clear();
                        pageDataList.addAll(list);
                    }
                    notifyResetPageSuccess();
                }
            });
        }
    }
}
