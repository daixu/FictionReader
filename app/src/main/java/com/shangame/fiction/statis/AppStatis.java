package com.shangame.fiction.statis;

import android.content.Context;
import android.os.AsyncTask;
import android.text.format.DateUtils;

import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.manager.AppSetting;
import com.shangame.fiction.core.manager.Logger;
import com.shangame.fiction.core.utils.RxUtils;
import com.shangame.fiction.net.api.ApiManager;
import com.shangame.fiction.net.response.HttpResult;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.manager.UserSetting;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * 应用统计
 * Create by Speedy on 2018/9/13
 */
public final class AppStatis {

    private static long startReadTime;
    private static AppStatis appStatis;
    private Context mContext;

    private AppStatis(Context context) {
        mContext = context;
    }

    public static final AppStatis getInstance(Context context) {
        if (appStatis == null) {
            appStatis = new AppStatis(context);
        }
        return appStatis;
    }

    public void startRead() {
        startReadTime = System.currentTimeMillis();
    }

    public void endRead(final long bookId, final long chapterId) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                //以秒为单位
                long readDuration = (System.currentTimeMillis() - startReadTime) / 1000;
                long userid = UserInfoManager.getInstance(mContext).getUserid();
                sendReadHour(userid, readDuration, bookId, chapterId);

                UserSetting userSetting = UserSetting.getInstance(mContext);

                long readDate = userSetting.getLong(SharedKey.READ_TIME, 0);
                if (DateUtils.isToday(readDate)) {

                    long todayReadTime = userSetting.getLong(SharedKey.TODAY_READ_TIME, 0);
                    todayReadTime = todayReadTime + readDuration;

                    userSetting.putLong(SharedKey.TODAY_READ_TIME, todayReadTime);
                    userSetting.putLong(SharedKey.READ_TIME, System.currentTimeMillis());
                } else {
                    userSetting.putLong(SharedKey.READ_TIME, System.currentTimeMillis());
                    userSetting.putLong(SharedKey.TODAY_READ_TIME, readDuration);
                }
                return null;
            }
        }.execute();
    }

    private void sendReadHour(long userid, long readingtime, final long bookId, final long chapterId) {
        long lastModifyTime = System.currentTimeMillis() / 1000;
        Observable<HttpResult<Object>> observable = ApiManager.getInstance().sendReadHour(userid, readingtime, lastModifyTime, bookId, chapterId);

        RxUtils.rxSchedulerHelper(observable)
                .subscribe(new Consumer<HttpResult<Object>>() {
                    @Override
                    public void accept(HttpResult<Object> objectHttpResult) throws Exception {
                        Logger.d("AppStatis", "sendReadHour success  ");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });
    }

    public void appLunch() {
        AppSetting.getInstance(mContext).putLong(SharedKey.APP_USE_TIME, System.currentTimeMillis());
    }

    public void sendAppLunchDurationTime(long userid) {
        long startTime = AppSetting.getInstance(mContext).getLong(SharedKey.APP_USE_TIME, System.currentTimeMillis());

        long apptime = (System.currentTimeMillis() - startTime) / 1000;

        Observable<HttpResult<Object>> observable = ApiManager.getInstance().sendAppLunchDurationTime(userid, apptime);

        RxUtils.rxSchedulerHelper(observable)
                .subscribe(new Consumer<HttpResult<Object>>() {
                    @Override
                    public void accept(HttpResult<Object> objectHttpResult) throws Exception {
                        Logger.d("AppStatis", "sendReadHour success  ");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });
    }
}
