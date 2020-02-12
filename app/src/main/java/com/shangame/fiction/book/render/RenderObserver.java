package com.shangame.fiction.book.render;

/**
 * Create by Speedy on 2018/8/31
 */
public interface RenderObserver {

    void onChanged();

    void indexChanged(int index);

}
