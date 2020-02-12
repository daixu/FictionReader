package com.shangame.fiction.ui.listen.play;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.shangame.fiction.core.base.RxPresenter;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.manager.HttpResultManager;
import com.shangame.fiction.net.response.AddToBookResponse;
import com.shangame.fiction.net.response.AlbumChapterDetailResponse;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.ui.listen.palyer.MusicPlayerService;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MusicPlayerPresenter extends RxPresenter<MusicPlayerContract.View> implements MusicPlayerContract.Presenter<MusicPlayerContract.View> {

    private Context mContext;
    private MusicPlayerContract.View mView;

    private MusicPlayerService mPlaybackService;
    private boolean mIsServiceBound;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            if (null == mPlaybackService) {
                mPlaybackService = ((MusicPlayerService.LocalBinder) service).getService();
                mView.onPlaybackServiceBound(mPlaybackService);
                mView.onSongUpdated(mPlaybackService.getPlayingSong());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            // mPlaybackService = null;
            // mView.onPlaybackServiceUnbound();
        }
    };

    public MusicPlayerPresenter(Context context, MusicPlayerContract.View view) {
        mContext = context;
        mView = view;
    }

    @Override
    public void attachView(MusicPlayerContract.View view) {
        super.attachView(view);

        bindService();
    }

    public void bindService() {
        bindPlaybackService();

        retrieveLastPlayMode();

        // TODO
        if (mPlaybackService != null && mPlaybackService.isPlaying()) {
            mView.onSongUpdated(mPlaybackService.getPlayingSong());
        } else {
            // - load last play list/folder/song
        }
    }

    @Override
    public void retrieveLastPlayMode() {
    }

    @Override
    public void bindPlaybackService() {
        // Establish a connection with the service.  We use an explicit
        // class name because we want a specific service implementation that
        // we know will be running in our own process (and thus won't be
        // supporting component replacement by other applications).
        // mContext.bindService(new Intent(mContext, MusicPlayerService.class), mConnection, Context.BIND_AUTO_CREATE);

        Intent intent = new Intent(mContext, MusicPlayerService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mContext.startForegroundService(intent);

            mContext.bindService(new Intent(mContext, MusicPlayerService.class), mConnection, Context.BIND_AUTO_CREATE);
        } else {
            mContext.startService(intent);

            mContext.bindService(new Intent(mContext, MusicPlayerService.class), mConnection, Context.BIND_AUTO_CREATE);
        }
        mIsServiceBound = true;
    }

    @Override
    public void unbindPlaybackService() {
        if (mIsServiceBound) {
            // Detach our existing connection.
            mContext.unbindService(mConnection);
            mIsServiceBound = false;
        }
    }

    @Override
    public void getAlbumChapterDetail(long userId, int albumId, final int cid, String deviceId) {
        Observable<HttpResult<AlbumChapterDetailResponse>> observable = ApiManager.getInstance().getAlbumChapterDetail(userId, albumId, cid, deviceId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<AlbumChapterDetailResponse>>() {
            @Override
            public void accept(HttpResult<AlbumChapterDetailResponse> result) throws Exception {
                if (mView != null && HttpResultManager.verify(result, mView)) {
                    mView.getAlbumChapterDetailSuccess(result.data, cid);
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void addToBookRack(long userId, final long bookId) {
        if (mView != null) {
            mView.showLoading();
        }
        Observable<HttpResult<AddToBookResponse>> observable = ApiManager.getInstance().addAlbumBookRack(userId, bookId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<AddToBookResponse>>() {
            @Override
            public void accept(HttpResult<AddToBookResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        mView.addToBookRackSuccess(bookId, result.data.receive);
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getTaskAward(long userId, final int taskId, boolean showLoading) {
        if (mView != null && showLoading) {
            mView.showLoading();
        }
        Observable<HttpResult<TaskAwardResponse>> observable = ApiManager.getInstance().getTaskAward(userId, taskId);
        Disposable disposable = HttpResultManager.rxResultHandler(observable, mView, new Consumer<HttpResult<TaskAwardResponse>>() {
            @Override
            public void accept(HttpResult<TaskAwardResponse> result) throws Exception {
                if (mView != null) {
                    mView.dismissLoading();
                    if (HttpResultManager.verify(result, mView)) {
                        Log.e("hhh", "getTaskAward result.data= " + result.data);
                        if (result.data != null) {
                            mView.getTaskAwardSuccess(result.data, taskId);
                        }
                    }
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void detachView() {
        unbindPlaybackService();
        // Release context reference
        mContext = null;
        mView = null;
    }
}
