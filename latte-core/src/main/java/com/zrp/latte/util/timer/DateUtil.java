package com.zrp.latte.util.timer;

import java.util.Calendar;
import java.util.Date;

public final class DateUtil {

	private DateUtil(){}
	public static String getWeek() {
		String week = "";
		Date today = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		int weekday = c.get(Calendar.DAY_OF_WEEK);
		if (weekday == 1) {
			week = "(周日)";
		} else if (weekday == 2) {
			week = "(周一)";
		} else if (weekday == 3) {
			week = "(周二)";
		} else if (weekday == 4) {
			week = "(周三)";
		} else if (weekday == 5) {
			week = "(周四)";
		} else if (weekday == 6) {
			week = "(周五)";
		} else if (weekday == 7) {
			week = "(周六)";
		}
		return "今天"+week;
	}
	public static String getWeek(int overDay) {
		String week = "";
		Date today = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(today);
		int weekday = c.get(Calendar.DAY_OF_WEEK) +1;
		if (weekday == 1) {
			week = "(周日)";
		} else if (weekday == 2) {
			week = "(周一)";
		} else if (weekday == 3) {
			week = "(周二)";
		} else if (weekday == 4) {
			week = "(周三)";
		} else if (weekday == 5) {
			week = "(周四)";
		} else if (weekday == 6) {
			week = "(周五)";
		} else if (weekday == 7) {
			week = "(周六)";
		}
		return "明天"+week;
	}

}
