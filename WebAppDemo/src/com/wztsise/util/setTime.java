package com.wztsise.util;

import java.util.Calendar;
public class setTime {
	
	public static String getTime() {
		Calendar cal = Calendar.getInstance();
		int	year = cal.get(Calendar.YEAR);
		int	month = cal.get(Calendar.MONTH)+1;
		int	day = cal.get(Calendar.DAY_OF_MONTH);
		String datetime =year+"-"+month+"-"+day;
		return datetime;
	}
	public static String initTime() {
		Calendar cal = Calendar.getInstance();
		int	year = cal.get(Calendar.YEAR);
		int	month = cal.get(Calendar.MONTH)+1;
		int	day = cal.get(Calendar.DAY_OF_MONTH);
		int	hour = cal.get(Calendar.HOUR_OF_DAY);
		int	minute = cal.get(Calendar.MINUTE);
		String time;
		String hour1 = String.valueOf(hour);
		if(hour<10) hour1 = "0"+hour;
		String minute1 = String.valueOf(minute);
		if(minute<10) minute1 = "0"+minute;
		time = year+"-"+month+"-"+day+" "+hour1+":"+minute1;
		return time;
	}
}
