package com.shangame.fiction.core.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.Locale;

/**
 * Created by Speedy on 2017/7/12.
 */
public class DateUtils {
	
	public static final String FORMAT_YMD = "yyyy-MM-dd";
	public static final String YMD_HMS_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String YMDHMS_FORMAT = "yyyyMMddHHmmss";
	
	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(YMD_HMS_FORMAT, Locale.getDefault());
		}
	};

	public static long getCurrentTime(){
		Calendar c = Calendar.getInstance();
		return c.getTimeInMillis();
	}
	
	
	/**
	 * 获得"yyyy-MM-dd HH:mm:ss"格式的时间输出
	 * @param date
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getStandardFormatTimeTo24(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

	@SuppressLint("SimpleDateFormat")
	public static String getStandardFormatTime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}

	public static String getYear(long millis){
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(millis);
		return now.get(Calendar.YEAR)+"";
	}

	public static String getMouth(long millis){
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(millis);
		return (now.get(Calendar.MONTH) +1)+"";
	}

	public static String getDay(long millis){
		Calendar now = Calendar.getInstance();
		now.setTimeInMillis(millis);
		return now.get(Calendar.DAY_OF_MONTH)+"";
	}

	public static String date2String(Date date, String format) {
		if (TextUtils.isEmpty(format)) {
			// default format
			format = FORMAT_YMD;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		return sdf.format(date);
	}
}
