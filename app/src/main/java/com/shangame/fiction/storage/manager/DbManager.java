package com.shangame.fiction.storage.manager;

import android.content.Context;

import com.shangame.fiction.core.config.AppConfig;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.core.manager.AppSetting;
import com.shangame.fiction.storage.db.DaoMaster;
import com.shangame.fiction.storage.db.DaoSession;
import com.shangame.fiction.storage.db.DbHelper;

import org.greenrobot.greendao.database.Database;

/**
 * Create by Speedy on 2018/8/16
 */
public class DbManager {

    private static final String DATABASE_NAME = "NovelReader.db";

    private static DaoSession daoSession;

    private static final Object mLock = new Object();


    /**
     * 获取
     * @param context
     * @return
     */
    public static DaoSession getDaoSession(Context context){
        if(daoSession == null) {
            synchronized (mLock) {
                if (daoSession == null) {
                    long userId = AppSetting.getInstance(context).getLong(SharedKey.CURRENT_USER_ID,0);
                    DbHelper helper = new DbHelper(context, userId + DATABASE_NAME, null);
                    Database db = AppConfig.isDebug ? helper.getWritableDb() : helper.getEncryptedWritableDb("http://www.shangame.com/");
                    daoSession = new DaoMaster(db).newSession();
                }
            }
        }
        return daoSession;
    }


    /**
     * 关闭
     */
    public static void close(){
        if(daoSession != null) {
            synchronized (mLock) {
                daoSession.clear();
                daoSession = null;
            }
        }
    }
}
