package com.shangame.fiction.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewAnimator;
import android.widget.ViewSwitcher;

/**
 * Create by Speedy on 2018/12/28
 */
public class ListSwicher extends ViewAnimator {

    /**
     * The factory used to create the two children.
     */
    ViewSwitcher.ViewFactory mFactory;

    /**
     * Creates a new empty ViewSwitcher.
     *
     * @param context the application's environment
     */
    public ListSwicher(Context context) {
        super(context);
    }

    /**
     * Creates a new empty ViewSwitcher for the given context and with the
     * specified set attributes.
     *
     * @param context the application environment
     * @param attrs a collection of attributes
     */
    public ListSwicher(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalStateException if this switcher already contains two children
     */
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return ViewSwitcher.class.getName();
    }


    private View obtainView() {
        View child = mFactory.makeView();
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        if (lp == null) {
            lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
        addView(child, lp);
        return child;
    }


    public void showNext(){
        super.showNext();
    }

    public boolean isLastView(){
        return getDisplayedChild() == getChildCount()-1;
    }

    /**
     * Sets the factory used to create the two views between which the
     * ViewSwitcher will flip. Instead of using a factory, you can call
     * {@link #addView(android.view.View, int, android.view.ViewGroup.LayoutParams)}
     * twice.
     *
     * @param factory the view factory used to generate the switcher's content
     */
    public void setFactory(ViewSwitcher.ViewFactory factory) {
        mFactory = factory;
        obtainView();
        obtainView();
    }


    /**
     * Creates views in a ViewSwitcher.
     */
    public interface ViewFactory {
        /**
         * Creates a new {@link android.view.View} to be added in a
         * {@link android.widget.ViewSwitcher}.
         *
         * @return a {@link android.view.View}
         */
        View makeView();
    }
}
