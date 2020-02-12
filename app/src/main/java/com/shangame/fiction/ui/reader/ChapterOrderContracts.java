package com.shangame.fiction.ui.reader;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.ChapterOrderConfigResponse;

/**
 * Create by Speedy on 2018/9/3
 */
public interface ChapterOrderContracts {

    interface View extends BaseContract.BaseView{
        void getChapterOrderConfigSuccess(ChapterOrderConfigResponse chapterOrderConfigResponse);
        void bugChapterOrderSuccess();
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getChapterOrderConfig(long userid, long bookid , long chapterid);
        void bugChapterOrder(long userid, long bookid , long chapterid,int subnumber,boolean autoPayNextChapter);
    }
}
