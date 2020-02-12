package com.shangame.fiction.core.base;

import android.app.Activity;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装BaseAdapter，统一 Adapter操作规范
 * Created by Speedy on 2018/8/20.
 */
public abstract class WrapBaseAdapter<T, H> extends BaseAdapter {

    private final Object mLocked = new Object();

    protected List<T> data = new ArrayList<T>();

    protected Activity mActivity;

    protected LayoutInflater mLayoutInflater;


    public WrapBaseAdapter(Activity activity){
        this.mActivity = activity;
        this.mLayoutInflater = LayoutInflater.from(activity.getApplicationContext());
    }


    @Override
    public int getCount() {
        synchronized (mLocked){
            return data.size();
        }
    }


    @Override
    public T getItem(int position) {
        synchronized (mLocked){
            if(position >=0 && data.size() > position){
                return data.get(position);
            }
            return null;
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }



    /**
     * 添加
     * @param t
     */
    public void add(T t){
        synchronized (mLocked){
            if(t != null){
                data.add(t);
            }
        }
    }

    /**
     * 添加所有
     * @param list
     */
    public void addAll(List<T> list){
        synchronized (mLocked){
            if(list != null && list.size()> 0){
                for (T t:list){
                    if(t != null){
                        data.add(t);
                    }
                }
            }
        }
    }

    /**
     * 删除
     * @param position
     */
    public void remove(int position){
        synchronized (mLocked){
            if( data.size() > position && position >= 0){
                data.remove(position);
            }
        }
    }


    /**
     * 删除
     * @param t
     */
    public void remove(T t){
        synchronized (mLocked){
            data.remove(t);
        }
    }



    /**
     * 清空
     */
    public void clear(){
        synchronized (mLocked){
            data.clear();
        }
    }



    /**
     * 获取所有数据
     * @return
     */
    public List<T> getData(){
        synchronized (mLocked){
            return data;
        }
    }

}
