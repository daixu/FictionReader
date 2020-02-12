package com.shangame.fiction.storage.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Create by Speedy on 2018/8/22
 */
public class UserSetting {

    private static final String SP_NAME = "UserSetting";

    private SharedPreferences sharedPreferences;

    private UserSetting(Context context, int userid) {
        String name = SP_NAME + userid;
        sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static UserSetting getInstance(Context context) {
        int userid = UserInfoManager.getInstance(context).getUserid();
        return new UserSetting(context, userid);
    }

    public void putString(String key, String value) {
        sharedPreferences.edit().putString(key, value).commit();
    }

    public void putInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).commit();
    }

    public void putFloat(String key, float value) {
        sharedPreferences.edit().putFloat(key, value).commit();
    }

    public void putBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    public void putLong(String key, long value) {
        sharedPreferences.edit().putLong(key, value).commit();
    }

    public long getLong(String key, long defVaule) {
        return sharedPreferences.getLong(key, defVaule);
    }

    public String getString(String key, String defVaule) {
        return sharedPreferences.getString(key, defVaule);
    }

    public int getInt(String key, int defVaule) {
        return sharedPreferences.getInt(key, defVaule);
    }

    public float getFloat(String key, float defVaule) {
        return sharedPreferences.getFloat(key, defVaule);
    }

    public boolean getBoolean(String key, boolean defVaule) {
        return sharedPreferences.getBoolean(key, defVaule);
    }
}
