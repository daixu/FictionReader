package com.shangame.fiction.ui.listen.palyer;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.AlbumChapterDetailResponse;

interface MusicPlayerContract {

    interface View extends BaseContract.BaseView {
        void getAlbumChapterDetailSuccess(AlbumChapterDetailResponse response, int albumId, int chapterId);

        void getAlbumChapterDetailFailure(String msg);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getAlbumChapterDetail(long userId, int albumId, int cid, String deviceId);
    }
}