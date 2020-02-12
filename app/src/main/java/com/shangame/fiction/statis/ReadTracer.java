package com.shangame.fiction.statis;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.shangame.fiction.ad.FeedAdBean;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.manager.AppSetting;
import com.shangame.fiction.core.utils.RxUtils;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.net.response.ReadTimeResponse;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.manager.UserSetting;
import com.shangame.fiction.ui.task.TaskId;

import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static com.shangame.fiction.core.constant.SharedKey.IS_NO_AD;
import static com.shangame.fiction.core.constant.SharedKey.NO_AD_TIME;

/**
 * Create by Speedy on 2019/4/3
 */
public class ReadTracer {

    //单位秒
    public static final long READ_10 = 60 * 10;
    public static final long READ_30 = 60 * 30;
    public static final long READ_100 = 60 * 100;

//    private static final long READ_30 = 1000*60*30;
//    private static final long READ_100 = 1000*60*100;
//    private static final long READ_200 = 1000*60*200;
    public static final long READ_200 = 60 * 200;
    private static final String TAG = "ReadTracer";
    private static final int SING_PAGE_MAX_READ_TIME = 1000 * 60;//单页最大统计阅读时间
    private static final int AUTO_SEND_INTERVAL = 1000 * 30;//自动发送阅读时长
    private Context mContext;
    private UserSetting userSetting;
    private Timer timer;
    private boolean isOffLine;

    public ReadTracer(Context context) {
        this.mContext = context;
        userSetting = UserSetting.getInstance(mContext);
    }

    public void updateReadTracer() {
        userSetting = UserSetting.getInstance(mContext);
        userSetting.putLong(SharedKey.CURRENT_READ_TIME, 0);
        userSetting.putLong(SharedKey.LAST_FLIP_TIME, System.currentTimeMillis());
        clearOffLineReadTime();
    }

    private void clearOffLineReadTime() {
        AppSetting appSetting = AppSetting.getInstance(mContext);
        appSetting.putLong(SharedKey.OFFLINE_READ_TIME, 0);
        appSetting.putInt(SharedKey.NEXT_TASK_ID, 0);
    }

    public void startRead() {
        userSetting.putLong(SharedKey.CURRENT_READ_TIME, 0);
        userSetting.putLong(SharedKey.LAST_FLIP_TIME, System.currentTimeMillis());

        startAutoSendTimer();
    }

    private void startAutoSendTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendReadTime();
            }
        }, 0, AUTO_SEND_INTERVAL);
    }

    private void sendReadTime() {
        long currentReadTime = computeReadTime();

        long isNoAdTime = userSetting.getLong(SharedKey.IS_NO_AD_TIME, 0);
        long totalTime = isNoAdTime + getReadTime();
        Log.i("hhh", "sendReadTime1: currentReadTime = " + currentReadTime + " ,totalTime= " + totalTime);

        int noAdTime = AppSetting.getInstance(mContext).getInt(NO_AD_TIME, 15);
        if (totalTime >= (noAdTime * 60)) {
            // AppSetting.getInstance(mContext).putBoolean(IS_NO_AD, false);
            userSetting.putLong(SharedKey.IS_NO_AD_TIME, 0);
        } else {
            userSetting.putLong(SharedKey.IS_NO_AD_TIME, totalTime);
        }

        if (currentReadTime > 0) {
            long userid = UserInfoManager.getInstance(mContext).getUserid();
            if (userid == 0) {
                sendOfflineReadTime(currentReadTime);
            } else {
                sendReadTime(currentReadTime);
            }
        }
    }

    //返回秒
    private long computeReadTime() {
        long currentReadTime = userSetting.getLong(SharedKey.CURRENT_READ_TIME, AUTO_SEND_INTERVAL);

        if (isOffLine) {
            return 0; //离线状态不累计时长
        }

        long lastFlipTime = userSetting.getLong(SharedKey.LAST_FLIP_TIME, System.currentTimeMillis());

        long readTime = System.currentTimeMillis() - lastFlipTime;

        if (readTime > SING_PAGE_MAX_READ_TIME) {
            isOffLine = true;
            return AUTO_SEND_INTERVAL / 1000;
        }
        currentReadTime = currentReadTime + AUTO_SEND_INTERVAL;
        return currentReadTime / 1000;
    }

    private long getReadTime() {
        long currentReadTime = userSetting.getLong(SharedKey.CURRENT_READ_TIME, AUTO_SEND_INTERVAL);

        long lastFlipTime = userSetting.getLong(SharedKey.LAST_FLIP_TIME, System.currentTimeMillis());

        long readTime = System.currentTimeMillis() - lastFlipTime;

        if (readTime > SING_PAGE_MAX_READ_TIME) {
            isOffLine = true;
            return AUTO_SEND_INTERVAL / 1000;
        }
        currentReadTime = currentReadTime + AUTO_SEND_INTERVAL;
        return currentReadTime / 1000;
    }

    public void sendOfflineReadTime(long offLineReadTime) {
        Log.i(TAG, "sendOfflineReadTime: offLineReadTime  = " + offLineReadTime);
        Observable<HttpResult<ReadTimeResponse>> observable = ApiManager.getInstance().sendOfflineReadTime(offLineReadTime, 0);
        RxUtils.rxSchedulerHelper(observable)
                .subscribe(new Consumer<HttpResult<ReadTimeResponse>>() {
                    @Override
                    public void accept(HttpResult<ReadTimeResponse> objectHttpResult) throws Exception {
                        long offlineReadTime = objectHttpResult.data.readTime;
                        AppSetting appSetting = AppSetting.getInstance(mContext);

                        if (objectHttpResult.data.videoOpen == 0) {
                            AppSetting.getInstance(mContext).putBoolean(SharedKey.IS_NO_AD, true);
                            FeedAdBean.getInstance().setCloseAd(true);
                        } else {
                            AppSetting.getInstance(mContext).putBoolean(SharedKey.IS_NO_AD, false);
                            FeedAdBean.getInstance().setCloseAd(false);
                        }

                        appSetting.putLong(SharedKey.OFFLINE_READ_TIME, offlineReadTime);
                        Log.i(TAG, "sendOfflineReadTime: offLineReadTime  = " + objectHttpResult.data.readTime);
                        if (offlineReadTime < READ_10) {
                            appSetting.putInt(SharedKey.NEXT_TASK_ID, 0);
                        }
                        int oldTaskId = appSetting.getInt(SharedKey.NEXT_TASK_ID, 0);
                        String desc = null;
                        if (offlineReadTime >= READ_200) {
                            desc = "阅读200分钟红包";
                            appSetting.putInt(SharedKey.NEXT_TASK_ID, TaskId.READ_200);
                        } else if (offlineReadTime >= READ_100) {
                            desc = "阅读100分钟红包";
                            appSetting.putInt(SharedKey.NEXT_TASK_ID, TaskId.READ_100);
                        } else if (offlineReadTime > READ_30) {
                            desc = "阅读30分钟红包";
                            appSetting.putInt(SharedKey.NEXT_TASK_ID, TaskId.READ_30);
                        } else if (offlineReadTime > READ_10) {
                            desc = "阅读10分钟红包";
                            appSetting.putInt(SharedKey.NEXT_TASK_ID, TaskId.READ_10);
                        } else {
                            appSetting.putInt(SharedKey.NEXT_TASK_ID, 0);
                            return;
                        }

                        int nextTaskId = appSetting.getInt(SharedKey.NEXT_TASK_ID, 0);

                        if (nextTaskId != 0 && nextTaskId != oldTaskId) {
                            Intent intent = new Intent(BroadcastAction.OFFLINE_READ_RED_PACKET);
                            intent.putExtra("desc", desc);
                            intent.putExtra(SharedKey.TASK_ID, nextTaskId);
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });
    }

    private void sendReadTime(final long currentReadTime) {
        Log.i(TAG, "sendReadTime: currentReadTime = " + currentReadTime);
        long userid = UserInfoManager.getInstance(mContext).getUserid();
        Observable<HttpResult<ReadTimeResponse>> observable = ApiManager.getInstance().sendReadTime(userid, currentReadTime, 0);

        RxUtils.rxSchedulerHelper(observable)
                .subscribe(new Consumer<HttpResult<ReadTimeResponse>>() {
                    @Override
                    public void accept(HttpResult<ReadTimeResponse> objectHttpResult) throws Exception {
                        userSetting.putLong(SharedKey.CURRENT_READ_TIME, 0);

                        long totalReadTime = objectHttpResult.data.readTime;
                        userSetting.putLong(SharedKey.TOTAL_READ_TIME, totalReadTime);
                        Log.i(TAG, "sendReadTime objectHttpResult.data.receive= " + objectHttpResult.data.receive);
                        if (objectHttpResult.data.receive == 0) {
                            checkTotalReadTime(objectHttpResult.data.readTime, objectHttpResult.data.nexttaskid);
                        }

                        if (objectHttpResult.data.videoOpen == 0) {
                            AppSetting.getInstance(mContext).putBoolean(SharedKey.IS_NO_AD, true);
                            FeedAdBean.getInstance().setCloseAd(true);
                        } else {
                            AppSetting.getInstance(mContext).putBoolean(SharedKey.IS_NO_AD, false);
                            FeedAdBean.getInstance().setCloseAd(false);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });
    }

    private void checkTotalReadTime(long totalReadTime, int taskId) {
        if (totalReadTime < READ_10) {
            userSetting.putInt(SharedKey.NEXT_TASK_ID, 0);
        }
        int nextTaskId = userSetting.getInt(SharedKey.NEXT_TASK_ID, 0);
        Log.i(TAG, "checkTotalReadTime nextTaskId: " + nextTaskId + "taskId: " + taskId);

        if (nextTaskId != taskId) {
            userSetting.putInt(SharedKey.NEXT_TASK_ID, taskId);
            Intent intent = new Intent(BroadcastAction.READ_RED_PACKET);
            intent.putExtra(SharedKey.TASK_ID, taskId);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }
    }

    public void stopRead() {
        if (timer != null) {
            timer.cancel();
        }
    }

    /**
     * 翻页：统计单页阅读时长
     */
    public void flipPage() {
        userSetting.putLong(SharedKey.LAST_FLIP_TIME, System.currentTimeMillis());
        isOffLine = false;
    }

    public long getTotalReadTime() {
        long userid = UserInfoManager.getInstance(mContext).getUserid();
        if (userid == 0) {
            final AppSetting appSetting = AppSetting.getInstance(mContext);
            return appSetting.getLong(SharedKey.OFFLINE_READ_TIME, 0);
        } else {
            return userSetting.getLong(SharedKey.TOTAL_READ_TIME, 0);
        }
    }
}
