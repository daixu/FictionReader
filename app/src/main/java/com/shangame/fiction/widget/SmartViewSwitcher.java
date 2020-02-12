package com.shangame.fiction.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ViewSwitcher;

import com.shangame.fiction.R;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Create by Speedy on 2018/9/20
 */
public class SmartViewSwitcher extends ViewSwitcher {

    private static final String TAG = "SmartViewSwitcher";

    private static final int loopTime = 5000;//循环时间
    private MyHandler myHandler ;

    private int index;

    private ViewSwitcherAdapter adapter;

    public SmartViewSwitcher(Context context) {
        this(context, null);
    }

    private Timer timer;
    public SmartViewSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAnimation();
        myHandler = new MyHandler(this);
    }




    /**
     * 初始化进入和出去动画
     */
    private void initAnimation(){
        setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_top_in ));
        setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_bootom_out));
    }


    /**
     *展示下一条广告
     */
    public void showNextView() {
        index++;
        if(index >= adapter.getItemCount()){
            index = 0;
        }
        View view = getNextView();
        adapter.onBindView(index,view);
        showNext();
    }

    /**
     * 启动轮播
     */
    public void startLooping() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                myHandler.sendEmptyMessage(0);
            }
        },0,loopTime);
    }

    /**
     * 停止轮播
     */
    public void stopLooping(){
        if(timer != null){
            timer.cancel();
        }
    }



    /**
     * @description 主线程Handler
     * @note 因为存在定时任务，并且TextSwitcherView持有Activity的引用
     * 所以这里采用弱引用，主要针对内存回收的时候Activity泄露
     **/
    private static class MyHandler extends Handler {

        private WeakReference<SmartViewSwitcher> mRef;
        public MyHandler(SmartViewSwitcher view){
            mRef = new WeakReference(view);
        }
        @Override
        public void handleMessage(Message msg) {
            SmartViewSwitcher mView = this.mRef.get();
            if(mView != null){
                mView.showNextView();//展示下一条广告，会调用shownext方法展示下一条广告
            }
        }
    }


    public Object getCurrentData(){
        return adapter.getItemData(index);
    }

    public void setAdapter(ViewSwitcherAdapter adapter) {
        this.adapter = adapter;
    }

    public void notifyDataChange(){
        View view = getCurrentView();
        index = 0;
        adapter.onBindView(index,view);
    }

    public interface ViewSwitcherAdapter<T>{
        void onBindView(int position,View view);
        T getItemData(int position);
        int getItemCount();
    }
}
