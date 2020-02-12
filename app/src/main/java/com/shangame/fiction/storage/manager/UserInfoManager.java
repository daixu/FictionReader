package com.shangame.fiction.storage.manager;

import android.content.Context;

import com.shangame.fiction.storage.db.UserInfoDao;
import com.shangame.fiction.storage.model.UserInfo;

import java.util.List;

/**
 * Create by Speedy on 2018/8/13
 */
public final class UserInfoManager {

    private static final Object mLock = new Object();

    private static UserInfoManager userInfoManager;

    private Context mContext;

    private UserInfoManager(Context context) {
        mContext = context.getApplicationContext();
    }

    public static UserInfoManager getInstance(Context context) {
        if (userInfoManager == null) {
            synchronized (mLock) {
                if (userInfoManager == null) {
                    userInfoManager = new UserInfoManager(context);

                }
            }
        }
        return userInfoManager;
    }

    public void saveUserInfo(UserInfo userInfo) {
        UserInfoDao userInfoDao = DbManager.getDaoSession(mContext.getApplicationContext()).getUserInfoDao();
        userInfoDao.deleteAll();
        userInfoDao.insertOrReplace(userInfo);
    }

    public int getUserid() {
        return (int) getUserInfo().userid;
    }

    public UserInfo getUserInfo() {
        List<UserInfo> list = DbManager.getDaoSession(mContext.getApplicationContext()).getUserInfoDao().queryBuilder().list();
        if (list == null || list.size() == 0) {
            return new UserInfo();
        } else {
            return list.get(0);
        }
    }
}
