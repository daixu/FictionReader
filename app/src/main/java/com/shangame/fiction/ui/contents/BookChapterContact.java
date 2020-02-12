package com.shangame.fiction.ui.contents;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.ChapterListResponse;
import com.shangame.fiction.net.response.ChapterDetailResponse;

/**
 * Create by Speedy on 2018/8/17
 */
public class BookChapterContact {

    interface View extends BaseContract.BaseView{
        void getChapterListSuccess(ChapterListResponse response);
        void getChapterDetailSuccess(ChapterDetailResponse chapterDetailResponse);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V>{
        void getChapterList(long userid, long bookId, int page, int pageSize);
        void getChapterDetail(long userid, long bookid , long cid);
    }
}
