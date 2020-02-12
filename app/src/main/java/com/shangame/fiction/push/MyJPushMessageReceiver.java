package com.shangame.fiction.push;

import android.content.Context;

import com.shangame.fiction.core.manager.Logger;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * Create by Speedy on 2018/9/7
 */
public class MyJPushMessageReceiver extends JPushMessageReceiver {

    private static final String TAG = "MyJPushMessageReceiver";

    @Override
    public void onTagOperatorResult(Context context,JPushMessage jPushMessage) {
        Logger.d(TAG,"onTagOperatorResult");
        TagAliasOperatorHelper.getInstance(context).onTagOperatorResult(jPushMessage);
        super.onTagOperatorResult(context, jPushMessage);
    }
    @Override
    public void onCheckTagOperatorResult(Context context,JPushMessage jPushMessage){
        Logger.d(TAG,"onCheckTagOperatorResult");
        TagAliasOperatorHelper.getInstance(context).onCheckTagOperatorResult(jPushMessage);
        super.onCheckTagOperatorResult(context, jPushMessage);
    }
    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        Logger.d(TAG,"onAliasOperatorResult");
        TagAliasOperatorHelper.getInstance(context).onAliasOperatorResult(jPushMessage);
        super.onAliasOperatorResult(context, jPushMessage);
    }

    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        Logger.d(TAG,"onMobileNumberOperatorResult");
        TagAliasOperatorHelper.getInstance(context).onMobileNumberOperatorResult(jPushMessage);
        super.onMobileNumberOperatorResult(context, jPushMessage);
    }

}
