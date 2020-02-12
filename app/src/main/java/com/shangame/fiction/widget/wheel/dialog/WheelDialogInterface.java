package com.shangame.fiction.widget.wheel.dialog;

import com.shangame.fiction.widget.wheel.base.IWheel;

public interface WheelDialogInterface<T extends IWheel> {

    boolean onClick(int witch, int selectedIndex, T item);
}