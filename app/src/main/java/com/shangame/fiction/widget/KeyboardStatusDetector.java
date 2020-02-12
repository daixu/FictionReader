package com.shangame.fiction.widget;

import android.app.Activity;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

public class KeyboardStatusDetector {

    private static final int SOFT_KEY_BOARD_MIN_HEIGHT = 200;
    private View rootView;
    private int rootViewVisibleHeight;
    private KeyboardVisibilityListener keyboardVisibilityListener;

    public KeyboardStatusDetector(Activity activity) {
        //获取activity的根视图
        rootView = activity.getWindow().getDecorView();
        //监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //获取当前根视图在屏幕上显示的大小
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int visibleHeight = r.height();
                Log.i("hhh", "" + visibleHeight);
                if (rootViewVisibleHeight == 0) {
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }
                //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
                if (rootViewVisibleHeight == visibleHeight) {
                    return;
                }
                //根视图显示高度变小超过200，可以看作软键盘显示了
                if (rootViewVisibleHeight - visibleHeight > SOFT_KEY_BOARD_MIN_HEIGHT) {
                    if (keyboardVisibilityListener != null) {
                        keyboardVisibilityListener.onVisibilityChanged(true);
                    }
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }
                //根视图显示高度变大超过200，可以看作软键盘隐藏了
                if (visibleHeight - rootViewVisibleHeight > SOFT_KEY_BOARD_MIN_HEIGHT) {
                    if (keyboardVisibilityListener != null) {
                        keyboardVisibilityListener.onVisibilityChanged(false);
                    }
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }
            }
        });
    }

    private void setVisibilityListener(KeyboardVisibilityListener keyboardVisibilityListener) {
        this.keyboardVisibilityListener = keyboardVisibilityListener;
    }

    public interface KeyboardVisibilityListener {
        void onVisibilityChanged(boolean keyboardVisible);
    }

    public static void setVisibilityListener(Activity activity, KeyboardVisibilityListener keyboardVisibilityListener) {
        KeyboardStatusDetector keyboardStatusDetector = new KeyboardStatusDetector(activity);
        keyboardStatusDetector.setVisibilityListener(keyboardVisibilityListener);
    }
}
