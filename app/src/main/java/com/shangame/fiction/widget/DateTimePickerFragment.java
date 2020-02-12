package com.shangame.fiction.widget;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DateTimePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private String mDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //得到Calendar类实例，用于获取当前时间
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        //返回DatePickerDialog对象
        //因为实现了OnDateSetListener接口，所以第二个参数直接传入this
        DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, year, month, day);
        DatePicker datePicker = dpd.getDatePicker();
        datePicker.setMinDate(calendar.getTimeInMillis());
        return dpd;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        //这样子写就将选择时间的fragment和选择日期的fragment完全绑定在一起
        //使用的时候只需直接调用DatePickerFragment的show()方法
        //即可选择完日期后选择时间
        TimePickerFragment timePicker = new TimePickerFragment();
        timePicker.show(getFragmentManager(), "time_picker");
        //将用户选择的日期传到TimePickerFragment

        String monthStr;
        if (monthOfYear < 9) {
            monthStr = "0" + (monthOfYear + 1);
        } else {
            monthStr = "" + (monthOfYear + 1);
        }

        String dayStr;
        if (dayOfMonth < 10) {
            dayStr = "0" + dayOfMonth;
        } else {
            dayStr = "" + dayOfMonth;
        }

        mDate = year + "-" + (monthStr) + "-" + dayStr + " ";
        timePicker.setTime(mDate);
    }
}