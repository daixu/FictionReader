package com.shangame.fiction.core.base;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Speedy on 2018/8/22
 */
public abstract class WrapRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected static final Object mLocked = new Object();

    protected List<T> data = new ArrayList<T>();

    /**
     * 添加
     *
     * @param t
     */
    public void add(T t) {
        synchronized (mLocked) {
            data.add(t);
        }
    }

    public void addToHeader(T t) {
        synchronized (mLocked) {
            data.add(0, t);
        }
    }

    /**
     * 添加所有
     *
     * @param list
     */
    public void addAll(List<T> list) {
        synchronized (mLocked) {
            if (list != null && list.size() > 0) {
                data.addAll(list);
            }
        }
    }

    /**
     * 删除
     *
     * @param position
     */
    public void remove(int position) {
        synchronized (mLocked) {
            if (data.size() > position && position >= 0) {
                data.remove(position);
            }
        }
    }

    /**
     * 删除
     *
     * @param t
     */
    public void remove(T t) {
        synchronized (mLocked) {
            data.remove(t);
        }
    }

    /**
     * 清空
     */
    public void clear() {
        synchronized (mLocked) {
            data.clear();
        }
    }

    /**
     * 获取所有数据
     *
     * @return
     */
    public List<T> getData() {
        synchronized (mLocked) {
            return data;
        }
    }

    @Override
    public int getItemCount() {
        synchronized (mLocked) {
            return data.size();
        }
    }

    @Nullable
    public T getItem(int position) {
        if (position < data.size()) {
            return data.get(position);
        } else {
            return null;
        }
    }

}
