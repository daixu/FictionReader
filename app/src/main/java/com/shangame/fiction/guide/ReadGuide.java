package com.shangame.fiction.guide;

import android.app.Activity;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ViewSwitcher;

import com.shangame.fiction.R;
import com.shangame.fiction.core.base.BasePopupWindow;
import com.shangame.fiction.core.manager.AppSetting;
import com.shangame.fiction.storage.manager.UserSetting;
import com.shangame.fiction.widget.ListSwicher;
import com.shangame.fiction.widget.SmartViewSwitcher;


/**
 * Create by Speedy on 2018/12/27
 */
public class ReadGuide extends PopupWindow {
    private static final String TAG = "ReadGuide";
    private  Activity mActivity;
    private ListSwicher mListSwicher;

    public ReadGuide(Activity activity) {
        super(activity);
        mActivity = activity;
        initView();
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        setOutsideTouchable(false);
    }


    private void initView() {
        View contentView =  LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.guide_read, null);
        mListSwicher = contentView.findViewById(R.id.viewSwitcher);
        mListSwicher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListSwicher.isLastView()){
                    dismiss();
                }else{
                    mListSwicher.showNext();
                }
            }
        });
        setContentView(contentView);
    }

    public static void showGuide(final Activity activity){
        AppSetting appSetting = AppSetting.getInstance(activity.getApplicationContext());
        boolean hasShowGuide = appSetting.getBoolean(TAG,false);
        if(!hasShowGuide){
            appSetting.putBoolean(TAG,true);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ReadGuide readGuide = new ReadGuide(activity);
                    readGuide.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM,0,0);
                }
            },300);
        }
    }

}
