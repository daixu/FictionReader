package com.shangame.fiction.ui.listen.detail;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.AlbumChapterDetailResponse;
import com.shangame.fiction.net.response.AlubmDetailResponse;
import com.shangame.fiction.net.response.TaskAwardResponse;

/**
 * Create by Speedy on 2018/8/13
 */
public interface ListenBookDetailContacts {

    interface View extends BaseContract.BaseView {
        void getAlbumDetailSuccess(AlubmDetailResponse response);

        void getAlbumDetailFailure(String msg);

        void getAlbumChapterDetailSuccess(AlbumChapterDetailResponse response);

        void getAlbumChapterDetailFailure(String msg);

        void addToBookRackSuccess(long bookId, int receive);

        void getTaskAwardSuccess(TaskAwardResponse taskAwardResponse, int taskId);

        void setAdvertLogSuccess();
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void getAlbumDetail(long userId, int albumId);

        void getAlbumChapterDetail(long userId, int albumId, int cid, String deviceId);

        void addToBookRack(long userId, long bookId);

        void getTaskAward(long userId, int taskId, boolean showLoading);

        void setAdvertLog(long userId, int albumId);
    }
}
