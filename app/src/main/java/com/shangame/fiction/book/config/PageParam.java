package com.shangame.fiction.book.config;


import android.graphics.Color;

import com.shangame.fiction.R;


/**
 * 页面配置参数，改接口不对外暴露，外界只能通过实现类（PageConfig）访问
 * Create by Speedy on 2018/8/15
 */
interface PageParam {

    //用户不可修改配置
    int padding = 16;
    int headerHeight = 50;
    int footerHeight = 30;
    int batteryWidth = 24;
    int batteryHeight = 8;

    int headerTextColor = Color.parseColor("#493723");
    int chapterTextColor = Color.parseColor("#000000");
    int contentTextColor = Color.parseColor("#362B0A");
    int footerTextColor = Color.parseColor("#493723");


    interface LineSpace {
        int SPACE_12 = 12;
        int SPACE_18 = 18;      //默认
        int SPACE_24 = 24;
    }

    interface BackgroundColor {
        int COLOR_1 = R.color.read_bg_color_1;
        int COLOR_2 = R.color.read_bg_color_2;
        int COLOR_3 = R.color.read_bg_color_3;
        int COLOR_4 = R.color.read_bg_color_4;
        int COLOR_5 = R.color.read_bg_color_5;  //默认
        int NIGHT_COLOR = R.color.night_color;  //默认
    }

    interface FontSize {
        int SIZE_1 = 14;
        int SIZE_2 = 16;
        int SIZE_3 = 17;     //默认
        int SIZE_4 = 19;
        int SIZE_5 = 21;
        int SIZE_6 = 26;
        int SIZE_7 = 30;
    }

    interface DayMode {
        int NIGHT_MODE = 0;
        int SUN_MODE = 1;
    }
}
