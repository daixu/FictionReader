package com.shangame.fiction.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangame.fiction.core.constant.BroadcastAction;
import com.shangame.fiction.core.manager.Logger;
import com.shangame.fiction.push.model.PayModel;
import com.shangame.fiction.push.model.PushExtra;

import cn.jpush.android.api.JPushInterface;

/**
 * Create by Speedy on 2018/9/7
 */
public class MyPushReceiver extends BroadcastReceiver {

    private static final String TAG = "MyPushReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            Logger.i(TAG, "EXTRA_MESSAGE " + bundle.getString(JPushInterface.EXTRA_ALERT));
            Logger.i(TAG, "EXTRA_EXTRA " + bundle.getString(JPushInterface.EXTRA_EXTRA));

            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (TextUtils.isEmpty(extra)) {
                return;
            }
            Gson gson = new Gson();
            PushExtra<PayModel> pushExtra = gson.fromJson(extra, new TypeToken<PushExtra<PayModel>>() {
            }.getType());
            if (pushExtra.type == PushType.PAY_SUCCESS_PUSH) {
                if (null == pushExtra.data) {
                    rechargeSuccess(pushExtra, context);
                    return;
                }
                if (null == pushExtra.data.currdata) {
                    rechargeSuccess(pushExtra, context);
                    return;
                }
                if (pushExtra.data.currdata.size() == 0) {
                    rechargeSuccess(pushExtra, context);
                    return;
                }
                if (pushExtra.data.currdata.get(0).goldtype == 2) {
                    if (pushExtra.data.currdata.get(0).propid == 1005) {
                        upgradeSuccess(pushExtra, context);
                    }
                } else {
                    rechargeSuccess(pushExtra, context);
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, "", e);
        }
    }

    private void rechargeSuccess(PushExtra<PayModel> pushExtra, Context context) {
        Intent intent = new Intent(BroadcastAction.PUSH_PAY_SUCCESS_ACTION);
        intent.putExtra("price", pushExtra.data.price);
        intent.putExtra("readmoney", pushExtra.data.readmoney);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private void upgradeSuccess(PushExtra<PayModel> pushExtra, Context context) {
        Intent intent = new Intent(BroadcastAction.PUSH_GRADE_SUCCESS_ACTION);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
