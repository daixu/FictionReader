package com.shangame.fiction.storage.model;

/**
 * Create by Speedy on 2018/8/31
 */
public class Chapter {
    public long cid;     //卷或者章节的ID
    public String title;
    public int volume;  //等于0为卷，大于0为卷的ID
    public int chapternumber;
    public int chargingmode;//收费模式 ：0公共章节，1.收费
}
