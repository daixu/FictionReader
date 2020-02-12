package com.shangame.fiction.book.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.shangame.fiction.core.utils.ScreenUtils;

/**
 * 阅读页配置（说明：不区分登录账户，所有用户配置一样，清楚缓存用户配置失效）
 * Create by Speedy on 2018/8/16
 */
public class PageConfig implements PageParam {

    //用户可以配置字段
    public int lineSpace;
    public int backgroundColor;
    public int fontSize;
    public int fontKind;
    public int sceenLight;
    public int dayMode;//0 白天模式,1夜间模式

    private SharedPreferences sharedPreferences;

    private PageConfig(Context context) {
        sharedPreferences = context.getSharedPreferences("PageConfig", Context.MODE_PRIVATE);
    }

    public static final PageConfig getInstance(Context context) {
        PageConfig pageConfig = new PageConfig(context);
        pageConfig.initConfig(context);
        return pageConfig;
    }

    private void initConfig(Context context) {
        lineSpace = sharedPreferences.getInt("lineSpace", PageParam.LineSpace.SPACE_18);
        backgroundColor = sharedPreferences.getInt("backgroundColor", PageParam.BackgroundColor.COLOR_5);
        fontSize = sharedPreferences.getInt("fontSize", PageParam.FontSize.SIZE_3);
        fontKind = sharedPreferences.getInt("fontKind", PageParam.LineSpace.SPACE_18);
        sceenLight = sharedPreferences.getInt("sceenLight", ScreenUtils.getSystemBrightness(context));
        dayMode = sharedPreferences.getInt("dayMode", PageParam.DayMode.SUN_MODE);

        //适配屏幕
        lineSpace = (int) ScreenUtils.spToPx(context, lineSpace);

        fontSize = (int) ScreenUtils.spToPx(context, fontSize);
    }

    public int getLineSpace() {
        return this.lineSpace;
    }

    public void saveLineSpace(int lineSpace) {
        this.lineSpace = lineSpace;
        sharedPreferences.edit().putInt("lineSpace", lineSpace).commit();
    }

    public int getBackgroundColor() {
        return this.backgroundColor;
    }

    public int getBackground() {
        return sharedPreferences.getInt("backgroundColor", PageConfig.BackgroundColor.COLOR_5);
    }

    public void saveBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        sharedPreferences.edit().putInt("backgroundColor", backgroundColor).commit();
    }


    public int getSize() {
        return sharedPreferences.getInt("fontSize", PageParam.FontSize.SIZE_3);
    }

    public int getFontSize() {
        return this.fontSize;
    }


    public void saveFontSize(int fontSize) {
        this.fontSize = fontSize;
        sharedPreferences.edit().putInt("fontSize", fontSize).commit();
    }


    public int getFontKind() {
        return this.fontKind;
    }


    public void saveFontKind(int fontKind) {
        this.fontKind = fontKind;
        sharedPreferences.edit().putInt("fontKind", fontKind).commit();
    }


    public int getSceenLight() {
        return this.sceenLight;
    }

    public void saveSceenLight(int sceenLight) {
        this.sceenLight = sceenLight;
        sharedPreferences.edit().putInt("sceenLight", sceenLight).commit();
    }

    public int getDayMode() {
        return this.dayMode;
    }

    public void saveDayMode(int dayMode) {
        this.dayMode = dayMode;
        sharedPreferences.edit().putInt("dayMode", dayMode).commit();
    }
}
