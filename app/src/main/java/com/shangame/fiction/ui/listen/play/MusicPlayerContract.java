package com.shangame.fiction.ui.listen.play;

import android.support.annotation.Nullable;

import com.shangame.fiction.core.base.BaseContract;
import com.shangame.fiction.net.response.AlbumChapterDetailResponse;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.ui.listen.palyer.MusicPlayerService;
import com.shangame.fiction.ui.listen.palyer.Song;

interface MusicPlayerContract {

    interface View extends BaseContract.BaseView {
        void handleError(Throwable error);

        void onPlaybackServiceBound(MusicPlayerService service);

        void onPlaybackServiceUnbound();

        void onSongUpdated(@Nullable Song song);

        void updatePlayToggle(boolean play);

        void getAlbumChapterDetailSuccess(AlbumChapterDetailResponse response, int chapterId);

        void getAlbumChapterDetailFailure(String msg);

        void addToBookRackSuccess(long bookId, int receive);

        void getTaskAwardSuccess(TaskAwardResponse taskAwardResponse, int taskId);
    }

    interface Presenter<V> extends BaseContract.BaserPresenter<V> {
        void retrieveLastPlayMode();

        void bindPlaybackService();

        void unbindPlaybackService();

        void getAlbumChapterDetail(long userId, int albumId, int cid, String deviceId);

        void addToBookRack(long userId, long bookId);

        void getTaskAward(long userId, int taskId, boolean showLoading);
    }
}