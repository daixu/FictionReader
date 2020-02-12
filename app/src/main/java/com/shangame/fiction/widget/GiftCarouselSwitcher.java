package com.shangame.fiction.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.shangame.fiction.R;
import com.shangame.fiction.net.response.ReceivedGiftResponse;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by Speedy on 2018/9/20
 */
public class GiftCarouselSwitcher extends ViewSwitcher {

    //循环时间
    private static final int LOOP_TIME = 3000;
    private int mCutItem;
    private MyHandler myHandler;
    private List<ReceivedGiftResponse.ReceivedGift> giftList;

    private Activity mActivity;

    public GiftCarouselSwitcher(Context context) {
        this(context, null);
    }

    public GiftCarouselSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
        initAnimation();
    }

    /**
     * 初始化一些变量
     */
    private void initData(){
        giftList = new ArrayList<>();
        myHandler = new MyHandler(this);
    }


    /**
     * 初始化进入和出去动画
     */
    private void initAnimation(){
        setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_bootom_out));
        setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_top_in));
    }

    /**
     * 设置数据源并展示view，外部调用
     *
     * @param list
     */
    public void setGiftList(Activity activity, List<ReceivedGiftResponse.ReceivedGift> list) {
        mActivity = activity;
        mCutItem = -1;
        if (null == list) {
            return;
        }
        giftList.clear();
        giftList.addAll(list);
        showNextView();
        if (null == giftList || giftList.size() < 2) {
            updateView(getCurrentView(), 0);
        } else {
            showNextView();
        }
    }

    /**
     * 展示下一条广告
     */
    public void showNextView() {
        if (null == giftList || giftList.size() < 2) {
            return;
        }
        mCutItem = (mCutItem == giftList.size() - 1) ? 0 : mCutItem + 1;
        updateView(getNextView(), mCutItem);
        showNext();
    }

    /**
     * 在当前view上设置数据
     *
     * @param view
     */
    private void updateView(View view, final int mCutItem) {
        ReceivedGiftResponse.ReceivedGift receivedGift = giftList.get(mCutItem);

        TextView tvCommentContent = view.findViewById(R.id.tvCommentContent);

        SpannableStringBuilder builder = new SpannableStringBuilder(receivedGift.nickname + " " + receivedGift.propname);
        builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), receivedGift.nickname.length(), (receivedGift.nickname + " " + receivedGift.propname).length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvCommentContent.setText(builder);

        ImageView ivHeadIcon = view.findViewById(R.id.ivHeadIcon);
        // ImageLoader.with(mActivity).loadUserIcon(ivHeadIcon, receivedGift.headimgurl);
        GlideApp.with(mActivity)
                .load(receivedGift.headimgurl)
                .centerCrop()
                .placeholder(R.drawable.default_head)
                .into(ivHeadIcon);
    }

    /**
     * 启动轮播
     */
    public void startLooping() {
        if (null == giftList || giftList.size() < 2) {
            return;
        }

        myHandler.removeMessages(0);
        myHandler.sendEmptyMessageDelayed(0, LOOP_TIME);
    }

    /**
     * 停止轮播
     */
    public void stopLooping() {
        myHandler.removeMessages(0);
    }

    /**
     * 主线程Handler
     * 因为存在定时任务，并且TextSwitcherView持有Activity的引用
     * 所以这里采用弱引用，主要针对内存回收的时候Activity泄露
     **/
    private static class MyHandler extends Handler {

        private WeakReference<GiftCarouselSwitcher> mRef;

        public MyHandler(GiftCarouselSwitcher view) {
            mRef = new WeakReference(view);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            GiftCarouselSwitcher mView = this.mRef.get();
            if (null != mView) {
                mView.showNextView();//展示下一条广告，会调用shownext方法展示下一条广告

                mView.startLooping();//启动轮播，间隔后展示下一条
            }
        }
    }
}
