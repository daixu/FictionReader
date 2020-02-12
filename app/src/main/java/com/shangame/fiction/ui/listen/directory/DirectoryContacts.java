package com.shangame.fiction.ui.listen.directory;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.AlbumChapterDetailResponse;
import com.shangame.fiction.net.response.AlbumChapterResponse;
import com.shangame.fiction.net.response.AlbumSelectionResponse;

/**
 * Create by Speedy on 2018/8/13
 */
public interface DirectoryContacts {

    interface View extends BaseContract.BaseView {
        void getAlbumChapterSuccess(AlbumChapterResponse response);

        void getAlbumChapterFailure(String msg);

        void getAlbumChapterDetailSuccess(AlbumChapterDetailResponse response, AlbumChapterResponse.PageDataBean bean);

        void getAlbumChapterDetailFailure(String msg);

        void setAdvertLogSuccess();
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getAlbumChapter(long userId, int albumId, int page, int pageSize, int orderBy);

        void getAlbumChapterDetail(long userId, int albumId, int cid, String deviceId, AlbumChapterResponse.PageDataBean bean);

        void setAdvertLog(long userId, int albumId);
    }
}
