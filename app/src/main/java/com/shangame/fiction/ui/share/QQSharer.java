package com.shangame.fiction.ui.share;

import android.app.Activity;
import android.os.Bundle;

import com.shangame.fiction.R;
import com.shangame.fiction.core.config.QQConfig;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;

/**
 * Create by Speedy on 2018/9/28
 */
public class QQSharer {

    public static void shareImageToQQFriend(Activity activity,String imgUrl,IUiListener uiListener){
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imgUrl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  activity.getString(R.string.app_name));

        Tencent tencent = Tencent.createInstance(QQConfig.appId, activity);
        tencent.shareToQQ(activity, params , uiListener);
    }

    public static void shareImageToQzone(Activity activity,String linkUrl,String title,String summary,String imgUrl,IUiListener uiListener){
        Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  summary);
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,  linkUrl);
        ArrayList<String> list = new ArrayList<>();
        list.add(imgUrl);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, list);
        params.putString(QzoneShare.SHARE_TO_QQ_APP_NAME,  activity.getString(R.string.app_name)+QQConfig.appId);
        params.putInt(QzoneShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);

        Tencent tencent = Tencent.createInstance(QQConfig.appId, activity);
        tencent.shareToQzone(activity, params , uiListener);
    }

    public static void shareToQQFriend(Activity activity,String title,String content,String linkUrl,String imgurl,IUiListener uiListener){
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  content);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  linkUrl);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imgurl);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  activity.getString(R.string.app_name));

        Tencent tencent = Tencent.createInstance(QQConfig.appId, activity);
        tencent.shareToQQ(activity, params , uiListener);
    }

    public static void shareToQzone(Activity activity,String title,String content,String linkUrl,String imgUrl,IUiListener uiListener){
        Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,  content);
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,  linkUrl);
        ArrayList<String> list = new ArrayList<>();
        list.add(imgUrl);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, list);
        params.putString(QzoneShare.SHARE_TO_QQ_APP_NAME,  activity.getString(R.string.app_name)+QQConfig.appId);
        params.putInt(QzoneShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);

        Tencent tencent = Tencent.createInstance(QQConfig.appId, activity);
        tencent.shareToQzone(activity, params , uiListener);
    }
}
