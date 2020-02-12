package com.shangame.fiction.widget;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private String time = "";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //新建日历类用于获取当前时间
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        //返回TimePickerDialog对象
        //因为实现了OnTimeSetListener接口，所以第二个参数直接传入this
        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        //判断activity是否是DataCallBack的一个实例
        if (getActivity() instanceof DataCallBack) {
            //将activity强转为DataCallBack
            DataCallBack dataCallBack = (DataCallBack) getActivity();
            String hourStr;
            if (hourOfDay < 10) {
                hourStr = "0" + hourOfDay;
            } else {
                hourStr = "" + hourOfDay;
            }

            String minuteStr;
            if (minute < 10) {
                minuteStr = "0" + minute;
            } else {
                minuteStr = "" + minute;
            }
            time = time + hourStr + ":" + minuteStr;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            try {
                Date strDate = sdf.parse(time);
                if (System.currentTimeMillis() > strDate.getTime()) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                    long fastTime = System.currentTimeMillis() + (5 * 60 * 1000);
                    Date date = new Date(fastTime);
                    time = simpleDateFormat.format(date);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //调用activity的getData方法将数据传回activity显示
            dataCallBack.getTime(time);
        }
    }

    public void setTime(String date) {
        time += date;
    }

}