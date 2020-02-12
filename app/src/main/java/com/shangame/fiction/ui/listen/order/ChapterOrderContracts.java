package com.shangame.fiction.ui.listen.order;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.AlbumChapterFigResponse;

/**
 * Create by Speedy on 2018/9/3
 */
public interface ChapterOrderContracts {

    interface View extends BaseContract.BaseView {
        void getChapterOrderConfigSuccess(AlbumChapterFigResponse chapterOrderConfigResponse);

        void setAlbumSubChapterSuccess();
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getChapterOrderConfig(long userId, long albumId, long chapterId);

        void setAlbumSubChapter(long userId, long albumId, long chapterId, int subNumber, boolean autoPayNextChapter);
    }
}
