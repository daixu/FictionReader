package com.shangame.fiction.storage.model;

/**
 * Create by Speedy on 2018/12/21
 */
public class HobbyItem {

    public HobbyItem(int kind, int backgroudResId, int txtResId) {
        this.kind = kind;
        this.backgroudResId = backgroudResId;
        this.txtResId = txtResId;
    }

    public boolean isChecked;
    public int kind;
    public int backgroudResId;
    public int txtResId;
}
