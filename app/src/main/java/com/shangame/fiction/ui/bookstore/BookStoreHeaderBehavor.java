package com.shangame.fiction.ui.bookstore;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Create by Speedy on 2019/2/28
 */
public class BookStoreHeaderBehavor extends CoordinatorLayout.Behavior<View> {

    private static final String TAG = "BookStoreHeaderBehavor";


    public BookStoreHeaderBehavor() {
    }

    public BookStoreHeaderBehavor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof NestedScrollView;
    }


    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                               @NonNull View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed, int type) {

        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed,
                dyConsumed, dxUnconsumed, dyUnconsumed, type);
        if(child.getHeight() > dyConsumed){
        }

    }
}
