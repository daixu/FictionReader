package com.shangame.fiction.core.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.format.DateUtils;
import android.util.Log;

import com.shangame.fiction.R;
import com.shangame.fiction.ui.listen.palyer.MusicPlayerService;
import com.shangame.fiction.ui.listen.palyer.TimeUtils;

/**
 * 定时器
 */
public class QuitTimer {

    private MusicPlayerService mPlayService;
    private Handler mHandler;
    private long mTimerRemain;

    private AppCompatTextView mTextView;
    private Context mContext;

    public static QuitTimer getInstance() {
        return SingletonHolder.QUIT_TIMER_INSTANCE;
    }

    private static class SingletonHolder {
        private static final QuitTimer QUIT_TIMER_INSTANCE = new QuitTimer();
    }

    private QuitTimer() {
    }

    /**
     * 初始化      用@NonNull注解，表示不能为null
     *
     * @param playService playService
     * @param handler     handler
     */
    public void init(@NonNull MusicPlayerService playService, @NonNull Handler handler) {
        mPlayService = playService;
        mHandler = handler;
    }

    public void start(long milli, AppCompatTextView textView, Context context) {
        if (mHandler == null) {
            Log.e("hhh", "请先进行初始化");
            return;
        }
        this.mTextView = textView;
        this.mContext = context;
        stop();
        if (milli > 0) {
            mTimerRemain = milli + DateUtils.SECOND_IN_MILLIS;
            mHandler.post(mQuitRunnable);
        } else {
            mTimerRemain = 0;
        }
    }

    public void setParameter(AppCompatTextView textView, Context context) {
        this.mTextView = textView;
        this.mContext = context;
    }

    public void stop() {
        mHandler.removeCallbacks(mQuitRunnable);
    }

    private Runnable mQuitRunnable = new Runnable() {
        @Override
        public void run() {
            mTimerRemain -= DateUtils.SECOND_IN_MILLIS;
            if (mTimerRemain > 0) {
                if (null != mTextView) {
                    mTextView.setTextColor(Color.parseColor("#F46464"));
                    String time = TimeUtils.formatDuration(mTimerRemain);
                    mTextView.setText(time);
                }
                mHandler.postDelayed(this, DateUtils.SECOND_IN_MILLIS);
            } else {
                if (null != mContext) {
                    Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.icon_timing);
                    if (null != drawable) {
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        if (null != mTextView) {
                            mTextView.setCompoundDrawables(null, drawable, null, null);
                        }
                    }
                }
                if (null != mTextView) {
                    mTextView.setTextColor(Color.parseColor("#333333"));
                    mTextView.setText("定时");
                }
                mPlayService.quit();
            }
        }
    };
}