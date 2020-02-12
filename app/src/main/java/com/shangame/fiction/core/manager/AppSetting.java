package com.shangame.fiction.core.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Create by Speedy on 2018/9/4
 */
public final class AppSetting {

    private static final String SP_NAME = "AppSetting";

    private Context mContext;

    private SharedPreferences sharedPreferences;

    private AppSetting(Context context) {
        this.mContext = context.getApplicationContext();
        sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static final AppSetting getInstance(Context context) {
        return new AppSetting(context);
    }

    public void putString(String key, String value) {
        sharedPreferences.edit().putString(key, value).commit();
    }

    public void putInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).commit();
    }

    public void putLong(String key, long value) {
        sharedPreferences.edit().putLong(key, value).commit();
    }

    public void putFloat(String key, float value) {
        sharedPreferences.edit().putFloat(key, value).commit();
    }

    public void putBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    public String getString(String key, String defVaule) {
        return sharedPreferences.getString(key, defVaule);
    }

    public int getInt(String key, int defVaule) {
        return sharedPreferences.getInt(key, defVaule);
    }

    public long getLong(String key, long defVaule) {
        return sharedPreferences.getLong(key, defVaule);
    }

    public float getFloat(String key, float defVaule) {
        return sharedPreferences.getFloat(key, defVaule);
    }

    public boolean getBoolean(String key, boolean defVaule) {
        return sharedPreferences.getBoolean(key, defVaule);
    }
}
