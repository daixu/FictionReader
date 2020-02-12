package com.shangame.fiction.ui.listen.palyer;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.shangame.fiction.R;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.utils.DeviceUtils;
import com.shangame.fiction.core.utils.QuitTimer;
import com.shangame.fiction.net.response.AlbumChapterDetailResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.ui.listen.PlayerSong;
import com.shangame.fiction.ui.listen.play.MusicPlayerActivity;

import static android.support.v4.app.NotificationCompat.PRIORITY_DEFAULT;
import static android.support.v4.app.NotificationManagerCompat.IMPORTANCE_DEFAULT;

public class MusicPlayerService extends Service implements IPlayback, IPlayback.Callback, MusicPlayerContract.View {

    private static final String ACTION_PLAY_TOGGLE = "com.shangame.fiction.listen.book.ACTION.PLAY_TOGGLE";
    private static final String ACTION_PLAY_LAST = "com.shangame.fiction.listen.book.ACTION.PLAY_LAST";
    private static final String ACTION_PLAY_NEXT = "com.shangame.fiction.listen.book.ACTION.PLAY_NEXT";
    private static final String ACTION_STOP_SERVICE = "com.shangame.fiction.listen.book.ACTION.STOP_SERVICE";
    private static final int NOTIFICATION_ID = 0x111;
    private final Binder mBinder = new LocalBinder();
    private RemoteViews mContentViewBig, mContentViewSmall;
    private Player mPlayer;
    private MusicPlayerPresenter mPresenter;
    private NotificationManager notificationManager;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Log.e("hhh", "handler");
            }
        }
    };

    @Override
    public void getAlbumChapterDetailSuccess(AlbumChapterDetailResponse response, int albumId, int chapterId) {
        if (null == response) {
            return;
        }
        if (null == response.play_url) {
            return;
        }
        if (TextUtils.isEmpty(response.play_url.small)) {
            return;
        }
        // 免费章节
        if (response.chargingmode == 0) {
            Song song = getSong(response, albumId, chapterId);
            PlayList playList = new PlayList(song);
            play(playList, 0);

            showNotification();

            Intent intent = new Intent(BroadcastAction.SWITCH_AUDIO_ACTION);
            intent.putExtra("song", song);

            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } else {
            // 1已订阅
            if (response.buystatus == 1) {
                Song song = getSong(response, albumId, chapterId);
                PlayList playList = new PlayList(song);
                play(playList, 0);

                showNotification();

                Intent intent = new Intent(BroadcastAction.SWITCH_AUDIO_ACTION);
                intent.putExtra("song", song);

                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            } else {
                boolean isRunning = PlayerSong.getInstance().isRunning();
                if (isRunning) {
                    Song song = getSong(response, albumId, chapterId);
                    Intent intent = new Intent(BroadcastAction.TOLL_PROMPT_BOX_ACTION);
                    intent.putExtra("song", song);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                } else {
                    Toast.makeText(this, "到付费章节啦!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void getAlbumChapterDetailFailure(String msg) {

    }

    private Song getSong(AlbumChapterDetailResponse response, int albumId, int chapterId) {
        Song song = new Song();
        song.albumid = albumId;
        song.id = chapterId;
        song.lastcid = response.lastcid;
        song.nextcid = response.nextcid;
        song.buyStatus = response.buystatus;
        song.albumName = response.albumName;
        song.showCover = response.showCover;
        song.showName = response.showName;
        song.url = response.play_url.small;
        song.duration = response.duration * 1000;
        song.bookShelves = response.bookshelves;
        song.readMoney = response.readmoney;
        song.chargingMode = response.chargingmode;
        song.chapterPrice = response.chapterprice;
        song.isVip = response.isvip;
        song.chapterNumber = response.sort;
        return song;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showNotNetworkView() {

    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showError(Throwable throwable) {

    }

    @Override
    public void lunchLoginActivity() {
        Intent intent = new Intent(BroadcastAction.LISTEN_LOGIN_ACTION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = Player.getInstance();
        mPlayer.registerCallback(this);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        initQuitTimer();
        initPresenter();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForeground(NOTIFICATION_ID, buildNotification(this));
            }
            String action = intent.getAction();
            if (ACTION_PLAY_TOGGLE.equals(action)) {
                if (isPlaying()) {
                    pause();
                    Intent intent2 = new Intent(BroadcastAction.PAUSE_PLAY_ACTION);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent2);
                } else {
                    play();
                    Intent intent2 = new Intent(BroadcastAction.START_PLAY_ACTION);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent2);
                }
            } else if (ACTION_PLAY_NEXT.equals(action)) {
                playNext();
            } else if (ACTION_PLAY_LAST.equals(action)) {
                playLast();
            } else if (ACTION_STOP_SERVICE.equals(action)) {
                if (isPlaying()) {
                    pause();
                }
                PlayerSong.getInstance().setPlayerSong(null);
                stopForeground(true);

                Intent intent2 = new Intent(BroadcastAction.STOP_PLAY_ACTION);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent2);
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        releasePlayer();
        Log.e("hhh", "onDestroy");
        super.onDestroy();

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }

        mPresenter.detachView();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        showNotification();
        return mBinder;
    }

    /**
     * 初始化计时器
     */
    private void initQuitTimer() {
        QuitTimer.getInstance().init(this, handler);
    }

    private void initPresenter() {
        mPresenter = new MusicPlayerPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public boolean stopService(Intent name) {
        Log.e("hhh", "stopService");
        stopForeground(true);
        unregisterCallback(this);

        return super.stopService(name);
    }

    @Override
    public void setPlayList(PlayList list) {
        mPlayer.setPlayList(list);
    }

    @Override
    public boolean play() {
        return mPlayer.play();
    }

    @Override
    public boolean play(PlayList list) {
        return mPlayer.play(list);
    }

    @Override
    public boolean play(PlayList list, int startIndex) {
        return mPlayer.play(list, startIndex);
    }

    @Override
    public boolean play(Song song) {
        return mPlayer.play(song);
    }

    @Override
    public boolean playLast() {
        return mPlayer.playLast();
    }

    @Override
    public boolean playNext() {
        return mPlayer.playNext();
    }

    @Override
    public boolean pause() {
        return mPlayer.pause();
    }

    @Override
    public boolean stop() {
        return mPlayer.stop();
    }

    @Override
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    @Override
    public int getProgress() {
        return mPlayer.getProgress();
    }

    @Override
    public Song getPlayingSong() {
        return mPlayer.getPlayingSong();
    }

    @Override
    public boolean seekTo(int progress) {
        return mPlayer.seekTo(progress);
    }

    @Override
    public void setPlayMode(PlayMode playMode) {
        mPlayer.setPlayMode(playMode);
    }

    @Override
    public void registerCallback(Callback callback) {
        mPlayer.registerCallback(callback);
    }

    @Override
    public void unregisterCallback(Callback callback) {
        mPlayer.unregisterCallback(callback);
    }

    @Override
    public void removeCallbacks() {
        mPlayer.removeCallbacks();
    }

    @Override
    public void releasePlayer() {
        mPlayer.releasePlayer();
        super.onDestroy();
    }

    @Override
    public void onSwitchLast(@Nullable Song last) {
        if (null != last) {
            long userId = UserInfoManager.getInstance(this).getUserid();
            String deviceId = DeviceUtils.getAndroidID();
            if (last.lastcid == 0) {
                Toast.makeText(this, "当前收听为第一章!", Toast.LENGTH_SHORT).show();
                if (isPlaying()) {
                    pause();
                }
                Log.e("hhh", "第一章");
            } else {
                mPresenter.getAlbumChapterDetail(userId, last.albumid, last.lastcid, deviceId);
            }
        }
    }

    @Override
    public void onSwitchNext(@Nullable Song next) {
        // showNotification();
        if (null != next) {
            long userId = UserInfoManager.getInstance(this).getUserid();
            String deviceId = DeviceUtils.getAndroidID();
            if (next.nextcid == 0) {
                Toast.makeText(this, "本书全部听讲结束,感谢收听!", Toast.LENGTH_SHORT).show();
                quit();
                Log.e("hhh", "最后一章");
            } else {
                mPresenter.getAlbumChapterDetail(userId, next.albumid, next.nextcid, deviceId);
            }
        }
    }

    public void quit() {
        // 先停止播放
        if (isPlaying()) {
            pause();
        }
        stopForeground(true);
        // unregisterCallback(this);
        // 移除定时器
        QuitTimer.getInstance().stop();
        PlayerSong.getInstance().setTimingBean(null);

        Intent intent2 = new Intent(BroadcastAction.STOP_PLAY_ACTION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent2);
        // 当另一个组件（如 Activity）通过调用 startService() 请求启动服务时，系统将调用onStartCommand。
        // 一旦执行此方法，服务即会启动并可在后台无限期运行。 如果自己实现此方法，则需要在服务工作完成后，
        // 通过调用 stopSelf() 或 stopService() 来停止服务。
        // stopSelf();
    }

    @Override
    public void onComplete(@Nullable Song next) {
        if (null != next) {
            long userId = UserInfoManager.getInstance(this).getUserid();
            String deviceId = DeviceUtils.getAndroidID();
            if (next.nextcid == 0) {
                Toast.makeText(this, "本书全部听讲结束,感谢收听!", Toast.LENGTH_SHORT).show();
                quit();
                Log.e("hhh", "最后一章");
            } else {
                mPresenter.getAlbumChapterDetail(userId, next.albumid, next.nextcid, deviceId);
            }
        }
    }

    @Override
    public void onPlayStatusChanged(boolean isPlaying) {
        showNotification();
    }

    private Notification buildNotification(Context context) {
        Intent intent = new Intent(context, MusicPlayerActivity.class);
        // intent.putExtra(Constant.EXTRA_NOTIFICATION, true);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (null != mPlayer) {
            intent.putExtra("song", mPlayer.getPlayingSong());
        }

        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("1234110", "开始播放音频文件", NotificationManager.IMPORTANCE_HIGH);
            //设置绕过免打扰模式
            channel.setBypassDnd(true);
            //检测是否绕过免打扰模式
            channel.canBypassDnd();
            //设置在锁屏界面上显示这条通知
            channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
            channel.setDescription("description of this notification");
            channel.setLightColor(Color.GREEN);
            channel.setName("name of this notification");
            channel.setShowBadge(true);
            // channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            channel.enableVibration(false);
            channel.setVibrationPattern(new long[]{0});
            channel.setSound(null, null);
            if (null != notificationManager) {
                notificationManager.createNotificationChannel(channel);
            }
            builder = new NotificationCompat.Builder(context, "1234110");
        } else {
            builder = new NotificationCompat.Builder(context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setPriority(IMPORTANCE_DEFAULT);
            } else {
                builder.setPriority(PRIORITY_DEFAULT);
            }
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                //设置通知的图标
                .setSmallIcon(R.mipmap.ic_launcher)
                .setCustomContentView(getSmallContentView())
                .setCustomBigContentView(getBigContentView())
                //设置状态栏的标题
                //.setTicker("有新消息呢")
                //设置标题
                .setContentTitle("标题")
                //消息内容
                .setContentText("内容")
                //在右边显示一个数量,等价于setContentInfo函数.如果有设置setContentInfo函数,那么本函数会给覆盖
                //.setNumber(12)
                //是否提示一次.true - 如果Notification已经存在状态栏即使在调用notify函数也不会更新
                .setOnlyAlertOnce(true)
                //滚动条,indeterminate true - 不确定的,不会显示进度,false - 根据max和progress情况显示进度条
                //.setProgress (100, 50, true)
                //设置默认的提示音
                //.setDefaults(Notification.DEFAULT_ALL)
                //让通知左右滑的时候不能取消通知
                .setOngoing(true)
                //设置该通知的优先级
                .setPriority(NotificationCompat.PRIORITY_MAX)
                //设置通知时间，默认为系统发出通知的时间，通常不用设置
                //.setWhen(System.currentTimeMillis())
                //打开程序后图标消失
                .setAutoCancel(false);
        return builder.build();
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // Send the notification.
        startForeground(NOTIFICATION_ID, buildNotification(this));
    }

    private RemoteViews getSmallContentView() {
        if (mContentViewSmall == null) {
            mContentViewSmall = new RemoteViews(getPackageName(), R.layout.remote_view_music_player_small);
            setUpRemoteView(mContentViewSmall);
        }
        updateRemoteViews(mContentViewSmall);
        return mContentViewSmall;
    }

    private RemoteViews getBigContentView() {
        if (mContentViewBig == null) {
            mContentViewBig = new RemoteViews(getPackageName(), R.layout.remote_view_music_player);
            setUpRemoteView(mContentViewBig);
        }
        updateRemoteViews(mContentViewBig);
        return mContentViewBig;
    }

    private void setUpRemoteView(RemoteViews remoteView) {
        remoteView.setImageViewResource(R.id.image_view_close, R.drawable.ic_remote_view_close);
        remoteView.setImageViewResource(R.id.image_view_play_last, R.drawable.ic_remote_view_play_last);
        remoteView.setImageViewResource(R.id.image_view_play_next, R.drawable.ic_remote_view_play_next);

        remoteView.setOnClickPendingIntent(R.id.button_close, getPendingIntent(ACTION_STOP_SERVICE));
        remoteView.setOnClickPendingIntent(R.id.button_play_last, getPendingIntent(ACTION_PLAY_LAST));
        remoteView.setOnClickPendingIntent(R.id.button_play_next, getPendingIntent(ACTION_PLAY_NEXT));
        remoteView.setOnClickPendingIntent(R.id.button_play_toggle, getPendingIntent(ACTION_PLAY_TOGGLE));
    }

    private void updateRemoteViews(RemoteViews remoteView) {
        Song currentSong = mPlayer.getPlayingSong();
        if (currentSong != null) {
            remoteView.setTextViewText(R.id.text_view_name, currentSong.showName);
            remoteView.setTextViewText(R.id.text_view_artist, currentSong.albumName);
        }
        boolean isPlay = isPlaying();
        remoteView.setImageViewResource(R.id.image_view_play_toggle, isPlay ? R.drawable.ic_remote_view_pause : R.drawable.ic_remote_view_play);
        remoteView.setImageViewResource(R.id.image_view_album, R.mipmap.ic_launcher);
    }

    private PendingIntent getPendingIntent(String action) {
        return PendingIntent.getService(this, 0, new Intent(action), 0);
    }

    public class LocalBinder extends Binder {
        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }
}
