package com.shangame.fiction.core.manager;

import android.app.Activity;
import android.os.Build;
import android.util.Log;

import java.util.Stack;

/**
 * Create by Speedy on 2018/7/17
 */
public final class ActivityStack {




    private final static Stack<Activity> activityStack = new Stack<>();





    /**
     * 添加 Activity
     *
     * @param activity
     */
    public final static void pushActivity(Activity activity){
        activityStack.add(activity);
    }







    /**
     * 移除Activity
     * @param activity
     */
    public final static void popActivity(Activity activity){

        if(activity == null ){
            return;
        }
        final int size = activityStack.size();
        Activity temp;
        for (int i = size -1; i >= 0 ; i--) {
            temp = activityStack.get(i);
            if(activity == temp){
                activityStack.remove(i);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 ){
                    if(!activity.isDestroyed()){
                        activity.finish();
                    }
                }else{
                    activity.finish();
                }
                break;
            }
        }
    }




    /**
     * 移除所有Activity
     */
    public final static void popAllActivity(){
        for (Activity activity:activityStack) {
            activity.finish();
        }
        activityStack.clear();
    }




    /**
     * 回退至指定的Activity
     * @param cls
     */
    public final static void popToSpecifyActivity(Class<? extends Activity> cls){
        int size = activityStack.size();
        Activity temp;
        while (size > 0){
            size--;
            temp = activityStack.pop();
            if(cls.getName().equals(temp.getClass().getName())){
               break;
            }else{
                temp.finish();
            }
        }
    }


}
