package com.springboot.basic.utils.time;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;

import javax.xml.datatype.XMLGregorianCalendar;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String DAY_PATTERN = "yyyy-MM-dd";
	public static final String[] WEEK = new String[]{"日", "一", "二", "三", "四", "五", "六"};
	public static final String[] patterns = new String[]{"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH",
			"yyyy-MM-dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM/dd HH", "yyyy/MM/dd", "yyyyMMddHHmmss",
			"yyyyMMddHHmm", "yyyyMMddHH", "yyyyMMdd", "MM/dd/yyyy HH:mm:ss", "MM/dd/yyyy HH:mm", "MM/dd/yyyy HH",
			"MM/dd/yyyy", "yyyyMM", "yyyy", "yyyy|MM|dd"};

	public static Date currentDate() {
		return new Date();
	}

	public static long currentTimeMillis() {
		return currentDate().getTime();
	}

	public static String currentString() {
		return format(currentDate());
	}

	public static Calendar getCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar;
	}

	public static Calendar getCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	public static Calendar getCalendar(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar;
	}

	public static Date getDate(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	public static Calendar getCalendar(java.sql.Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		return calendar;
	}

	public static Calendar getCalendar(Timestamp date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		return calendar;
	}

	public static String getWeek(Calendar calendar) {
		return WEEK[calendar.get(7) - 1];
	}

	public static String format(Date date) {
		return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
	}

	public static String format(String pattern) {
		return (new SimpleDateFormat(pattern)).format(currentDate());
	}

	public static String format(Date date, String pattern) {
		return (new SimpleDateFormat(pattern)).format(date);
	}

	public static String format(java.sql.Date date, String pattern) {
		return format(new Date(date.getTime()), pattern);
	}

	public static String format(Timestamp date, String pattern) {
		return format(new Date(date.getTime()), pattern);
	}

	public static String format(String date, String pattern) throws ParseException {
		return format(parse(date), pattern);
	}

	public static String format(String request, String date, String result) throws ParseException {
		return format(parse(date, request), result);
	}

	public static Date parse(String date) throws ParseException {
		return parseDate(date, patterns);
	}

	@SuppressWarnings("unchecked")
	public static Date parse(String date, String... patterns) throws ParseException {
		@SuppressWarnings("rawtypes")
		HashSet all = new HashSet();
		all.addAll(Arrays.asList(patterns));
		all.addAll(Arrays.asList(DateUtils.patterns));
		return parseDate(date, (String[]) ((String[]) all.toArray()));
	}

	public static Date parse(String date, String pattern) throws ParseException {
		return (new SimpleDateFormat(pattern)).parse(date);
	}

	public static Date parse(Date date, int hour, int minute, int second, int millis) {
		Calendar retval = getCalendar(date);
		retval.set(11, hour);
		retval.set(12, minute);
		retval.set(13, second);
		retval.set(14, millis);
		return retval.getTime();
	}

	public static Date parse(String date, String pattern, int hour, int minute, int second, int millis)
			throws ParseException {
		Calendar retval = getCalendar(parse(date, pattern));
		retval.set(11, hour);
		retval.set(12, minute);
		retval.set(13, second);
		retval.set(14, millis);
		return retval.getTime();
	}

	public static Date add(Date date, int field, int amount) {
		return add(getCalendar(date), field, amount).getTime();
	}

	public static Calendar add(Calendar date, int field, int amount) {
		date.add(field, amount);
		return date;
	}

	public static Date convert(XMLGregorianCalendar calendar) {
		GregorianCalendar retval = calendar.toGregorianCalendar();
		return convert((Calendar) retval);
	}

	public static Date convert(Calendar calendar) {
		return calendar.getTime();
	}

	public static Date convert(Timestamp timestamp) {
		return new Date(timestamp.getTime());
	}

	public static Date convert(java.sql.Date date) {
		return new Date(date.getTime());
	}

	public static Date convert(String millisecond) {
		return new Date(Long.valueOf(millisecond).longValue());
	}

	public static Boolean isWeekend(Calendar calendar) {
		int day = calendar.get(7);
		return Boolean.valueOf(day == 1 || day == 7);
	}

	public static Boolean isWeekend(Date date) {
		return isWeekend(getCalendar(date));
	}

	public static Boolean isWeekend(java.sql.Date date) {
		return isWeekend(getCalendar(date));
	}

	public static Boolean isWeekend(Timestamp date) {
		return isWeekend(getCalendar(date));
	}

	public static Between between(Date start, Date end) {
		return new Between(start, end);
	}

	public static NextWeek nextWeek(Date date) {
		return new NextWeek(date);
	}
}