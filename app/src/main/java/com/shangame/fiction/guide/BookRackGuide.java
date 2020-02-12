package com.shangame.fiction.guide;

import android.app.Activity;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.shangame.fiction.R;
import com.shangame.fiction.storage.manager.UserSetting;
import com.shangame.fiction.widget.ListSwicher;

/**
 * Create by Speedy on 2018/12/28
 */
public class BookRackGuide extends PopupWindow {

    private static final String TAG = "BookRackGuide";
    private  Activity mActivity;
    private ListSwicher mListSwicher;

    public BookRackGuide(Activity activity) {
        super(activity);
        mActivity = activity;
        initView();
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        setOutsideTouchable(false);
    }


    private void initView() {
        View contentView =  LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.guide_book_rack, null);
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

//    public static void showGuide(final Activity activity){
//        UserSetting userSetting = UserSetting.getInstance(activity.getApplicationContext());
//        boolean hasShowGuide = userSetting.getBoolean(TAG,false);
//        if(!hasShowGuide){
//            userSetting.putBoolean(TAG,true);
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    BookRackGuide readGuide = new BookRackGuide(activity);
//                    readGuide.setOnDismissListener(new OnDismissListener() {
//                        @Override
//                        public void onDismiss() {
//
//                        }
//                    });
//                    readGuide.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM,0,0);
//                }
//            },300);
//        }
//    }

}
