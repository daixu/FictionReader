package com.shangame.fiction.ui.reader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Create by Speedy on 2018/9/12
 */
public class BatteryReceiver extends BroadcastReceiver {

    public static float batterPercent;

    @Override
    public void onReceive(Context context, Intent intent) {
        float current = intent.getExtras().getInt("level");// 获得当前电量
        float total = intent.getExtras().getInt("scale");// 获得总电量
        batterPercent = current / total;
    }
}
