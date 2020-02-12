package com.shangame.fiction.ui.popup;

import android.app.Activity;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BasePopupWindow;
import com.shangame.fiction.core.constant.SharedKey;
import com.shangame.fiction.guide.ReadGuide;
import com.shangame.fiction.storage.manager.UserInfoManager;
import com.shangame.fiction.storage.manager.UserSetting;
import com.shangame.fiction.storage.model.UserInfo;

/**
 * Create by Speedy on 2018/12/24
 */
public class NewUserReadRemindPopupWindow extends BasePopupWindow {

    private static final String TAG = " NewUserReadRemind";

    public NewUserReadRemindPopupWindow(Activity activity) {
        super(activity);
        initView();
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        setBackgroundAlpha(1f);
    }

    private void initView() {
        View contentView = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.popup_window_new_user_read_remind,null);
        contentView.findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(contentView);
    }

    public static void showGuide(final Activity activity){
        UserInfo userInfo = UserInfoManager.getInstance(activity).getUserInfo();
        if(userInfo.newvip == 1){
            UserSetting userSetting = UserSetting.getInstance(activity.getApplicationContext());
            boolean isNewUserFreeReminded = userSetting.getBoolean(SharedKey.IS_NEW_USER_FREE_REMINDED,false);
            if(!isNewUserFreeReminded && userInfo.newvip == 1 ){
                userSetting.putBoolean(SharedKey.IS_NEW_USER_FREE_REMINDED,true);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NewUserReadRemindPopupWindow newUserReadRemindPopupWindow = new NewUserReadRemindPopupWindow(activity);
                        newUserReadRemindPopupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM,0,0);
                    }
                },300);
            }
        }
    }


}
