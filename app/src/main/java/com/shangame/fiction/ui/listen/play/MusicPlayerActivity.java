package com.shangame.fiction.ui.listen.play;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lxj.xpopup.XPopup;
import com.shangame.fiction.R;
import com.shangame.fiction.RxBus;
import com.shangame.fiction.core.base.BaseActivity;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.utils.BitmapUtils;
import com.shangame.fiction.core.utils.BlurUtil;
import com.shangame.fiction.core.utils.DeviceUtils;
import com.shangame.fiction.core.utils.NetworkUtils;
import com.shangame.fiction.core.utils.QuitTimer;
import com.shangame.fiction.core.utils.ViewSwitchUtils;
import com.shangame.fiction.net.response.AlbumChapterDetailResponse;
import com.shangame.fiction.net.response.AlbumChapterResponse;
import com.shangame.fiction.net.response.TaskAwardResponse;
import com.shangame.fiction.storage.db.BookBrowseHistoryDao;
import com.shangame.fiction.storage.db.BookReadProgressDao;
import com.shangame.fiction.storage.manager.DbManager;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.manager.VisitorDbManager;
import com.shangame.fiction.storage.model.BookBrowseHistory;
import com.shangame.fiction.storage.model.BookReadProgress;
import com.shangame.fiction.ui.listen.PlaySongEvent;
import com.shangame.fiction.ui.listen.PlayerSong;
import com.shangame.fiction.ui.listen.directory.DirectoryPopupWindow;
import com.shangame.fiction.ui.listen.order.ChapterOrderPopWindow;
import com.shangame.fiction.ui.listen.palyer.IPlayback;
import com.shangame.fiction.ui.listen.palyer.MusicPlayerService;
import com.shangame.fiction.ui.listen.palyer.PlayList;
import com.shangame.fiction.ui.listen.palyer.Song;
import com.shangame.fiction.ui.listen.palyer.TimeUtils;
import com.shangame.fiction.ui.listen.reward.RewardActivity;
import com.shangame.fiction.ui.listen.timing.TimingBean;
import com.shangame.fiction.ui.listen.timing.TimingPopupWindow;
import com.shangame.fiction.ui.task.TaskId;
import com.shangame.fiction.ui.task.TaskRewardPopupWindow;
import com.shangame.fiction.widget.CircleRotateImageView;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 播放界面
 *
 * @author hhh
 */
public class MusicPlayerActivity extends BaseActivity implements MusicPlayerContract.View, IPlayback.Callback, View.OnClickListener {
    private static final long UPDATE_PROGRESS_INTERVAL = 1000;
    private static final int FROM_ADD_TO_BOOK_RACK = 601;
    private TextView mTvAddBookRack;
    private ImageView mImgCollapse;
    private CircleRotateImageView mImageViewAlbum;
    private AppCompatSeekBar mSeekBar;
    private TextView mTextViewProgress;
    private TextView mTextViewDuration;
    private TextView mTextViewName;
    private TextView mTextViewArtist;
    private AppCompatImageView mButtonDirectory;
    private AppCompatImageView mButtonPlayLast;
    private AppCompatImageView mButtonPlayToggle;
    private AppCompatImageView mButtonPlayNext;
    private AppCompatImageView mButtonTiming;
    private AppCompatTextView mTvTiming;
    private ImageView mView;
    private MusicPlayerPresenter mPresenter;

    private IPlayback mPlayer;
    private Song mSong;

    private int albumId;
    private int chapterId;
    private ChapterOrderPopWindow chapterOrderPopWindow;
    private DirectoryPopupWindow popupWindow;
    private TimingPopupWindow timingPopupWindow;

    private Handler mHandler = new Handler();

    private Runnable mProgressCallback = new Runnable() {

        @Override
        public void run() {
            if (mPlayer.isPlaying()) {
                int progress = (int) (mSeekBar.getMax() * ((float) mPlayer.getProgress() / (float) getCurrentSongDuration()));
                updateProgressTextWithDuration(mPlayer.getProgress());
                if (progress >= 0 && progress <= mSeekBar.getMax()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mSeekBar.setProgress(progress, true);
                    } else {
                        mSeekBar.setProgress(progress);
                    }
                    mHandler.postDelayed(this, UPDATE_PROGRESS_INTERVAL);
                }
            }
        }
    };

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BroadcastAction.UPDATE_LISTEN_CHAPTER.equals(action)) {
                int chapterId = intent.getIntExtra("chapterId", 0);
                if (null != popupWindow) {
                    popupWindow.setChapterId(chapterId);
                }
            } else if (BroadcastAction.SWITCH_AUDIO_ACTION.equals(action)) {
                Song song = intent.getParcelableExtra("song");
                if (null != song) {
                    setPlayInfo(song);
                }
            } else if (BroadcastAction.TOLL_PROMPT_BOX_ACTION.equals(action)) {
                final Song song = intent.getParcelableExtra("song");
                showChapterOrderPopWindow(albumId, chapterId, new ChapterOrderPopWindow.OnOrderPayListener() {
                    @Override
                    public void onPaySuccess() {
                        Log.e("hhh", "onPaySuccess");
                        showToast(mActivity.getString(R.string.book_order_success));
                        playSong(song, 0);
                    }

                    @Override
                    public void onCancelPay() {
                        Log.e("hhh", "onCancelPay");
                    }
                });
            } else if (BroadcastAction.LISTEN_LOGIN_ACTION.equals(action)) {
                MusicPlayerActivity.super.lunchLoginActivity();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        initView();
        initListener();

        PlayerSong.getInstance().setRunning(true);

        mPresenter = new MusicPlayerPresenter(this, this);
        mPresenter.attachView(this);
        subscribeEvents();
        initReceiver();

        TimingBean bean = PlayerSong.getInstance().getTimingBean();
        if (null != bean) {
            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.icon_timing_already);
            if (null != drawable) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                mTvTiming.setCompoundDrawables(null, drawable, null, null);
            }
            QuitTimer.getInstance().setParameter(mTvTiming, mContext);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPlayer != null && mPlayer.isPlaying()) {
            mHandler.removeCallbacks(mProgressCallback);
            mHandler.post(mProgressCallback);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeCallbacks(mProgressCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();

        PlayerSong.getInstance().setRunning(false);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    private void initReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.UPDATE_LISTEN_CHAPTER);
        intentFilter.addAction(BroadcastAction.SWITCH_AUDIO_ACTION);
        intentFilter.addAction(BroadcastAction.TOLL_PROMPT_BOX_ACTION);
        intentFilter.addAction(BroadcastAction.LISTEN_LOGIN_ACTION);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, intentFilter);
    }

    private void initListener() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    updateProgressTextWithProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mProgressCallback);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekTo(getDuration(seekBar.getProgress()));
                if (mPlayer.isPlaying()) {
                    mHandler.removeCallbacks(mProgressCallback);
                    mHandler.post(mProgressCallback);
                }
            }
        });

        mButtonDirectory.setOnClickListener(this);
        mButtonPlayLast.setOnClickListener(this);
        mButtonPlayToggle.setOnClickListener(this);
        mButtonPlayNext.setOnClickListener(this);
        mButtonTiming.setOnClickListener(this);
        mTvAddBookRack.setOnClickListener(this);
        mImgCollapse.setOnClickListener(this);
        mTvTiming.setOnClickListener(this);
        findViewById(R.id.layout_reward).setOnClickListener(this);
    }

    private void initView() {
        mView = findViewById(R.id.view);
        mTvAddBookRack = findViewById(R.id.tv_add_book_rack);
        mImgCollapse = findViewById(R.id.img_collapse);
        mImageViewAlbum = findViewById(R.id.image_view_album);
        mSeekBar = findViewById(R.id.seek_bar);
        mTextViewProgress = findViewById(R.id.text_view_progress);
        mTextViewDuration = findViewById(R.id.text_view_duration);
        mTextViewName = findViewById(R.id.text_view_name);
        mTextViewArtist = findViewById(R.id.text_view_artist);
        mButtonPlayLast = findViewById(R.id.button_play_last);
        mButtonDirectory = findViewById(R.id.button_directory);
        mButtonPlayToggle = findViewById(R.id.button_play_toggle);
        mButtonPlayNext = findViewById(R.id.button_play_next);
        mButtonTiming = findViewById(R.id.button_timing);
        mTvTiming = findViewById(R.id.tv_timing);
    }

    @Override
    public void onSwitchLast(Song last) {
        onSongUpdated(last);
    }

    @Override
    public void onSwitchNext(Song next) {
        onSongUpdated(next);
    }

    @Override
    public void onComplete(Song next) {
        onSongUpdated(next);
    }

    @Override
    public void onPlayStatusChanged(boolean isPlaying) {
        updatePlayToggle(isPlaying);
        if (isPlaying) {
            mImageViewAlbum.resumeRotateAnimation();
            mHandler.removeCallbacks(mProgressCallback);
            mHandler.post(mProgressCallback);
        } else {
            mImageViewAlbum.pauseRotateAnimation();
            mHandler.removeCallbacks(mProgressCallback);
        }
    }

    private void updateProgressTextWithProgress(int progress) {
        int targetDuration = getDuration(progress);
        mTextViewProgress.setText(TimeUtils.formatDuration(targetDuration));
    }

    private void seekTo(int duration) {
        mPlayer.seekTo(duration);
    }

    private void setPlayInfo(Song song) {
        mSong = song;

        albumId = song.albumid;
        chapterId = song.id;

        Intent intent = new Intent(BroadcastAction.UPDATE_LISTEN_CHAPTER);
        intent.putExtra("chapterId", chapterId);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        PlayerSong.getInstance().setPlayerSong(song);

        // Step 1: Song name and artist
        mTextViewName.setText(song.showName);
        mTextViewArtist.setText(song.albumName);
        // Step 2: Duration
        mTextViewDuration.setText(TimeUtils.formatDuration(song.duration));
        // Step 3: Keep these things updated
        // - Album rotation
        // - Progress(textViewProgress & seekBarProgress)

        RequestOptions options = new RequestOptions()
                .circleCrop()
                .placeholder(R.drawable.default_record_album)
                .override(211, 211);
        Glide.with(mContext)
                .load(song.showCover)
                .apply(options)
                .into(mImageViewAlbum);
        mImageViewAlbum.pauseRotateAnimation();

        Glide.with(mContext)
                .asBitmap()
                .load(song.showCover)
                .placeholder(R.drawable.default_cover)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Resources res = getResources();
                        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.bg_splice_bottom);
                        Bitmap bitmap = BitmapUtils.newBitmap(resource, bmp);

                        Bitmap newImg = null;
                        if (null != bitmap) {
                            newImg = BlurUtil.doBlur(bitmap, 20, 10);
                        }
                        if (null != newImg) {
                            ViewSwitchUtils.startSwitchBackgroundAnim(mView, newImg, 0.5f);
                        } else {
                            if (null != bitmap) {
                                ViewSwitchUtils.startSwitchBackgroundAnim(mView, bitmap, 0.5f);
                            } else {
                                ViewSwitchUtils.startSwitchBackgroundAnim(mView, resource, 0.5f);
                            }
                        }
                    }
                });

        mHandler.removeCallbacks(mProgressCallback);
        if (mPlayer.isPlaying()) {
            mImageViewAlbum.startRotateAnimation();
            mHandler.post(mProgressCallback);
        }

        setPlayIcon(song);

        saveBookReadProgress();
    }

    private int getDuration(int progress) {
        return (int) (getCurrentSongDuration() * ((float) progress / mSeekBar.getMax()));
    }

    private void setPlayIcon(Song song) {
        if (song.lastcid == 0) {
            mButtonPlayLast.setImageResource(R.drawable.icon_play_last_1);
        } else {
            mButtonPlayLast.setImageResource(R.drawable.icon_play_last);
        }

        if (song.nextcid == 0) {
            mButtonPlayNext.setImageResource(R.drawable.icon_play_next_1);
        } else {
            mButtonPlayNext.setImageResource(R.drawable.icon_play_next);
        }
    }

    private void saveBookReadProgress() {
        if (mSong != null) {
            Flowable.just(mSong).map(new Function<Song, Boolean>() {
                @Override
                public Boolean apply(Song song) throws Exception {
                    BookReadProgress bookReadProgress = new BookReadProgress();
                    bookReadProgress.bookId = song.albumid;
                    bookReadProgress.chapterId = song.id;
                    bookReadProgress.chapterIndex = song.chapterNumber;
                    BookReadProgressDao bookReadProgressDao = DbManager.getDaoSession(mContext).getBookReadProgressDao();
                    bookReadProgressDao.insertOrReplace(bookReadProgress);

                    updateBookBrowseHistory(song);

                    return true;
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            Intent intent = new Intent(BroadcastAction.REFRESH_BOOK_RACK_ACTION);
                            intent.putExtra("bookId", (long) albumId);
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.e("hhh", "saveBookReadProgress ", throwable);
                        }
                    });
        }
    }

    private int getCurrentSongDuration() {
        Song currentSong = mPlayer.getPlayingSong();
        int duration = 0;
        if (currentSong != null) {
            duration = currentSong.duration;
        }
        return duration;
    }

    private void updateBookBrowseHistory(Song song) {
        BookBrowseHistoryDao bookBrowseHistoryDao = VisitorDbManager.getDaoSession(mContext).getBookBrowseHistoryDao();
        BookBrowseHistory bookBrowseHistory = bookBrowseHistoryDao.loadByRowId(song.albumid);
        if (null != bookBrowseHistory) {
            bookBrowseHistory.readTime = System.currentTimeMillis();
            bookBrowseHistory.chapternumber = song.chapterNumber;
            bookBrowseHistory.booktype = 1;
            bookBrowseHistoryDao.update(bookBrowseHistory);
        } else {
            bookBrowseHistory = new BookBrowseHistory();
            bookBrowseHistory.bookid = song.albumid;
            bookBrowseHistory.bookname = song.albumName;
            bookBrowseHistory.bookcover = song.showCover;
            bookBrowseHistory.readTime = System.currentTimeMillis();
            bookBrowseHistory.bookshelves = song.bookShelves;
            bookBrowseHistory.chapteId = song.id;
            bookBrowseHistory.pageIndex = 1;
            bookBrowseHistory.chapternumber = song.id;
            bookBrowseHistory.isLocal = false;
            bookBrowseHistory.booktype = 1;

            VisitorDbManager.getDaoSession(mContext).getBookBrowseHistoryDao().insertOrReplace(bookBrowseHistory);
        }
    }

    private void subscribeEvents() {
        RxBus.getInstance().toObservable().map(new Function<Object, PlaySongEvent>() {
            @Override
            public PlaySongEvent apply(Object o) throws Exception {
                return (PlaySongEvent) o;
            }
        }).subscribe(new Consumer<PlaySongEvent>() {
            @Override
            public void accept(PlaySongEvent event) throws Exception {
                if (event != null) {
                    onPlaySongEvent(event);
                }
            }
        });
    }

    private void onPlaySongEvent(PlaySongEvent event) {
        mSong = event.song;
        playSong(mSong, 0);
    }

    private void updateProgressTextWithDuration(int duration) {
        mTextViewProgress.setText(TimeUtils.formatDuration(duration));
    }

    @Override
    public void handleError(Throwable error) {
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPlaybackServiceBound(MusicPlayerService service) {
        mPlayer = service;
        mPlayer.registerCallback(this);

        mSong = getIntent().getParcelableExtra("song");
        int type = getIntent().getIntExtra("type", 0);
        if (null != mSong) {
            if (mSong.bookShelves == 1) {
                mTvAddBookRack.setText("已加入书架");
                mTvAddBookRack.setEnabled(false);
            } else {
                mTvAddBookRack.setText("加入书架");
                mTvAddBookRack.setEnabled(true);
            }
            if (type == 1) {
                if (mPlayer == null) {
                    playSong(mSong, 1);
                } else {
                    if (mPlayer.isPlaying()) {
                        mButtonPlayToggle.setImageResource(R.drawable.icon_pause);
                    } else {
                        mButtonPlayToggle.setImageResource(R.drawable.icon_play);
                    }
                    setPlayIcon(mSong);
                }
            } else if (type == 2) {
                if (mPlayer == null) {
                    playSong(mSong, 2);
                } else {
                    if (null == PlayerSong.getInstance().getPlayerSong()) {
                        playSong(mSong, 2);
                    } else if (mSong.id == PlayerSong.getInstance().getPlayerSong().id) {
                        if (mPlayer.isPlaying()) {
                            mButtonPlayToggle.setImageResource(R.drawable.icon_pause);
                            setPlayIcon(mSong);
                        } else {
                            mPlayer.pause();
                            setPlayIcon(mSong);
                        }
                    } else {
                        playSong(mSong, 2);
                    }
                }
            } else {
                if (mPlayer == null) {
                    playSong(mSong, 0);
                } else {
                    if (null == PlayerSong.getInstance().getPlayerSong()) {
                        playSong(mSong, 0);
                    } else if (mSong.id == PlayerSong.getInstance().getPlayerSong().id) {
                        if (mPlayer.isPlaying()) {
                            mButtonPlayToggle.setImageResource(R.drawable.icon_pause);
                            setPlayIcon(mSong);
                        } else {
                            mPlayer.play();
                            setPlayIcon(mSong);
                        }
                    } else {
                        playSong(mSong, 0);
                    }
                }
            }
        } else {
            if (null != mPlayer) {
                if (mPlayer.isPlaying()) {
                    mButtonPlayToggle.setImageResource(R.drawable.icon_pause);
                } else {
                    mButtonPlayToggle.setImageResource(R.drawable.icon_play);
                }
            }
        }
    }

    private void playSong(Song song, int type) {
        if (null != song) {
            if (mPlayer == null) {
                PlayList playList = new PlayList(song);
                playSong(playList, 0, type);
            } else {
                if (null == PlayerSong.getInstance().getPlayerSong()) {
                    PlayList playList = new PlayList(song);
                    playSong(playList, 0, type);
                } else if (song.id == PlayerSong.getInstance().getPlayerSong().id) {
                    if (mPlayer.isPlaying()) {
                        mButtonPlayToggle.setImageResource(R.drawable.icon_pause);
                        setPlayIcon(song);
                    } else {
                        mPlayer.pause();
                        setPlayIcon(song);
                    }
                } else {
                    PlayList playList = new PlayList(song);
                    playSong(playList, 0, type);
                }
            }
        }
    }

    private void playSong(PlayList playList, int index, int type) {
        if (playList == null) {
            return;
        }
        mPlayer.play(playList, index);
        if (type == 2) {
            mPlayer.pause();
        }

        mSong = playList.getCurrentSong();
        onSongUpdated(mSong);
    }

    @Override
    public void onPlaybackServiceUnbound() {
        mPlayer.unregisterCallback(this);
        mPlayer = null;
    }

    @Override
    public void onSongUpdated(@Nullable Song song) {
        if (song == null) {
            mImageViewAlbum.cancelRotateAnimation();
            mButtonPlayToggle.setImageResource(R.drawable.icon_play);
            mSeekBar.setProgress(0);
            updateProgressTextWithProgress(0);
            seekTo(0);
            mHandler.removeCallbacks(mProgressCallback);
            return;
        }

        setPlayInfo(song);
    }

    @Override
    public void updatePlayToggle(boolean play) {
        mButtonPlayToggle.setImageResource(play ? R.drawable.icon_pause : R.drawable.icon_play);
    }

    @Override
    public void getAlbumChapterDetailSuccess(final AlbumChapterDetailResponse response, final int chapterId) {
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
            checkNetType(response, chapterId);
        } else {
            // 1已订阅
            if (response.buystatus == 1) {
                checkNetType(response, chapterId);
            } else {
                showChapterOrderPopWindow(albumId, chapterId, new ChapterOrderPopWindow.OnOrderPayListener() {
                    @Override
                    public void onPaySuccess() {
                        Log.e("hhh", "onPaySuccess");
                        showToast(mActivity.getString(R.string.book_order_success));
                        checkNetType(response, chapterId);
                    }

                    @Override
                    public void onCancelPay() {
                        Log.e("hhh", "onCancelPay");
                    }
                });
            }
        }
    }

    @Override
    public void getAlbumChapterDetailFailure(String msg) {

    }

    @Override
    public void addToBookRackSuccess(long bookId, int receive) {
        showToast(getString(R.string.add_to_bookrack_success));

        mTvAddBookRack.setText("已加入书架");
        mTvAddBookRack.setEnabled(false);

        BookBrowseHistoryDao bookBrowseHistoryDao = VisitorDbManager.getDaoSession(mContext).getBookBrowseHistoryDao();
        List<BookBrowseHistory> list = bookBrowseHistoryDao.queryBuilder().where(BookBrowseHistoryDao.Properties.Bookid.eq(bookId)).list();

        for (BookBrowseHistory bookBrowseHistory : list) {
            bookBrowseHistory.bookshelves = 1;
            bookBrowseHistoryDao.update(bookBrowseHistory);
        }

        Song song = PlayerSong.getInstance().getPlayerSong();
        if (null != song) {
            if (bookId == song.albumid) {
                song.bookShelves = 1;
            }
        }

        Intent intent = new Intent(BroadcastAction.ADD_BOOK_TO_RACK_ACTION);
        intent.putExtra("bookId", bookId);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

        if (receive == 0) {
            long userId = UserInfoManager.getInstance(mContext).getUserid();
            mPresenter.getTaskAward(userId, TaskId.ADD_BOOK_TO_RACK, true);
        }
    }

    @Override
    public void getTaskAwardSuccess(TaskAwardResponse taskAwardResponse, int taskId) {
        if (taskAwardResponse.number > 0) {
            TaskRewardPopupWindow taskRewardPopupWindow = new TaskRewardPopupWindow(mActivity);
            taskRewardPopupWindow.show(taskAwardResponse.desc, taskAwardResponse.number + "");
        }
    }

    private void checkNetType(final AlbumChapterDetailResponse response, final int chapterId) {
        NetworkUtils.NetworkType netWorkType = NetworkUtils.getNetworkType();
        switch (netWorkType) {
            case NETWORK_2G:
            case NETWORK_3G:
            case NETWORK_4G:
            case NETWORK_UNKNOWN:
            case NETWORK_ETHERNET:
                Log.e("hhh", "non wifi");
                alertNonWifi(response, chapterId);
                break;
            case NETWORK_WIFI:
                Log.e("hhh", "wifi");
                play(response, chapterId);
                break;
            default:
                break;
        }
    }

    private void alertNonWifi(final AlbumChapterDetailResponse response, final int chapterId) {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("当前为非WIFI环境，是否继续播放？")
                .setPositiveButton("继续播放", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        play(response, chapterId);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    private void play(AlbumChapterDetailResponse response, int chapterId) {
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
        playSong(song, 0);
    }

    private void showChapterOrderPopWindow(long bookId, long chapterId, ChapterOrderPopWindow.OnOrderPayListener onOrderPayListener) {
        if (chapterOrderPopWindow == null || !chapterOrderPopWindow.isShow()) {

            chapterOrderPopWindow = new ChapterOrderPopWindow(this, this, bookId, chapterId);
            chapterOrderPopWindow.setOnOrderPayListener(onOrderPayListener);

            new XPopup.Builder(this)
                    .moveUpToKeyboard(false)
                    //.enableDrag(false)
                    .asCustom(chapterOrderPopWindow)
                    .show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_directory: {
                directory();
            }
            break;
            case R.id.button_play_toggle: {
                play();
            }
            break;
            case R.id.button_play_next: {
                playNext();
            }
            break;
            case R.id.button_play_last: {
                playLast();
            }
            break;
            case R.id.tv_add_book_rack: {
                addBookRack();
            }
            break;
            case R.id.tv_timing: {
                setTiming();
            }
            break;
            case R.id.img_collapse: {
                finish();
            }
            break;
            case R.id.layout_reward: {
                reward();
            }
            break;
            default:
                break;
        }
    }

    private void directory() {
        showDirectoryPopWindow(albumId, chapterId, new DirectoryPopupWindow.OnClickItemListener() {
            @Override
            public void onItemClick(AlbumChapterResponse.PageDataBean bean) {
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                String deviceId = DeviceUtils.getAndroidID();
                mPresenter.getAlbumChapterDetail(userId, albumId, bean.cid, deviceId);
            }
        });
    }

    private void play() {
        if (mPlayer == null) {
            return;
        }

        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.play();
        }
    }

    private void playNext() {
        if (null != mSong) {
            int nextCid = mSong.nextcid;
            if (nextCid != 0) {
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                String deviceId = DeviceUtils.getAndroidID();
                mPresenter.getAlbumChapterDetail(userId, albumId, nextCid, deviceId);
            } else {
                Toast.makeText(mContext, "已经是最后一章了", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void playLast() {
        if (null != mSong) {
            int lastCid = mSong.lastcid;
            if (lastCid != 0) {
                long userId = UserInfoManager.getInstance(mContext).getUserid();
                String deviceId = DeviceUtils.getAndroidID();
                mPresenter.getAlbumChapterDetail(userId, albumId, lastCid, deviceId);
            } else {
                Toast.makeText(mContext, "已经是第一章了", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addBookRack() {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        if (userId == 0) {
            lunchLoginActivity(FROM_ADD_TO_BOOK_RACK);
        } else {
            mPresenter.addToBookRack(userId, albumId);
        }
    }

    private void setTiming() {
        showTimingPopupWindow(new TimingPopupWindow.OnClickItemListener() {
            @Override
            public void onItemClick(TimingBean bean) {
                Log.e("hhh", "bean.value= " + bean.value);
                Log.e("hhh", "bean.time= " + bean.time);
                PlayerSong.getInstance().setTimingBean(bean);
                QuitTimer.getInstance().start(bean.value * 60 * 1000, mTvTiming, mContext);

                Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.icon_timing_already);
                if (null != drawable) {
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    mTvTiming.setCompoundDrawables(null, drawable, null, null);
                }
            }

            @Override
            public void onCloseTiming() {
                PlayerSong.getInstance().setTimingBean(null);
                QuitTimer.getInstance().stop();
                Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.icon_timing);
                if (null != drawable) {
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    mTvTiming.setCompoundDrawables(null, drawable, null, null);
                }
                mTvTiming.setText("定时");
                mTvTiming.setTextColor(Color.parseColor("#333333"));
            }
        });
    }

    private void reward() {
        long userId = UserInfoManager.getInstance(mContext).getUserid();
        if (userId == 0) {
            super.lunchLoginActivity();
        } else {
            Intent intent = new Intent(mContext, RewardActivity.class);
            intent.putExtra("albumId", albumId);
            startActivity(intent);
        }
    }

    private void showDirectoryPopWindow(int albumId, int chapterId, DirectoryPopupWindow.OnClickItemListener onClickItemListener) {
        if (popupWindow == null || !popupWindow.isShow()) {

            popupWindow = new DirectoryPopupWindow(this, albumId, chapterId);
            popupWindow.setOnClickItemListener(onClickItemListener);

            new XPopup.Builder(this)
                    .moveUpToKeyboard(false)
                    //.enableDrag(false)
                    .asCustom(popupWindow)
                    .show();
        }
    }

    private void showTimingPopupWindow(TimingPopupWindow.OnClickItemListener onClickItemListener) {
        if (timingPopupWindow == null || !timingPopupWindow.isShow()) {

            TimingBean bean = PlayerSong.getInstance().getTimingBean();
            timingPopupWindow = new TimingPopupWindow(this, bean);
            timingPopupWindow.setOnClickItemListener(onClickItemListener);

            new XPopup.Builder(this)
                    .moveUpToKeyboard(false)
                    //.enableDrag(false)
                    .asCustom(timingPopupWindow)
                    .show();
        }
    }
}
